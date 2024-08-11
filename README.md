<h1 align="center">Lazy Pagination - Compose Multiplatform</h1>

<p align="center">
    <a href="https://opensource.org/licenses/MIT"><img alt="API" src="https://img.shields.io/badge/License-MIT-blue.svg"/></a>
    <a href="https://maven-badges.herokuapp.com/maven-central/io.github.ahmad-hamwi/lazy-pagination-compose"><img alt="API" src="https://maven-badges.herokuapp.com/maven-central/io.github.ahmad-hamwi/lazy-pagination-compose/badge.svg"/></a>
    <a href="http://kotlinlang.org"><img alt="API" src="https://img.shields.io/badge/kotlin-2.0.0-blue.svg?logo=kotlin"/></a>
    <img alt="API" src="https://github.com/Ahmad-Hamwi/lazy-pagination-compose/actions/workflows/unit-test.yml/badge.svg"/>
    <img alt="API" src="https://github.com/Ahmad-Hamwi/lazy-pagination-compose/actions/workflows/deploy-central.yml/badge.svg"/>
    <br/>
    <br/>
    <img alt="API" src="https://img.shields.io/badge/-Android-gray.svg?style=flat"/>
    <img alt="API" src="https://img.shields.io/badge/-iOS-gray.svg?style=flat"/>
    <img alt="API" src="https://img.shields.io/badge/-JVM-gray.svg?style=flat"/>
    <img alt="API" src="https://img.shields.io/badge/-Windows-gray.svg?style=flat"/>
    <img alt="API" src="https://img.shields.io/badge/-Linux-gray.svg?style=flat"/>
    <img alt="API" src="https://img.shields.io/badge/-macOS-gray.svg?style=flat"/>
    <img alt="API" src="https://img.shields.io/badge/-JS-gray.svg?style=flat"/>
    <img alt="API" src="https://img.shields.io/badge/-WASM-gray.svg?style=flat"/>
</p>
<br>

An intuitive and customizable Compose Multiplatform pagination solution built on top of lazy lists such as `LazyColumn` & `LazyRow` and it extends their APIs.

Two points where in mind while creating this library:
- Should have a simple and intuitive APIs with an easy learning curve.
- Can be placed in your Presentation/UI layer ONLY.

## Features ##

- Vertical & horizontal pagination using `PaginatedLazyColumn` & `PaginatedLazyRow`.
- Seamless integration with a `PaginationState` that you use along with your data layer.  
- Generic fetching strategies such as offset-based, cursor-based, time-based, etc...
- Resetting your pagination state using `refresh()` function
- Retrying your data fetches using `retryLastFailedRequest()` function

### `PaginatedLazyColumn`: ###

<p>
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExaDZtN3dzajNicXpxZjYwNWdlMTZuNmEydzJqeXI4bzhlZThmYmVyayZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/UQtqa7NP2DedMIQQE0/giphy.gif" width="27%" align="top" />
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExNHZydHgyNmJyYjI2bXA3M3VsbnRvbXYwcTdkMWVycWlwa3ZudTMxaiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/hyOaVKanSlkesmRWLS/giphy.gif" width="25.62%" align="top" />
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExb3ozdXpubDA2enptdW81aHhucndpZ2Y2MGw5cTFuMmNneDcxM3JocyZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/RxUB3WW69I3N65pQuv/giphy.gif" width="45.5%" align="top" />
</p>

### `PaginatedLazyRow`: ###

<p>
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExODIwZHduaXhhaGN6MWUxb3luZHlqN2xvMm9vNDBmcGoyNzF4bnhnYSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/Tv7k3mPfmkDzss8EBk/giphy.gif" width="27%" align="top" />
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExdXg2NXp1bHlmbWV1OXB0Nzd0eTRrOXV0eGo1eHRvZXA2Nmg3ejQ0dyZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/U0EhwO1KN5keyfBFH1/giphy.gif" width="25.62%" align="top" />
    <img src="https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExN2M3cWV3MWFtbGQxMmhmaWx1ejRtYnVoM202bHN0NzRiMnVyYW8xYiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/BfuyEzSm7ZcH6A4sYj/giphy.gif" width="45.5%" align="top" />
</p>



<details>
    <summary> 
        <h1>Example (no ViewModel)</h1> 
    </summary>
    
```kotlin
// Int is passed as the KEY which represents your pagination fetching strategy
// Int example here means the page number but could really by anything such as a cursor, time, etc...
@Composable
fun Content() {
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
        firstPageErrorIndicator = { e -> // from setError
            ... e.message ...
            ... onRetry = { paginationState.retryLastFailedRequest() } ...
        },
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

</details>
<details open>
    <summary> 
        <h1>Example with ViewModel</h1> 
    </summary>

```kotlin
class MyViewModel : ViewModel() {
    // Int is passed as the KEY which represents your pagination fetching strategy
    // Int example here means the page number but could really by anything such as a cursor, time, etc...
    val paginationState = PaginationState<Int, Model>(
       initialPageKey = 1,
       onRequestPage = { loadPage(it) }
    )
    
    fun loadPage(pageKey: Int) {
       viewModelScope.launch {
          try {
              val page = DataSource.getPage(pageNumber = pageKey, pageSize = 10)
    
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
        firstPageErrorIndicator = { e -> // from setError
            ... e.message ...
            ... onRetry = { paginationState.retryLastFailedRequest() } ...
        },
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

</details>

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

### More complex sample can be found in the [sample module](https://github.com/Ahmad-Hamwi/lazy-pagination-compose/tree/main/sample) ###

# Contributing #
This library is made to help other developers out in their app developments, feel free to contribute by suggesting ideas and creating issues and PRs that would make this repository more helpful.

# Honorable Mentions #
Thank you for the following contributors:
- [Tomislav Mladenov](https://github.com/TomislavMladenov): For adding support to the JS target.

# Support #
You can show support by either contributing to the repository or by buying me a cup of coffee!

<p>
    <a href="https://www.buymeacoffee.com/ahmadhamwi" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="Buy Me A Coffee" />
</p>

# License

Copyright (C) 2024 Ahmad Hamwi

Licensed under the MIT License
