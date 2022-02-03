# ChefBook Android
[![Kotlin Version](https://img.shields.io/badge/Kotlin-1.5.31-blue.svg)](https://kotlinlang.org)
[![AGP](https://img.shields.io/badge/AGP-7.0.4-blue?style=flat)](https://developer.android.com/studio/releases/gradle-plugin)
[![Gradle](https://img.shields.io/badge/Gradle-7.0.2-blue?style=flat)](https://gradle.org)

ChefBook Android Client 🤖

Common information can be found at [main repository](https://github.com/mephistolie/chefbook)
## About
* **Language**: Kotlin 
* **Architecture:** MVI + UseCases
* **Reactive Code**: Kotlin Flow, StateFlow, SharedFlow
* **Build Tool:** Gradle Kotlin DSL
### Dependencies
* **DI Framework:** Hilt
* **Settings:** Proto DataStore
* **Navigation:** Jetpack Navigation Component
* **Network:** OkHttp3 / Retrofit2
* **Images:** Coil
* **QR Codes:** ZXing
## Branches
* `main` - version from Google Play; legacy
* `develop` - development branch with old version; freezed due app's full recreation until it'll be done
* `feature-chefbook-api` - actual WIP develompent branch
## Screenshots
<p>
  <img src="https://user-images.githubusercontent.com/18068004/152433325-a9c264f0-7dda-43a7-933c-52b4efb52bad.jpeg" width="200">
  <img src="https://user-images.githubusercontent.com/18068004/152433320-881bc092-1012-4ec0-97ff-185b2cd9d5e7.jpeg" width="200">
  <img src="https://user-images.githubusercontent.com/18068004/152433341-342b3c37-3b50-431c-bced-713a1cf2f59b.jpeg" width="200">
  <img src="https://user-images.githubusercontent.com/18068004/152433329-1b90ba5a-f139-4b5b-8526-8aabbbfb93d8.jpeg" width="200">
</p>
<p>
  <img src="https://user-images.githubusercontent.com/18068004/152433983-681c3663-ea7c-4e79-aee9-398431b47cd3.jpeg" width="200">
  <img src="https://user-images.githubusercontent.com/18068004/152432853-611eee0f-c86f-44a8-8d9a-555e9cef9732.jpeg" width="200">
  <img src="https://user-images.githubusercontent.com/18068004/152433257-0b758739-52f1-4653-9ac0-3227b06ac37a.jpeg" width="200">
  <img src="https://user-images.githubusercontent.com/18068004/152434665-e1654119-5cbe-4f1b-9245-ef87989be86b.jpeg" width="200">
</p>

## Goals List for 4.0 release
### Core
- [x] Migrate from Firebase/Firestore to own Go + Postgres backend solution
- [x] Migrate from MVVM to MVI
- [x] Add DI Framework
- [x] New UI
- [ ] Add Google / VK Sign In Method
- [ ] Fix bugs
### Social
- [x] Add user's categories
- [x] Add likes
- [ ] Add public recipes screen
### Encryption
- [x] Add encryption
- [ ] Add sharing encrypted recipes
### User Experience
- [x] Add local mode
- [x] Add recipe preview
- [x] Add Profile Customization
- [ ] Add cooking step preview
- [ ] Improve ingredients/cooking system
- [ ] Add Premium Subscription

##### PS: Jetpack Compose soon...
