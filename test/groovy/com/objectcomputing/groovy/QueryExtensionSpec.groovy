package com.objectcomputing.groovy

import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

import spock.lang.Specification

class QueryExtensionSpec extends Specification {

    def 'test that adding a closure as value listener will be called when server cancels'() {
        given:
        def mockError = Mock(DatabaseError)
        def mockQuery = Mock(Query) {
            1 * addValueEventListener(_ as ValueEventListener) >> { ValueEventListener listener -> listener }
        }

        when:
        def result = null
        def listener = QueryExtension.addValueEventListener(mockQuery) { error, snapshot ->
            result = error ?: snapshot
        }

        then:
        listener instanceof ValueEventListener

        when:
        listener.onCancelled(mockError)

        then:
        result.is mockError
    }

    def 'test that adding a closure as value listener will be called when data changes'() {
        given:
        def mockSnapshot = Mock(DataSnapshot)
        def mockQuery = Mock(Query) {
            1 * addValueEventListener(_ as ValueEventListener) >> { ValueEventListener listener -> listener }
        }

        when:
        def result = null
        def listener = QueryExtension.addValueEventListener(mockQuery) { error, snapshot ->
            result = error ?: snapshot
        }

        then:
        listener instanceof ValueEventListener

        when:
        listener.onDataChange(mockSnapshot)

        then:
        result.is mockSnapshot
    }

}
