package com.objectcomputing.firebase.tasks;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.tasks.*;
import groovy.lang.Closure;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TaskExtension {

    /**
     * Attach a Groovy closure that is called when the task succeeds.
     *
     * @param self Firebase asynchronous task
     * @param closure Groovy closure { T result -> ... } called when the task succeeds.
     * @param <T> type of result of asynchronous task
     * @return the Firebase task: permits chaining of listeners/closures
     */
    public static <T> Task<T> onSuccess(Task<T> self, @NotNull final Closure closure) {
        self.addOnSuccessListener(new OnSuccessListener<T>() {
            @Override
            public void onSuccess(T result) {
                closure.call(result);
            }
        });
        return self;
    }

    /**
     * Attach a Groovy closure that is called when the task fails.
     *
     * @param self Firebase asynchronous task
     * @param closure Groovy closure { Exception ex -> ... } called when the task fails.
     * @param <T> type of result of asynchronous task
     * @return the Firebase task: permits chaining of listeners/closures
     */
    public static <T> Task<T> onFailure(Task<T> self, @NotNull final Closure closure) {
        self.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                closure.call(e);
            }
        });
        return self;
    }

    /**
     * Attach a Groovy closure that is called when the task completes.
     *
     * @param self Firebase asynchronous task
     * @param closure the Groovy closure { Exception ex, T result -> ... } to call when done
     * @param <T> type of result of asynchronous task
     * @return the Firebase task: permits chaining of listeners/closures
     */
    public static <T> Task<T> onComplete(Task<T> self, @NotNull final Closure closure) {
        self.addOnCompleteListener(new OnCompleteListener<T>() {
            @Override
            public void onComplete(Task<T> task) {
                if (task.isSuccessful()) {
                    closure.call(null, task.getResult());
                }
                else {
                    closure.call(task.getException(), null);
                }
            }
        });
        return self;
    }

    /**
     * Adds await method to Task; can call task.await() rather than Tasks.await(task).
     *
     * @param self Firebase asynchronous task
     * @param <T> type of result of asynchronous task
     * @return the result of the asynchronous task
     * @throws InterruptedException if the task was interrupted
     * @throws ExecutionException if the task fails
     */
    public static <T> T await(Task<T> self) throws InterruptedException, ExecutionException {
        return Tasks.await(self);
    }

    /**
     * Adds await method to Task; can call task.await(timeout, unit) rather than Tasks.await(task, timeout, unit).
     *
     * @param self Firebase asynchronous task
     * @param timeout how long to wait (combined with unit) before throwing TimeoutException
     * @param unit the unit of time for timeout
     * @param <T> type of result of asynchronous task
     * @return the result of the asynchronous task
     * @throws InterruptedException if the task was interrupted
     * @throws ExecutionException if the task fails
     * @throws TimeoutException if the task doesn't complete before the provided timeout
     */
    public static <T> T await(Task<T> self, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return Tasks.await(self, timeout, unit);
    }

}