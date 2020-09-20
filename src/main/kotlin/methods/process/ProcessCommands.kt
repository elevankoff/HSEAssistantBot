package methods.process

import Bot
import methods.send.SendMessage
import Bot.Companion.State
import org.telegram.telegrambots.meta.api.objects.Message
import Model

object ProcessCommands {
    fun help(message: Message, bot: Bot) {
        SendMessage.sendMessage(message,
                "Доступны следующие команды: \n" +
                        "/timetable - Расписание по ФИО и дню \n" +
                        "/menusite - Страница с меню по адресу \n" +
                        "/menupdf - PDF с меню по адресу \n" +
                        "/menuimage - Картинка с меню по адресу \n" +
                        "/weather - Погода в Москве \n",
                bot
        )
    }

    fun id(message: Message, bot: Bot) {
        bot.state = State.TYPING_NAME
        SendMessage.sendMessage(message, "Введи свое ФИО \n ", bot)
    }

    fun timetable(message: Message, bot: Bot) {
        bot.state = State.TYPING_TIMETABLE
        SendMessage.sendMessage(message, "Введи свое ФИО и день в формате Фамилия Имя Отчество день.месяц.год \n ", bot)
    }

    fun menuSite(message: Message, bot: Bot) {
        bot.state = State.TYPING_MENUSITE
        SendMessage.sendMessage(message, "Выберите адрес по которому хотите посмотреть меню. \n ", bot)
    }

    fun menuPDF(message: Message, bot: Bot) {
        bot.state = State.TYPING_MENUPDF
        SendMessage.sendMessage(message, "Выберите адрес по которому хотите посмотреть меню. \n", bot)
    }

    fun menuImage(message: Message, bot: Bot) {
        bot.state = State.TYPING_MENUIMAGE
        SendMessage.sendMessage(message, "Выберите адрес по которому хотите посмотреть меню. \n", bot)
    }

    fun weather(model: Model, message: Message, bot: Bot) {
        SendMessage.sendMessage(message, Weather.getWeather("Moscow", model), bot)
    }
}