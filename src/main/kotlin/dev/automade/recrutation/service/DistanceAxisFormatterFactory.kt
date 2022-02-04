package dev.automade.recrutation.service

import dev.automade.recrutation.domain.DistanceAxisFormatterInterface
import java.text.SimpleDateFormat
import java.util.*

/**
 * @TODO Normalnie ta fabryka miałby możliwość rejestrowania nowych formatterów. Jednak na potrzeby tego zadania uznałem że to przerost formy nad treścią..
 */
object DistanceAxisFormatterFactory {
    enum class Types {
        ONE_MINUTES, FIVE_MINUTES, TEN_MINUTES
    }

    class OneMinuteFormatter: DistanceAxisFormatterInterface {
        override fun format(milliseconds: Long): String = SimpleDateFormat("dd/MM HH:mm").format(Date(milliseconds))
    }

    class FiveMinuteFormatter: DistanceAxisFormatterInterface {
        override fun format(milliseconds: Long): String {
            val divider = 5 * 60 * 1000
            val ms = (milliseconds / divider) * divider

            return SimpleDateFormat("dd/MM HH:mm").format(Date(ms))
        }
    }

    class TenMinuteFormatter: DistanceAxisFormatterInterface {
        override fun format(milliseconds: Long): String {
            val divider = 10 * 60 * 1000
            val ms = (milliseconds / divider) * divider

            return SimpleDateFormat("dd/MM HH:mm").format(Date(ms))
        }
    }

    @JvmStatic
    fun make(type: Types): DistanceAxisFormatterInterface {
        return when (type) {
            Types.ONE_MINUTES -> OneMinuteFormatter()
            Types.FIVE_MINUTES -> FiveMinuteFormatter()
            Types.TEN_MINUTES -> TenMinuteFormatter()
        }
    }
}