package org.imgcnv.spring;

import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.imgcnv.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Class for logging with Spring AOP.
 * @author Dmitry_Slepchenkov
 *
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * Used for hold duration interval.
     */
    private static final ThreadLocal<Long> INTERVAL = new  ThreadLocal<>();

    /**
     * Logging with Spring AOP.
     * @param joinPoint ProceedingJoinPoint.
     * @return invoke result.
     */
    @Around("execution(* org.imgcnv.service.concurrent.resize.*.*(..)) ||"
          + "execution(* org.imgcnv.service.concurrent.download.*.*(..)) ||"
          + "execution(* org.imgcnv.service.concurrent.read.*.*(..))")
    public final Object logAround(final ProceedingJoinPoint joinPoint) {

        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String methodAndArguments = getMethodAndArgumentsAsString(joinPoint);
        INTERVAL.set(Instant.now().toEpochMilli());
        boolean hasError = false;
        Object result;
        try {
            result = joinPoint.proceed();
            INTERVAL.set(getDuration(INTERVAL.get()));
        } catch (Throwable ex) {
            hasError = true;
            log.error(methodAndArguments + getExceptionAsString(ex,
                    INTERVAL.get()), ex);
            throw new ApplicationException(ex);
        }
        if (!hasError) {
            log.info(methodAndArguments + getResultAsString(result,
                    INTERVAL.get()));
        }
        return result;
    }

    /**
     * Calculate time interval of method executing.
     * @param start long start time for duration
     * @return int time interval of method executing.
     */
    protected final long getDuration(final long start) {
        return Instant.now().toEpochMilli()  - start;
    }

    /**
     * String representation of method and arguments.
     * @param joinPoint ProceedingJoinPoint.
     * @return String representation of method and arguments.
     */
    protected final String getMethodAndArgumentsAsString(
            final ProceedingJoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getArgs()).map(arg -> arg.toString())
                .collect(Collectors.joining(", ", getMethodName(joinPoint)
                        + "(", ")"));
    }

    /**
     * String representation of method name.
     * @param joinPoint  ProceedingJoinPoint
     * @return String representation of method name.
     */
    protected final String getMethodName(final ProceedingJoinPoint joinPoint) {
        return MethodSignature.class.cast(joinPoint.getSignature())
                .getMethod()
                .getName();
    }

    /**
     * String representation of result.
     * @param result Object.
     * @param duration time interval.
     * @return String representation of result.
     */
    protected final String getResultAsString(final Object result,
            final long duration) {
        return new StringBuilder(" returned ")
                .append(result)
                .append(" in ")
                .append(duration)
                .append(" ms")
                .toString();
    }

    /**
     * String representation of result if exception exist.
     * @param ex Exception.
     * @param duration time interval.
     * @return String representation of result if exception exist.
     */
    protected final String getExceptionAsString(final Throwable ex,
            final long duration) {
        return new StringBuilder(" threw ")
                .append(ex.getClass().getSimpleName())
                .append(" after ")
                .append(duration)
                .append(" ms with message ")
                .append(ex.getMessage()).toString();
    }
}
