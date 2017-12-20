package com.objectcomputing.firebase.database;

import com.google.api.core.ApiFuture;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.annotations.NotNull;

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
     * Access to the asynchronous task is not available, so completion/error notices
     * will go unnoticed.
     *
     * @param self Firebase database DatabaseReference
     * @param value the value to set on a newly created child
     * @return the parent DatabaseReference (that on which leftShift was used)
     */
    public static DatabaseReference leftShift(DatabaseReference self, Object value) {
        self.push().setValueAsync(value);
        return self;
    }

    /**
     * Provide setPriority method that handles completion via a Groovy closure.
     *
     * @param self Firebase database DatabaseReference
     * @param priority the priority to set on the database reference
     * @param closure the Groovy closure { DatabaseError error, DatabaseReference reference -> ... } to call when done
     */
    public static void setPriority(DatabaseReference self, Object priority, @NotNull final Closure closure) {
        self.setPriority(priority, closureAsCompletionListener(closure));
    }

    /**
     * Provide setValue method that handles completion via a Groovy closure.
     *
     * @param self Firebase database DatabaseReference
     * @param value the value to set on the database reference
     * @param closure the Groovy closure { DatabaseError error, DatabaseReference reference -> ... } to call when done
     */
    public static void setValue(DatabaseReference self, Object value, @NotNull final Closure closure) {
        self.setValue(value, closureAsCompletionListener(closure));
    }

    /**
     * Provide setValue method that sets both value and priority, and that handles completion via a Groovy closure.
     *
     * @param self Firebase database DatabaseReference
     * @param value the value to set on the database reference
     * @param priority the priority to set on the database reference
     * @param closure the Groovy closure { DatabaseError error, DatabaseReference reference -> ... } to call when done
     */
    public static void setValue(DatabaseReference self, Object value, Object priority, @NotNull final Closure closure) {
        self.setValue(value, priority, closureAsCompletionListener(closure));
    }

    /**
     * Provide removeValue method to clear the value, and that handles completion via a Groovy closure.
     *
     * @param self Firebase database DatabaseReference
     * @param closure the Groovy closure { DatabaseError error, DatabaseReference reference -> ... } to call when done
     */
    public static void removeValue(DatabaseReference self, @NotNull final Closure closure) {
        self.removeValue(closureAsCompletionListener(closure));
    }

    /**
     * Remove a descendant database reference by clearing its value.
     *
     * @param self Firebase database DatabaseReference
     * @param pathString relative path from provided reference to descendant reference
     */
    public static ApiFuture<Void> remove(DatabaseReference self, String pathString) {
        return self.child(pathString).removeValueAsync();
    }

    /**
     * Provide method to remove a descendant database reference by clearing its value, and that handles completion via
     * a Groovy closure.
     *
     * @param self Firebase database DatabaseReference
     * @param pathString relative path from provided reference to descendant reference
     * @param closure the Groovy closure { DatabaseError error, DatabaseReference reference -> ... } to call when done
     */
    public static void remove(DatabaseReference self, String pathString, @NotNull final Closure closure) {
        self.child(pathString).removeValue(closureAsCompletionListener(closure));
    }

    /**
     * Provide updateChildren method that handles completion via a Groovy closure.
     *
     * @param self Firebase database DatabaseReference
     * @param update map of paths to values to update under provided database reference
     * @param closure the Groovy closure { DatabaseError error, DatabaseReference reference -> ... } to call when done
     */
    public static void updateChildren(DatabaseReference self, Map<String, Object> update,
                                      @NotNull final Closure closure) {
        self.updateChildren(update, closureAsCompletionListener(closure));
    }

    /**
     * Provides a wrapper for runTransaction, with the transaction work done in the provided Groovy closure.
     * Like doTransaction of firebase.database.Transaction.Handler, the closure may be called multiple times for the
     * same transaction. Be careful of any side effects or state within the closure. Implicitly sets fireLocalEvents
     * to true.
     *
     * @param self Firebase database DatabaseReference
     * @param handler the Groovy Closure { MutableData -> } called to modify data within a transaction
     * @param complete the Groovy Closure { DatabaseError, DataSnapshot -> } called when transaction is complete
     */
    public static void withTransaction(
            DatabaseReference self,
            @NotNull final Closure handler,
            final Closure complete
    ) {
        withTransaction(self, true, handler, complete);
    }

    /**
     * Provides a wrapper for runTransaction, with the transaction work done in the provided Groovy closure.
     * Like doTransaction of firebase.database.Transaction.Handler, the closure may be called multiple times for the
     * same transaction. Be careful of any side effects or state within the closure.
     *
     * @param self Firebase database DatabaseReference
     * @param handler the Groovy Closure { MutableData -> } called to modify data within a transaction
     * @param complete the Groovy Closure { DatabaseError, DataSnapshot -> } called when transaction is complete
     */
    public static void withTransaction(
            DatabaseReference self,
            final boolean fireLocalEvents,
            @NotNull final Closure handler,
            final Closure complete
    ) {
        self.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                // If call throws an exception, the transaction will abort().
                Object result = handler.call(mutableData);

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
                    complete.call(null, currentData);
                }
                else {
                    complete.call(databaseError, null);
                }
            }
        }, fireLocalEvents);
    }

    private static CompletionListener closureAsCompletionListener(@NotNull final Closure closure) {
        return new CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                closure.call(databaseError, databaseReference);
            }
        };
    }

}