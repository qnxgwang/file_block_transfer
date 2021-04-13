package ThreadPool;

import java.util.LinkedList;

import ThreadPoolUtil.DenyPolicy;
import ThreadPoolUtil.RunnableQueue;
import ThreadPoolUtil.ThreadPool;
/**
 * 封装的线程安全的队列
 * @author 14005
 *
 */
public class LinkedRunnableQueue implements RunnableQueue{
	
	private final int limit;//队列大小限制
	
	private final DenyPolicy denyPolicy;//拒绝任务策略
	
	private final LinkedList<Runnable> runnableList = new LinkedList<Runnable>();//队列
	
	private final ThreadPool threadPool;

	public LinkedRunnableQueue(int limit, DenyPolicy denyPolicy, ThreadPool threadPool) {
		super();
		this.limit = limit;
		this.denyPolicy = denyPolicy;
		this.threadPool = threadPool;
	}

	/**
	 * 向队列提交任务
	 */
	@Override
	public void offer(Runnable runnable) {
		synchronized(runnableList) {
			if(runnableList.size() >= limit) {
				denyPolicy.reject(runnable, threadPool);
			}else {
				runnableList.addLast(runnable);
				runnableList.notifyAll();
			}
		}	
	}

	/**
	 * 从队列取出任务
	 */
	@Override
	public Runnable Take() {
		synchronized(runnableList) {
			while(runnableList.isEmpty()) {
				try {
					runnableList.wait();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		return runnableList.removeFirst();
	}

	/**
	 * 返回队列大小
	 */
	@Override
	public int size() {
		synchronized(runnableList) {
			return runnableList.size();
		}
	}

}
