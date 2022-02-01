# ChefBook Android
ChefBook Android Client 🤖

Common information can be found at [main repository](https://github.com/mephistolie/chefbook)
## About
* **Language**: Kotlin 
* **Architecture:** MVI + UseCases
* **Reactive Code**: Kotlin Flow, StateFlow, SharedFlow
### Dependencies
* **DI Framework:** Hilt
* **Navigation:** Jetpack Navigation Component
* **Network:** OkHttp3 / Retrofit2
* **Images:** Coil
* **QR Codes:** ZXing
## Branches
* `main` - version from Google Play
* `develop` - development branch with old version; freezed due app's full recreation until it'll be done
* `feature-chefbook-api` - actual WIP develompent branch
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
