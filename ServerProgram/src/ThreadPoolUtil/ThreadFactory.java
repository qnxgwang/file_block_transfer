package ThreadPoolUtil;

public interface ThreadFactory {

	Thread createThread(Runnable runnable);
		
}
