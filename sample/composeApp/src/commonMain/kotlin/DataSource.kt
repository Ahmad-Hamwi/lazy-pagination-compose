import kotlinx.coroutines.delay

object DataSource {
    private val attemptsMap = mapOf(
        1 to "error",
        2 to "success",
        3 to "error",
        4 to "success",
        5 to "success",
    )

    private const val TOTAL_PAGES_COUNT = 3

    private var attempts = 0

    suspend fun getPage(pageNumber: Int): MyPageModel<String> {
        attempts++

        delay(1500)

        if (attemptsMap[attempts] == "error") {
            throw Exception("Something went wrong.\nTry again.")
        } else {
            return MyPageModel(
                pageNumber,
                (((pageNumber - 1) * 10 + 1)..(pageNumber * 10)).map { "Item $it" },
                pageNumber == TOTAL_PAGES_COUNT
            )
        }
    }
}