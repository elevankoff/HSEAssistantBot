import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.InputStream
import java.lang.Exception
import java.lang.StringBuilder
import java.net.URL
import java.util.*
import java.io.FileOutputStream
import java.nio.channels.Channels
import methods.Parsers.parseFullName
import methods.Parsers.parseFakeDate
import javax.imageio.ImageIO

object Opp {

    private val addressID = mapOf(
            "Большая Ордынка, дом 47/7" to 0,
            "Большая Переяславская, дом 50, стр. 1" to 1,
            "Пионерская улица, дом 12/4" to 2,
            "Басманная улица, дом 21/4, стр. 6, 2 этаж" to 3,
            "Большой Трехсвятительский переулок, дом 3" to 4,
            "Малый Трехсвятительский переулок, дом 8/2, стр. 1" to 5,
            "Хитровский переулок, дом 2/8, стр. 5" to 6,
            "Шаболовка, дом 28/11, стр.2" to 7,
            "Шаболовка, дом 26, стр. 4" to 8
    )

    private val menuSiteID = listOf(
            listOf("329416835"),
            listOf("329086532"),
            listOf("329086498"),
            listOf(
                    "329785353",
                    "329785122"
            ),
            listOf("326923743"),
            listOf("329086488"),
            listOf("329086483"),
            listOf("329086458"),
            listOf("329086467")
    )

    private data class DayOfWeek(private val day: Int) {
        companion object{
            val days = listOf("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье")
        }
        val dayOfWeek = days[day]
    }

    data class Student(val fullName: String) {
            private val fullNameParsed = parseFullName(fullName)
            val secondName: String = fullNameParsed[0]
            val firstName: String = fullNameParsed[1]
            val patronymic: String = fullNameParsed[2]
    }

    data class Date(private val fakeDate: String) {
        val date = parseFakeDate(fakeDate)
    }

    fun getId(s: String): String {
        val student = Student(s)
        val url = URL("https://ruz.hse.ru/api/search?term=${student.secondName}_${student.firstName}_${student.patronymic}")

        val input = Scanner(url.content as InputStream)

        var result = ""
        while (input.hasNext()) {
            result += input.nextLine()
        }

        val array = JSONArray(result)
        return (array.get(0) as JSONObject).getString("id")
    }

    fun getTimetable(student: Student, date: Date): String {
        val url = URL("https://ruz.hse.ru/api/schedule/student/${getId(student.fullName)}?start=${date.date}&finish=${date.date}&lng=1")
        val input = Scanner(url.content as InputStream)

        var result = ""
        while (input.hasNext()) {
            result += input.nextLine()
        }

        val resultOfGetter= StringBuilder("")
        val array = JSONArray(result)

        if (!array.isEmpty()) {
            resultOfGetter.append("День недели: ${DayOfWeek((array.get(0) as JSONObject).getInt("dayOfWeek") - 1).dayOfWeek} \n")
        } else {
            return "В этот день занятий нет"
        }

        resultOfGetter.append("Количество занятий: ${array.length()} \n \n")

        for (lesson in array) {
            lesson as JSONObject

            println(getId(student.fullName))
            println(date.date);

            resultOfGetter.append("Тип занятия: ${lesson.getString("kindOfWork")} \n" +
                        "Предмет: ${lesson.getString("discipline")} \n" +
                        "Аудитория: ${lesson.getString("auditorium")} \n" +
                        "Начало: ${lesson.getString("beginLesson")} \n" +
                        "Конец: ${lesson.getString("endLesson")} \n" +
                        "Лектор: ${lesson.getString("lecturer")} \n" +
                        "Здание: ${lesson.getString("building")} \n \n"
                )
        }

        return resultOfGetter.toString()
    }

    fun getMenuSite(address: String): String {
        if (!addressID.containsKey(address)) throw Exception("Bad address") else {
            var result = ""
            val menuSiteID = menuSiteID[addressID[address]!!]
            for (id in menuSiteID) {
                result += "https://www.hse.ru/mirror/pubs/share/$id \n"
            }
            return result
        }
    }

    fun getMenuPDF(address: String): List<File> {
        if (!addressID.containsKey(address)) throw Exception("Bad address") else {
            val result = mutableListOf<File>()
            val menuSiteID = menuSiteID[addressID[address]!!]
            var counter = 0
            for (id in menuSiteID) {
                val FILE_URL = "https://www.hse.ru/mirror/pubs/share/$id"
                val FILE_PATH = "src\\main\\resources\\Menu${++counter}.pdf"
                val url = URL(FILE_URL)
                val file = File(FILE_PATH)
                file.createNewFile()
                val readableByteChannel = Channels.newChannel(url.openStream())
                val fileOutputStream = FileOutputStream(file)

                val fileChannel = fileOutputStream.getChannel()
                fileOutputStream.getChannel()
                        .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                result.add(file)
            }
            return result
        }
    }

    private fun PDFtoJPEG (input: String, output: String): File {
        val document = PDDocument.load(File(input))
        val renderer = PDFRenderer(document)
        val buffered = renderer.renderImageWithDPI(0, 300F)
        ImageIO.write(buffered, "JPEG", File(output))
        document.close()
        return File(output)
    }

    fun getMenuImage(address: String): List<File> {
        val list = getMenuPDF(address)
        val result = mutableListOf<File>()
        for (pdf in list) {
            result.add(PDFtoJPEG(pdf.absolutePath, pdf.absolutePath.substring(0, pdf.absolutePath.length - 4) + ".jpeg"))
        }
        return result
    }

}