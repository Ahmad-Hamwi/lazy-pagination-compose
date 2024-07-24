import kotlinx.coroutines.delay

object DataSource {
    private const val TOTAL = 3

    suspend fun getPage(pageNumber: Int): MyPageModel<String> {
        delay(1200)

        return MyPageModel(
            pageNumber,
            (((pageNumber - 1) * 10 + 1)..(pageNumber * 10)).map { "Item $it" },
            pageNumber == TOTAL
        )
    }
}