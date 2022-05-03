# Merca App Android

This app allows you to find and see information about products exposed on the marketplace of Mercado Libre. You can see information about prices, shipping, condition 
and also you can take a look at the product on the official store.

## How it looks &#127916;

![video](https://user-images.githubusercontent.com/25846563/166396655-07fd39a2-9ce4-4257-830b-73ed7b201c92.gif)

## Specifications &#9989;

**Current version:** 1.0.0

**Minimum OS version:** Android 9 - Pie (API level 28)

**Maximum OS version:** Android 13 (API level 32)

**Percentage of support devices (based on OS version):** ~69%

**Support for landscape:** Yes

**Dark mode:** No

**Offline support:** No

**Permissions required:** `ACCESS_NETWORK_STATE`, `INTERNET`

## Architecture &#128736;

Following concepts of clean architecture, there 3 packages that represents the data, domain and UI. There's also one package for dependency injection.

<img width="110" alt="Screen Shot 2022-05-02 at 9 52 58 PM" src="https://user-images.githubusercontent.com/25846563/166399495-676f4fa7-7609-4de0-b23d-2af5be1cc6ca.png">

- In the data layer, network operations resides inside a `RemoteProductsDataSource` and it is accessible through and implementation of `ProductsRepository`. 
Data models are converted to entities using `mappers`, this way complex deserialization logic doesn't not require changes on the business logic models.

- In the domain layer, business logic operations lives in `usecases` and real life objects are represented as `entities`. There's also the definition of the `ProductsRepository`, this, in order to follow the dependency inversion principle.

- In the UI layer, `ProductsViewModel` handles the events from the UI components and interacts with the domain layer through `usecases`, the data is presented to the interface using `LiveData` observables and compose `States`. observables All the screens are built using `composables` instead of the view system (XML).


## Stack &#128221;

This apps was built using this libraries and frameworks:

### UI

[Jetpack Compose](https://developer.android.com/jetpack/compose), [Accompanist](https://github.com/google/accompanist)
, [Compose Material](https://developer.android.com/jetpack/androidx/releases/compose-material)
, [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
, [Splash Screen](https://developer.android.com/guide/topics/ui/splash-screen)

### Image Loading:

[Coil](https://coil-kt.github.io/coil/compose/) 

### Logging: 

[Timber](https://github.com/JakeWharton/timber) 
, [OkHttp Logging Interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor) 

### Dependency Injection: 

[Hilt](https://developer.android.com/training/dependency-injection/hilt-android)

### Serialization: 

[Gson](https://github.com/google/gson)

### Networking: 

[Retrofit](https://square.github.io/retrofit/)

### Testing: 

[MockK](https://mockk.io)
, [Robolectric](http://robolectric.org) 
, [JUnit](https://developer.android.com/training/testing/local-tests)
, [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines)

## Testing &#128270;

Due to time reasons, testinng efforts were focused on the bussines logic and some part of the data layer. Mappers and networking logic are under unit tests.
In the domain layer, some methods and logic of the ProductsViewModel are covered with unit tests, here's the coverage report:

<img width="371" alt="Test coverage" src="https://user-images.githubusercontent.com/25846563/166401553-a97798ae-6555-408c-9bb2-39270975b8b9.png">


## Assumptions/Restrictions &#128072;

- Based on the [API documentation](https://developers.mercadolibre.com.ar/es_ar/items-y-busquedas) provided, searchs are limit up to 1000 results because at 
the endpoint for searching products (`search?q`) requires an authentication token.

- There are two entities that wraps the information about a product, `ProductEntity` and `ProductsDetailsEntity`, the first one is used for showing the basic
information of a product in the search results list. The second one provides the information of the details screen, this is done with the purpose of presenting the
the most recent data. 

- Filters are not available and the sorting criteria of the products is the same as the received from the API.

## TROUBLESHOOTING ⚠️

If you are experiencing having troubles when trying to see Previews of composables, upgrade Android Studio to the most recent version. The AS version used for building this project was **2021.1.1 Path 3** with Gradle Plugin **7.1.3** and Gradle Version **7.2**.

Sometimes, unit tests that depends on the test coroutines returns failed results when they're in fact successful (false positives), if this happens, please re-run the test again and try to rebuild the project.


### That's it! Thanks for checking the repo
