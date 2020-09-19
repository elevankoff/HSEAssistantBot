package methods.send

import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.io.File
import Bot

object SendFile {
    fun sendFile(message: Message, file: File, bot: Bot) {
        val sendDocument = SendDocument().apply {
            setChatId(message.getChatId().toString())
            setDocument(file)
        }
        try {
            Buttons.setButtons(sendDocument = sendDocument, bot = bot)
            bot.execute(sendDocument)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }
}