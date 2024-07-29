package top.rows.cloud.owl.job.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import top.rows.cloud.owl.job.api.OwlJobHelper;
import top.rows.cloud.owl.job.core.dashboard.OwlJobDashboard;

import java.util.concurrent.RejectedExecutionException;

/**
 * @author 张治保
 * @since 2024/7/16
 */
public class OwlJobHelperTest {

    @Test
    void testGroupTaskAnalysis() {
        String group = "AASDSADaawew3214asdasga";
        String taskId = "ag22aassgag:Asga33sfadadassddasd:a11sdsa";
        String router = OwlJobHelper.router(group, taskId);
        String[] groupTaskId = OwlJobHelper.groupAndTaskIdFromRouter(router);
        Assertions.assertEquals(group, groupTaskId[0]);
        Assertions.assertEquals(taskId, groupTaskId[1]);
    }

    @Test
    void testErrorToString() {
        try {
            throw new RejectedExecutionException("aaa", new RuntimeException("1111"));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println(OwlJobDashboard.errorToString(throwable, 3));
        }
    }
}
