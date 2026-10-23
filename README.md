# Smart Pantry Manager

A simple Android app that tracks what is in your pantry and shows which recipes you can cook with it right now, built with Java and SQLite.

## Implementation

| Area | Technology |
|---|---|
| Language | Java 11 |
| Platform | Android, minSdk 26, targetSdk 37 |
| Data | SQLite through `SQLiteOpenHelper` (`spm.db`) |
| UI | Material Components 1.14.0, RecyclerView, BottomNavigationView, always-dark GitHub-style theme |
| Settings | SharedPreferences |
| Build | Gradle 9.4.1 |

## Dependencies

- **Android Studio** (built with Build #AI-262.9437.185.2621.16467767) with the bundled JDK.
- **Android SDK 37**. An emulator or device on API 26 or higher. Tested on the *Medium Phone* emulator, API 37.
- No other tools are needed. All libraries are downloaded by Gradle on the first sync.

## Quick start

1. Open the project folder in Android Studio.
2. Select a device (emulator or linked) and press **Run**.
3. On first launch the app creates the database and seeds 22 recipes, so recipe matching works as soon as you add ingredients.

## Screens

| Screen | What it does |
|---|---|
| Pantry | Lists ingredients. Tap a row to edit it, use the red icon to delete, and use *Remove expired* to clear everything out of date. |
| Add / Edit ingredient | Name (max 30 characters), quantity, unit and an optional expiry date chosen with a date picker. |
| Suggested recipes | Recipes the pantry can make now. Below them, an *Almost there* list of recipes missing exactly one ingredient. |
| Recipe detail | Ingredients and numbered method steps. |
| Settings | Expiring-soon highlighting on or off, and the default unit used when adding an ingredient. |

Navigation uses a bottom bar (Pantry, Recipes, Settings). Screens pass only record IDs through Intents.
Zero matches shows the message "No recipes match your pantry".

## How recipe matching works

- Names are normalised before comparing (trimmed, lower case, spaces collapsed, simple plurals removed).
- Quantities of the same ingredient are summed and compared in base units, so `1 cup` covers `250 ml`.
- Units only match within the same family (mass, volume, count). Grams never satisfy millilitres.
- Expired items are ignored.
- *Almost there* lists recipes where exactly one ingredient is missing or not enough.

## Settings

| Setting | Default |
|---|---|
| Expiring-soon highlight | On (items expiring within 2 days are shown in amber, expired items in red) |
| Default unit | g |

## Project structure

```
spm-java
├── app/src/main
│   ├── java/com/example/spm_java
│   │   ├── data                            SQLite helper, seeder and repo's
│   │   ├── logic                           Name normaliser and recipe matcher
│   │   ├── model                           PantryItem, Recipe, RecipeIngredient, Unit
│   │   ├── ui                  
│   │   │   ├── base                        Shared activity with the bottom navigation
│   │   │   ├── pantry                      Pantry list, add/edit form, adapter
│   │   │   ├── recipes                     Suggested recipes, recipe detail, adapter
│   │   │   └── settings                    Settings screen
│   │   └── util                            Preferences and Intent keys
│   ├── res                 
│   │   ├── layout                          Screen and row layouts
│   │   ├── drawable                        Icons and card background
│   │   ├── color                           Navigation colours
│   │   ├── menu                            Bottom navigation menu
│   │   └── values                          Strings, colours and theme
│   └── AndroidManifest.xml
├── build.gradle.kts
├── settings.gradle.kts
├── gradle
└── README.md
```

## Database

| Table | Columns |
|---|---|
| `pantry_item` | id, name, quantity, unit, expiry_date |
| `recipe` | id, name, steps |
| `recipe_ingredient` | id, recipe_id (cascade delete), name, quantity, unit |

# License

![MIT License](https://github.com/Chester-xx/spm-java/blob/main/LICENSE)