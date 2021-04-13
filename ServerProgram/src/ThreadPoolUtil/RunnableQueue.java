package ThreadPoolUtil;

public interface RunnableQueue {
	
	void offer(Runnable runnable);
	
	Runnable Take();
	
	int size();

}
