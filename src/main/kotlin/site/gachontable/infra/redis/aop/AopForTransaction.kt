package site.gachontable.infra.redis.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class AopForTransaction {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Throws(Throwable::class)
    fun proceed(joinPoint: ProceedingJoinPoint, key: String): Any {
        log.info("Lock 수행 : {}", key)
        return joinPoint.proceed()
    }

    companion object {
        private val log = LoggerFactory.getLogger(AopForTransaction::class.java)
    }
}
