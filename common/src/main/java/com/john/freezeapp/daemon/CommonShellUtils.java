package com.john.freezeapp.daemon;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CommonShellUtils {

    public static final String COMMAND_SU = "su"; // 获取root权限的命令
    public static final String COMMAND_SH = "sh"; // 执行sh文件的命令
    public static final String COMMAND_EXIT = "exit\n"; // 退出的命令
    public static final String COMMAND_LINE_END = "\n"; // 执行命令必须加在末尾

    private static final ExecutorService sExecutor = Executors.newCachedThreadPool();

    public static void stop() {
        sExecutor.shutdown();
    }

    private CommonShellUtils() {
        throw new AssertionError();
    }

    public static void execCommand(String command, boolean isRoot, ShellCommandResultCallback callback) {
        execCommand(new String[]{command}, isRoot, 5000, callback);
    }

    public static void execCommand(String command, boolean isRoot, long timeout, ShellCommandResultCallback callback) {
        execCommand(new String[]{command}, isRoot, timeout, callback);
    }

    public static void execCommand(List<String> commands, boolean isRoot, long timeout, ShellCommandResultCallback callback) {
        execCommand(commands == null ? null : commands.toArray(new String[]{}), isRoot, timeout, callback);
    }


    //执行命令,获得返回的信息
    public static void execCommand(String[] commands, boolean isRoot, long timeout, ShellCommandResultCallback callback) {
        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                boolean result = false;
                if (commands == null || commands.length == 0) {
                    if (callback != null) {
                        callback.callback(new ShellCommandResult(result, null, null));
                    }
                    return;
                }
                CountDownLatch countDownLatch = new CountDownLatch(2);
                Process process = null;
                StringBuilder successMsg = new StringBuilder();
                StringBuilder errorMsg = new StringBuilder();
                DataOutputStream os = null;
                try {
                    process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
                    os = new DataOutputStream(process.getOutputStream());
                    for (String command : commands) {
                        if (command == null) {
                            continue;
                        }
                        os.write(command.getBytes());
                        os.writeBytes(COMMAND_LINE_END);
                        os.flush();
                    }
                    os.writeBytes(COMMAND_EXIT);
                    os.flush();
                    sExecutor.execute(new CommandTask(process, successMsg) {
                        @Override
                        public void run() {
                            BufferedReader successResult = null;
                            try {
                                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                                String s;
                                while ((s = successResult.readLine()) != null) {
                                    stringBuilder.append(s + "\n");
                                }
                            } catch (Exception e) {
                                //
                            } finally {
                                if (successResult != null) {
                                    try {
                                        successResult.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            countDownLatch.countDown();
                        }
                    });

                    sExecutor.execute(new CommandTask(process, errorMsg) {
                        @Override
                        public void run() {
                            BufferedReader errorResult = null;
                            try {
                                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                                String s = "";
                                while ((s = errorResult.readLine()) != null) {
                                    stringBuilder.append(s + "\n");
                                }
                            } catch (Exception e) {
                                //
                            } finally {
                                if (errorResult != null) {
                                    try {
                                        errorResult.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            countDownLatch.countDown();
                        }
                    });
                    result = waitFor(process, timeout, TimeUnit.MILLISECONDS);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (os != null) {
                            os.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (process != null) {
                        process.destroy();
                    }
                }
                try {
                    countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (callback != null) {
                    callback.callback(new ShellCommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null : errorMsg.toString()));
                }
            }
        });
    }


    public static boolean waitFor(Process process, long timeout, TimeUnit unit) throws InterruptedException {
        long startTime = System.nanoTime();
        long rem = unit.toNanos(timeout);
        boolean result = false;
        do {
            try {
                process.exitValue();
                return true;
            } catch (IllegalThreadStateException ex) {
                if (rem > 0)
                    Thread.sleep(Math.min(TimeUnit.NANOSECONDS.toMillis(rem) + 1, 100));
            }
            rem = unit.toNanos(timeout) - (System.nanoTime() - startTime);
        } while (rem > 0);
        return result;
    }


    public static boolean requestRoot() {

        try {
            Process process = Runtime.getRuntime().exec("su -c true");
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return true;
            }

        } catch (Exception e) {
            //
        }
        return false;
    }

    public interface ShellCommandResultCallback {
        void callback(ShellCommandResult commandResult);
    }

    static abstract class CommandTask implements Runnable {
        StringBuilder stringBuilder;
        Process process;

        public CommandTask(Process process, StringBuilder stringBuilder) {
            this.process = process;
            this.stringBuilder = stringBuilder;
        }
    }

    //封装了返回信息
    public static class ShellCommandResult {

        public boolean result;
        public String successMsg; //成功信息
        public String errorMsg; // 错误信息

        public ShellCommandResult(boolean result) {
            this.result = result;
        }

        public ShellCommandResult(boolean result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }

        @Override
        public String toString() {
            return "ShellCommandResult{" + "result=" + result + ", successMsg='" + successMsg + ", errorMsg='" + errorMsg + '}';
        }
    }
}