package io.github.ahmad_hamwi.compose.pagination

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

class PaginatedLazyColumnTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun firstPageProgressIndicatorShownInitially() = runComposeUiTest {
        setContent {
            PaginatedLazyColumn(
                paginationState = rememberPaginationState<Int, String>(
                    initialPageKey = 1,
                    onRequestPage = {}
                ),
                firstPageProgressIndicator = {
                    Box(
                        modifier = Modifier.testTag("indicator")
                    )
                },
            ) {}
        }

        onNodeWithTag("indicator").assertExists()
    }
}