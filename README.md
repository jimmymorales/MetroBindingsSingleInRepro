# Metro DI Singleton Scope Bug Reproducer

This project is a minimal Kotlin (JVM) example that reproduces a scoping issue found in [Metro DI](https://github.com/rahulrvachhani/metro). Specifically, it demonstrates how an unqualified `@SingleIn` binding is not treated as a singleton, while a version of the same binding with a qualifier (`@Named`) works correctly.

## 🔍 Issue Description

In the provided DI graph, the same singleton dependency (`Platform`) is injected in two different places. When no qualifier is used, two different instances are created. Adding a qualifier fixes the issue.

### Expected Behavior
Instances of `Platform` injected anywhere in the graph should be the same:
```kotlin
platform1 === platform2 // should be true
````

### Actual Behavior

Without a qualifier:

```kotlin
platform1 !== platform2 // unexpectedly false
```

## 🧪 How to Reproduce

1. Clone the repo:

   ```bash
   git clone https://github.com/jimmymorales/MetroBindingsSingleInRepro.git
   cd MetroBindingsSingleInRepro
   ```

2. Run the application:

   ```bash
   ./gradlew run
   ```

3. Observe the console output — two distinct instances of `Platform` are printed, and the `check(platform1 === platform2)` call will fail.

## 📁 Key Files

* `ExampleDependencyGraph.kt`: Defines the DI graph and reproduces the issue.

## 🛠️ Workaround

Adding a qualifier (e.g., `@Named("platform")`) to the `@Provides` method and updating all injection points resolves the issue and correctly maintains a single instance.

```kotlin
@Provides
@SingleIn(AppScope::class)
@Named("platform")
fun providePlatform(): Platform = getPlatform()
```

## 🧩 Dependencies

* Kotlin: 2.1.20
* Metro: 0.3.0
* Gradle: 8.9

## 🙏 Credits

Thanks to the [Metro](https://github.com/ZacSweers/metro) team for their work on the library! This repo is purely for testing and debugging purposes.

---

Feel free to open an issue or PR if you want to improve this reproducer.
