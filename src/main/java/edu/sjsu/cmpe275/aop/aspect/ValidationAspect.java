package edu.sjsu.cmpe275.aop.aspect;

import java.io.IOException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

@Aspect
@Order(0)
public class ValidationAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

//	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.*(..))")
//	public void dummyAdvice(JoinPoint joinPoint) {
//		System.out.printf("Doing validation prior to the executuion of the metohd %s\n", joinPoint.getSignature().getName());
//	}
	
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.createSecret(..))")
	public void validateCreateSecret(JoinPoint joinPoint) {
			
		Object[] params = joinPoint.getArgs();
		//if the userId is null
		//or if the secret content is greater than 100
		//the IllegalArgumentException is thrown
		
//		if(params[0] == null || params[1].toString().length() > 100) throw new IllegalArgumentException();	
		if(params[0] == null) {
			System.out.println("*-> The userId provided is null");
			throw new IllegalArgumentException();
		}
		if (params[1] != null) {
			if(params[1].toString().length() > 100) {
				System.out.println("*-> The secret is more than 100 characters long");
				throw new IllegalArgumentException();
			}
		}
		
	}
	
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.readSecret(..))")	
	public void validateReadSecret(JoinPoint joinPoint) {
				
		Object[] params = joinPoint.getArgs();
		for( Object param : params) 
			if( param == null) throw new IllegalArgumentException();		
	}
	
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.shareSecret(..))")
	public void validateShareSecret(JoinPoint joinPoint) {
				
		Object[] params = joinPoint.getArgs();
		for( Object param : params) 
			if( param == null) {
				System.out.println("Null Parameter entered");
				throw new IllegalArgumentException();
			}
	}
	
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.unshareSecret(..))")
	public void validateUnshareSecret(JoinPoint joinPoint) {
				
		Object[] params = joinPoint.getArgs();
		for( Object param : params) 
			if( param == null) throw new IllegalArgumentException();
	}
	

}
