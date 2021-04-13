package ThreadPool;

import ThreadPoolUtil.RunnableQueue;

/**
 * 线程池中负责处理任务的线程
 * @author 14005
 *
 */
public class InternalTask implements Runnable{

	private final RunnableQueue runnableQueue; //任务队列
	
	private volatile boolean running = true; //线程运行标志

	public InternalTask(RunnableQueue runnableQueue) {
		this.runnableQueue = runnableQueue;
	}
	
	@Override
	public void run() {
		while(running && !Thread.currentThread().isInterrupted()) {
			try {
				Runnable task = runnableQueue.Take();
				task.run();
			} catch(Exception e) {
				running = false;
				break;
			}
		}		
	}
	
	public void stop() {
		this.running = false;
	}
	
}
