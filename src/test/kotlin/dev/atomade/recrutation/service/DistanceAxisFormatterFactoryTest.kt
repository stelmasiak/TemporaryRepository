package dev.atomade.recrutation.service

import dev.automade.recrutation.service.DistanceAxisFormatterFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DistanceAxisFormatterFactoryTest {
    @Test
    fun `verify formatter factory`() {
        mapOf(
            DistanceAxisFormatterFactory.Types.ONE_MINUTES to DistanceAxisFormatterFactory.OneMinuteFormatter::class.java,
            DistanceAxisFormatterFactory.Types.FIVE_MINUTES to DistanceAxisFormatterFactory.FiveMinuteFormatter::class.java,
            DistanceAxisFormatterFactory.Types.TEN_MINUTES to DistanceAxisFormatterFactory.TenMinuteFormatter::class.java
        ).forEach {
            val instance = DistanceAxisFormatterFactory.make(it.key)

            Assertions.assertInstanceOf(it.value, instance)
        }
    }

    @Test
    fun `verify one minute formatter formating`() {
        val instance = DistanceAxisFormatterFactory.OneMinuteFormatter()

        mapOf(
            1643937715228 to "04/02 02:21",
            1643937725460 to "04/02 02:22"
        ).forEach {
            val value = instance.format(it.key)

            Assertions.assertEquals(it.value, value)
        }
    }

    @Test
    fun `verify five minute formatter formating`() {
        val instance = DistanceAxisFormatterFactory.FiveMinuteFormatter()

        mapOf(
            1643937715228 to "04/02 02:20",
            1643937725460 to "04/02 02:20"
        ).forEach {
            val value = instance.format(it.key)

            Assertions.assertEquals(it.value, value)
        }
    }

    @Test
    fun `verify ten minute formatter formating`() {
        val instance = DistanceAxisFormatterFactory.TenMinuteFormatter()

        mapOf(
            1643937715228 to "04/02 02:20",
            1643937725460 to "04/02 02:20",
            1643938014376 to "04/02 02:20"
        ).forEach {
            val value = instance.format(it.key)

            Assertions.assertEquals(it.value, value)
        }
    }
}