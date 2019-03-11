package edu.sjsu.cmpe275.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.*;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.SecretStatsImpl;
import java.util.*;
@Aspect
@Order(1)
public class StatsAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

	
	
	
	@Autowired SecretStatsImpl stats;
	
//	@Before("execution(public void edu.sjsu.cmpe275.aop.SecretService.*(..))")
//	public void dummyBeforeAdvice(JoinPoint joinPoint) {
//		System.out.printf("Doing stats before the executuion of the metohd %s\n", joinPoint.getSignature().getName());
//	}
	
	@AfterReturning(pointcut = "execution(public * edu.sjsu.cmpe275.aop.SecretService.createSecret(..))", returning = "retval")
	public void updateCreater(JoinPoint joinPoint, UUID retval) {			
		
		Object[] params = joinPoint.getArgs();		
		String userName = params[0].toString();
		UUID uuid = retval;
		String secretContent = params[1].toString();		
		stats.updateCreateOccurance(userName, uuid, secretContent);
		System.out.println(userName+" created a secret with UUID: "+uuid);		
					
	}	
	
	
	
	
	
	@After("execution(public void edu.sjsu.cmpe275.aop.SecretService.shareSecret(..))")
	public void onShareSecret(JoinPoint joinPoint) {
		Object[] params = joinPoint.getArgs();
		stats.whenShare(params);
	}
	
	@After("execution(public void edu.sjsu.cmpe275.aop.SecretService.unshareSecret(..))")
	public void dummyAfterAdvice(JoinPoint joinPoint) {
		Object[] params = joinPoint.getArgs();
		stats.whenUnshare(params);
	}	
}
