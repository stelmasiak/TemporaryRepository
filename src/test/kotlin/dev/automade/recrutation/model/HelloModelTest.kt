package dev.automade.recrutation.model

import dev.automade.recrutation.library.recorder.InputListenerInterface
import dev.automade.recrutation.library.recorder.RecorderInterface
import dev.automade.recrutation.service.DistanceAxisFormatterFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class HelloModelTest {

    @Test
    fun `verify change formatting`() {
        var needToBeChange = false
        HelloModel(
            recorderService = object :RecorderInterface {
                override fun start() { /* Nothing */ }

                override fun stop() { /* Nothing */ }

                override fun addListener(listener: InputListenerInterface) { /* Nothing */ }
            }
        ).apply {
            initialize {
                assertInstanceOf(HelloModel.Event.Success::class.java, it)
                needToBeChange = true
            }

            changeFormating(DistanceAxisFormatterFactory.Types.TEN_MINUTES)
            assertEquals(true, needToBeChange)
        }
    }

    @Test
    fun `verify throw error event`() {
        var eventCounter = 0

        HelloModel(
            recorderService = object :RecorderInterface {
                override fun start() { throw Exception("abc") }

                override fun stop() { /* Nothing */ }

                override fun addListener(listener: InputListenerInterface) { /* Nothing */ }
            }
        ).apply {
            initialize {
                eventCounter++

                when(eventCounter) {
                    1 -> { /* Nothing */ }
                    2 -> {
                        val event = (it as? HelloModel.Event.Error).apply {
                            assertNotNull(this)
                        }

                        assertEquals("abc", event?.message)
                    }
                    else -> assert(false) // "This case will never run in this test...")
                }
            }

            toggleRecording()
        }
    }

    @Test
    fun `verify switch recording mode`() {
        var exprectedRunning = false
        HelloModel(
            recorderService = object :RecorderInterface {
                override fun start() { /* Nothing */ }

                override fun stop() { /* Nothing */ }

                override fun addListener(listener: InputListenerInterface) { /* Nothing */ }
            }
        ).apply {
            initialize {
                val event = (it as? HelloModel.Event.Success<*>).apply {
                    assertNotNull(this)
                }

                val data = (event?.data as? HelloModel.Data).apply {
                    assertNotNull(this)
                }

                assertEquals(exprectedRunning, data?.isRunningRecording)
            }

            exprectedRunning = true
            toggleRecording()
        }
    }
}