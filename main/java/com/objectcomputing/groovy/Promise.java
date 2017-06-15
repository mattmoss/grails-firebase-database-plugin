package com.objectcomputing.groovy;

import com.google.firebase.tasks.TaskCompletionSource;
import com.google.firebase.tasks.Tasks;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Promise<T> implements Future<T> {

    private final TaskCompletionSource<T> taskCompletionSource;

    public Promise() {
        this.taskCompletionSource = new TaskCompletionSource<>();
    }

    public Promise<T> accept(T value) {
        taskCompletionSource.trySetResult(value);
        return this;
    }

    public Promise<T> accept(Exception exception) {
        taskCompletionSource.trySetException(exception);
        return this;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return taskCompletionSource.getTask().isComplete();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return Tasks.await(taskCompletionSource.getTask());
    }

    @Override
    public T get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return Tasks.await(taskCompletionSource.getTask(), timeout, unit);
    }

}
