import methods.send.SendMessage.sendMessage
import methods.process.ProcessCommands
import methods.process.ProcessStates
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.io.IOException

class Bot : TelegramLongPollingBot() {

    override fun getBotUsername(): String = "HSEAssistantBot"
    override fun getBotToken(): String = ""

    var state: State = State.NONE

    override fun onUpdateReceived(update: Update) {
        val model = Model()
        val message = update.message
        if (message != null && message.hasText()) {
            when(state) {
                State.TYPING_NAME -> ProcessStates.sendId(message, this)
                State.TYPING_TIMETABLE -> ProcessStates.sendTimetable(message, this)
                State.TYPING_MENUSITE -> ProcessStates.sendMenuSite(message, this)
                State.TYPING_MENUPDF -> ProcessStates.sendMenuPDF(message, this)
                State.TYPING_MENUIMAGE -> ProcessStates.sendImage(message, this)
                else -> when (message.text) {
                    "/help" -> ProcessCommands.help(message, this)
                    "/id" -> ProcessCommands.id(message, this)
                    "/timetable" -> ProcessCommands.timetable(message, this)
                    "/menusite" -> ProcessCommands.menuSite(message, this)
                    "/menupdf" -> ProcessCommands.menuPDF(message, this)
                    "/menuimage" -> ProcessCommands.menuImage(message, this)
                    "/weather" -> ProcessCommands.weather(model, message, this)
                    else -> {
                        sendMessage(message, ERROR.ERROR_FORMAT.msg, this)
                    }
                }
            }
        }
    }

    companion object {
        enum class ERROR(val msg: String) {
            ERROR_FORMAT("Неправильный формат ввода \n")
        }

        enum class State { NONE, TYPING_NAME, TYPING_TIMETABLE, TYPING_MENUSITE, TYPING_MENUPDF, TYPING_MENUIMAGE }

        @JvmStatic
        fun main(args: Array<String>) {
            ApiContextInitializer.init()
            val telegramBotsApi = TelegramBotsApi()
            try {
                telegramBotsApi.registerBot(Bot())
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }
        }
    }
}
