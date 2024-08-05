package top.rows.cloud.owl.job.dashboard.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

/**
 * @author 张治保
 * @since 2024/8/5
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class OverviewVO {

    private Long error = 0L;

    private Long success = 0L;

    private List<DateCount> counts;

    @Getter
    @Setter
    @ToString
    @Accessors(chain = true)
    public static class DateCount {
        private LocalDate date;
        private Long error;
        private Long success;
    }
}
