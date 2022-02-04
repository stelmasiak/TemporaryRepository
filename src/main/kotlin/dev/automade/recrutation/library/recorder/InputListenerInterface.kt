package dev.automade.recrutation.library.recorder

import dev.automade.recrutation.domain.MousePosition

interface InputListenerInterface {
    fun onMousePressed()

    fun onKeyboardPressed()

    fun onMouseMoved(oldPosition: MousePosition, newPosition: MousePosition)
}