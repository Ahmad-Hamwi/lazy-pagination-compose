package io.github.ahmad_hamwi.compose.pagination

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToKey
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class PaginatedLazyColumnTest {

    companion object {
        const val LAZY_COLUMN_TAG = "lazyColumn"
        const val FIRST_PAGE_PROGRESS_INDICATOR_TAG = "firstPageProgressIndicator"
        const val FIRST_PAGE_ERROR_INDICATOR_TAG = "firstPageErrorIndicator"
        const val ITEM_CONTENT_TAG = "itemContent"
        const val NEW_PAGE_PROGRESS_INDICATOR_TAG = "newPageProgressIndicator"
        const val NEW_PAGE_ERROR_INDICATOR_TAG = "newPageErrorIndicator"
    }

    @Suppress("TestFunctionName")
    @Composable
    fun SutComposable(
        paginationState: PaginationState<String>
    ) {
        PaginatedLazyColumn(
            modifier = Modifier.testTag(LAZY_COLUMN_TAG),
            paginationState = paginationState,
            firstPageProgressIndicator = {
                Box(modifier = Modifier.testTag(FIRST_PAGE_PROGRESS_INDICATOR_TAG))
            },
            firstPageErrorIndicator = {
                Box(modifier = Modifier.testTag(FIRST_PAGE_ERROR_INDICATOR_TAG)) {
                    Text(it.message.toString())
                }
            },
            newPageProgressIndicator = {
                Box(modifier = Modifier.testTag(NEW_PAGE_PROGRESS_INDICATOR_TAG))
            },
            newPageErrorIndicator = {
                Box(modifier = Modifier.testTag(NEW_PAGE_ERROR_INDICATOR_TAG)) {
                    Text(it.message.toString())
                }
            }
        ) {
            itemsIndexed(
                items = paginationState.allItems,
                key = { i, _ -> i }
            ) { _, _ ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .testTag(ITEM_CONTENT_TAG)
                ) {}
            }
        }
    }

    private fun defaultPaginationState(onRequestPage: (Int) -> Unit) = PaginationState<String>(
        initialPageNumber = 1,
        onRequestPage = {onRequestPage(it) }
    )

    @Test
    fun `first page progress indicator shown when null page`() = runComposeUiTest {
        val pageNumbersCalled = mutableListOf<Int>()

        val state = defaultPaginationState { pageNumber ->
            pageNumbersCalled += pageNumber
        }

        setContent { SutComposable(state) }

        onNodeWithTag(FIRST_PAGE_PROGRESS_INDICATOR_TAG).assertExists()
        assertThat(pageNumbersCalled).isEqualTo(listOf(1))
    }

    @Test
    fun `first page progress indicator hidden when a page has been appended`() = runComposeUiTest {
        val pageNumbersCalled = mutableListOf<Int>()

        val paginationState = defaultPaginationState { pageNumberCalled ->
            pageNumbersCalled += pageNumberCalled
        }

        setContent { SutComposable(paginationState) }

        paginationState.appendPage(items = listOf(""), nextPageNumber = 2, isLastPage = true)

        onNodeWithTag(FIRST_PAGE_PROGRESS_INDICATOR_TAG).assertDoesNotExist()
        assertThat(pageNumbersCalled).isEqualTo(listOf(1))
    }

    @Test
    fun `first page error is shown when no page and error happened`() = runComposeUiTest {
        val pageNumbersCalled = mutableListOf<Int>()
        val state = defaultPaginationState { pageNumber ->
            pageNumbersCalled += pageNumber
        }

        setContent {
            SutComposable(paginationState = state)
        }

        state.setError(Exception("First page error"))

        onNodeWithTag(FIRST_PAGE_ERROR_INDICATOR_TAG).assertExists()
        onNodeWithText("First page error").assertExists()
        assertThat(pageNumbersCalled).isEqualTo(listOf(1))
    }

    @Test
    fun `first page is shown when put page is triggered for the first time`() = runComposeUiTest {
        val pageNumbersCalled = mutableListOf<Int>()
        val state = defaultPaginationState { pageNumber ->
            pageNumbersCalled += pageNumber
        }

        setContent { SutComposable(state) }

        state.appendPage(items = listOf(""), nextPageNumber = 2, isLastPage = true)

        onNodeWithTag(ITEM_CONTENT_TAG).assertExists()
        assertThat(pageNumbersCalled).isEqualTo(listOf(1))
    }

    @Test
    fun `scrolling down the list will show progress and trigger page request`() = runComposeUiTest {
        val pageNumbersCalled = mutableListOf<Int>()
        val state = defaultPaginationState { pageNumber ->
            pageNumbersCalled += pageNumber
        }

        setContent { SutComposable(state) }

        state.appendPage(items = listOf("", "", "", "", ""), nextPageNumber = 2)

        onNodeWithTag(LAZY_COLUMN_TAG).performScrollToIndex(4)
        onNodeWithTag(LAZY_COLUMN_TAG).performScrollToKey(LazyListKeys.NEW_PAGE_PROGRESS_INDICATOR_KEY)

        onNodeWithTag(NEW_PAGE_PROGRESS_INDICATOR_TAG).assertExists()
        assertThat(pageNumbersCalled).isEqualTo(listOf(1, 2))
    }

    @Test
    fun `scrolling down the list will show error and trigger page request`() = runComposeUiTest {
        val pageNumbersCalled = mutableListOf<Int>()
        val state = defaultPaginationState { pageNumber ->
            pageNumbersCalled += pageNumber
        }

        setContent { SutComposable(state) }

        state.appendPage(items = listOf("", "", "", "", ""), nextPageNumber = 2)
        state.setError(Exception("New page error"))

        onNodeWithTag(LAZY_COLUMN_TAG).performScrollToIndex(4)
        onNodeWithTag(LAZY_COLUMN_TAG).performScrollToKey(LazyListKeys.NEW_PAGE_ERROR_INDICATOR_KEY)

        onNodeWithTag(NEW_PAGE_ERROR_INDICATOR_TAG).assertExists()
        onNodeWithText("New page error").assertExists()
        assertThat(pageNumbersCalled).isEqualTo(listOf(1))
    }


    @Test
    fun `appending last page prevents loading and new page requests`() = runComposeUiTest {
        val pageNumbersCalled = mutableListOf<Int>()
        val state = defaultPaginationState { pageNumbersCalled += it }

        setContent { SutComposable(state) }

        state.appendPage(items = listOf(""), nextPageNumber = 2, isLastPage = true)

        onNodeWithTag(NEW_PAGE_PROGRESS_INDICATOR_TAG).assertDoesNotExist()
        assertThat(pageNumbersCalled).isEqualTo(listOf(1))
    }

    @Test
    fun `retry first failed request would request again the same page and show progress`() =
        runComposeUiTest {
            val pageNumbersCalled = mutableListOf<Int>()

            val state = defaultPaginationState { pageNumbersCalled += it }

            setContent { SutComposable(state) }
            state.setError(Exception())
            state.retryLastFailedRequest()

            onNodeWithTag(FIRST_PAGE_PROGRESS_INDICATOR_TAG).assertExists()
            assertThat(pageNumbersCalled).isEqualTo(listOf(1, 1))
        }

    @Test
    fun `retry new page failed request would request again the same page and show progress`() =
        runComposeUiTest {
            val pageNumbersCalled = mutableListOf<Int>()

            val state = defaultPaginationState { pageNumbersCalled += it }

            setContent { SutComposable(state) }
            state.appendPage(items = listOf(), nextPageNumber = 2)
            state.setError(Exception())
            state.retryLastFailedRequest()

            onNodeWithTag(LAZY_COLUMN_TAG).performScrollToIndex(0)

            onNodeWithTag(NEW_PAGE_PROGRESS_INDICATOR_TAG).assertExists()
            assertThat(pageNumbersCalled).isEqualTo(listOf(1, 2))
        }

    @Test
    fun `Refreshing resets the state and an initial load starts`() = runComposeUiTest {
        val pageNumbersCalled = mutableListOf<Int>()
        val state = defaultPaginationState { pageNumbersCalled += it }

        setContent { SutComposable(state) }

        state.refresh()

        onNodeWithTag(FIRST_PAGE_PROGRESS_INDICATOR_TAG).assertExists()
        assertThat(pageNumbersCalled).isEqualTo(listOf(1, 1))
    }

    @Test
    fun `first page error then refresh then first page requested again then show first page`() =
        runComposeUiTest {
            val pageNumbersCalled = mutableListOf<Int>()
            val state = defaultPaginationState { pageNumbersCalled += it }
            state.setError(Exception())
            setContent { SutComposable(state) }
            onNodeWithTag(FIRST_PAGE_ERROR_INDICATOR_TAG).assertExists()

            state.refresh()
            onNodeWithTag(FIRST_PAGE_PROGRESS_INDICATOR_TAG).assertExists()

            state.appendPage(listOf(""), nextPageNumber = 2, isLastPage = true)
            onNodeWithTag(ITEM_CONTENT_TAG).assertExists()

            assertThat(pageNumbersCalled).isEqualTo(listOf(1))
        }

    @Test
    fun `first page loaded then refresh then first page requested again then show first page`() =
        runComposeUiTest {
            val pageNumbersCalled = mutableListOf<Int>()
            val state = defaultPaginationState { pageNumbersCalled += it }
            state.appendPage(listOf(""), nextPageNumber = 2, isLastPage = true)
            setContent { SutComposable(state) }

            state.refresh()
            onNodeWithTag(FIRST_PAGE_PROGRESS_INDICATOR_TAG).assertExists()

            state.appendPage(listOf(""), nextPageNumber = 2, isLastPage = true)
            onNodeWithTag(ITEM_CONTENT_TAG).assertExists()

            assertThat(pageNumbersCalled).isEqualTo(listOf(1))
        }

    @Test
    fun `loads only one page after a scroll`() = runComposeUiTest {
        val pageNumbersCalled = mutableListOf<Int>()
        val state = defaultPaginationState { pageNumbersCalled += it }

        setContent { SutComposable(state) }
        state.appendPage(items = listOf("", "", "", "", ""), nextPageNumber = 2, isLastPage = false)
        onNodeWithTag(LAZY_COLUMN_TAG).performScrollToIndex(4)
        waitForIdle()
        state.appendPage(items = listOf("", "", "", "", ""), nextPageNumber = 3, isLastPage = false)
        waitForIdle()

        assertThat(pageNumbersCalled).isEqualTo(listOf(1, 2))
    }

    @Test
    fun `initial page is 2 would load page 2`() = runComposeUiTest {
        var pageNumberCalled: Int? = null

        val state = PaginationState<String>(
            initialPageNumber = 2,
            onRequestPage = { pageNumber ->
                pageNumberCalled = pageNumber
            }
        )

        setContent { SutComposable(state) }

        assertThat(pageNumberCalled).isEqualTo(2)
    }

    @Test
    fun `refreshing with initial page of 2 would load page 2`() = runComposeUiTest {
        var pageNumberCalled: Int? = null

        val state = PaginationState<String>(
            initialPageNumber = 1,
            onRequestPage = { pageNumber ->
                pageNumberCalled = pageNumber
            }
        )

        setContent { SutComposable(state) }

        state.refresh(initialPageNumber = 3)
        waitForIdle()

        assertThat(pageNumberCalled).isEqualTo(3)
    }
}