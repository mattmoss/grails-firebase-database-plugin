package com.objectcomputing.firebase.database

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import spock.lang.Specification

class QueryExtensionSpec extends Specification {

    def 'test that adding a closure as a value event listener can handle cancelled event'() {
        given:
        def mockError = Mock(DatabaseError)
        def mockQuery = Mock(Query) {
            1 * addValueEventListener(_ as ValueEventListener) >> { ValueEventListener listener -> listener }
        }

        when:
        def result = null
        def listener = QueryExtension.addValueEventListener(mockQuery) {
            onCancelled { DatabaseError error ->
                result = error
            }
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
        def listener = QueryExtension.onDataChange(mockQuery) { DataSnapshot snapshot ->
            result = snapshot
        }

        then:
        listener instanceof ValueEventListener

        when:
        listener.onDataChange(mockSnapshot)

        then:
        result.is mockSnapshot
    }

    def 'test that adding a closure as a child event listener can handle cancelled event'() {
        given:
        def mockError = Mock(DatabaseError)
        def mockQuery = Mock(Query) {
            1 * addChildEventListener(_ as ChildEventListener) >> { ChildEventListener listener -> listener }
        }

        when:
        def result = null
        def listener = QueryExtension.addChildEventListener(mockQuery) {
            onCancelled { DatabaseError error ->
                result = error
            }
        }

        then:
        listener instanceof ChildEventListener

        when:
        listener.onCancelled(mockError)

        then:
        result.is mockError
    }

    def 'test that adding a closure as a child event listener can handle child added event'() {
        given:
        def mockSnapshot = Mock(DataSnapshot)
        def mockQuery = Mock(Query) {
            1 * addChildEventListener(_ as ChildEventListener) >> { ChildEventListener listener -> listener }
        }

        when:
        def result = null
        def listener = QueryExtension.onChildAdded(mockQuery) { DataSnapshot snapshot, String previousChildName ->
            result = [snapshot: snapshot, previousChildName: previousChildName]
        }

        then:
        listener instanceof ChildEventListener

        when:
        listener.onChildAdded(mockSnapshot, 'alpha')

        then:
        result.snapshot.is mockSnapshot
        result.previousChildName == 'alpha'
    }

    def 'test that adding a closure as a child event listener can handle child changed event'() {
        given:
        def mockSnapshot = Mock(DataSnapshot)
        def mockQuery = Mock(Query) {
            1 * addChildEventListener(_ as ChildEventListener) >> { ChildEventListener listener -> listener }
        }

        when:
        def result = null
        def listener = QueryExtension.onChildChanged(mockQuery) { DataSnapshot snapshot, String previousChildName ->
            result = [snapshot: snapshot, previousChildName: previousChildName]
        }

        then:
        listener instanceof ChildEventListener

        when:
        listener.onChildChanged(mockSnapshot, 'sigma')

        then:
        result.snapshot.is mockSnapshot
        result.previousChildName == 'sigma'
    }

    def 'test that adding a closure as a child event listener can handle child moved event'() {
        given:
        def mockSnapshot = Mock(DataSnapshot)
        def mockQuery = Mock(Query) {
            1 * addChildEventListener(_ as ChildEventListener) >> { ChildEventListener listener -> listener }
        }

        when:
        def result = null
        def listener = QueryExtension.onChildMoved(mockQuery) { DataSnapshot snapshot, String previousChildName ->
            result = [snapshot: snapshot, previousChildName: previousChildName]
        }

        then:
        listener instanceof ChildEventListener

        when:
        listener.onChildMoved(mockSnapshot, 'gamma')

        then:
        result.snapshot.is mockSnapshot
        result.previousChildName == 'gamma'
    }

    def 'test that adding a closure as a child event listener can handle child removed event'() {
        given:
        def mockSnapshot = Mock(DataSnapshot)
        def mockQuery = Mock(Query) {
            1 * addChildEventListener(_ as ChildEventListener) >> { ChildEventListener listener -> listener }
        }

        when:
        def result = null
        def listener = QueryExtension.onChildRemoved(mockQuery) { DataSnapshot snapshot ->
            result = [snapshot: snapshot]
        }

        then:
        listener instanceof ChildEventListener

        when:
        listener.onChildRemoved(mockSnapshot)

        then:
        result.snapshot.is mockSnapshot
    }

}