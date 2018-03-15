package chat.demo

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChatService {

    FirebaseDatabase firebaseDatabase
    DatabaseReference document

    CensorService censorService

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

        def listener = incoming.onChildAdded { DataSnapshot snapshot, prevChild ->
            // Get the incoming message, process it, and post it to outgoing (if it hasn't been vetoed).
            ChatMessage message = processMessage(channel, snapshot.getValue(ChatMessage))
            if (message) {
                document["outgoing/${channel}"] << message
            }

            // Once we've processed the message, remove it from incoming.
            incoming.remove snapshot.key
        }

        // Once we start listening to a channel, provide a way to stop listening.
        removeListener[channel] = { -> incoming.removeEventListener(listener) }
    }

    def ignoreChannel(String channel) {
        log.info "Ignoring channel #${channel}"

        removeListener.remove(channel).call()
    }

    ChatMessage processMessage(String channel, ChatMessage message) {
        new ChatMessage(
                author: message.author,
                message: censorService.censor(message.message),
                timestamp: message.timestamp
        )
    }

}
