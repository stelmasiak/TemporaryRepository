package dev.automade.recrutation.library.recorder

import dev.automade.recrutation.domain.MousePosition
import org.jnativehook.GlobalScreen
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import org.jnativehook.mouse.NativeMouseEvent
import org.jnativehook.mouse.NativeMouseListener
import org.jnativehook.mouse.NativeMouseMotionListener

class JNativeRecorder: RecorderInterface {
    private val listeners = mutableListOf<InputListenerInterface>()

    init {
        registerMouseListener()
        registerMouseMotionListener()
        registerKeyboardListener()
    }

    override fun start() {
        GlobalScreen.registerNativeHook()
    }

    override fun stop() {
        GlobalScreen.unregisterNativeHook()
    }

    override fun addListener(listener: InputListenerInterface) {
        listeners.add(listener)
    }

    private fun registerMouseListener() {
        GlobalScreen.addNativeMouseListener(object: NativeMouseListener {
            override fun nativeMouseClicked(p0: NativeMouseEvent?) { /* Nothing */ }

            override fun nativeMousePressed(p0: NativeMouseEvent?) {
                listeners.forEach {
                    it.onMousePressed()
                }
            }

            override fun nativeMouseReleased(p0: NativeMouseEvent?) { /* Nothing */ }
        })
    }

    private fun registerMouseMotionListener() {
        GlobalScreen.addNativeMouseMotionListener(object: NativeMouseMotionListener {
            private var previousMousePosition: MousePosition? = null

            override fun nativeMouseMoved(event: NativeMouseEvent?) {
                event?.let {
                    val currentMousePosition = MousePosition(event.x, event.y)

                    previousMousePosition?.let { previous ->
                        listeners.forEach {
                            it.onMouseMoved(previous, currentMousePosition)
                        }
                    }

                    previousMousePosition = currentMousePosition
                }
            }

            override fun nativeMouseDragged(p0: NativeMouseEvent?) { /* Nothing */ }
        })
    }

    private fun registerKeyboardListener() {
        GlobalScreen.addNativeKeyListener(object: NativeKeyListener {
            override fun nativeKeyTyped(p0: NativeKeyEvent?) { /* Nothing */ }

            override fun nativeKeyPressed(p0: NativeKeyEvent?) {
                listeners.forEach {
                    it.onKeyboardPressed()
                }
            }

            override fun nativeKeyReleased(p0: NativeKeyEvent?) { /* Nothing */ }
        })
    }
}