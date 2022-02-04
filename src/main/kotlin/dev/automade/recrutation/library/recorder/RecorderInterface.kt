package dev.automade.recrutation.library.recorder

/**
 *  Obsługa nagrywania zdarzeń
 */
interface RecorderInterface {
    fun start()

    fun stop()

    fun addListener(listener: InputListenerInterface)
}