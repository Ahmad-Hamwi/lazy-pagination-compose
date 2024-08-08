package io.github.ahmad_hamwi.compose.pagination

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import kotlin.test.Test

class PaginatedLazyColumnTest : PaginatedLazyListTest() {

    @Suppress("TestFunctionName")
    @Composable
    override fun SutComposable(
        paginationState: PaginationState<Int, String>
    ) {
        PaginatedLazyColumn(
            modifier = Modifier.testTag(LAZY_LIST_TAG),
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

    @Test
    override fun `first page progress indicator shown when null page`() =
        super.`first page progress indicator shown when null page`()

    @Test
    override fun `first page progress indicator hidden when a page has been appended`() =
        super.`first page progress indicator hidden when a page has been appended`()

    @Test
    override fun `first page error is shown when no page and error happened`() =
        super.`first page error is shown when no page and error happened`()

    @Test
    override fun `first page is shown when put page is triggered for the first time`() =
        super.`first page is shown when put page is triggered for the first time`()

    @Test
    override fun `scrolling down the list will show progress and trigger page request`() =
        super.`scrolling down the list will show progress and trigger page request`()

    @Test
    override fun `scrolling down the list will show error and trigger page request`() =
        super.`scrolling down the list will show error and trigger page request`()


    @Test
    override fun `appending last page prevents loading and new page requests`() =
        super.`appending last page prevents loading and new page requests`()

    @Test
    override fun `retry first failed request would request again the same page and show progress`() =
        super.`retry first failed request would request again the same page and show progress`()

    @Test
    override fun `retry new page failed request would request again the same page and show progress`() =
        super.`retry new page failed request would request again the same page and show progress`()

    @Test
    override fun `Refreshing resets the state and an initial load starts`() =
        super.`Refreshing resets the state and an initial load starts`()

    @Test
    override fun `first page error then refresh then first page requested again then show first page`() =
        super.`first page error then refresh then first page requested again then show first page`()

    @Test
    override fun `first page loaded then refresh then first page requested again then show first page`() =
        super.`first page loaded then refresh then first page requested again then show first page`()

    @Test
    override fun `loads only one page after a scroll`() =
        super.`loads only one page after a scroll`()

    @Test
    override fun `initial page is 2 would load page 2`() =
        super.`initial page is 2 would load page 2`()

    @Test
    override fun `refreshing with initial page of 2 would load page 2`() =
        super.`refreshing with initial page of 2 would load page 2`()
}