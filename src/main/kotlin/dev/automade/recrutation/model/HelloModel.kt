package dev.automade.recrutation.model

import dev.automade.recrutation.domain.MousePosition
import dev.automade.recrutation.library.recorder.InputListenerInterface
import dev.automade.recrutation.library.recorder.JNativeRecorder
import dev.automade.recrutation.library.recorder.RecorderInterface
import dev.automade.recrutation.service.DistanceAxisFormatterFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

typealias MouseDistanceStatistic = Pair<String, Double>

class HelloModel(
    // @TODO Powinno być wstrzykiwane za pomocą jakiegoś DI... Pewnie użyłbym Koin'a
    private val recorderService: RecorderInterface = JNativeRecorder()
) {
    data class Data (
        var isRunningRecording: Boolean,
        val mousePressedCounter: Int = 0,
        val keyboardPressedCounter: Int = 0,
        val allMouseDistance: Double = .0,
        val mouseDistances: List<MouseDistanceStatistic> = CopyOnWriteArrayList()
    )

    sealed class Event {
        class Error(val message: String): Event()
        class Success<T>(val data: T): Event()
    }

    inner class InputListener : InputListenerInterface {
        override fun onMousePressed() {
            data = data.copy(
                mousePressedCounter = data.mousePressedCounter + 1
            )
        }

        override fun onKeyboardPressed() {
            data = data.copy(
                keyboardPressedCounter = data.keyboardPressedCounter + 1
            )
        }

        override fun onMouseMoved(oldPosition: MousePosition, newPosition: MousePosition) {
            val seconds = System.currentTimeMillis() / 1000
            val calculateDistance = oldPosition.distance(newPosition)

            mouseDistances[seconds] = calculateDistance + mouseDistances.getOrDefault(seconds, .0)

            data = data.copy(
                allMouseDistance = data.allMouseDistance + calculateDistance,
                mouseDistances = convertMouseDistanceToUI()
            )
        }
    }

    private fun convertMouseDistanceToUI(): List<MouseDistanceStatistic> {
        return mouseDistances.map {
            val ms = TimeUnit.MILLISECONDS.convert(it.key, TimeUnit.SECONDS)

            Pair(
                DistanceAxisFormatterFactory.make(formatterType).format(ms),
                it.value
            )
        }.groupBy {
            it.first
        }.mapValues {
            it.value.sumOf { it.second }
        }.map {
            MouseDistanceStatistic(it.key, it.value)
        }.sortedBy {
            it.first
        }
    }

    private var data: Data by Delegates.observable(Data(
        isRunningRecording = false
    )) { _, _, value  ->
        notifyDataChange(value)
    }

    private lateinit var listener: (Event) -> Unit

    private var formatterType = DistanceAxisFormatterFactory.Types.ONE_MINUTES

    /** Oryginalna lista przechowująca dystane myszki */
    private var mouseDistances: ConcurrentMap<Long, Double> = ConcurrentHashMap()

    private fun notifyDataChange(value: Data) {
        listener(Event.Success(value))
    }

    fun initialize(listener: (event: Event) -> Unit) {
        this.listener = listener
        this.recorderService.addListener(InputListener())

        notifyDataChange(data)
    }

    fun toggleRecording() {
        if( data.isRunningRecording ) {
            stopRecording()
        } else {
            startRecording()
        }
    }

    fun changeFormating(newValue: DistanceAxisFormatterFactory.Types) {
        this.formatterType = newValue
        notifyDataChange(data)
    }

    private fun startRecording() {
        try {
            recorderService.start()

            data = data.copy(
                isRunningRecording = true
            )
        } catch(ex: Throwable) {
            listener(Event.Error(ex.message ?: "Nie rozpoznany błąd"))
        }
    }

    private fun stopRecording() {
        recorderService.stop()

        data = data.copy(
            isRunningRecording = false
        )
    }
}