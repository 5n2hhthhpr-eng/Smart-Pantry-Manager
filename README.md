# Smart Pantry Manager

## App Description

Smart Pantry Manager is a Java-based Android application designed to help users keep track of the ingredients available in their pantry and identify recipes that can be prepared using those ingredients.

The application allows users to add, edit and delete pantry ingredients while recording the ingredient name, quantity, measurement unit and optional expiry date. Users can also view their stored pantry items and access suggested recipes and recipe details.

A key feature of Smart Pantry Manager is its strict recipe-matching functionality. A recipe is only suggested when **all of its required ingredients are available in the user's pantry in sufficient quantities**. The application also supports basic ingredient-name normalisation and compatible unit conversions, such as kilograms to grams and litres to millilitres.

The application was developed using Java in Android Studio.

## Main Features

* Add pantry ingredients
* Edit existing pantry ingredients
* Delete pantry ingredients
* Record ingredient quantities and measurement units
* Record optional expiry dates
* View stored pantry ingredients
* Validate user input when adding or editing ingredients
* View suggested recipes
* View individual recipe details and instructions
* Match recipes against the ingredients and quantities available in the pantry
* Store pantry and recipe information locally using SQLite
* Access application settings

## Database

Smart Pantry Manager uses **SQLite** as its local database.

SQLite was selected because the application primarily stores structured information such as pantry ingredients, recipes and recipe ingredients. The application's core functionality does not require an internet connection or a remote database, making a local SQLite database suitable for storing and retrieving the application's data on the Android device.

The database contains three main tables:

* `pantry_items` — stores pantry ingredients, quantities, units and expiry dates.
* `recipes` — stores recipe names and instructions.
* `recipe_ingredients` — stores the ingredients, quantities and units required by each recipe.

The application uses a `DatabaseHelper` class to create and manage the SQLite database and perform database operations.

## Technologies Used

* Java
* Android Studio
* Android SDK
* XML layouts
* SQLite
* RecyclerView
* Android Activities
* Gradle

## Project Structure

The main Java components are organised into the following areas:

* `database` — database creation and database operations
* `model` — pantry and recipe data models
* `adapter` — RecyclerView adapters
* Activities — application screens and user interaction

## Setup and Run Instructions

### Requirements

To run the application, you will need:

* Android Studio
* A compatible Android SDK
* A Java Development Kit supported by the project
* An Android emulator or compatible Android device

### Steps

1. Open Android Studio.
2. Select **Open** and choose the Smart Pantry Manager project folder.
3. Allow Android Studio to complete the Gradle project synchronisation.
4. Ensure that an Android emulator or compatible physical Android device is available.
5. Select the device from the Android Studio device selector.
6. Click the **Run** button.
7. Android Studio will build and install the application on the selected device.
8. Launch Smart Pantry Manager and use the application features.

## Recipe Matching Rule

The application follows a strict "no shopping trip" recipe-matching rule.

For a recipe to appear as a suggested recipe:

1. Every ingredient required by the recipe must exist in the pantry.
2. The available quantity must be sufficient for the recipe requirement.
3. Compatible measurement units are converted where applicable.
4. If even one required ingredient is missing or insufficient, the recipe is not suggested.

This ensures that suggested recipes can be prepared using the ingredients currently recorded in the user's pantry.
