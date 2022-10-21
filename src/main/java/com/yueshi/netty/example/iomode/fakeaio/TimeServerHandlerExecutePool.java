package com.yueshi.netty.example.iomode.fakeaio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TimeServerHandlerExecutePool
 *
 * @author dengzihui
 * @version 1.0
 * @date 2021/6/1 8:44 PM
 */
public class TimeServerHandlerExecutePool {

	private ExecutorService executor;

	public TimeServerHandlerExecutePool(int corePoolSize, int maxPoolSize, int queueSize) {
		executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 120L, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(queueSize));
	}

	public void execute(Runnable task) {
		executor.execute(task);
	}

}
