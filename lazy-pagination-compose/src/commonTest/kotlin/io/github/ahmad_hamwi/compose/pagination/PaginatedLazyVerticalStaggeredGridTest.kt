package io.github.ahmad_hamwi.compose.pagination

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import kotlin.test.Test

class PaginatedLazyVerticalStaggeredGridTest : PaginatedLazyScrollableTest() {
    @Suppress("TestFunctionName")
    @Composable
    override fun SutComposable(
        paginationState: PaginationState<Int, String>
    ) {
        PaginatedLazyVerticalStaggeredGrid(
            modifier = Modifier.testTag(LAZY_SCROLLABLE_TAG),
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
            },
            columns = StaggeredGridCells.Fixed(3),
        ) {
            itemsIndexed(
                items = paginationState.allItems!!,
                key = { i, _ -> i }
            ) { _, _ ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(100.dp)
                        .testTag(ITEM_CONTENT_TAG)
                ) {}
            }
        }
    }

    @Test
    override fun firstPageProgressIndicatorShownWhenNullPage() =
        super.firstPageProgressIndicatorShownWhenNullPage()

    @Test
    override fun firstPageProgressIndicatorHiddenWhenAPageHasBeenAppended() =
        super.firstPageProgressIndicatorHiddenWhenAPageHasBeenAppended()

    @Test
    override fun firstPageErrorIsShownWhenNoPageAndErrorHappened() =
        super.firstPageErrorIsShownWhenNoPageAndErrorHappened()

    @Test
    override fun firstPageIsShownWhenPutPageIsTriggeredForTheFirstTime() =
        super.firstPageIsShownWhenPutPageIsTriggeredForTheFirstTime()

    @Test
    override fun scrollingDownTheListWillShowProgressAndTriggerPageRequest() =
        super.scrollingDownTheListWillShowProgressAndTriggerPageRequest()

    @Test
    override fun scrollingDownTheListWillShowErrorAndTriggerPageRequest() =
        super.scrollingDownTheListWillShowErrorAndTriggerPageRequest()

    @Test
    override fun appendingLastPagePreventsLoadingAndNewPageRequests() =
        super.appendingLastPagePreventsLoadingAndNewPageRequests()

    @Test
    override fun retryFirstFailedRequestWouldRequestAgainTheSamePageAndShowProgress() =
        super.retryFirstFailedRequestWouldRequestAgainTheSamePageAndShowProgress()

    @Test
    override fun retryNewPageFailedRequestWouldRequestAgainTheSamePageAndShowProgress() =
        super.retryNewPageFailedRequestWouldRequestAgainTheSamePageAndShowProgress()

    @Test
    override fun refreshingResetsTheStateAndAnInitialLoadStarts() =
        super.refreshingResetsTheStateAndAnInitialLoadStarts()

    @Test
    override fun firstPageErrorThenRefreshThenFirstPageRequestedAgainThenShowFirstPage() =
        super.firstPageErrorThenRefreshThenFirstPageRequestedAgainThenShowFirstPage()

    @Test
    override fun firstPageLoadedThenRefreshThenFirstPageRequestedAgainThenShowFirstPage() =
        super.firstPageLoadedThenRefreshThenFirstPageRequestedAgainThenShowFirstPage()

    @Test
    override fun loadsOnlyOnePageAfterAScroll() =
        super.loadsOnlyOnePageAfterAScroll()

    @Test
    override fun initialPageIs2WouldLoadPage2() =
        super.initialPageIs2WouldLoadPage2()

    @Test
    override fun refreshingWithInitialPageOf2WouldLoadPage2() =
        super.refreshingWithInitialPageOf2WouldLoadPage2()
}