package com.objectcomputing.firebase.database;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

class ChildEventListenerBuilder {
    private Closure cancelledHandler = null;
    private Closure childAddedHandler = null;
    private Closure childChangedHandler = null;
    private Closure childMovedHandler = null;
    private Closure childRemovedHandler = null;

    private ChildEventListenerBuilder() { }

    public ChildEventListenerBuilder onCancelled(Closure closure) {
        this.cancelledHandler = closure;
        return this;
    }

    public ChildEventListenerBuilder onChildAdded(Closure closure) {
        this.childAddedHandler = closure;
        return this;
    }

    public ChildEventListenerBuilder onChildChanged(Closure closure) {
        this.childChangedHandler = closure;
        return this;
    }

    public ChildEventListenerBuilder onChildMoved(Closure closure) {
        this.childMovedHandler = closure;
        return this;
    }

    public ChildEventListenerBuilder onChildRemoved(Closure closure) {
        this.childRemovedHandler = closure;
        return this;
    }

    ChildEventListener build() {
        return new ChildEventListener() {
            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (cancelledHandler != null) {
                    cancelledHandler.call(databaseError);
                }
            }

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (childAddedHandler != null) {
                    childAddedHandler.call(dataSnapshot, s);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (childChangedHandler != null) {
                    childChangedHandler.call(dataSnapshot, s);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                if (childMovedHandler != null) {
                    childMovedHandler.call(dataSnapshot, s);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (childRemovedHandler != null) {
                    childRemovedHandler.call(dataSnapshot);
                }
            }
        };
    }

    static ChildEventListenerBuilder create() {
        return new ChildEventListenerBuilder();
    }

    static ChildEventListenerBuilder create(@DelegatesTo(ChildEventListenerBuilder.class) Closure closure) {
        ChildEventListenerBuilder builder = create();

        closure.setDelegate(builder);
        closure.call();

        return builder;
    }
}