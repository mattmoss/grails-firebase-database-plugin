package chat.demo

class CensorService {

    // These should be loaded from configuration, resource file, or use a dedicated service.
    final wordsToCensor = ['pineapple', 'php', 'fake', 'irregardless', 'perhaps']
    final censorPattern = "?!#%@\u2605!"

    // Eliminate unwanted words.
    String censor(String message) {
        wordsToCensor.inject(message) { String msg, String word ->
            msg.replaceAll("(?i)${word}", censorPattern)
        }
    }

}
