# Build script for MealDBApp
mvn clean package
Remove-Item -Path "output" -Recurse -ErrorAction SilentlyContinue -Force
jpackage --name "mealdbapp" `
  --input target/ `
  --main-jar meal-lab-app-1.0-SNAPSHOT.jar `
  --main-class gr.unipi.meallabapp.Launcher `
  --type app-image `
  --dest output `
  --module-path "C:\javafx\javafx-jmods-25.0.2" `
  --add-modules "javafx.controls,javafx.fxml,java.net.http,java.sql,java.scripting"