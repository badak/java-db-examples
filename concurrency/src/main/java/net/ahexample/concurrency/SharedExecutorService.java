package net.ahexample.concurrency;

import java.time.LocalTime;
import java.util.ArrayList;
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
 */
public class SharedExecutorService {
    private static final int MAX_RESULTS = 3;
    private static final int MAX_THREADS = 10;
    
    private final ExecutorService executorService;


    private static class Result {
        private int index;
        private long threadId;
    }
    
    
    private interface TaskRunner {
        void runTasks(SharedExecutorService es, String executorId) throws Exception;
    }
    
    
    public SharedExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
    
    
    public void runCompletionTasks(String executorId) throws Exception {
        final CompletionService<Result> completionService = new ExecutorCompletionService<>(executorService);
        final List<Callable<Result>> callables = makeCallables(executorId, MAX_RESULTS);

        print(executorId, "Callables = " + callables.size());
        print(executorId, "Submitting callables: time = " + LocalTime.now());
        callables.forEach(callable -> {
            completionService.submit(callable);
        });

        print(executorId, "Waiting for callable results: time = " + LocalTime.now());
        for (int i = 0; i < callables.size(); i++) {
            try {
                final Result result = completionService.take().get();
                
                print(executorId, "result[" + result.index
                                    + "], threadId = " + result.threadId
                                    + ", time = " + LocalTime.now());
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
        print(executorId, "Submitting callables: time = " + LocalTime.now());
        callables.forEach(callable -> {
            futures.add(executorService.submit(callable));
        });

        print(executorId, "Waiting for callable results: time = " + LocalTime.now());
        for (int i = 0; i < futures.size(); i++) {
            try {
                final Result result = futures.get(i).get();
                
                print(executorId, "result[" + result.index
                                    + "], threadId = " + result.threadId
                                    + ", time = " + LocalTime.now());
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
        
        threads.add(runTasks(executor, "--- CS_1", (es, id) -> es.runCompletionTasks(id)));
        threads.add(runTasks(executor, "+++ CS_2", (es, id) -> es.runCompletionTasks(id)));
        threads.add(runTasks(executor, "=== E_1", (es, id) -> es.runExecutorTasks(id)));
        
        try {
            threads.forEach(thread ->
                thread.start()
            );
            
            while (threads.stream().anyMatch(thread -> thread.isAlive())) {
                Thread.sleep(1000);
            }
        }
        finally {
            executor.shutdown();
        }
    }


    private static Thread runTasks(final ExecutorService executor, final String executorId,
                        TaskRunner taskRunner) {
        return new Thread(() -> {
            final SharedExecutorService es = new SharedExecutorService(executor);
            
            try {
                taskRunner.runTasks(es, executorId);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    
    private List<Callable<Result>> makeCallables(String executorId, int numberOfResults) {
        final List<Callable<Result>> retval = new ArrayList<>();
        
        for (int i = 0; i < numberOfResults; i++) {
            final int index = i;
            
            retval.add(() -> {
                final Result result = new Result();
                
                result.index = index;
                result.threadId = Thread.currentThread().getId();
                Thread.sleep((numberOfResults - index) * 1000);
                
                return result;
            });
        }
        
        return retval;
    }
    
    
    private static void print(String executorId, String str) {
        System.out.println(executorId + ": " + str);
    }
}
