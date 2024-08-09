<h1 align="center">Lazy Pagination - Compose Multiplatform</h1>

<p align="center">
    <a href="https://opensource.org/licenses/MIT"><img alt="API" src="https://img.shields.io/badge/License-MIT-blue.svg"/></a>
    <a href="https://maven-badges.herokuapp.com/maven-central/io.github.ahmad-hamwi/lazy-pagination-compose"><img alt="API" src="https://maven-badges.herokuapp.com/maven-central/io.github.ahmad-hamwi/lazy-pagination-compose/badge.svg"/></a>
</p>
<br>

<p align="center">
    An intuitive and customizable Compose Multiplatform pagination solution built on top of lazy lists and handles pagination states automatically as you scroll.
</p>
<p align="center">
    Available on Android, iOS, and JVM Desktop
</p>

## What to expect? ##

The library is build on top of lazy composables such as `LazyColumn` & `LazyRow` and it extends its API ot be `PaginatedLazyColumn` & `PaginatedLazyRow`, and provides you with a custom `PaginationState` that you can control in your Compose UI or in a ViewModel to handle different pagination states as the user scroll.

## Available on Android, iOS, and Desktop ##

In a Compose Multiplaform Project, You can now add your composable to the `commonMain` source set only

# `PaginatedLazyColumn`: #

<p>
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExaDZtN3dzajNicXpxZjYwNWdlMTZuNmEydzJqeXI4bzhlZThmYmVyayZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/UQtqa7NP2DedMIQQE0/giphy.gif" width="27%" align="top" />
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExNHZydHgyNmJyYjI2bXA3M3VsbnRvbXYwcTdkMWVycWlwa3ZudTMxaiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/hyOaVKanSlkesmRWLS/giphy.gif" width="25.62%" align="top" />
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExb3ozdXpubDA2enptdW81aHhucndpZ2Y2MGw5cTFuMmNneDcxM3JocyZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/RxUB3WW69I3N65pQuv/giphy.gif" width="45.5%" align="top" />
</p>

# `PaginatedLazyRow`: #

<p>
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExODIwZHduaXhhaGN6MWUxb3luZHlqN2xvMm9vNDBmcGoyNzF4bnhnYSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/Tv7k3mPfmkDzss8EBk/giphy.gif" width="27%" align="top" />
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExdXg2NXp1bHlmbWV1OXB0Nzd0eTRrOXV0eGo1eHRvZXA2Nmg3ejQ0dyZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/U0EhwO1KN5keyfBFH1/giphy.gif" width="25.62%" align="top" />
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExN2M3cWV3MWFtbGQxMmhmaWx1ejRtYnVoM202bHN0NzRiMnVyYW8xYiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/BfuyEzSm7ZcH6A4sYj/giphy.gif" width="45.5%" align="top" />
</p>

# Setup #

Get the latest version via Maven Central:

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.ahmad-hamwi/lazy-pagination-compose/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.ahmad-hamwi/lazy-pagination-compose)

Add Maven Central repository to your root build.gradle at the end of repositories:

```kotlin
allprojects {
    repositories {
        ...
        mavenCentral()
    }
}
```

### For Compose Multiplatform Project ###

```toml
[versions]
lazy-pagination-compose = "1.2.5"

[libraries]
lazyPaginationCompose = { module = "io.github.ahmad-hamwi:lazy-pagination-compose", version.ref = "lazy-pagination-compose" }
```

```kotlin
// Compose Multiplatform
sourceSets {
    commonMain.dependencies {
        implementation(libs.lazyPaginationCompose)
    }
}
```

For an Android Project use `io.github.ahmad-hamwi:lazy-pagination-compose-android`

# Usage #

### Full sample can be found in the [sample module](https://github.com/Ahmad-Hamwi/lazy-pagination-compose/tree/main/sample) ###

## 1- Prepare your pagination state ##

### Create a `PaginationState` by remembering it in your composable or holding it in your ViewModel.

```kotlin
// Int is the key which in this example represents the page number
val paginationState = rememberPaginationState<Int, Model>(
    initialPageKey = 1,
    ...
)
```

### Pass your `onRequestPage` callback when creating your `PaginationState` and call your data source ###

```kotlin
val scope = rememberCoroutineScope()

val paginationState = rememberPaginationState<Int, Model>(
    initialPageKey = 1,
    onRequestPage = { pageKey: Int ->
        scope.launch {
            val page = DataSource.getPage(pageNumber = pageKey, pageSize = 10,)
        }
    }
)
```

