import kotlinx.coroutines.delay

object DataSource {
    private const val TOTAL = 3

    suspend fun getPage(pageNumber: Int): MyPageModel<String> {
        delay(2000)

        return MyPageModel(
            pageNumber,
            (((pageNumber - 1) * 15 + 1)..(pageNumber * 15)).map { "Item $it" },
            pageNumber == TOTAL
        )
    }
}