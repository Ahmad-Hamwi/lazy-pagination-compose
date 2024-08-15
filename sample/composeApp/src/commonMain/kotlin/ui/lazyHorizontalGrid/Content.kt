package ui.lazyHorizontalGrid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.DataSource
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyHorizontalGrid
import io.github.ahmad_hamwi.compose.pagination.rememberPaginationState
import kotlinx.coroutines.launch

@Composable
fun PaginatedLazyHorizontalGridSampleContent(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val dataSource = remember { DataSource(itemsCount = 20) }

    val paginationState = rememberPaginationState(
        initialPageKey = 1,
        onRequestPage = { pageKey: Int ->
            scope.launch {
                try {
                    val page = dataSource.getPage(pageNumber = pageKey)

                    appendPage(
                        items = page.items,
                        nextPageKey = page.nextPageKey,
                        isLastPage = page.isLastPage
                    )
                } catch (e: Exception) {
                    setError(e)
                }
            }
        }
    )

    PaginatedLazyHorizontalGrid(
        modifier = modifier,
        paginationState = paginationState,
        firstPageProgressIndicator = { FirstPageProgressIndicator(modifier) },
        newPageProgressIndicator = { NewPageProgressIndicator(modifier) },
        firstPageErrorIndicator = { e ->
            FirstPageErrorIndicator(
                modifier = modifier,
                exception = e,
                onRetryClicked = {
                    paginationState.retryLastFailedRequest()
                }
            )
        },
        newPageErrorIndicator = { e ->
            NewPageErrorIndicator(
                exception = e,
                onRetryClicked = {
                    paginationState.retryLastFailedRequest()
                }
            )
        },
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        rows = GridCells.Fixed(3),
    ) {
        itemsIndexed(
            paginationState.allItems,
        ) { _, item ->
            HorizontalGridItem(modifier = modifier, value = item)
        }
    }
}
