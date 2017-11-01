package pl.pollub.shoppinglist.util;

import java.util.Calendar;
import java.util.Date;

import lombok.*;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;

import static java.util.Calendar.*;

/**
 * @author Adrian
 * @since 2017-10-28
 */
@EqualsAndHashCode
@ToString
public class DateTimeWrapper {
    private Calendar calendar = Calendar.getInstance();
    @Getter
    private LocalDateTime localDateTime;

    public Date getDateTime() {
        return calendar.getTime();
    }

    public void setDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
        this.calendar.setTimeInMillis(localDateTime.toEpochSecond(ZoneOffset.UTC));
    }

    public void setDateTime(Date dateTime) {
        this.calendar.setTime(dateTime);
        this.localDateTime = LocalDateTime.of(
                calendar.get(YEAR),
                calendar.get(MONTH) + 1,
                calendar.get(DAY_OF_MONTH),
                calendar.get(HOUR_OF_DAY),
                calendar.get(MINUTE),
                calendar.get(SECOND),
                calendar.get(MILLISECOND * 1_000_000)
        );
    }
}
