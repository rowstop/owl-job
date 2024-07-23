package top.rows.cloud.owl.job.dashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import top.rows.cloud.owl.job.dashboard.dao.model.User;
import top.rows.cloud.owl.job.dashboard.dao.repository.UserRepository;

import java.util.Random;

/**
 * @author 张治保
 * @since 2024/7/18
 */
@RequiredArgsConstructor
@RestController
public class DashboardController {

    private final UserRepository userRepository;

    @GetMapping("/test")
    public Flux<User> users() {
        return userRepository.save(new User().setUsername("这张表" + new Random().nextInt(10000)))
                .flatMapMany(
                        user -> userRepository.findAll()
                );
    }
}
