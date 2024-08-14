package io.github.ahmad_hamwi.compose.pagination

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToKey
import androidx.compose.ui.test.runComposeUiTest
import assertk.assertThat
import assertk.assertions.isEqualTo

@OptIn(ExperimentalTestApi::class)
abstract class PaginatedLazyListTest {

    companion object {
        const val LAZY_LIST_TAG = "lazyList"
        const val FIRST_PAGE_PROGRESS_INDICATOR_TAG = "firstPageProgressIndicator"
        const val FIRST_PAGE_ERROR_INDICATOR_TAG = "firstPageErrorIndicator"
        const val ITEM_CONTENT_TAG = "itemContent"
        const val NEW_PAGE_PROGRESS_INDICATOR_TAG = "newPageProgressIndicator"
        const val NEW_PAGE_ERROR_INDICATOR_TAG = "newPageErrorIndicator"
    }

    @Suppress("TestFunctionName")
    @Composable
    abstract fun SutComposable(
        paginationState: PaginationState<Int, String>
    )

    private fun defaultPaginationState(onRequestPage: (Int) -> Unit) = PaginationState<Int, String>(
        initialPageKey = 1,
        onRequestPage = {onRequestPage(it) }
    )

    open fun `first page progress indicator shown when null page`() = runComposeUiTest {
        val pageKeysCalled = mutableListOf<Int>()

        val state = defaultPaginationState { pageKey ->
            pageKeysCalled += pageKey
        }

        setContent { SutComposable(state) }

        onNodeWithTag(FIRST_PAGE_PROGRESS_INDICATOR_TAG).assertExists()
        assertThat(pageKeysCalled).isEqualTo(listOf(1))
    }

    open fun `first page progress indicator hidden when a page has been appended`() = runComposeUiTest {
        val pageKeysCalled = mutableListOf<Int>()

        val paginationState = defaultPaginationState { pageKeyCalled ->
            pageKeysCalled += pageKeyCalled
        }

        setContent { SutComposable(paginationState) }

        paginationState.appendPage(items = listOf(""), nextPageKey = 2, isLastPage = true)

        onNodeWithTag(FIRST_PAGE_PROGRESS_INDICATOR_TAG).assertDoesNotExist()
        assertThat(pageKeysCalled).isEqualTo(listOf(1))
    }

    open fun `first page error is shown when no page and error happened`() = runComposeUiTest {
        val pageKeysCalled = mutableListOf<Int>()
        val state = defaultPaginationState { pageKey ->
            pageKeysCalled += pageKey
        }

        setContent {
            SutComposable(paginationState = state)
        }

        state.setError(Exception("First page error"))

        onNodeWithTag(FIRST_PAGE_ERROR_INDICATOR_TAG).assertExists()
        onNodeWithText("First page error").assertExists()
        assertThat(pageKeysCalled).isEqualTo(listOf(1))
    }

    open fun `first page is shown when put page is triggered for the first time`() = runComposeUiTest {
        val pageKeysCalled = mutableListOf<Int>()
        val state = defaultPaginationState { pageKey ->
            pageKeysCalled += pageKey
        }

        setContent { SutComposable(state) }

        state.appendPage(items = listOf(""), nextPageKey = 2, isLastPage = true)

        onNodeWithTag(ITEM_CONTENT_TAG).assertExists()
        assertThat(pageKeysCalled).isEqualTo(listOf(1))
    }

    open fun `scrolling down the list will show progress and trigger page request`() = runComposeUiTest {
        val pageKeysCalled = mutableListOf<Int>()
        val state = defaultPaginationState { pageKey ->
            pageKeysCalled += pageKey
        }

        setContent { SutComposable(state) }

        state.appendPage(items = listOf("", "", "", "", ""), nextPageKey = 2)

        onNodeWithTag(LAZY_LIST_TAG).performScrollToIndex(4)
        onNodeWithTag(LAZY_LIST_TAG).performScrollToKey(LazyListKeys.NEW_PAGE_PROGRESS_INDICATOR_KEY)

        onNodeWithTag(NEW_PAGE_PROGRESS_INDICATOR_TAG).assertExists()
        assertThat(pageKeysCalled).isEqualTo(listOf(1, 2))
    }

    open fun `scrolling down the list will show error and trigger page request`() = runComposeUiTest {
        val pageKeysCalled = mutableListOf<Int>()
        val state = defaultPaginationState { pageKey ->
            pageKeysCalled += pageKey
        }

        setContent { SutComposable(state) }

        state.appendPage(items = listOf("", "", "", "", ""), nextPageKey = 2)
        state.setError(Exception("New page error"))

        onNodeWithTag(LAZY_LIST_TAG).performScrollToIndex(4)
        onNodeWithTag(LAZY_LIST_TAG).performScrollToKey(LazyListKeys.NEW_PAGE_ERROR_INDICATOR_KEY)

        onNodeWithTag(NEW_PAGE_ERROR_INDICATOR_TAG).assertExists()
        onNodeWithText("New page error").assertExists()
        assertThat(pageKeysCalled).isEqualTo(listOf(1))
    }


