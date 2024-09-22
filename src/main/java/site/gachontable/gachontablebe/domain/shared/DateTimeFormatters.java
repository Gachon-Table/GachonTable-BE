package site.gachontable.gachontablebe.domain.shared;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeFormatters {

    public static final DateTimeFormatter WITH_WEEKDAY = DateTimeFormatter.ofPattern(
            "M월 d일 (E) HH:mm", Locale.KOREAN);

}
