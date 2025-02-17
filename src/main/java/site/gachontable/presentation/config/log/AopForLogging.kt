package site.gachontable.presentation.config.log

import jakarta.servlet.http.HttpServletRequest
import net.minidev.json.JSONObject
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.*
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import site.gachontable.independent.error.exception.ServiceException
import site.gachontable.independent.type.ErrorCode
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Aspect
@Component
class AopForLogging {
    @Pointcut(
        "execution(public * site.gachontable.domain..*.*(..)) && " +
                "!execution(* site.gachontable.infra..*(..)) && " +
                "!execution(public * site.gachontable.presentation..*Controller.*(..))"
    )
    fun publicMethodsFromService() {
    }

    @Pointcut(
        "execution(public * site.gachontable.presentation..*Controller.*(..)) && " +
                "!execution(* site.gachontable.presentation.auth.api.AuthController.checkHealthStatus(..))"
    )
    fun publicMethodsFromController() {
    }

    @Around("publicMethodsFromService()")
    @Throws(Throwable::class)
    fun logAround(joinPoint: ProceedingJoinPoint): Any {
        val start = System.currentTimeMillis()
        try {
            return joinPoint.proceed()
        } finally {
            val duration = System.currentTimeMillis() - start
            val className = joinPoint.signature.declaringType.simpleName
            val methodName = joinPoint.signature.name
            log.info("⏰{}.{} | time = {}ms", className, methodName, duration)
        }
    }

    @Before("publicMethodsFromController()")
    fun logController(joinPoint: JoinPoint) {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        val request = requestAttributes.request

        val controllerName = joinPoint.signature.declaringType.simpleName
        val methodName = joinPoint.signature.name

        val params = mutableMapOf<String, Any>()
        try {
            val decodedURI = URLDecoder.decode(request.requestURI, StandardCharsets.UTF_8)
            params["controller"] = controllerName
            params["method"] = methodName
            params["params"] = getParams(request)
            params["log_time"] = System.currentTimeMillis()
            params["request_uri"] = decodedURI
            params["http_method"] = request.method
        } catch (e: Exception) {
            log.error("LogAspect Error", e)
        }

        log.info(
            "🛫[{}] {}\nmethod: {}.{}\nparams: {}",
            params["http_method"],
            params["request_uri"],
            params["controller"],
            params["method"],
            params["params"]
        )
    }

    private fun getParams(request: HttpServletRequest): JSONObject {
        val jsonObject = JSONObject()
        val paramNames = request.parameterNames
        while (paramNames.hasMoreElements()) {
            val param = paramNames.nextElement()
            val replacedParam = param.replace(Regex("\\."), "-")
            jsonObject[replacedParam] = request.getParameter(param)
        }
        return jsonObject
    }

    @AfterReturning(pointcut = "publicMethodsFromController()", returning = "result")
    fun logAfterController(joinPoint: JoinPoint, result: Any) {
        val methodName = joinPoint.signature.declaringType.simpleName
        if (result is ResponseEntity<*>) {
            log.info("✅End: {}() - {}", methodName, result.statusCode)
        } else {
            log.info("✅End: {}() - {}", methodName, result)
        }
    }

    @AfterThrowing(pointcut = "publicMethodsFromService()", throwing = "exception")
    fun logException(joinPoint: JoinPoint, exception: Throwable) {
        val methodName = joinPoint.signature.name
        if (exception is ServiceException) {
            val errorCode: ErrorCode = exception.errorCode
            log.error(
                "🚨Error: {}() - ErrorCode: {}, ErrorMessage: {}",
                methodName, errorCode.code, errorCode.message
            )
        } else {
            log.error("🚨Error: {}() - {}", methodName, exception.message)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(AopForLogging::class.java)
    }
}
