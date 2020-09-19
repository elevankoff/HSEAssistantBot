package methods.process

import Bot
import methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import Bot.Companion.State
import Bot.Companion.ERROR
import methods.send.SendFile
import methods.send.SendImage

object ProcessStates {
    fun sendId(message: Message, bot: Bot) {
        bot.state = State.NONE
        try {
            SendMessage.sendMessage(message, Opp.getId(message.text), bot)
        } catch (e: Exception) {
            SendMessage.sendMessage(message, ERROR.ERROR_FORMAT.msg, bot)
        }
    }

    fun sendTimetable(message: Message, bot: Bot) {
        bot.state = State.NONE
        val list = message.text.split(" ")
        try {
            SendMessage.sendMessage(message, Opp.getTimetable(Opp.Student("${list[0]} ${list[1]} ${list[2]}"), Opp.Date(list[3])), bot)
        } catch (e: Exception) {
            SendMessage.sendMessage(message, ERROR.ERROR_FORMAT.msg, bot)
        }
    }

    fun sendMenuSite(message: Message, bot: Bot) {
        bot.state = State.NONE
        try {
            SendMessage.sendMessage(message, Opp.getMenuSite(message.text), bot)
        } catch(e: Exception) {
            SendMessage.sendMessage(message, ERROR.ERROR_FORMAT.msg, bot)
        }
    }

    fun sendMenuPDF(message: Message, bot: Bot) {
        bot.state = State.NONE
        for (file in Opp.getMenuPDF(message.text)) {
            SendFile.sendFile(message, file, bot)
        }
    }

    fun sendImage(message: Message, bot: Bot) {
        bot.state = State.NONE
        for (file in Opp.getMenuImage(message.text)) {
            SendImage.sendImage(message, file, bot)
        }
    }
}