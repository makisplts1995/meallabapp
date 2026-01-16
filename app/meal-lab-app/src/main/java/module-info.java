module gr.unipi.meallab.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires gr.unipi.meallab.api;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;

    exports gr.unipi.meallabapp;

    opens gr.unipi.meallabapp to javafx.fxml;
}
