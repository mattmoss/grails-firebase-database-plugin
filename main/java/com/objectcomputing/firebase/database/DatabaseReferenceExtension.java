package com.objectcomputing.firebase.database;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.tasks.Task;
import com.google.firebase.tasks.TaskCompletionSource;

import groovy.lang.Closure;

import java.util.Map;

public class DatabaseReferenceExtension {

    /**
     * Provide getAt method (i.e. indexing operator) to alias child method.
     *
     * @param self Firebase database DatabaseReference
     * @param pathString relative path from provided reference to new reference
     * @return new DatabaseReference to the given path
     */
    public static DatabaseReference getAt(DatabaseReference self, String pathString) {
        return self.child(pathString);
    }

    /**
     * Provide leftShift method (i.e. << operator) to alias .push().setValue().
     * Returns the target DatabaseReference so multiple leftShift calls can be chained.
     *
     */
    public static DatabaseReference leftShift(DatabaseReference self, Object value) {
        self.push().setValue(value);
        return self;
    }

    private static CompletionListener asCompletionListener(@NotNull final Closure closure) {
        return new CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                closure.call(databaseError, databaseReference);
            }
        };
    }

    public static void setPriority(DatabaseReference self, Object priority, @NotNull final Closure closure) {
        self.setPriority(priority, asCompletionListener(closure));
    }

    public static void setValue(DatabaseReference self, Object value, Object priority, @NotNull final Closure closure) {
        self.setValue(value, priority, asCompletionListener(closure));
    }

    public static void setValue(DatabaseReference self, Object value, @NotNull final Closure closure) {
        self.setValue(value, asCompletionListener(closure));
    }

    public static void removeValue(DatabaseReference self, @NotNull final Closure closure) {
        self.removeValue(asCompletionListener(closure));
    }

    public static Task<Void> remove(DatabaseReference self, String pathString) {
        return self.child(pathString).removeValue();
    }

    public static void remove(DatabaseReference self, String pathString, @NotNull final Closure closure) {
        self.child(pathString).removeValue(asCompletionListener(closure));
    }

    public static void updateChildren(DatabaseReference self, Map<String, Object> update, @NotNull final Closure closure) {
        self.updateChildren(update, asCompletionListener(closure));
    }

    public static Task<DataSnapshot> withTransaction(DatabaseReference self, @NotNull final Closure closure) {
        return withTransaction(self, true, closure);
    }

    public static Task<DataSnapshot> withTransaction(DatabaseReference self, boolean fireLocalEvents, @NotNull final Closure closure) {
        final TaskCompletionSource<DataSnapshot> source = new TaskCompletionSource<>();

        self.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                // If call throws an exception, the transaction will abort().
                Object result = closure.call(mutableData);

                if (result instanceof Transaction.Result) {
                    // Return result as-is if closure returned success/abort.
                    return (Transaction.Result) result;
                }
                else if (result instanceof MutableData) {
                    // Succeed with result as-is if it's MutableData.
                    return Transaction.success((MutableData) result);
                }
                else if (result == null) {
                    // Succeed with provided MutableData object if null returned;
                    // we assume the user setValue in the closure.
                    return Transaction.success(mutableData);
                }
                else {
                    // Succeed with provided MutableData object with value set to result.
                    mutableData.setValue(result);
                    return Transaction.success(mutableData);
                }
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot currentData) {
                if (databaseError == null) {
                    source.setResult(currentData);
                }
                else {
                    source.setException(databaseError.toException());
                }
            }
        }, fireLocalEvents);

        return source.getTask();
    }
}
