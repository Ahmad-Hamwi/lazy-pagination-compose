expect class DataSource() {
    suspend fun getPage(pageNumber: Int): MyPageModel<String>
}