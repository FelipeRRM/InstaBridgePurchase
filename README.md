# Insta Bridge - Data Purchase Flow

This project is an implementation example of the proposed Data Purchase flow for the Instabridge app. It's mostly focused in the presentation layer, therefore no network requests, nor databases are implemented in the app. All data is static and mocked just to test the presentation layer.
Bseides, the base "skeleton" of the app (bottom navigation bar, etc.), three Screens have been implemented:
* Screen to select the amount of data to be purchased
* Screen to change the selected country
* Success screen
  
The other screens in the bottom navgation bar are merely placeholders.

The app was implemented entirely using the newest best practices and guidelines for Android as of May 2024:
* Kotlin was used in every single class in the project
* Jetpack Compose was used for a high-performance, declarative-ui implementation
* Navigation component was used to handle navigation in the project
* MVVM was the architectural pattern used
* Flow was the technology choice to asynchronously process streams of data and make them observable

A big fofcus was put into making smooth animations that enhance the user experience.
There are many things to improve in this prototype, but doing so was out of scope.

Video of the project running bellow:

https://github.com/FelipeRRM/InstaBridgePurchase/assets/10159615/22b12f64-1db9-41de-a834-c15d4e58a578

