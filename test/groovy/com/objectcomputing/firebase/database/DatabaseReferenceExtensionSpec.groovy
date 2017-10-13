package com.objectcomputing.firebase.database

import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DatabaseReference.CompletionListener
import spock.lang.Specification


class DatabaseReferenceExtensionSpec extends Specification {

    def 'test that getAt() does the same as child()'() {
        given:
        def mockRef = Mock(DatabaseReference) {
            2 * child('foo/bar') >> { String pathString ->
                Mock(DatabaseReference) {
                    toString() >> "/base/${pathString}"
                }
            }
        }

        when: "ref['foo/bar']"
        DatabaseReference viaChild = mockRef.child("foo/bar")
        DatabaseReference viaGetAt = DatabaseReferenceExtension.getAt(mockRef, "foo/bar")

        then: 'the references refer to the same child'
        // (viaChild == viaGetAt) fails... doesn't call DatabaseReference.equals ???
        viaChild.toString() == viaGetAt.toString()
    }

    def 'test that leftShift() does the same work as push().setValue()'() {
        given:
        def mockRef = Mock(DatabaseReference) {
            2 * push() >> Mock(DatabaseReference) {
                1 * setValue('foo')
                1 * setValue('bar')
            }
        }

        when: "ref << 'foo' << 'bar'"
        def firstRef = DatabaseReferenceExtension.leftShift(mockRef, 'foo')
        def checkRef = DatabaseReferenceExtension.leftShift(firstRef, 'bar')

        then: 'leftShift returns the reference operated on, not the child as push() does'
        firstRef.is mockRef
        checkRef.is mockRef
    }

    def 'test that setPriority with closure can receive results via closure'() {
        given:
        def mockError = Mock(DatabaseError)
        def mockRef = Mock(DatabaseReference)

        1 * mockRef.setPriority(42, _) >> { Object priority, CompletionListener listener ->
            listener.onComplete(mockError, mockRef)
        }

        when:
        def result = null
        mockRef.setPriority(42) { DatabaseError error, DatabaseReference dbRef ->
            result = [error: error, dbRef: dbRef]
        }

        then:
        result.error == mockError
        result.dbRef == mockRef
    }


    def 'test that setValue with closure can receive results via closure'() {
        given:
        def mockError = Mock(DatabaseError)
        def mockRef = Mock(DatabaseReference)

        1 * mockRef.setValue('foobar', _) >> { Object value, CompletionListener listener ->
            listener.onComplete(mockError, mockRef)
        }

        when:
        def result = null
        mockRef.setValue('foobar') { DatabaseError error, DatabaseReference dbRef ->
            result = [error: error, dbRef: dbRef]
        }

        then:
        result.error == mockError
        result.dbRef == mockRef
    }

    def 'test that setValue w/priority with closure can receive results via closure'() {
        given:
        def mockError = Mock(DatabaseError)
        def mockRef = Mock(DatabaseReference)

        1 * mockRef.setValue('foobar', 42, _) >> { Object value, Object priority, CompletionListener listener ->
            listener.onComplete(mockError, mockRef)
        }

        when:
        def result = null
        mockRef.setValue('foobar', 42) { DatabaseError error, DatabaseReference dbRef ->
            result = [error: error, dbRef: dbRef]
        }

        then:
        result.error == mockError
        result.dbRef == mockRef
    }

}