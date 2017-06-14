package com.objectcomputing.groovy;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

class ValueEventListenerBuilder {
    private Closure cancelledHandler = null;
    private Closure dataChangeHandler = null;

    private ValueEventListenerBuilder() { }

    ValueEventListenerBuilder onCancelled(Closure closure) {
        this.cancelledHandler = closure;
        return this;
    }

    ValueEventListenerBuilder onDataChange(Closure closure) {
        this.dataChangeHandler = closure;
        return this;
    }

    ValueEventListener build() {
        return new ValueEventListener() {
            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (cancelledHandler != null) {
                    cancelledHandler.call(databaseError);
                }
            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataChangeHandler != null) {
                    dataChangeHandler.call(dataSnapshot);
                }
            }
        };
    }

    static ValueEventListenerBuilder create() {
        return new ValueEventListenerBuilder();
    }

    static ValueEventListener create(@DelegatesTo(ValueEventListenerBuilder.class) Closure closure) {
        ValueEventListenerBuilder builder = create();

        closure.setDelegate(builder);
        closure.call();

        return builder.build();
    }
}