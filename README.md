Meal Lab APP
A robust desktop application built in Java for searching and managing culinary recipes. The app interacts with TheMealDB REST API to provide users with a seamless cooking experience.
Developed as part of the MSc "Information Systems and Services" (Specialization: Advanced Information Systems).

Key Features
Advanced Search: Filter recipes by name, primary ingredient, or category.
User Management: Secure login system with username validation (Regex) and isolated local data storage per user.
Favorites & History: Save preferred recipes and track your culinary journey with "Cooked History" (including undo functionality).
I'm Feeling Lucky: A random recipe generator for undecided users.
Modern UI/UX: Responsive design using JavaFX, flat-design CSS, keyboard shortcuts (ENTER/ESC), and informative "Clean States."
Data Persistence: Offline storage via JSON files with built-in versioning and automatic data migration.

Tech Stack & Tools
Language: Java 17+
GUI Framework: JavaFX (FXML)
Build Tool: Maven
Networking: Retrofit 2
JSON Parsing: Jackson Databind (utilizing @JsonAnySetter for dynamic ingredient mapping)
Testing: JUnit 5
API Testing: Postman

Version Control: Git

Architecture & Implementation
The application follows the MVC (Model-View-Controller) pattern to ensure separation of concerns:
Model: Data modeling (Recipe, Ingredient) and UserDataService for persistence.
View: FXML layouts and CSS styling for a professional look.
Controller: Event handling and asynchronous UI updates via Platform.runLater() to maintain responsiveness during API calls.

Technical Highlights
Lazy Loading: Full recipe details (instructions/ingredients) are fetched only when a specific recipe is selected, optimizing bandwidth and speed.
Dynamic Deserialization: Handled the complex structure of TheMealDB API by dynamically collecting ingredient fields into a Map, keeping the code clean and maintainable.
