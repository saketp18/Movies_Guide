# Movies_Guide
Movies Guide app

#### Features of app ####
- Fetch details for movies playing now via api and paginate it.
- Search movies based on keywords also load paginated results.
- Support on different orientations.
- Bookmark movies.

Architecture used: MVVM

#### Components used to develop: ####
-  Coroutines, for offloading work which will certainly block UI.
- Livedata, observing data from viewmodel to activity.
- Dagger2, for dependency injection for 3rd party libraries as well as scoping injection of objects to particular activity.
- DataBinding, to avoid boilerplate code of findViewById
- Recyclerview
- Motion Layout, for animations
- Retrofit 2 for making network calls
- Gson for incorporating messages from network response.
- Kotlin
- Proguard to minify the apk size and obfuscate code.
- Glide

#### Tools used to develop: ####
- Emulators
- Android Studio
- Chrome stetho for observing database in app







