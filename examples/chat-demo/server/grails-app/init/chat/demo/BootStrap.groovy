package chat.demo

class BootStrap {

    ChatService chatService

    def init = { servletContext ->
        chatService.initialize()
    }

    def destroy = {
    }
}
