package methods.send

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.io.File
import Bot

object SendImage {
    fun sendImage(message: Message, file: File, bot: Bot) {
        val sendImage = SendPhoto().apply {
            setChatId(message.getChatId().toString())
            setPhoto(file)
        }
        try {
            Buttons.setButtons(sendImage = sendImage, bot = bot)
            bot.execute(sendImage)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }
}