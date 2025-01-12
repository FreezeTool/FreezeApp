package com.john.freezeapp;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShellClient {

    private static final ExecutorService sExecutorService = Executors.newFixedThreadPool(4);

    public static void stop(Callback callback) {
        doHandle("stop", "", callback);
    }

    interface Callback {
        void success(String data);

        void fail();
    }

    public static void bind(Callback callback) {
        doHandle("bind", "", callback);
    }

    private static void doHandle(String type, String data, Callback callback) {
        sExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                BufferedWriter bufferedWriter = null;
                BufferedReader bufferedReader = null;
                try {
                    Socket socket = new Socket("localhost",33456);
                    socket.setSoTimeout(5000);
                    OutputStream outputStream = socket.getOutputStream();
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", type);
                    jsonObject.put("data", data);
                    bufferedWriter.write(jsonObject.toString());
                    bufferedWriter.write("\n");
                    bufferedWriter.flush();
                    InputStream inputStream = socket.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    String s = bufferedReader.readLine();
                    callback.success(s);

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.fail();
                } finally {
                    if (bufferedWriter != null) {
                        try {
                            bufferedWriter.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedWriter.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

    public static void command(String command, Callback callback) {
        doHandle("shell", command, callback);
    }
}
