package com.objectcomputing.groovy

import com.google.firebase.database.DatabaseReference
import spock.lang.Specification


class DatabaseReferenceExtensionSpec extends Specification {

    def 'test that getAt() does the same as child()'() {
        given:
        def mockRef = Mock(DatabaseReference) {
            child(_) >> { String pathString ->
                Mock(DatabaseReference) {
                    toString() >> "/base/${pathString}"
                }
            }
        }

        when:
        def viaChild = mockRef.child("foo/bar")
        def viaGetAt = DatabaseReferenceExtension.getAt(mockRef, "foo/bar")

        then:
        viaGetAt instanceof DatabaseReference
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

        then:
        firstRef.is mockRef
        checkRef.is mockRef
    }
}