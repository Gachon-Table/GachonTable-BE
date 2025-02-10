package site.gachontable.presentation.shared

import java.time.format.DateTimeFormatter
import java.util.*

object DateTimeFormatters {
    val WITH_WEEKDAY: DateTimeFormatter = DateTimeFormatter
        .ofPattern(
            "M월 d일 (E) HH:mm", Locale.KOREAN
        )
}
