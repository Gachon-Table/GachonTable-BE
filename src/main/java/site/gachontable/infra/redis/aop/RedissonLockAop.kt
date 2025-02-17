package site.gachontable.infra.redis.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import site.gachontable.independent.parser.CustomSpringELParser.getDynamicValue
import site.gachontable.infra.redis.RedissonLock

@Aspect
@Component
class RedissonLockAop(
    private val redissonClient: RedissonClient,
    private val aopForTransaction: AopForTransaction,
) {
    @Around("@annotation(site.gachontable.infra.redis.RedissonLock)")
    @Throws(Throwable::class)
    fun lock(joinPoint: ProceedingJoinPoint): Any {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val redissonLock = method.getAnnotation<RedissonLock>(RedissonLock::class.java)

        val key = getDynamicValue(
            signature.parameterNames, joinPoint.args, redissonLock.key
        ) as String
        val rLock = redissonClient.getLock(key)

        try {
            val available = rLock.tryLock(
                redissonLock.waitTime, redissonLock.leaseTime, redissonLock.timeUnit
            )
            if (!available) {
                log.info("Lock 획득 실패 : {}", key)
                return false
            }

            return aopForTransaction.proceed(joinPoint, key)
        } catch (e: InterruptedException) {
            throw InterruptedException()
        } finally {
            try {
                log.info("Lock 해제 : {}", key)
                rLock.unlock()
            } catch (e: IllegalMonitorStateException) {
                log.info(
                    "이미 해제된 Lock : {} {}", method.name, key
                )
            }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(RedissonLockAop::class.java)
    }
}
