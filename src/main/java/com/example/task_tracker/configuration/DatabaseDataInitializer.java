package com.example.task_tracker.configuration;

import com.example.task_tracker.entity.RoleType;
import com.example.task_tracker.entity.User;
import com.example.task_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseDataInitializer {


    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Value("${app.security.adminPassword}")
    private String adminPassword;

    @Value("${app.security.adminUsername}")
    private String adminUsername;

    @EventListener(ApplicationReadyEvent.class)
    public void createAdminUserInDb() {
        userRepository.findByUsername(adminUsername)
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("Создание администратора по умолчанию: {}",
                            adminUsername);

                    var admin = new User();

                    admin.setUsername(adminUsername);
                    admin.setPassword(passwordEncoder.encode(adminPassword));
                    admin.setRoles(Collections.singleton(RoleType.ROLE_ADMIN));

                    return userRepository.save(admin);
                }))
                .doOnSuccess(user -> log.info("Администратор готов к работе: {}",
                        user.getUsername()))
                .doOnError(e -> log.error("Ошибка при инициализации администратора", e))
                .subscribe();
    }
}
