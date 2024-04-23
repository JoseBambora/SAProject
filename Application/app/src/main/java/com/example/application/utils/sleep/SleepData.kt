import java.time.LocalDateTime

class SleepData private constructor() {
    private var startTime: LocalDateTime? = null
    private var endTime: LocalDateTime? = null

    companion object {
        val instance = SleepData()
    }

    fun getStartTime(): LocalDateTime? {
        return startTime
    }

    fun getEndTime(): LocalDateTime? {
        return endTime
    }

    fun setSleepTime(start: LocalDateTime, end: LocalDateTime) {
        startTime = start
        endTime = end
    }
}
