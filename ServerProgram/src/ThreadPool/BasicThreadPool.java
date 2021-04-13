package ThreadPool;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import ThreadPoolUtil.DenyPolicy;
import ThreadPoolUtil.RunnableQueue;
import ThreadPoolUtil.ThreadFactory;
import ThreadPoolUtil.ThreadPool;
/**
 * 实现了ThreadPool接口的线程池
 * @author 14005
 *
 */
public class BasicThreadPool extends Thread implements ThreadPool{
	
	/**
	 * 工作线程的一个组合
	 * @author 14005
	 *
	 */
	private static class ThreadTask{
		
		Thread thread;
		
		InternalTask internalTask;
		
		public ThreadTask(Thread thread,InternalTask internalTask) {
			this.thread = thread;
			this.internalTask = internalTask;
		}
	}
	
	/**
	 * 负责命名的线程工厂
	 * @author 14005
	 *
	 */
	private static class DefaultThreadFactory implements ThreadFactory{

		private static final AtomicInteger GROUP_COUNTER = new AtomicInteger(1);
		
		private static final ThreadGroup group = new ThreadGroup("MyThreadPool-"+GROUP_COUNTER.getAndIncrement());
		
		private static final AtomicInteger COUNTER = new AtomicInteger(0);
		
		@Override
		public Thread createThread(Runnable runnable) {
			return new Thread(group,runnable,"thread-pool-"+COUNTER.getAndIncrement());
		}		
	}

	
	private final int initSize;//初始化线程数量
	
	private final int coreSize;//期望的线程数量
	
	private final int maxSize;//最大的线程数量
	
	private int activeCount;//活跃的线程数量
	
	private final ThreadFactory threadFactory;
	
	private final RunnableQueue runnableQueue; //任务队列
	
	private volatile boolean isShutDown = false;
	
	private final Queue<ThreadTask> threadQueue = new ArrayDeque<>();//工作线程队列
	
	private final static DenyPolicy DEFAULT_DENY_POLICY = new DenyPolicy.DiscardDenyPolicy();
	
	private final static ThreadFactory DEFAULT_THREAD_FACTORY = new DefaultThreadFactory();
	
	private final long keepAliveTime;
	
	private final TimeUnit timeUnit;
	
	public BasicThreadPool(int initSize, int coreSize, int maxSize, int queueSize) {
		this(initSize, coreSize, maxSize, queueSize,
				DEFAULT_THREAD_FACTORY, DEFAULT_DENY_POLICY, 10, TimeUnit.SECONDS);
	}
	
	public BasicThreadPool(int initSize, int coreSize, int maxSize,int queueSize, ThreadFactory threadFactory,
			DenyPolicy denyPolicy, long keepAliveTime, TimeUnit timeUnit) {
		super();
		this.initSize = initSize;
		this.coreSize = coreSize;
		this.maxSize = maxSize;
		this.threadFactory = threadFactory;
		this.runnableQueue = new LinkedRunnableQueue(queueSize, denyPolicy, this);
		this.keepAliveTime = keepAliveTime;
		this.timeUnit = timeUnit;
		this.init();
	}
	
	/**
	 * 初始化若干个线程
	 */
	private void init() {
		start();
		for(int i=0;i<initSize;i++) {
			newThread();
		}
	}
	
	/**
	 * 新建一个线程
	 */
	private void newThread() {
		InternalTask internalTask = new InternalTask(runnableQueue);
		Thread thread = this.threadFactory.createThread(internalTask);
		ThreadTask threadTask = new ThreadTask(thread, internalTask);
		threadQueue.offer(threadTask);
		this.activeCount++;
		thread.start();
	}
	
	/**
	 * 销毁一个线程
	 */
	private void removeThread() {
		ThreadTask threadTask = threadQueue.remove();
		threadTask.internalTask.stop();
		this.activeCount--;
	}

	/**
	 * 提交任务方法
	 */
	@Override
	public void execute(Runnable runnable) {
		if(this.isShutDown)
			throw new IllegalStateException("The thread pool is destory");
		this.runnableQueue.offer(runnable);
		
	}

	/**
	 * 自动管理线程池，进行扩容，回收操作等
	 */
	@Override
	public void run() {
		while(!isShutDown && !isInterrupted()) {
			synchronized(this){
				if(isShutDown)
					break;
				if(runnableQueue.size() > 0 && activeCount < coreSize) {
					for(int i=activeCount; i<coreSize; i++) {
						newThread();
					}
					continue;
				}
				if(runnableQueue.size() > 0 && activeCount < maxSize) {
					for(int i=activeCount; i<maxSize; i++) {
						newThread();
					}
				}
				if(runnableQueue.size() == 0 && activeCount > coreSize) {
					for(int i=coreSize; i<activeCount; i++) {
						removeThread();
					}
				}				
			}
		}
	}

	/**
	 * 销毁线程池
	 */
	@Override
	public void shutDown() {
		synchronized(this) {
			if(isShutDown) return;
			isShutDown = true;
			threadQueue.forEach(threadTask ->{
				threadTask.internalTask.stop();
				threadTask.thread.interrupt();
			});
			this.interrupt();
		}		
	}

	/**
	 * 未实现的方法
	 */
	@Override
	public int getInitSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCoreSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getQueueSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getActiveSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isShutDown() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
