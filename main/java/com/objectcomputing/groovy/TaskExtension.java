package com.objectcomputing.groovy;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.tasks.*;
import groovy.lang.Closure;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TaskExtension {

    public static <T> Task<T> onSuccess(Task<T> self, @NotNull Closure closure) {
        self.addOnSuccessListener(new OnSuccessListener<T>() {
            @Override
            public void onSuccess(T t) {
                closure.call(t);
            }
        });
        return self;
    }

    public static <T> Task<T> onFailure(Task<T> self, @NotNull Closure closure) {
        self.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                closure.call(e);
            }
        });
        return self;
    }

    public static <T> Task<T> onComplete(Task<T> self, @NotNull Closure closure) {
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

    public static <T> T await(Task<T> self) throws InterruptedException, ExecutionException {
        return Tasks.await(self);
    }

    public static <T> T await(Task<T> self, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return Tasks.await(self, timeout, unit);
    }

}
