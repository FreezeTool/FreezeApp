package com.john.freezeapp;

import android.app.ActivityThread;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Keep;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Keep
public class Main {

    private static final ExecutorService sExecutorService = Executors.newFixedThreadPool(4);

    private static final ThreadPoolExecutor sCommandExecutorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);

    private static boolean stopServer = false;
    private static ServerSocket serverSocket = null;

    private static Context mContext = null;


    public static void main(String[] args) {
        log("main", "main start");
        try {
            Looper.prepareMainLooper();
            log("main", "main execute 1");
            ActivityThread activityThread =  ActivityThread.systemMain();
            log("main", "main execute 2");
            mContext = activityThread.getSystemContext();
            log("main", "main execute 3");
            startServer();
            log("main", "main execute 4");
            Looper.loop();
        } catch (Exception e) {
            log("main", "main Exception");
        }

    }

    private static void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(AppProcessHelper.PORT);
                    Log.d("freeze-server", "startServer");
                    int index = 0;
                    while (!stopServer) {
                        final Socket socket = serverSocket.accept();
                        index++;
                        sExecutorService.execute(new Task("TASK-" + index, socket));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("freeze-server", "startServer exception");
                }
            }
        }).start();
    }

    private static void log(String name, String msg) {
        Log.d("freeze-server", name + " : " + msg);
    }


    public static class Command implements Runnable {
        Socket socket;
        String command;
        String name;

        public Command(Socket socket, String command, String name) {
            this.socket = socket;
            this.command = command;
            this.name = name;
        }


        @Override
        public void run() {
            BufferedWriter bufferedWriter = null;
            try {
                Gson gson = new Gson();
                OutputStream outputStream = socket.getOutputStream();
                log(name, "shell execute start cmd=" + command);
                ShellUtils.ShellCommandResult commandResult = ShellUtils.execCommand(command, false);
                log(name, "shell execute end");

                log(name, "shell write start");
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                JSONObject resultObject = new JSONObject();
                resultObject.put("code", "0");
                resultObject.put("data", gson.toJson(commandResult));
                log(name, "result - " + resultObject.toString());
                bufferedWriter.write(resultObject.toString());
                bufferedWriter.write("\n");
                bufferedWriter.flush();
                log(name, "shell end");
            } catch (Exception e) {
                e.printStackTrace();
                doFail(name, socket);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static class Task implements Runnable {
        Socket socket;
        String name;

        public Task(String name, Socket socket) {
            this.name = name;
            this.socket = socket;
        }

        @Override
        public void run() {
            doHandle(socket);
        }

        private void log(String name, String msg) {
            Log.d("freeze-server", name + " : " + msg);
        }

        private void doHandle(Socket socket) {
            BufferedReader bufferedReader = null;
            boolean isFinish = false;
            log(name, "start");
            try {
                InputStream inputStream = socket.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String read = bufferedReader.readLine();
                JSONObject jsonObject = new JSONObject(read);
                String type = jsonObject.optString("type");
                log(name, "execute type=" + type);
                if (TextUtils.equals(type, AppProcessHelper.IPC_TYPE_SHELL)) {
                    String command = jsonObject.optString("data");
                    sCommandExecutorService.execute(new Command(socket, command, name));
                } else if (TextUtils.equals(type, AppProcessHelper.IPC_TYPE_BIND)) {
                    doBind(socket, name);
                    log(name, "bind end");
                    isFinish = true;
                } else if (TextUtils.equals(type, AppProcessHelper.IPC_TYPE_STOP)) {
                    doStop(socket, name);
                    log(name, "stop end");
                    isFinish = true;
                } else {
                    doFail(name, socket);
                    isFinish = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                doFail(name, socket);

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
                stopServerSocket();
            } catch (Exception e) {
                e.printStackTrace();
                doFail(name, socket);
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
                doFail(name, socket);
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

        private void stopServerSocket() {
            stopServer = true;
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                serverSocket = null;
            }
            sExecutorService.shutdown();
            sCommandExecutorService.shutdown();
            Looper.getMainLooper().quit();
        }
    }

    private static void doFail(String name, Socket socket) {
        log(name, "doFail");
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
}
