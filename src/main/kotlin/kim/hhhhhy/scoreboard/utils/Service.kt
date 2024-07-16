package kim.hhhhhy.scoreboard.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.ConcurrentHashMap

object Service {
    inline fun run(id: String, func: () -> Unit) {
        mirrorData.computeIfAbsent(id) { MirrorData() }.define()
        func()
        mirrorData[id]?.finish()
    }

    val mirrorData = ConcurrentHashMap<String, MirrorData>()

    class MirrorData {

        private var count = 0L
        private var time = 0L
        private var timeTotal = BigDecimal.ZERO
        private var timeLatest = BigDecimal.ZERO
        private var timeLowest = BigDecimal.ZERO
        private var timeHighest = BigDecimal.ZERO

        init {
            reset()
        }

        fun define(): MirrorData {
            time = System.nanoTime()
            return this
        }

        fun finish(): MirrorData {
            return finish(time)
        }

        private fun finish(startTime: Long): MirrorData {
            val stopTime = System.nanoTime()
            timeLatest = BigDecimal((stopTime - startTime) / 1000000.0).setScale(2, RoundingMode.HALF_UP)
            timeTotal = timeTotal.add(timeLatest)
            if (timeLatest.compareTo(timeHighest) == 1) {
                timeHighest = timeLatest
            }
            if (timeLatest.compareTo(timeLowest) == -1) {
                timeLowest = timeLatest
            }
            count++
            return this
        }

        private fun reset(): MirrorData {
            count = 0
            timeTotal = BigDecimal.ZERO
            timeLatest = BigDecimal.ZERO
            timeLowest = BigDecimal.ZERO
            timeHighest = BigDecimal.ZERO
            return this
        }

    }

}