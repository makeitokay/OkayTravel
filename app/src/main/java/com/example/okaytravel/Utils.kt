package com.example.okaytravel

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

const val DATETIME_FORMAT = "dd.MM.yyyy HH:mm:ss.SSS"
const val DATE_FORMAT = "dd.MM.yyyy"

fun String.sha256(): String {
    return this.hashWithAlgorithm("SHA-256")
}

private fun String.hashWithAlgorithm(algorithm: String): String {
    val digest = MessageDigest.getInstance(algorithm)
    val bytes = digest.digest(this.toByteArray(Charsets.UTF_8))
    return bytes.fold("", { str, it -> str + "%02x".format(it) })
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

@Suppress("DEPRECATION")
fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }

    return result
}

fun getCurrentDate(): String {
    return parseDate(Date())
}

fun getCurrentDatetime(): String {
    return parseDatetime(Date())
}

fun parseDatetimeString(date: String): Date? {
    val isoFormat = SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault())
    return isoFormat.parse(date)
}

fun parseDateString(date: String): Date? {
    val isoFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    return isoFormat.parse(date)
}

fun parseDatetime(date: Date): String {
    val isoFormat = SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault())
    return isoFormat.format(date)
}

fun parseDate(date: Date): String {
    val isoFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    return isoFormat.format(date)
}

fun uuid(): String {
    return UUID.randomUUID().toString()
}

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = activity.currentFocus
    if (view == null) view = View(activity)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

