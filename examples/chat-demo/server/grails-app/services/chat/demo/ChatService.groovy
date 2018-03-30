package chat.demo

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChatService {

    FirebaseDatabase firebaseDatabase
    DatabaseReference document

    MessageProcessorService messageProcessorService

    Map<String, Closure> removeListener = [:]

    def initialize() {
        document = firebaseDatabase.reference
        listenChannels()
    }

    def listenChannels() {
        document['channels'].addChildEventListener {
            onChildAdded { DataSnapshot snapshot, prevChild ->
                listenChannel snapshot.key
            }
            onChildRemoved { DataSnapshot snapshot ->
                ignoreChannel snapshot.key
            }
        }
    }

    def listenChannel(String channel) {
        log.info "Listening to channel #${channel}"

        def incoming = document["incoming/${channel}"]
        def messages = document["messages/${channel}"]

        def listener = incoming.onChildAdded { DataSnapshot snapshot, prevChild ->
            // Get the incoming message, process it, and post it to outgoing (if it hasn't been vetoed).
            def msg = messageProcessorService.process(snapshot.getValue(ChatMessage))
            if (!msg) {
                // TODO Should notify user their message was rejected.
                log.warn "Message rejected: ${msg}"
            }
            else {
                messages.push().setValue(msg) { DatabaseError error, DatabaseReference ref ->
                    if (error) {
                        // TODO Should notify user that something went wrong.
                        log.error "Firebase Database error: ${error.message}"
                    }
                }
            }

            // We're done processing the message for good or ill. Remove it from incoming.
            incoming.remove snapshot.key
        }

        // Once we start listening to a channel, provide a way to stop listening.
        removeListener[channel] = { -> incoming.removeEventListener(listener) }
    }

    def ignoreChannel(String channel) {
        log.info "Ignoring channel #${channel}"

        removeListener.remove(channel).call()
    }

}