    open fun `appending last page prevents loading and new page requests`() = runComposeUiTest {
        val pageKeysCalled = mutableListOf<Int>()
        val state = defaultPaginationState { pageKeysCalled += it }

        setContent { SutComposable(state) }

        state.appendPage(items = listOf(""), nextPageKey = 2, isLastPage = true)

        onNodeWithTag(NEW_PAGE_PROGRESS_INDICATOR_TAG).assertDoesNotExist()
        assertThat(pageKeysCalled).isEqualTo(listOf(1))
    }

    open fun `retry first failed request would request again the same page and show progress`() =
        runComposeUiTest {
            val pageKeysCalled = mutableListOf<Int>()

            val state = defaultPaginationState { pageKeysCalled += it }

            setContent { SutComposable(state) }
            state.setError(Exception())
            state.retryLastFailedRequest()

            onNodeWithTag(FIRST_PAGE_PROGRESS_INDICATOR_TAG).assertExists()
            assertThat(pageKeysCalled).isEqualTo(listOf(1, 1))
        }

    open fun `retry new page failed request would request again the same page and show progress`() =
        runComposeUiTest {
            val pageKeysCalled = mutableListOf<Int>()

            val state = defaultPaginationState { pageKeysCalled += it }

            setContent { SutComposable(state) }
            state.appendPage(items = listOf(), nextPageKey = 2)
            state.setError(Exception())
            state.retryLastFailedRequest()

            onNodeWithTag(LAZY_LIST_TAG).performScrollToIndex(0)

            onNodeWithTag(NEW_PAGE_PROGRESS_INDICATOR_TAG).assertExists()
            assertThat(pageKeysCalled).isEqualTo(listOf(1, 2))
        }

    open fun `Refreshing resets the state and an initial load starts`() = runComposeUiTest {
        val pageKeysCalled = mutableListOf<Int>()
        val state = defaultPaginationState { pageKeysCalled += it }

        setContent { SutComposable(state) }

        state.refresh()

        onNodeWithTag(FIRST_PAGE_PROGRESS_INDICATOR_TAG).assertExists()
        assertThat(pageKeysCalled).isEqualTo(listOf(1, 1))
    }

    open fun `first page error then refresh then first page requested again then show first page`() =
        runComposeUiTest {
            val pageKeysCalled = mutableListOf<Int>()
            val state = defaultPaginationState { pageKeysCalled += it }
            state.setError(Exception())
            setContent { SutComposable(state) }
            onNodeWithTag(FIRST_PAGE_ERROR_INDICATOR_TAG).assertExists()

            state.refresh()
            onNodeWithTag(FIRST_PAGE_PROGRESS_INDICATOR_TAG).assertExists()

            state.appendPage(listOf(""), nextPageKey = 2, isLastPage = true)
            onNodeWithTag(ITEM_CONTENT_TAG).assertExists()

            assertThat(pageKeysCalled).isEqualTo(listOf(1))
        }

    open fun `first page loaded then refresh then first page requested again then show first page`() =
        runComposeUiTest {
            val pageKeysCalled = mutableListOf<Int>()
            val state = defaultPaginationState { pageKeysCalled += it }
            state.appendPage(listOf(""), nextPageKey = 2, isLastPage = true)
            setContent { SutComposable(state) }

            state.refresh()
            onNodeWithTag(FIRST_PAGE_PROGRESS_INDICATOR_TAG).assertExists()

            state.appendPage(listOf(""), nextPageKey = 2, isLastPage = true)
            onNodeWithTag(ITEM_CONTENT_TAG).assertExists()

            assertThat(pageKeysCalled).isEqualTo(listOf(1))
        }

    open fun `loads only one page after a scroll`() = runComposeUiTest {
        val pageKeysCalled = mutableListOf<Int>()
        val state = defaultPaginationState { pageKeysCalled += it }

        setContent { SutComposable(state) }
        state.appendPage(items = listOf("", "", "", "", ""), nextPageKey = 2, isLastPage = false)
        onNodeWithTag(LAZY_LIST_TAG).performScrollToIndex(4)
        waitForIdle()
        state.appendPage(items = listOf("", "", "", "", ""), nextPageKey = 3, isLastPage = true)
        waitForIdle()

        assertThat(pageKeysCalled).isEqualTo(listOf(1, 2))
    }

    open fun `initial page is 2 would load page 2`() = runComposeUiTest {
        var pageKeyCalled: Int? = null

        val state = PaginationState<Int, String>(
            initialPageKey = 2,
            onRequestPage = { pageKey ->
                pageKeyCalled = pageKey
            }
        )

        setContent { SutComposable(state) }

        assertThat(pageKeyCalled).isEqualTo(2)
    }

    open fun `refreshing with initial page of 2 would load page 2`() = runComposeUiTest {
        var pageKeyCalled: Int? = null

        val state = PaginationState<Int, String>(
            initialPageKey = 1,
            onRequestPage = { pageKey ->
                pageKeyCalled = pageKey
            }
        )

        setContent { SutComposable(state) }

        state.refresh(initialPageKey = 3)
        waitForIdle()

        assertThat(pageKeyCalled).isEqualTo(3)
    }
}