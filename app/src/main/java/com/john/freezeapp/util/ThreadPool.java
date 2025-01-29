package com.john.freezeapp.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private static ExecutorService sExecutor = Executors.newFixedThreadPool(4);

    public static void execute(Runnable runnable) {
        sExecutor.execute(runnable);
    }
}
