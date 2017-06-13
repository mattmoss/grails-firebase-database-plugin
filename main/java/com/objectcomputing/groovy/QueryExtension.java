package com.objectcomputing.groovy;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import groovy.lang.Closure;
import groovy.transform.stc.ClosureParams;      // TODO Add to Closure uses.

public class QueryExtension {

    public static ValueEventListener addValueEventListener(Query self, Closure closure) {
        return self.addValueEventListener(new ValueEventListener() {
            @Override
            public void onCancelled(DatabaseError error) {
                closure.call(error, null);
            }

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                closure.call(null, snapshot);
            }
        });
    }

}
