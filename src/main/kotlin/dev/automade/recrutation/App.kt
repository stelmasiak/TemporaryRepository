package dev.automade.recrutation

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import org.jnativehook.GlobalScreen
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.Logger

class HelloApplication : Application() {
    private lateinit var scene: Scene
    private lateinit var loader: FXMLLoader

    override fun start(stage: Stage) {
        loader = FXMLLoader(HelloApplication::class.java.getResource("hello-view.fxml"))
        scene = Scene(loader.load(), 420.0, 290.0)

        stage.title = "AutoMade - Rekrutacja"
        stage.scene = scene
        stage.show()

    }
}

fun main() {
    LogManager.getLogManager().reset()
    Logger.getLogger(GlobalScreen::class.java.`package`.name).apply {
        level = Level.OFF
    }

    Application.launch(HelloApplication::class.java)
}