package com.john.freezeapp.daemon;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class DaemonSocketServer {

    private static final ExecutorService sExecutorService = Executors.newFixedThreadPool(4);
    private static final ThreadPoolExecutor sCommandExecutorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);

    private boolean stopServer = false;
    private ServerSocket serverSocket = null;
    private Daemon daemon;

    public DaemonSocketServer(Daemon daemon) {
        this.daemon = daemon;
        startServer();
    }

    private void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(DaemonHelper.PORT);
                    Log.d("freeze-server", "startServer");
                    int index = 0;
                    while (!stopServer) {
                        final Socket socket = serverSocket.accept();
                        index++;
                        sExecutorService.execute(new Task(DaemonSocketServer.this, "TASK-" + index, socket));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    Log.d("freeze-server", "startServer exception");
                }
            }
        }).start();
    }


    public static class Command implements Runnable {
        DaemonSocketServer server;
        Socket socket;
        String command;
        String name;

        public Command(DaemonSocketServer server, Socket socket, String command, String name) {
            this.server = server;
            this.socket = socket;
            this.command = command;
            this.name = name;
        }


        @Override
        public void run() {

            DaemonShellUtils.execCommand(command, false, new DaemonShellUtils.ShellCommandResultCallback() {
                @Override
                public void callback(DaemonShellUtils.ShellCommandResult result) {
                    BufferedWriter bufferedWriter = null;
                    try {
                        Gson gson = new Gson();
                        OutputStream outputStream = socket.getOutputStream();
                        bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                        JSONObject resultObject = new JSONObject();
                        resultObject.put("code", "0");
                        resultObject.put("data", gson.toJson(result));
                        bufferedWriter.write(resultObject.toString());
                        bufferedWriter.write("\n");
                        bufferedWriter.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                        server.doFail("Command run", name, socket);
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }


    public static class Task implements Runnable {
        Socket socket;
        String name;
        DaemonSocketServer server;

        public Task(DaemonSocketServer server, String name, Socket socket) {
            this.name = name;
            this.socket = socket;
        }

        @Override
        public void run() {
            doHandle(socket);
        }


        private void doHandle(Socket socket) {
            BufferedReader bufferedReader = null;
            boolean isFinish = false;
            try {
                InputStream inputStream = socket.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String read = bufferedReader.readLine();
                JSONObject jsonObject = new JSONObject(read);
                String type = jsonObject.optString("type");
                log(name, "doHandle type" + type);
                if (TextUtils.equals(type, DaemonHelper.IPC_TYPE_SHELL)) {
                    String command = jsonObject.optString("data");
                    sCommandExecutorService.execute(new Command(server, socket, command, name));
                } else if (TextUtils.equals(type, DaemonHelper.IPC_TYPE_BIND)) {
                    doBind(socket, name);
                    isFinish = true;
                } else if (TextUtils.equals(type, DaemonHelper.IPC_TYPE_STOP)) {
                    doStop(socket, name);
                    isFinish = true;
                } else {
                    server.doFail("doHandle not type", name, socket);
                    isFinish = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                server.doFail("doHandle Exception", name, socket);

            } finally {
                if (isFinish) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        //
                    }
                }
            }
        }

        private void doStop(Socket socket, String name) {
            BufferedWriter bufferedWriter = null;
            try {
                OutputStream outputStream = socket.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                JSONObject resultObject = new JSONObject();
                resultObject.put("code", "0");
                bufferedWriter.write(resultObject.toString());
                bufferedWriter.write("\n");
                bufferedWriter.flush();
                server.stopServerSocket(true);
            } catch (Exception e) {
                e.printStackTrace();
                server.doFail("doStop", name, socket);
            } finally {
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void doBind(Socket socket, String name) {
            BufferedWriter bufferedWriter = null;
            try {
                OutputStream outputStream = socket.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                JSONObject resultObject = new JSONObject();
                resultObject.put("code", "0");
                JSONObject dataObject = new JSONObject();
                dataObject.put("command_active_count", sCommandExecutorService.getActiveCount());
                dataObject.put("command_task_count", sCommandExecutorService.getTaskCount());
                dataObject.put("command_total_count", 4);
                resultObject.put("data", dataObject);
                bufferedWriter.write(resultObject.toString());
                bufferedWriter.write("\n");
                bufferedWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
                server.doFail("doBind", name, socket);
            } finally {
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void stopServerSocket(boolean stopDaemon) {
        log("DaemonSocketServer", "stopServerSocket");
        stopServer = true;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            serverSocket = null;
        }
        DaemonBinder.unregister();
        sExecutorService.shutdown();
        sCommandExecutorService.shutdown();
        if (stopDaemon) {
            daemon.stop();
        }

    }

    public void stop() {
        stopServerSocket(false);
    }

    private void doFail(String tag, String name, Socket socket) {
        log(name, "doFail - " + tag);
        BufferedWriter bufferedWriter = null;
        try {
            OutputStream outputStream = socket.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            JSONObject resultObject = new JSONObject();
            resultObject.put("code", "-1");
            bufferedWriter.write(resultObject.toString());
            bufferedWriter.write("\n");
            bufferedWriter.flush();
        } catch (Exception e) {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException ex) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void log(String name, String msg) {
        DaemonLog.log(name + " : " + msg);
    }
}
