package site.gachontable.gachontablebe.global.config.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import site.gachontable.gachontablebe.global.error.ErrorCode;
import site.gachontable.gachontablebe.global.error.exception.ServiceException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Slf4j
@Component
public class AopForLogging {

    @Pointcut("execution(public * site.gachontable.gachontablebe..*.*(..)) && " +
            "!execution(* site.gachontable.gachontablebe.global..*.*(..)) && " +
            "!execution(public * site.gachontable.gachontablebe.domain..presentation.*Controller.*(..))")
    private void publicMethodsFromService() {
    }

    @Pointcut("execution(public * site.gachontable.gachontablebe.domain..presentation.*Controller.*(..)) && " +
            "!execution(* site.gachontable.gachontablebe.domain.auth.presentation.*Controller.checkHealthStatus(..))")
    private void publicMethodsFromController() {
    }

    // Around 어드바이스를 통한 서비스 메서드 시간 측정
    @Around("publicMethodsFromService()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;

            String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
            String method = joinPoint.getSignature().getName();

            log.info("⏰{}.{} | time = {}ms", className, method, duration);
        }
    }

    // Before 어드바이스를 통한 컨트롤러 요청 로깅
    @Before(value = "publicMethodsFromController()")
    public void logController(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        String controllerName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        Map<String, Object> params = new HashMap<>();

        try {
            String decodedURI = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);

            params.put("controller", controllerName);
            params.put("method", methodName);
            params.put("params", getParams(request));
            params.put("log_time", System.currentTimeMillis());
            params.put("request_uri", decodedURI);
            params.put("http_method", request.getMethod());
        } catch (Exception e) {
            log.error("LogAspect Error", e);
        }

        log.info("🛫[{}] {}\nmethod: {}.{}\nparams: {}",
                params.get("http_method"),
                params.get("request_uri"),
                params.get("controller"),
                params.get("method"),
                params.get("params"));
    }

    private JSONObject getParams(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {
            String param = params.nextElement();
            String replaceParam = param.replaceAll("\\.", "-");
            jsonObject.put(replaceParam, request.getParameter(param));
        }

        return jsonObject;
    }

    // After 어드바이스를 통한 컨트롤러 종료 로깅
    @AfterReturning(value = "publicMethodsFromController()", returning = "result")
    public void logAfterController(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getDeclaringType().getSimpleName();

        if (result instanceof ResponseEntity<?> response) {
            log.info("✅End: {}() - {}", methodName, response.getStatusCode());
            return;
        }
        log.info("✅End: {}() - {}", methodName, result);
    }

    // AfterThrowing 어드바이스를 통한 오류 로깅
    @AfterThrowing(pointcut = "publicMethodsFromService()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();

        if (exception instanceof ServiceException serviceException) {
            ErrorCode errorCode = serviceException.getErrorCode();

            log.error("🚨Error: {}() - ErrorCode: {}, ErrorMessage: {}",
                    methodName, errorCode.getCode(), errorCode.getMessage());
        } else {
            log.error("🚨Error: {}() - {}", methodName, exception.getMessage());
        }
    }
}
