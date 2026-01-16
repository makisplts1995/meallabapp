module gr.unipi.meallab.api {
    requires retrofit2;
    requires retrofit2.converter.jackson;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires okhttp3;
    requires okhttp3.logging;

    exports gr.unipi.meallab;
    exports gr.unipi.meallab.model;
    exports gr.unipi.meallab.network;

    opens gr.unipi.meallab.model to com.fasterxml.jackson.databind;
}
