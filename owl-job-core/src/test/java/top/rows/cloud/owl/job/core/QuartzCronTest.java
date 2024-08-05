package top.rows.cloud.owl.job.core;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import org.junit.jupiter.api.Test;
import top.rows.cloud.owl.job.core.model.OwlJob;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author 张治保
 * @since 2024/7/17
 */
public class QuartzCronTest {
    static final CronParser cronParser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));

    @Test
    void parseCron() {
        String cron = "1,5,9,20,39,48,59 * * * * ?";
        LocalDateTime localDateTime = OwlJob.nextCronTime(ZonedDateTime.now(), cron);
        System.out.println(localDateTime);
        localDateTime = OwlJob.nextCronTime(localDateTime.atZone(ZoneId.systemDefault()), cron);
        System.out.println(localDateTime);
    }

}
