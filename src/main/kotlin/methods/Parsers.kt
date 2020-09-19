package methods

import java.lang.Exception

object Parsers {
    fun parseFakeDate(date: String): String {
        val list = date.split('.')
        return "${list[2]}.${list[1]}.${list[0]}"
    }

    fun parseFullName(s: String): List<String> {
        val parsed = s.split(' ')
        if (parsed.size != 3) throw Exception("Bad request")
        return parsed
    }
}