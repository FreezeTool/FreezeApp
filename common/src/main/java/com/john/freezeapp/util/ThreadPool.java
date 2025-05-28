package com.john.freezeapp.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private static final ExecutorService sExecutor = Executors.newFixedThreadPool(8);
    public static void execute(Runnable runnable) {
        sExecutor.execute(runnable);
    }


    public static ExecutorService createExecutorService(int threads) {
        return Executors.newFixedThreadPool(threads);
    }
}
