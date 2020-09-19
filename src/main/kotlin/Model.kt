class Model {

    private lateinit var name: String
    private var temp: Double = 0.0
    private var humidity: Double = 0.0
    private lateinit var icon: String
    private lateinit var main: String

    fun getName() = name
    fun setName(name: String) {
        this.name = name
    }

    fun getTemp() = temp
    fun setTemp(temp: Double) {
        this.temp = temp
    }

    fun getHumidity() = humidity
    fun setHumidity(humidity: Double) {
        this.humidity = humidity
    }

    fun getIcon() = icon
    fun setIcon(icon: String) {
        this.icon = icon
    }

    fun getMain() = main
    fun setMain(main: String) {
        this.main = main
    }
}