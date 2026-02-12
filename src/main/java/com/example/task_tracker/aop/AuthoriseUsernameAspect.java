package com.example.task_tracker.aop;

import com.example.task_tracker.security.AppUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;

import static com.example.task_tracker.aop.AspectMessagesTemplates.CALL_OPERATION;
import static com.example.task_tracker.aop.AspectMessagesTemplates.TEMPLATE_OPERATION_FORBIDDEN;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthoriseUsernameAspect {

    @Around("@annotation(AuthoriseUsernameForUserUpdateAndDelete)")
    public Mono<?> AuthUsernameForUserBefore(ProceedingJoinPoint joinPoint) {

        String pathId = getId(joinPoint);

        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(auth -> {
                    AppUserPrincipal principal = (AppUserPrincipal) auth.getPrincipal();

                    log.info(CALL_OPERATION,
                            principal.getUsername(),
                            joinPoint.getSignature().getName(),
                            pathId,
                            Arrays.toString(joinPoint.getArgs()));

                    boolean isAdmin = auth.getAuthorities()
                            .stream()
                            .anyMatch(role ->
                                    role.getAuthority().equals("ROLE_ADMIN"));

                    boolean isOwner = principal.getId().equals(pathId);

                    if (isAdmin || isOwner) {
                        try {
                            return (Mono<?>) joinPoint.proceed();
                        } catch (Throwable e) {
                            return Mono.error(e);
                        }
                    }

                    log.warn(TEMPLATE_OPERATION_FORBIDDEN);
                    return Mono.error(new AccessDeniedException(TEMPLATE_OPERATION_FORBIDDEN));
                });
    }

    private String getId(ProceedingJoinPoint joinPoint) {
        ServerRequest request = (ServerRequest) joinPoint.getArgs()[0];
        return request.pathVariable("id");
    }
}
