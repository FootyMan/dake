package com.unicom.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPollUnicom {

	public static ExecutorService executors=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
}
