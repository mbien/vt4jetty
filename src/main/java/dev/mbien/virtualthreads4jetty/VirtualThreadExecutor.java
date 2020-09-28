package dev.mbien.virtualthreads4jetty;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.util.thread.ThreadPool;

/**
 * Executes each task in a new virtual thread.
 * 
 * <p>Java's default {@link java.util.concurrent.ForkJoinPool ForkJoinPool} is used as scheduler. To influence carrier
 * thread count use -Djdk.defaultScheduler.parallelism=N. Default is
 * {@link Runtime#availableProcessors()}.
 * 
 * <p>
 * Async mode (FIFO) of 
 * {@link java.util.concurrent.ForkJoinPool#ForkJoinPool(int, ForkJoinPool.ForkJoinWorkerThreadFactory, UncaughtExceptionHandler, boolean) new ForkJoinPool(..., asyncMode)}
 * is true by default and can be changed to false by setting -Djdk.defaultScheduler.lifo=true. 
 * 
 * @author mbien
 */
public class VirtualThreadExecutor implements ThreadPool {
    
    private final ExecutorService executor;

    public VirtualThreadExecutor() {
        this(false);
    }

    public VirtualThreadExecutor(boolean disallowThreadLocals) {
        Thread.Builder builder = Thread.builder().virtual().name("jetty-vt#", 0);
        if(disallowThreadLocals)
            builder.disallowThreadLocals();
        
        executor = Executors.newThreadExecutor(builder.factory());
        System.out.println("VirtualThreadExecutor is active."); // too early for logging
    }
    
    @Override
    public void execute(Runnable command) {
        executor.execute(command);
    }

    @Override
    public void join() throws InterruptedException {
        // @see ExecutorThreadPool::join()
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    // those are hopefully only used for stats/dashboards etc
    @Override
    public int getThreads() { return -1; }

    @Override
    public int getIdleThreads() { return -1; }

    @Override
    public boolean isLowOnThreads() { return false; }
    
}
