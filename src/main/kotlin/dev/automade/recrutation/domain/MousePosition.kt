package dev.automade.recrutation.domain

import javafx.geometry.Point2D

data class MousePosition(val x: Int, val y: Int) {
    fun distance(other: MousePosition): Double {
        val source = this.toPoint2D()
        val target = other.toPoint2D()

        return source.distance(target)
    }

    private fun toPoint2D() = Point2D(
        x.toDouble(),
        y.toDouble()
    )
}