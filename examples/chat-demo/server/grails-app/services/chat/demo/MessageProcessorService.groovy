package chat.demo

class MessageProcessorService {

    CensorService censorService

    def process(ChatMessage message) {
        // Processors should take a ChatMessage instance and return a new ChatMessage instance.
        def processors = [censorMessage, updateTimestamp]

        processors.inject(message) { ChatMessage msg, fn ->
            fn(msg)
        }
    }

    private updateTimestamp = { ChatMessage message ->
        if (message) {
            // Ignore timestamp the client may have provided; set it here.
            new ChatMessage(
                    author: message.author,
                    message: message.message,
                    timestamp: new Date().time
            )
        }
    }

    private censorMessage = { ChatMessage message ->
        if (message) {
            new ChatMessage(
                    author: message.author,
                    message: censorService.censor(message.message),
                    timestamp: new Date().time
            )
        }
    }
}