### Append data using `appendPage` and flag the end of your list using `isLastPage` ###
```kotlin
val scope = rememberCoroutineScope()

val paginationState = rememberPaginationState<Int, Model>(
    initialPageKey = 1,
    onRequestPage = { pageKey: Int ->
        scope.launch {
            val page = DataSource.getPage(pageNumber = pageKey, pageSize = 10,)

            appendPage(
                items = page.items,
                nextPageKey = page.nextPageNumber,
                isLastPage = page.isLastPage // optional, defaults to false
            )
        }
    }
}
```

### Handle errors using `setError` ###
```kotlin
val paginationState = rememberPaginationState<Int, Model>(
    initialPageKey = 1,
    onRequestPage = { pageKey: Int ->
        scope.launch {
            try {
                val page = DataSource.getPage(pageNumber = pageKey, pageSize = 10,)

                appendPage(
                    items = page.items,
                    nextPageKey = page.nextPageNumber,
                    isLastPage = page.isLastPage
                )
            } catch (e: Exception) {
                setError(e)
            }
        }
    }
)
```

## 2- Define your paginated lazy list `PaginatedLazyColumn` or `PaginatedLazyRow` ##

### Provide your composables for every pagination state you would like to render ###

```kotlin
@Composable
fun Content() {
    val paginationState = ...
    
    // Or PaginatedLazyRow
    PaginatedLazyColumn(
        paginationState = paginationState,
        firstPageProgressIndicator = { ... },
        newPageProgressIndicator = { ... },
        firstPageErrorIndicator = { e -> ... },
        newPageErrorIndicator = { e -> ... },
    ) {
        itemsIndexed(
            paginationState.allItems,
        ) { _, item ->
            Item(value = item)
        }
    }
}
```

### Retrying your last failed request can be through `retryLastFailedRequest` ###
```kotlin
paginationState.retryLastFailedRequest()
```

### Refreshing can be through `refresh` method ###
```kotlin
paginationState.refresh(
    initialPageKey = 1 // optional, defaults to the value provided when creating the state
)
```

**Altogether**
```kotlin
val paginationState = rememberPaginationState<Int, Model>(
    initialPageKey = 1,
    onRequestPage = { pageKey: Int ->
        scope.launch {
            try {
                val page = DataSource.getPage(pageNumber = pageKey, pageSize = 10)
    
                appendPage(
                    items = page.items,
                    nextPageKey = page.nextPageNumber,
                    isLastPage = page.isLastPage
                )
            } catch (e: Exception) {
                setError(e)
            }
        }
    }
)

// Or PaginatedLazyRow
PaginatedLazyColumn(
    paginationState = paginationState,
    firstPageProgressIndicator = { ... },
    newPageProgressIndicator = { ... },
    firstPageErrorIndicator = { e ->
        ... onRetry = { paginationState.retryLastFailedRequest() } ...
    },
    newPageErrorIndicator = { e ->
        ... onRetry = { paginationState.retryLastFailedRequest() } ...
    },
) {
    itemsIndexed(
        paginationState.allItems,
    ) { _, item ->
        Item(value = item)
    }
}
```


**An Example with a ViewModel**
```kotlin
class MyViewModel : ViewModel() {
    val paginationState = PaginationState<Int, Model>(
       initialPageKey = 1,
       onRequestPage = { loadPage(it) }
    )
    
    fun loadPage(pageKey: Int) {
       viewModelScope.launch {
          try {
              val page = DataSource.getPage(pageNumber = pageKey)
    
              paginationState.appendPage(
                  items = page.items,
                  isLastPage = page.isLastPage
              )
          } catch (e: Exception) {
              paginationState.setError(e)
          }
       }
    }
}

@Composable
fun Content(viewModel: MyViewModel) {
    val paginationState = viewModel.paginationState

    // Or PaginatedLazyRow
    PaginatedLazyColumn(
       paginationState = paginationState,
       firstPageProgressIndicator = { ... },
       newPageProgressIndicator = { ... },
       firstPageErrorIndicator = { e ->
           ... onRetry = { paginationState.retryLastFailedRequest() } ...
       },
       newPageErrorIndicator = { e ->
           ... onRetry = { paginationState.retryLastFailedRequest() } ...
       },
    ) {
       itemsIndexed(
           paginationState.allItems,
       ) { _, item ->
           Item(value = item)
       }
    }
}
```

### Full sample can be found in the [sample module](https://github.com/Ahmad-Hamwi/lazy-pagination-compose/tree/main/sample) ###

# Contributing #
This library is made to help other developers out in their app developments, feel free to contribute by suggesting ideas and creating issues and PRs that would make this repository more helpful.

# Support #
You can show support by either contributing to the repository or by buying me a cup of coffee!

<p>
    <a href="https://www.buymeacoffee.com/ahmadhamwi" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="Buy Me A Coffee" />
</p>

# License

Copyright (C) 2024 Ahmad Hamwi

Licensed under the MIT License
