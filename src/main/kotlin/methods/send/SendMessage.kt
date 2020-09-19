package methods.send

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import Bot

object SendMessage {
    fun sendMessage(message: Message, text: String, bot: Bot) {
        val sendMessage = SendMessage().apply {
            enableHtml(true)
            setChatId(message.getChatId().toString())
            setText(text)
        }
        try {
            Buttons.setButtons(sendMessage = sendMessage, bot = bot)
            bot.execute(sendMessage)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }
}