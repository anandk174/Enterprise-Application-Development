package edu.sjsu.cmpe275.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import java.util.*;

import edu.sjsu.cmpe275.aop.SecretStatsImpl;

@Aspect
@Order(0)
public class AccessControlAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */
	@Autowired SecretStatsImpl stats;
	
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.readSecret(..))")
	public void readSecretAuthorize(JoinPoint joinPoint) {
		Object[] params = joinPoint.getArgs();
		String userId = params[0].toString();
		UUID secretId = (UUID)params[1];
		stats.authorizeReadShare(userId, secretId);
	}
	
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.shareSecret(..))")
	public void shareSecretAuthorize(JoinPoint joinPoint) {
		Object[] params = joinPoint.getArgs();
		String userId = params[0].toString();
		UUID secretId = (UUID)params[1];
		stats.authorizeReadShare(userId, secretId);
	}
	
	@Before("execution(public * edu.sjsu.cmpe275.aop.SecretService.unshareSecret(..))")
	public void unshareSecretAuthorize(JoinPoint joinPoint) {
		Object[] params = joinPoint.getArgs();
		String userId = params[0].toString();
		UUID secretId = (UUID)params[1];
		stats.authorizeUnshare(userId, secretId);
	}
}
