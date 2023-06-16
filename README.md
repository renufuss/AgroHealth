![logo-agro-health](https://i.postimg.cc/N0f8q11h/logo-with-text-landscape.png)

# Agro Health

Agro Health is a powerful application designed to assist hydroponic plant growers in detecting and managing diseases effectively. This innovative app harnesses advanced algorithms and image recognition technology to analyze plant health and identify potential diseases accurately. By simply capturing images of the plants, Agro Health quickly assesses their condition, providing real-time insights into any signs of diseases, nutrient deficiencies, or other issues. With its user-friendly interface and comprehensive disease database, Agro Health empowers growers to take timely actions, offering recommendations for targeted treatments and optimal plant care strategies. Whether you are a beginner or an experienced hydroponic enthusiast, Agro Health is an indispensable tool that promotes healthier and more productive crops, enabling you to achieve optimal growth and maximize your agricultural success.

## Features

- Disease Detector : Provide the diagnosis and explanation of the detected disease and care tips on the plant that is detected to have a disease

- Agro Community : Provide a safe place to communicate with each other and share experiences in caring for plants
- Agro Library : Provide articles for you to read anytime, anywhere
- Agro History : Provides data on scan results of hydroponic plant diseases

## Technology Used

[![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)](https://developer.android.com/kotlin)

- [Kotlin](https://developer.android.com/kotlin) : Kotlin is a modern programming language designed to be a powerful alternative to Java, particularly for Android app development. Developed by JetBrains, Kotlin offers a concise and expressive syntax, reducing boilerplate code and increasing developer productivity. It seamlessly interoperates with existing Java code, making it easy to integrate into Android projects. With features like null safety, extension functions, coroutines, and type inference, Kotlin promotes cleaner and more efficient code. Its growing popularity and strong community support make it an excellent choice for developers looking to build high-quality Android applications with ease and efficiency.

## Demo

Demo is unavailable right now

## Installation

[![Download APK](https://img.shields.io/badge/Download%20APK-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://drive.google.com/file/d/1REjBNlAOOtPdTXHdbv8xU6g-iJ23mcJz/view?usp=sharing)

u can download the apk and install in your android smartphone [click here](https://drive.google.com/file/d/1REjBNlAOOtPdTXHdbv8xU6g-iJ23mcJz/view?usp=sharing)

## Run Locally

If you want to run app in local area

- You need an API backend for you to run, [Backend Repository](https://github.com/rosfandy/backend-bangkit-2023-fp)

- change base url in build.gradle to your local IP (Module:app)

```bash
  buildConfigField("String", 'BASE_URL', '"http://YOUR_LOCAL_IP/"')
```

- Build -> Make Project, and now you can run the app in your local area
