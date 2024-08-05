package top.rows.cloud.owl.job.dashboard.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author 张治保
 * @since 2024/8/5
 */
@Getter
@Setter
@ToString
public class OverviewDTO {

    private String namespace;

    private LocalDate start;

    private LocalDate end;


    public void init() {
        if (start != null && end != null) {
            return;
        }
        end = LocalDate.now();
        start = end.minusMonths(1);
    }

}