val AVAILABLE_CITY_IMAGES: List<String> = listOf(
    "Гонконг",
    "Hong Kong",
    "Таиланд, Бангкок",
    "Thailand, City of Bangkok",
    "Великобритания, Лондон",
    "United Kingdom, London",
    "Сингапур",
    "Singapore",
    "Макао",
    "Macau",
    "Объединенные Арабские Эмираты, Дубай",
    "United Arab Emirates, city of Dubai",
    "Франция, Иль-де-Франс, Париж",
    "France, Île-de-France, Paris",
    "Соединённые Штаты Америки, штат Нью-Йорк",
    "United States of America, New York",
    "Китай, провинция Гуандун, город Шэньчжэнь",
    "China, Guangdong Province, City of Shenzhen",
    "Малайзия, Куала-Лумпур",
    "Malaysia, City of Kuala Lumpur",
    "Таиланд, Пхукет, остров Пхукет",
    "Thailand, Phuket",
    "Италия, Рим",
    "Italy, Rome",
    "Япония, Токио",
    "Japan, Tokyo, Ostrovnyye territorii Tokio",
    "Тайвань, Тайбэй",
    "Тайвань, Тайбэй",
    "Турция, Стамбул",
    "Türkiye, İstanbul",
    "Республика Корея, Сеул",
    "Republic of Korea, Seoul",
    "Китай, провинция Гуандун, город субпровинциального значения Гуанчжоу, Гуанчжоу",
    "China, Guangdong Province, Sub-provincial city Guangzhou, Guangzhou",
    "Чехия, Прага",
    "Czechia, Praha",
    "Саудовская Аравия, Мекка",
    "Saudi Arabia, Makkah",
    "Соединённые Штаты Америки, штат Флорида, Дейд-Каунти, город Майами",
    "United States of America, Florida, Dade County, City of Miami",
    "Индия, Дели",
    "India, Deli",
    "Индия, штат Махараштра, Мумбаи",
    "India, State of Maharashtra, Mumbai",
    "Испания, Каталония, город Барселона",
    "Spain, Comunidad Autònoma de Catalunya, City of Barcelona",
    "Таиланд, город Паттайя",
    "Thailand, Pattaya",
    "Китай, Шанхай",
    "China, Shanghai",
    "Соединённые Штаты Америки, штат Невада, Кларк-Каунти, город Лас-Вегас",
    "United States of America, Nevada, Clark County, City of Las Vegas",
    "Италия, Ломбардия, Милан",
    "Italy, Lombardia, Milan",
    "Нидерланды, Амстердам",
    "Netherlands, Amsterdam",
    "Турция, Анталья",
    "Turkey, Antalya",
    "Австрия, Вена",
    "Austria, Wien",
    "Соединённые Штаты Америки, Калифорния, Лос-Анджелес",
    "United States of America, California, Los Angeles",
    "Мексика, Кинтана-Роо, город Канкун",
    "Mexico, Quintana Roo, Cancun",
    "Япония, префектура Осака, Осака",
    "Japan, Osaka Prefecture, Osaka City",
    "Германия, Берлин",
    "Germany, Berlin",
    "Индия, штат Уттар-Прадеш, Агра",
    "India, State of Uttar Pradesh, Agra",
    "Вьетнам, город центрального подчинения Хошимин, город Хошимин",
    "Vietnam, Hồ Chí Minh Municipality, Ho Chi Minh City",
    "Южно-Африканская Республика, провинция Гаутенг, Йоханнесбург",
    "Republic of South Africa, Gauteng Province, Johannesburg",
    "Италия, Венеция",
    "Italy, Veneto, Venice",
    "Испания, Мадрид",
    "Spain, Madrid",
    "Соединённые Штаты Америки, штат Флорида, округ Ориндж, Орландо",
    "United States of America, Florida, Orange County, Orlando",
    "Саудовская Аравия, Эр-Рияд",
    "Saudi Arabia, Ar Riyad",
    "Малайзия, Джохор",
    "Malaysia, Johor",
    "Ирландия, Дублин",
    "Ireland, Dublin",
    "Италия, Тоскана, Флоренция",
    "Italy, Toscana, Firenze, Florence",
    "Индия, штат Тамилнад, Ченнаи",
    "India, State of Tamil Nadu, Chennai",
    "Россия, Москва",
    "Russia, Moscow",
    "Греция, Афины",
    "Greece, Athens",
    "Индия, штат Раджастхан, Джайпур",
    "India, State of Rajasthan, Jaipur",
    "Китай, Пекин",
    "China, Beijing",
    "Индонезия, провинция Бали, Денпасар",
    "Indonesia, Bali, Denpasar",
    "Канада, провинция Онтарио, город Торонто",
    "Canada, Ontario, City of Toronto",
    "Вьетнам, Ханой",
    "Vietnam, Hanoi",
    "Австралия, Новый Южный Уэльс, город Сидней",
    "Australia, New South Wales, Сity of Sydney",
    "Соединённые Штаты Америки, Калифорния, округ Сан-Франциско, город Сан-Франциско",
    "United States of America, California, San Francisco County, City and County of San Francisco",
    "Венгрия, Будапешт",
    "Hungary, Budapest",
    "Вьетнам, бухта Халонг",
    "Доминиканская Республика, Ла Альтаграсия, аэропорт Пунта Кана",
    "Dominican Republic, La Altagracia, Punta Cana",
    "Саудовская Аравия, Эш-Шаркия, город Эд-Даммам",
    "Saudi Arabia, Eastern Province, City of Dammam",
    "Германия, Бавария, Мюнхен",
    "Германия, Бавария, Мюнхен",
    "Китай, провинция Гуандун, городской округ Чжухай, город Чжухай",
    "China, Guangdong Province, Zhuhai District, Zhuhai City",
    "Португалия, Лиссабон",
    "Portugal, Lisbon District",
    "Египет, Каир",
    "Egypt, Cairo",
    "Malaysia, Penang",
    "Катар, Доха",
    "Qatar, Doha",
    "Дания, Столичная область, Копенгаген",
    "Denmark, Copenhagen",
    "Греция, периферия Крит, Ираклион",
    "Greece, periferiya Krit, City of Heraklion",
    "Израиль, Иерусалим",
    "Israel, Jerusalem",
    "Турция, Эдирне",
    "Turkey, Edirne",
    "Камбоджа, Пномпень",
    "Cambodia, Phnom Penh",
    "Россия, Санкт-Петербург, Петродворцовый район, посёлок Стрельна, Санкт-Петербургское шоссе",
    "Russia, Saint Petersburg",
    "Республика Корея, провинция с особой автономией Чеджудо, Чеджу",
    "Republic of Korea, Jeju Special Self-Governing Province, Jeju",
    "Япония, префектура Киото, Киото",
    "Japan, Kyoto Prefecture, Kyoto CIty",
    "Таиланд, Chiang Mai, город Чиангмай",
    "Thailand, Chiang Mai",
    "Польша, Варшава",
    "Poland, Warsaw",
    "Польша, Малопольское воеводство, Повят-Кракув, Кракув, Краков",
    "Poland, Lesser Poland Voivodeship, Powiat Kraków, Kraków, Krakow",
    "Соединённые Штаты Америки, штат Огайо, Хайленд-Каунти, Гонолулу",
    "United States of America, Hawaii, Honolulu County",
    "Австралия, Виктория, город Мельбурн",
    "Australia, Victoria, City of Melbourne",
    "Израиль, Тель-Авив",
    "Israel, Tel Aviv",
    "Марокко, город Марракеш",
    "Morocco, gorod Marrakesh",
    "Бельгия, Брюссель",
    "Belgium, Brussels",
    "Новая Зеландия, Окленд",
    "New Zealand, Auckland Region, Auckland",
    "Канада, провинция Британская Колумбия, Метро-Ванкувер-Риджинал-Дистрикт, город Ванкувер",
    "Canada, British Columbia, Metro Vancouver Regional District, Vancouver",
    "Индонезия, Джакарта",
    "Indonesia, Special Capital Region of Jakarta",
    "Германия, Гессен, город Франкфурт-на-Майне",
    "Germany, Hessen, Frankfurt am Main",
    "Турция, Артвин",
    "Turkey, Artvin",
    "Китай, Гуанси-Чжуанский автономный район, городской округ Гуйлинь, город Гуйлинь",
    "China, Guangxi Zhuang Autonomous Region, Guilin District",
    "Швеция, Стокгольм",
    "Sweden, Stockholm",
    "Бразилия, Рио-де-Жанейро",
    "Brasil, Rio de Janeiro",
    "Индия, штат Западная Бенгалия, Калькутта",
    "India, State of West Bengal, Kolkata",
    "Аргентина, Буэнос-Айрес",
    "Argentina, Ciudad Autónoma de Buenos Aires",
    "Япония, префектура Тиба, город Тиба",
    "Japan, Chiba Prefecture, gorod Tiba",
    "Камбоджа, город Сиемреап",
    "Франция, Прованс-Альпы-Лазурный Берег, Приморские Альпы, город Ницца",
    "France, Provence-Alpes-Côte d'Azur, Alpes-Maritimes, Nice",
    "Мексика, Мехико",
    "Mexico",
    "Перу, Лима",
    "Perú, Lima",
    "Тайвань, Тайчжун",
    "Taiwan, Taichung, City of Taichung",
    "Соединённые Штаты Америки, округ Колумбия, Вашингтон",
    "United States of America, District of Columbia, City of Washington",
    "Объединенные Арабские Эмираты, Абу-Даби",
    "United Arab Emirates, City of Abu Dhabi",
    "Шри-Ланка, Западная провинция, Коломбо",
    "Sri Lanka, Western, Colombo",
    "Россия, Челябинская область, Магнитогорск",
    "Russia, Chelyabinsk Region, Magnitogorsk",
    "Россия, Свердловская область, Екатеринбург",
    "Russia, Sverdlovsk Region, Yekaterinburg",
    "Россия, Новосибирск",
    "Russia, Novosibirsk",
    "Россия, Нижний Новгород",
    "Russia, Nizhniy Novgorod",
    "Россия, Челябинск",
    "Russia, Chelyabinsk",
    "Россия, Республика Башкортостан, Уфа",
    "Russia, Republic of Bashkortostan, Ufa",
    "Россия, Приморский край, Владивосток",
    "Russia, Primorye Territory, Vladivostok",
    "Россия, Краснодарский край, Сочи",
    "Russia, Krasnodar Territory, Sochi",
    "Россия, Ростов-на-Дону",
    "Russia, Rostov-on-Don",
    "Россия, Краснодар",
    "Russia, Krasnodar",
    "Россия, Калининград",
    "Russia, Kaliningrad",
    "Россия, Ярославль",
    "Russia, Yaroslavl",
    "Россия, Самара",
    "Russia, Samara",
    "Россия, Саратов",
    "Russia, Saratov",
    "Россия, Тверь",
    "Russia, Tver",
    "Россия, Тюмень",
    "Russia, Tyumen",
    "Россия, Омск",
    "Russia, Omsk",
    "Россия, Самарская область, Тольятти",
    "Russia, Samara Region, Tolyatti",
    "Россия, Республика Марий Эл, Йошкар-Ола",
    "Russia, Republic of Mari El, Yoshkar-Ola",
    "Россия, Красноярск",
    "Russia, Krasnoyarsk",
    "Албания, Тирана",
    "Shqipërisë, Tirana",
    "Андорра, община Андорра-ла-Велья",
    "Andorra, Parròquia Andorra la Vella",
    "Беларусь, Минск",
    "Belarus, Minsk",
    "Болгария, София",
    "Bulgaria, Sofia",
    "Босния и Герцеговина, Сараево",
    "Bosnia and Herzegovina, Sarajevo",
    "Ватикан",
    "Vatican",
    "Исландия, Рейкьявик",
    "Iceland, Reykjavíkurborg",
    "Латвия, Рига",
    "Latvia, Riga",
    "Литва, Вильнюс",
    "Lithuania, Vilnius",
    "Лихтенштейн, Вадуц",
    "Liechtenstein, Vaduz",
    "Бельгия, Люксембург",
    "Belgium, Luxembourg",
    "Северная Македония, Скопье",
    "North Macedonia, Skopje",
    "Мальта, Валлетта",
    "Malta, Valletta",
    "Молдова, Кишинёв",
    "Moldova, Chisinau",
    "Монако",
    "Monaco",
    "Норвегия, Осло",
    "Norway, Oslo",
    "Румыния, Бухарест",
    "Romania, Bucharest",
    "Сан-Марино",
    "San Marino, city of San Marino",
    "Сербия, Белград",
    "Serbia, Belgrade",
    "Словакия, Братислава",
    "Slovakia, Bratislava",
    "Словения, Любляна",
    "Slovenia, Ljubljana",
    "Украина, Киев",
    "Ukraine, Kyiv",
    "Финляндия, Хельсинки",
    "Finland, Helsinki",
    "Черногория, Подгорица",
    "Montenegro, Podgorica",
    "Хорватия, Загреб",
    "Croatia, Zagreb",
    "Швейцария, Берн",
    "Schweiz, Bern",
    "Эстония, Таллин",
    "Estonia, Tallinn",
    "Россия, Республика Татарстан, Казань",
    "Russia, Republic of Tatarstan, City of Kazan"
)