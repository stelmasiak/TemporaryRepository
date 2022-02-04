package dev.automade.recrutation.ui

import dev.automade.recrutation.model.HelloModel
import dev.automade.recrutation.service.DistanceAxisFormatterFactory
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.control.*
import java.net.URL
import java.util.*

class HelloController : Initializable {
    private val model = HelloModel()

    @FXML
    private lateinit var mouseDistanceLabel: Label

    @FXML
    private lateinit var mouseClicksLabel: Label

    @FXML
    private lateinit var keyboardPressedLabel: Label

    @FXML
    private lateinit var lineChart: LineChart<String, Number>

    @FXML
    private lateinit var startToggleButton: ToggleButton

    @FXML
    private lateinit var formatterToggleGroup: ToggleGroup

    private val mouseDistanceSeries by lazy {
        if( lineChart.data.isEmpty() ) {
            XYChart.Series<String, Number>().apply {
                lineChart.data.add(this)
            }
        } else {
            lineChart.data[0]
        }
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        lineChart.animated = false

        model.initialize {
            when(it) {
                is HelloModel.Event.Error -> showCriticalMessageDialog(it.message)
                is HelloModel.Event.Success<*> -> refreshUIData(it.data as HelloModel.Data)
            }
        }

        startToggleButton.setOnAction {
            model.toggleRecording()
        }

        formatterToggleGroup.selectedToggleProperty().addListener { _, _, newValue ->
            // @TODO Nie podoba mi się ten case.. Jednak nie przychodzi mi nic innego na myśl jak przekazać możliwe opcje do radio buttonów bez budowania ich po stronie kotlina zamiast XML'a
            when (newValue?.userData) {
                "1" -> model.changeFormating(DistanceAxisFormatterFactory.Types.ONE_MINUTES)
                "2" -> model.changeFormating(DistanceAxisFormatterFactory.Types.FIVE_MINUTES)
                "3" -> model.changeFormating(DistanceAxisFormatterFactory.Types.TEN_MINUTES)
                else -> { /* Nothing */
                }
            }
        }
    }

    private fun refreshUIData(data: HelloModel.Data) {
        Platform.runLater {
            startToggleButton.text = if( data.isRunningRecording ) { "Zatrzymaj" } else { "Uruchom" }
            mouseDistanceLabel.text = "Przebyty dystans ${data.allMouseDistance.toInt()} pikseli"
            mouseClicksLabel.text = "Wciśnięto ${data.mousePressedCounter} razy klawisz myszy"
            keyboardPressedLabel.text = "Wciśnięto ${data.keyboardPressedCounter} klawiszy na klawiaturze"

            mouseDistanceSeries.data.clear()

            data.mouseDistances.forEach { pair ->
                mouseDistanceSeries.data.add(XYChart.Data(pair.first, pair.second))
            }
        }
    }

    private fun showCriticalMessageDialog(message: String) {
        val okButton = ButtonType("OK", ButtonBar.ButtonData.OK_DONE)

        Dialog<String>().apply {
            dialogPane.buttonTypes.add(okButton)
            title = "Komunikat błędu"
            contentText = message

            showAndWait()
            Platform.exit()
        }
    }
}