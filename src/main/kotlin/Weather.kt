import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*

class Weather {
    // 0964ae2c8b39bf3820afdb03249d0b6e
    companion object {
        @Throws(IOException::class)
        fun getWeather(message: String, model: Model): String {
            val url = URL("***REMOVED***")

            val input = Scanner(url.content as InputStream)
            var result = ""
            while (input.hasNext()) {
                result += input.nextLine()
            }

            val Object = JSONObject(result)
            model.setName(Object.getString("name"))

            val main = Object.getJSONObject("main")
            model.apply {
                setTemp(main.getDouble("temp"))
                setHumidity(main.getDouble("humidity"))
            }

            val getArray = Object.getJSONArray("weather")

            for (i in 0 until getArray.length()) {
                val obj = getArray.getJSONObject(i)
                model.setIcon(obj.get("icon") as String)
                model.setMain(obj.get("main") as String)
            }

            return "City: ${model.getName()} \n" +
                    "Temperature: ${model.getTemp()} ${176.toChar()}C \n" +
                    "Humidity: ${model.getHumidity()} % \n" +
                    "Main: ${model.getMain()} \n"
        }
    }
}

