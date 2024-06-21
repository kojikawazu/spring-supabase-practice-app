package com.example.app.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Logging用Aspect
 * メソッドの実行前後にログを記録するためのアスペクト
 * @since 2024/06/21
 * @author koji kawazu
 */
@Aspect
@Component
public class LoggingAspect {
	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
	
	/**
	 * メソッドの入力時と終了時にログを記録する
	 * @param joinPoint メソッド呼び出しのコンテキストを提供するJoinPoint
	 * @return result
	 * @throws Throwable
	 */
	@Around("execution(* com.example.app..*.*(..))")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		String className  = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		
		logger.info("START: {}.{}", className, methodName);
		
		try {
			// メソッドを実行し、その結果を取得
			Object result = joinPoint.proceed();
			
			logger.info("END: {}.{}", className, methodName);
			return result;
		} catch (Throwable throwable) {
			logger.error("EXCEPTION in {}.{}: {}", className, methodName, throwable.getMessage());
			throw throwable;
		}
	}
}
