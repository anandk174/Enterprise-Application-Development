package edu.sjsu.cmpe275.aop.aspect;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.aspectj.lang.annotation.Around;

@Aspect
@Order(2)
public class RetryAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

//	@Around("execution(public void edu.sjsu.cmpe275.aop.SecretService.*(..))")
//	public void dummyAdvice(ProceedingJoinPoint joinPoint) {
//		System.out.printf("Retry aspect prior to the executuion of the metohd %s\n", joinPoint.getSignature().getName());		
//		//Existing code(given by professor)
//		Object result = null;
//		try {
//			result = joinPoint.proceed();
//			System.out.printf("Finished the executuion of the metohd %s with result %s\n", joinPoint.getSignature().getName(), result);
//		} catch (Throwable e) {
//			e.printStackTrace();
//			System.out.printf("Aborted the executuion of the metohd %s\n", joinPoint.getSignature().getName());
//		}
//	}
	
//	@Around("execution(public * edu.sjsu.cmpe275.aop.SecretService.createSecret(..))")
//	public void retryCreateSecret(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//		System.out.println("\nChecking network before executing createSecret function");
//		int attempt = 0;
//		while(attempt < 3) {
//			try {
//				proceedingJoinPoint.proceed();
//				break;
//			}
//			catch (IOException e) {
//				if (attempt < 3) {
//					attempt++;
//					continue;
//				}	
//				else throw e;				
//			}
//		}
//	}
	
	@Around("execution(public * edu.sjsu.cmpe275.aop.SecretService.readSecret(..))")
	public void retryReadSecret(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		System.out.println("\nChecking network before executing readSecret function");
		int attempt = 0;
		while(attempt < 3) {
			try {
				proceedingJoinPoint.proceed();
				break;
			}
			catch (IOException e) {
				if (attempt < 3) {
					attempt++;
					continue;
				}	
				else throw e;				
			}
		}
	}
	
//	@Around("execution(public * edu.sjsu.cmpe275.aop.SecretService.shareSecret(..))")
//	public void retryShareSecret(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//		System.out.println("\nChecking network before executing shareSecret function");
//		int attempt = 0;
//		while(attempt < 3) {
//			try {
//				proceedingJoinPoint.proceed();
//				break;
//			}
//			catch (IOException e) {
//				if (attempt < 3) {
//					attempt++;
//					continue;
//				}	
//				else throw e;				
//			}
//		}
//	}
	
	@Around("execution(public * edu.sjsu.cmpe275.aop.SecretService.unshareSecret(..))")
	public void retryUnshareSecret(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		System.out.println("\nChecking network before executing unshareSecret function");
				
		int attempt = 0;
		while(attempt < 3) {
			try {
				proceedingJoinPoint.proceed();
				break;
			}
			catch (IOException e) {
				if (attempt < 3) {
					attempt++;
					continue;
				}	
				else throw e;				
			}
		}
	}

}
