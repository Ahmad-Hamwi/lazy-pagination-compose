data class MyPageModel<T>(
    val pageNumber: Int,
    val items: List<T>,
    val isLastPage: Boolean
) {
    val nextPageNumber: Int = pageNumber + 1
}