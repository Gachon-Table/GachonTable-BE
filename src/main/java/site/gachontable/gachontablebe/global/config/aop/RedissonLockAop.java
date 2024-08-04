package site.gachontable.gachontablebe.global.config.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import site.gachontable.gachontablebe.global.config.parser.CustomSpringELParser;
import site.gachontable.gachontablebe.global.config.redis.RedissonLock;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonLockAop {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(site.gachontable.gachontablebe.global.config.redis.RedissonLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);

        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser
                .getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), redissonLock.key());
        RLock rLock = redissonClient.getLock(key);

        try {
            boolean available = rLock.tryLock(redissonLock.waitTime(), redissonLock.leaseTime(), redissonLock.timeUnit());
            if (!available) {
                log.info("Lock 획득 실패 : {}", key);
                return false;
            }

            return aopForTransaction.proceed(joinPoint, key);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            try {
                log.info("Lock 해제 : {}", key);
                rLock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.info("이미 해제된 Lock : {} {}", method.getName(), key);
            }
        }
    }
}
