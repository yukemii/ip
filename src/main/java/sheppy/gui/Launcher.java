package sheppy.gui;

import javafx.application.Application;

/** Launches the JavaFX application without extending the chatbot's core class. */
public class Launcher {
    /** Prevents instantiation of this application launcher. */
    private Launcher() {
    }

    /**
     * Starts Sheppy's JavaFX application.
     *
     * @param args command-line arguments passed to JavaFX
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
