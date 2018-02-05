package bolts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Task<TResult> {
    public static final ExecutorService BACKGROUND_EXECUTOR = BoltsExecutors.background();
    private static final Executor IMMEDIATE_EXECUTOR = BoltsExecutors.immediate();
    public static final Executor UI_THREAD_EXECUTOR = AndroidExecutors.uiThread();
    private boolean cancelled;
    private boolean complete;
    private List<Continuation<TResult, Void>> continuations = new ArrayList();
    private Exception error;
    private final Object lock = new Object();
    private TResult result;

    final class AnonymousClass10 implements Runnable {
        final /* synthetic */ Continuation val$continuation;
        final /* synthetic */ Task val$task;
        final /* synthetic */ TaskCompletionSource val$tcs;

        AnonymousClass10(Continuation continuation, Task task, TaskCompletionSource taskCompletionSource) {
            this.val$continuation = continuation;
            this.val$task = task;
            this.val$tcs = taskCompletionSource;
        }

        public void run() {
            try {
                Task task = (Task) this.val$continuation.then(this.val$task);
                if (task == null) {
                    this.val$tcs.setResult(null);
                } else {
                    task.continueWith(new Continuation<TContinuationResult, Void>() {
                        public Void then(Task<TContinuationResult> task) {
                            if (task.isCancelled()) {
                                AnonymousClass10.this.val$tcs.setCancelled();
                            } else if (task.isFaulted()) {
                                AnonymousClass10.this.val$tcs.setError(task.getError());
                            } else {
                                AnonymousClass10.this.val$tcs.setResult(task.getResult());
                            }
                            return null;
                        }
                    });
                }
            } catch (Exception e) {
                this.val$tcs.setError(e);
            }
        }
    }

    final class AnonymousClass2 implements Runnable {
        final /* synthetic */ Callable val$callable;
        final /* synthetic */ TaskCompletionSource val$tcs;

        AnonymousClass2(TaskCompletionSource taskCompletionSource, Callable callable) {
            this.val$tcs = taskCompletionSource;
            this.val$callable = callable;
        }

        public void run() {
            try {
                this.val$tcs.setResult(this.val$callable.call());
            } catch (Exception e) {
                this.val$tcs.setError(e);
            }
        }
    }

    final class AnonymousClass3 implements Continuation<Object, Void> {
        final /* synthetic */ TaskCompletionSource val$allFinished;
        final /* synthetic */ ArrayList val$causes;
        final /* synthetic */ AtomicInteger val$count;
        final /* synthetic */ Object val$errorLock;
        final /* synthetic */ AtomicBoolean val$isCancelled;

        AnonymousClass3(Object obj, ArrayList arrayList, AtomicBoolean atomicBoolean, AtomicInteger atomicInteger, TaskCompletionSource taskCompletionSource) {
            this.val$errorLock = obj;
            this.val$causes = arrayList;
            this.val$isCancelled = atomicBoolean;
            this.val$count = atomicInteger;
            this.val$allFinished = taskCompletionSource;
        }

        public Void then(Task<Object> task) {
            if (task.isFaulted()) {
                synchronized (this.val$errorLock) {
                    this.val$causes.add(task.getError());
                }
            }
            if (task.isCancelled()) {
                this.val$isCancelled.set(true);
            }
            if (this.val$count.decrementAndGet() == 0) {
                if (this.val$causes.size() != 0) {
                    if (this.val$causes.size() == 1) {
                        this.val$allFinished.setError((Exception) this.val$causes.get(0));
                    } else {
                        this.val$allFinished.setError(new AggregateException(String.format("There were %d exceptions.", new Object[]{Integer.valueOf(this.val$causes.size())}), (Throwable[]) this.val$causes.toArray(new Throwable[this.val$causes.size()])));
                    }
                } else if (this.val$isCancelled.get()) {
                    this.val$allFinished.setCancelled();
                } else {
                    this.val$allFinished.setResult(null);
                }
            }
            return null;
        }
    }

    final class AnonymousClass9 implements Runnable {
        final /* synthetic */ Continuation val$continuation;
        final /* synthetic */ Task val$task;
        final /* synthetic */ TaskCompletionSource val$tcs;

        AnonymousClass9(Continuation continuation, Task task, TaskCompletionSource taskCompletionSource) {
            this.val$continuation = continuation;
            this.val$task = task;
            this.val$tcs = taskCompletionSource;
        }

        public void run() {
            try {
                this.val$tcs.setResult(this.val$continuation.then(this.val$task));
            } catch (Exception e) {
                this.val$tcs.setError(e);
            }
        }
    }

    public class TaskCompletionSource {
        private TaskCompletionSource() {
        }

        public Task<TResult> getTask() {
            return Task.this;
        }

        public boolean trySetCancelled() {
            boolean z = true;
            synchronized (Task.this.lock) {
                if (Task.this.complete) {
                    z = false;
                } else {
                    Task.this.complete = true;
                    Task.this.cancelled = true;
                    Task.this.lock.notifyAll();
                    Task.this.runContinuations();
                }
            }
            return z;
        }

        public boolean trySetResult(TResult tResult) {
            boolean z = true;
            synchronized (Task.this.lock) {
                if (Task.this.complete) {
                    z = false;
                } else {
                    Task.this.complete = true;
                    Task.this.result = tResult;
                    Task.this.lock.notifyAll();
                    Task.this.runContinuations();
                }
            }
            return z;
        }

        public boolean trySetError(Exception exception) {
            boolean z = true;
            synchronized (Task.this.lock) {
                if (Task.this.complete) {
                    z = false;
                } else {
                    Task.this.complete = true;
                    Task.this.error = exception;
                    Task.this.lock.notifyAll();
                    Task.this.runContinuations();
                }
            }
            return z;
        }

        public void setCancelled() {
            if (!trySetCancelled()) {
                throw new IllegalStateException("Cannot cancel a completed task.");
            }
        }

        public void setResult(TResult tResult) {
            if (!trySetResult(tResult)) {
                throw new IllegalStateException("Cannot set the result of a completed task.");
            }
        }

        public void setError(Exception exception) {
            if (!trySetError(exception)) {
                throw new IllegalStateException("Cannot set the error on a completed task.");
            }
        }
    }

    private Task() {
    }

    public static <TResult> TaskCompletionSource create() {
        Task task = new Task();
        task.getClass();
        return new TaskCompletionSource();
    }

    public boolean isCompleted() {
        boolean z;
        synchronized (this.lock) {
            z = this.complete;
        }
        return z;
    }

    public boolean isCancelled() {
        boolean z;
        synchronized (this.lock) {
            z = this.cancelled;
        }
        return z;
    }

    public boolean isFaulted() {
        boolean z;
        synchronized (this.lock) {
            z = this.error != null;
        }
        return z;
    }

    public TResult getResult() {
        TResult tResult;
        synchronized (this.lock) {
            tResult = this.result;
        }
        return tResult;
    }

    public Exception getError() {
        Exception exception;
        synchronized (this.lock) {
            exception = this.error;
        }
        return exception;
    }

    public void waitForCompletion() {
        synchronized (this.lock) {
            if (!isCompleted()) {
                this.lock.wait();
            }
        }
    }

    public static <TResult> Task<TResult> forResult(TResult tResult) {
        TaskCompletionSource create = create();
        create.setResult(tResult);
        return create.getTask();
    }

    public static <TResult> Task<TResult> forError(Exception exception) {
        TaskCompletionSource create = create();
        create.setError(exception);
        return create.getTask();
    }

    public static <TResult> Task<TResult> cancelled() {
        TaskCompletionSource create = create();
        create.setCancelled();
        return create.getTask();
    }

    public <TOut> Task<TOut> cast() {
        return this;
    }

    public Task<Void> makeVoid() {
        return continueWithTask(new Continuation<TResult, Task<Void>>() {
            public Task<Void> then(Task<TResult> task) {
                if (task.isCancelled()) {
                    return Task.cancelled();
                }
                if (task.isFaulted()) {
                    return Task.forError(task.getError());
                }
                return Task.forResult(null);
            }
        });
    }

    public static <TResult> Task<TResult> callInBackground(Callable<TResult> callable) {
        return call(callable, BACKGROUND_EXECUTOR);
    }

    public static <TResult> Task<TResult> call(Callable<TResult> callable, Executor executor) {
        TaskCompletionSource create = create();
        executor.execute(new AnonymousClass2(create, callable));
        return create.getTask();
    }

    public static <TResult> Task<TResult> call(Callable<TResult> callable) {
        return call(callable, IMMEDIATE_EXECUTOR);
    }

    public static Task<Void> whenAll(Collection<? extends Task<?>> collection) {
        if (collection.size() == 0) {
            return forResult(null);
        }
        TaskCompletionSource create = create();
        ArrayList arrayList = new ArrayList();
        Object obj = new Object();
        AtomicInteger atomicInteger = new AtomicInteger(collection.size());
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        for (Task continueWith : collection) {
            continueWith.continueWith(new AnonymousClass3(obj, arrayList, atomicBoolean, atomicInteger, create));
        }
        return create.getTask();
    }

    public Task<Void> continueWhile(Callable<Boolean> callable, Continuation<Void, Task<Void>> continuation) {
        return continueWhile(callable, continuation, IMMEDIATE_EXECUTOR);
    }

    public Task<Void> continueWhile(Callable<Boolean> callable, Continuation<Void, Task<Void>> continuation, Executor executor) {
        final Capture capture = new Capture();
        final Callable<Boolean> callable2 = callable;
        final Continuation<Void, Task<Void>> continuation2 = continuation;
        final Executor executor2 = executor;
        capture.set(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) {
                if (((Boolean) callable2.call()).booleanValue()) {
                    return Task.forResult(null).onSuccessTask(continuation2, executor2).onSuccessTask((Continuation) capture.get(), executor2);
                }
                return Task.forResult(null);
            }
        });
        return makeVoid().continueWithTask((Continuation) capture.get(), executor);
    }

    public <TContinuationResult> Task<TContinuationResult> continueWith(final Continuation<TResult, TContinuationResult> continuation, final Executor executor) {
        final TaskCompletionSource create = create();
        synchronized (this.lock) {
            boolean isCompleted = isCompleted();
            if (!isCompleted) {
                this.continuations.add(new Continuation<TResult, Void>() {
                    public Void then(Task<TResult> task) {
                        Task.completeImmediately(create, continuation, task, executor);
                        return null;
                    }
                });
            }
        }
        if (isCompleted) {
            completeImmediately(create, continuation, this, executor);
        }
        return create.getTask();
    }

    public <TContinuationResult> Task<TContinuationResult> continueWith(Continuation<TResult, TContinuationResult> continuation) {
        return continueWith(continuation, IMMEDIATE_EXECUTOR);
    }

    public <TContinuationResult> Task<TContinuationResult> continueWithTask(final Continuation<TResult, Task<TContinuationResult>> continuation, final Executor executor) {
        final TaskCompletionSource create = create();
        synchronized (this.lock) {
            boolean isCompleted = isCompleted();
            if (!isCompleted) {
                this.continuations.add(new Continuation<TResult, Void>() {
                    public Void then(Task<TResult> task) {
                        Task.completeAfterTask(create, continuation, task, executor);
                        return null;
                    }
                });
            }
        }
        if (isCompleted) {
            completeAfterTask(create, continuation, this, executor);
        }
        return create.getTask();
    }

    public <TContinuationResult> Task<TContinuationResult> continueWithTask(Continuation<TResult, Task<TContinuationResult>> continuation) {
        return continueWithTask(continuation, IMMEDIATE_EXECUTOR);
    }

    public <TContinuationResult> Task<TContinuationResult> onSuccess(final Continuation<TResult, TContinuationResult> continuation, Executor executor) {
        return continueWithTask(new Continuation<TResult, Task<TContinuationResult>>() {
            public Task<TContinuationResult> then(Task<TResult> task) {
                if (task.isFaulted()) {
                    return Task.forError(task.getError());
                }
                if (task.isCancelled()) {
                    return Task.cancelled();
                }
                return task.continueWith(continuation);
            }
        }, executor);
    }

    public <TContinuationResult> Task<TContinuationResult> onSuccess(Continuation<TResult, TContinuationResult> continuation) {
        return onSuccess(continuation, IMMEDIATE_EXECUTOR);
    }

    public <TContinuationResult> Task<TContinuationResult> onSuccessTask(final Continuation<TResult, Task<TContinuationResult>> continuation, Executor executor) {
        return continueWithTask(new Continuation<TResult, Task<TContinuationResult>>() {
            public Task<TContinuationResult> then(Task<TResult> task) {
                if (task.isFaulted()) {
                    return Task.forError(task.getError());
                }
                if (task.isCancelled()) {
                    return Task.cancelled();
                }
                return task.continueWithTask(continuation);
            }
        }, executor);
    }

    public <TContinuationResult> Task<TContinuationResult> onSuccessTask(Continuation<TResult, Task<TContinuationResult>> continuation) {
        return onSuccessTask(continuation, IMMEDIATE_EXECUTOR);
    }

    private static <TContinuationResult, TResult> void completeImmediately(TaskCompletionSource taskCompletionSource, Continuation<TResult, TContinuationResult> continuation, Task<TResult> task, Executor executor) {
        executor.execute(new AnonymousClass9(continuation, task, taskCompletionSource));
    }

    private static <TContinuationResult, TResult> void completeAfterTask(TaskCompletionSource taskCompletionSource, Continuation<TResult, Task<TContinuationResult>> continuation, Task<TResult> task, Executor executor) {
        executor.execute(new AnonymousClass10(continuation, task, taskCompletionSource));
    }

    private void runContinuations() {
        synchronized (this.lock) {
            for (Continuation then : this.continuations) {
                try {
                    then.then(this);
                } catch (RuntimeException e) {
                    throw e;
                } catch (Throwable e2) {
                    throw new RuntimeException(e2);
                }
            }
            this.continuations = null;
        }
    }
}
