package net.ahexample.concurrency;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Example of an executor service that is shared by multiple threads.
 * 
 * This is a Java 7 version of {@link SharedExecutorService}.
 */
public class SharedExecutorServiceJava7 {
    private static final int MAX_RESULTS = 3;
    private static final int MAX_THREADS = 10;
    
    private final ExecutorService executorService;


    private static class Result {
        private int index;
        private long threadId;
        private Date startTime;
        private Date endTime;
    }
    
    
    private interface TaskRunner {
        void runTasks(SharedExecutorServiceJava7 es, String executorId) throws Exception;
    }
    
    
    public SharedExecutorServiceJava7(ExecutorService executorService) {
        this.executorService = executorService;
    }
    
    
    public void runCompletionTasks(String executorId) throws Exception {
        final CompletionService<Result> completionService = new ExecutorCompletionService<>(executorService);
        final List<Callable<Result>> callables = makeCallables(executorId, MAX_RESULTS);

        print(executorId, "Callables = " + callables.size());
        print(executorId, "Submitting callables: time = " + new Date());
        for (Callable<Result> callable : callables) {
            completionService.submit(callable);
        }

        print(executorId, "Waiting for callable results: time = " + new Date());
        for (int i = 0; i < callables.size(); i++) {
            try {
                final Result result = completionService.take().get();
                
                print(executorId, "result[" + result.index
                                    + "], threadId = " + result.threadId
                                    + ", time = " + new Date()
                                    + ", startTime = " + result.startTime
                                    + ", endTime = " + result.endTime);
            }
            catch (InterruptedException | ExecutionException e) {
                throw e;
            }
        };
        
        print(executorId, "### execution completed");
    }
    
    
    public void runExecutorTasks(String executorId) throws Exception {
        final List<Callable<Result>> callables = makeCallables(executorId, MAX_RESULTS);
        final List<Future<Result>> futures = new ArrayList<>();

        print(executorId, "Callables = " + callables.size());
        print(executorId, "Submitting callables: time = " + new Date());
        for (Callable<Result> callable : callables) {
            futures.add(executorService.submit(callable));
        }

        print(executorId, "Waiting for callable results: time = " + new Date());
        for (int i =  0; i < futures.size(); i++) {
            try {
                final Result result = futures.get(i).get();
                
                print(executorId, "result[" + result.index
                                    + "], threadId = " + result.threadId
                                    + ", time = " + new Date()
                                    + ", startTime = " + result.startTime
                                    + ", endTime = " + result.endTime);
            }
            catch (InterruptedException | ExecutionException e) {
                throw e;
            }
        };
        
        print(executorId, "### execution completed");
    }
    
    
    public static void main(String[] args) throws InterruptedException {
        final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        final List<Thread> threads = new ArrayList<>();

        print("main", "Threads = " + MAX_THREADS);
        print("main", "Main thread Id = " + Thread.currentThread().getId());
        
        threads.add(runTasks(executor, "--- CS_1", 
                new TaskRunner() {
                    @Override
                    public void runTasks(SharedExecutorServiceJava7 es, String id)
                            throws Exception {
                        es.runCompletionTasks(id);
                    }
            
        }));
        
        threads.add(runTasks(executor, "+++ CS_2", 
                new TaskRunner() {
                    @Override
                    public void runTasks(SharedExecutorServiceJava7 es, String id)
                            throws Exception {
                        es.runCompletionTasks(id);
                    }
            
        }));
        
        threads.add(runTasks(executor, "=== E_1", 
                new TaskRunner() {
                    @Override
                    public void runTasks(SharedExecutorServiceJava7 es, String id)
                            throws Exception {
                        es.runExecutorTasks(id);
                    }
            
        }));
        
        try {
            for (Thread thread : threads) {
                thread.start();
            }
            
            while (true) {
                boolean anyThreadAlive = false;
                
                for (Thread thread : threads) {
                    if (thread.isAlive()) {
                        anyThreadAlive = true;
                        break;
                    }
                }
                
                if (anyThreadAlive) {
                    Thread.sleep(1000);
                }
                else {
                    break;
                }
            }
        }
        finally {
            executor.shutdown();
        }
    }


    private static Thread runTasks(final ExecutorService executor, final String executorId,
                        final TaskRunner taskRunner) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                final SharedExecutorServiceJava7 es = new SharedExecutorServiceJava7(executor);
                
                try {
                    taskRunner.runTasks(es, executorId);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    
    private List<Callable<Result>> makeCallables(String executorId, final int numberOfResults) {
        final List<Callable<Result>> retval = new ArrayList<>();
        
        for (int i = 0; i < numberOfResults; i++) {
            final int index = i;
            
            retval.add((new Callable<Result>() {
                @Override
                public Result call() throws Exception {
                    final Result result = new Result();

                    result.index = index;
                    result.threadId = Thread.currentThread().getId();
                    result.startTime = new Date();
                    Thread.sleep((numberOfResults - index) * 1000);
                    result.endTime = new Date();
                    
                    return result;
                }
            }));
        }
        
        return retval;
    }
    
    
    private static void print(String executorId, String str) {
        System.out.println(executorId + ": " + str);
    }
}
