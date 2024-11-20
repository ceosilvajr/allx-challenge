# Technology Showcase

This project demonstrates a modern Android app built with a robust and efficient technology stack. The app leverages industry-standard libraries, tools, and frameworks to create a clean, scalable, and high-performing application.

---

## 🛠️ Technology Stack

### **Core**
- **[Kotlin](https://kotlinlang.org/)**  
  A modern, expressive, and concise programming language for Android development.
- **[Jetpack Compose](https://developer.android.com/compose)**  
  Android's modern toolkit for building native UI declaratively and efficiently.
- **[Koin](https://insert-koin.io/)**  
  A lightweight and powerful dependency injection framework for Kotlin.

---

### **UI**
- **[Coil](https://coil-kt.github.io/coil/)**  
  An image loading library optimized for Kotlin and Jetpack Compose.
- **[SplashScreen API](https://developer.android.com/develop/ui/views/launch/splash-screen)**  
  A simple API for designing engaging app launch screens.
- **[Material 3](https://developer.android.com/develop/ui/compose/designsystems/material3)**  
  The latest design system for building beautiful and consistent user interfaces.
- **[Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)**  
  A library for handling large datasets efficiently with infinite scrolling support.

---

### **Database**
- **Room**  
  Android's persistence library for managing local database operations with a modern SQLite wrapper.

---

### **Network**
- **[Retrofit](https://square.github.io/retrofit/)**  
  A type-safe HTTP client for making API requests with ease.
- **[OkHttp](https://square.github.io/okhttp/)**  
  A powerful HTTP client for handling network operations.

---

### **Logger**
- **[Timber](https://github.com/JakeWharton/timber)**  
  A lightweight, extensible logging library for debugging.
- **[Chucker](https://github.com/ChuckerTeam/chucker)**  
  An in-app HTTP inspector for viewing network requests and responses.

---

### **Unit Testing**
- **[MockK](https://mockk.io/)**  
  A mocking library tailored for Kotlin to test behaviors and interactions.
- **[Kotest](https://kotest.io/)**  
  A flexible and powerful testing framework for writing comprehensive test cases.

---

### **Code Analysis**
- **[Detekt](https://detekt.dev/)**  
  A static code analysis tool for Kotlin that ensures code quality and compliance.
- **[Kover](https://kotlin.github.io/kotlinx-kover/gradle-plugin/)**  
  A Kotlin coverage plugin for tracking and improving test coverage.

### **CI/CD**
- **[GitHub Actions](https://docs.github.com/en/actions)**  
  This repository utilizes GitHub Actions for continuous integration and delivery (CI/CD).  
  The pipeline automates:

---

## 🚀 Features
This project showcases:
1. **Modern UI Development**: Built with Jetpack Compose and Material 3 for seamless and reactive user interfaces.
2. **Efficient State Management**: Paging 3 for handling infinite scrolling and large datasets.
3. **Reliable Networking**: Retrofit and OkHttp for robust and maintainable API interactions.
4. **Robust Testing**: MockK and Kotest for reliable unit testing.
5. **Enhanced Developer Experience**: Detekt, Kover, Timber, and Chucker for code quality, coverage, and debugging.

---

## 💡 How to Run
  ```bash
    $ ./gradlew build :app:koverHtmlReport :app:detekt
  ```
