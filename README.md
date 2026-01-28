![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white) ![PowerShell](https://img.shields.io/badge/PowerShell-%235391FE.svg?style=for-the-badge&logo=powershell&logoColor=white)

# 🍳 Meal Lab APP

> **A robust, enterprise-ready desktop application built in Java for searching and managing culinary recipes.**

The app leverages **TheMealDB REST API** to provide users with a seamless, high-performance cooking experience.
_Developed as part of the MSc "Information Systems and Services" (Specialization: Advanced Information Systems)._

---

## 🌟 Key Features

- **Advanced Search**: Filter recipes by name, primary ingredient, or category with real-time results.
- **User Management**: Secure login system featuring Regex-based validation and isolated local data storage per user profile.
- **Favorites & History**: Save preferred recipes and track your culinary journey with a "Cooked History" timeline (includes undo functionality).
- **"I'm Feeling Lucky"**: A random recipe generator designed for the undecided chef.
- **Modern UI/UX**: Responsive JavaFX design using flat-design CSS, keyboard shortcuts (ENTER/ESC), and informative "Clean States" for empty views.
- **Data Persistence**: Robust offline storage via JSON files with built-in versioning and automatic data migration logic.

---

## 🏗️ Architecture & Implementation

The application follows the **MVC (Model-View-Controller)** pattern to ensure a clean separation of concerns and maintainability:

| Layer          | Responsibility                         | Key Components                            |
| -------------- | -------------------------------------- | ----------------------------------------- |
| **Model**      | Data structures and persistence logic. | `Recipe`, `Ingredient`, `UserDataService` |
| **View**       | Declarative UI layouts and styling.    | FXML Files, CSS Stylesheets               |
| **Controller** | Event handling and API orchestration.  | JavaFX Controllers, Retrofit Clients      |

### ⚡ Technical Highlights

- **Lazy Loading**: Full recipe details (instructions/ingredients) are fetched only upon selection, significantly reducing bandwidth and improving initial load times.
- **Dynamic Deserialization**: Utilizes `@JsonAnySetter` to map the complex, indexed ingredient structure of TheMealDB API into a clean `Map<String, String>`, avoiding bloated POJOs.
- **Thread Management**: UI responsiveness is maintained via asynchronous calls, utilizing `Platform.runLater()` to update the view without blocking the main thread.

---

## 🛠️ Tech Stack & Tools

- **Language**: Java 17+
- **GUI Framework**: JavaFX (FXML)
- **Build Tool**: Maven
- **Networking**: Retrofit 2
- **JSON Parsing**: Jackson Databind
- **Testing**: JUnit 5 (Unit testing) & Postman (API verification)
- **Version Control**: Git

---

## 📂 Project Structure

```plaintext
├── src/main/java/       # Application source code (MVC packages)
├── src/main/resources/  # FXML layouts, CSS styles, and Icons
├── src/test/            # Unit and Integration tests
├── data/                # Local JSON storage (per user)
├── docs/                # Academic documentation and API specs
└── pom.xml              # Maven configuration and dependencies
```

## 🚀 Getting Started

### Prerequisites

- **JDK 17** or higher
- **Maven 3.6+**
- An active internet connection (for API fetching)

### Installation & Execution

1. **Clone the repository:**

   ```bash
   git clone <repository-url>
   cd MealLabApp
   ```

2. **Build the project:**

   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn javafx:run
   ```

---

## 📄 License

This project is an **academic exercise** for the _Advanced Information Systems_ module at the University of Piraeus.
