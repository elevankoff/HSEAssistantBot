import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import Bot.Companion.State
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton

object Buttons {
    val listOfCommands = listOf(
            "/help",
            "/timetable",
            "/menusite",
            "/menupdf",
            "/menuimage",
            "/weather"
    )

    val listOfAddresses = listOf(
            "Большая Ордынка, дом 47/7",
            "Большая Переяславская, дом 50, стр. 1",
            "Пионерская улица, дом 12/4",
            "Басманная улица, дом 21/4, стр. 6, 2 этаж",
            "Большой Трехсвятительский переулок, дом 3",
            "Малый Трехсвятительский переулок, дом 8/2, стр. 1",
            "Хитровский переулок, дом 2/8, стр. 5",
            "Шаболовка, дом 28/11, стр.2"
    )

    private fun addRow(keyboardRowList: MutableList<KeyboardRow>, line: String) {
        keyboardRowList.add(KeyboardRow()); keyboardRowList.last().add(KeyboardButton(line))
    }

    private fun addAllRows(keyboardRowList: MutableList<KeyboardRow>, list: List<String>) {
        for (line in list) {
            addRow(keyboardRowList, line)
        }
    }

    fun setButtons(sendMessage: SendMessage? = null,
                   sendDocument: SendDocument? = null,
                   sendImage: SendPhoto? = null,
                   bot: Bot) {
        val replyKeyboardMarkup = ReplyKeyboardMarkup()
        sendMessage?.setReplyMarkup(replyKeyboardMarkup)
        sendDocument?.setReplyMarkup(replyKeyboardMarkup)
        sendImage?.setReplyMarkup(replyKeyboardMarkup)

        replyKeyboardMarkup.apply {
            setSelective(true)
            setResizeKeyboard(true)
            setOneTimeKeyboard(false)
        }

        val keyboardRowList = mutableListOf<KeyboardRow>()

        when (bot.state) {
            State.TYPING_MENUSITE, State.TYPING_MENUPDF, State.TYPING_MENUIMAGE -> {
                addAllRows(keyboardRowList, listOfAddresses)
            } else -> {
                addAllRows(keyboardRowList, listOfCommands)
           }
        }
        replyKeyboardMarkup.setKeyboard(keyboardRowList)
    }
}