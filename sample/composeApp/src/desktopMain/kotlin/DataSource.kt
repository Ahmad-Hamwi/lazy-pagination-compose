import kotlinx.coroutines.delay

actual class DataSource {
    private val totalPagesCount = 2

    actual suspend fun getPage(pageNumber: Int): MyPageModel<String> {
        delay(500)

        return MyPageModel(
            pageNumber,
            (((pageNumber - 1) * 10 + 1)..(pageNumber * 10)).map { "Item $it" },
            pageNumber == totalPagesCount
        )
    }
}