package database.typeConverter;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class LocalDateTypeConverter {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public long convertDatetoLong (LocalDateTime date)
    {
        ZonedDateTime zdt = ZonedDateTime.of(date, ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public LocalDateTime convertLongtoDate(Long epochMilli){
        Instant instant = Instant.ofEpochMilli(epochMilli);
        return LocalDateTime.ofInstant(instant,ZoneId.systemDefault());
    }
}
