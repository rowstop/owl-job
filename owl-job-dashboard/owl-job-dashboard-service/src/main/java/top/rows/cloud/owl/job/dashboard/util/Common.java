package top.rows.cloud.owl.job.dashboard.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author 张治保
 * @since 2024/7/25
 */
public interface Common {

    String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    String TIME_FORMAT_PATTERN = "HH:mm:ss";

    /**
     * 毫秒转 local date time
     *
     * @param mills 毫秒数
     * @return LocalDateTime
     */
    static LocalDateTime millsToLocalDateTime(long mills) {
        Instant instant = Instant.ofEpochMilli(mills);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }


}
