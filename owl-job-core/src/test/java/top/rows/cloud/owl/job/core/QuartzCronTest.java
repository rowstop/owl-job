package top.rows.cloud.owl.job.core;

import com.cronutils.builder.CronBuilder;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import org.junit.jupiter.api.Test;
import top.rows.cloud.owl.job.core.model.OwlJob;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.cronutils.model.field.expression.FieldExpressionFactory.always;
import static com.cronutils.model.field.expression.FieldExpressionFactory.on;

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

    @Test
    void test() {

        Cron cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ))
                .withYear(always())
                .withMonth(always())
                .withHour(always())
                .withMinute(always())
                .withSecond(on(0))
                .instance();
        String cronAsString = cron.asString();
        System.out.println(cronAsString);
    }
}
