package com.objectcomputing.firebase.database

import com.google.firebase.database.DatabaseReference
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

        when:
        DatabaseReference viaChild = mockRef.child("foo/bar")
        DatabaseReference viaGetAt = DatabaseReferenceExtension.getAt(mockRef, "foo/bar")

        then: 'the references refer to the same child'
        // (viaChild == viaGetAt) fails... doesn't call DatabaseReference.equals ???
        viaChild.toString() == viaGetAt.toString()
    }

    def 'test that leftShift() does the same as push().setValue()'() {
        given:
        def mockRef = Mock(DatabaseReference) {
            2 * push() >> Mock(DatabaseReference) {
                1 * setValue('foo')
                1 * setValue('bar')
            }
        }

        when:
        def firstRef = DatabaseReferenceExtension.leftShift(mockRef, 'foo')
        def checkRef = DatabaseReferenceExtension.leftShift(firstRef, 'bar')

        then: 'leftShift returns the reference operated on, not the child as push() does'
        firstRef.is mockRef
        checkRef.is mockRef
    }
}