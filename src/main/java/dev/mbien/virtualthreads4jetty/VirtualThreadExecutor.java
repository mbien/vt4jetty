package dev.mbien.virtualthreads4jetty;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.util.thread.ThreadPool;

/**
 * Executes each task in a new virtual thread.
 * 
 * <p>Java's default ForkJoinPool is used as scheduler. To influence carrier
 * thread count use -Djdk.defaultScheduler.parallelism=N. Default is
 * {@link Runtime#availableProcessors()}.
 * 
 * @author mbien
 */
public class VirtualThreadExecutor implements ThreadPool {
    
    private final ExecutorService executor;

    public VirtualThreadExecutor() {
        executor = Executors.newThreadExecutor(
                Thread.builder().virtual().name("jetty-vt#", 0).factory());
        System.out.println("VirtualThreadExecutor is active."); // too early for logging
    }
    
    @Override
    public void execute(Runnable command) {
//        System.out.println("executing: "+command);
        executor.execute(command);
    }

    @Override
    public void join() throws InterruptedException {
        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
    }

    // those are hopefully only used for stats/dashboards etc
    @Override
    public int getThreads() { return -1; }

    @Override
    public int getIdleThreads() { return -1; }

    @Override
    public boolean isLowOnThreads() { return false; }
    
}
