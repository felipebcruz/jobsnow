package br.com.jobsnow.database.resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
//import org.assertj.core.util.Arrays;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class DatabaseAspect {

	private static final Logger logger = LogManager.getLogger(DatabaseAspect.class);

	@Before("execution(* br.com.jobsnow.database.service.impl.ImplServiceDatabase.*(..))")
	public void interceptGet(JoinPoint jp) {
		
		Signature signature = jp.getSignature();
		Object[] args = jp.getArgs();
//		List<Object> asList = Arrays.asList(args);
//		logger.info("" + signature +": " + asList);
		
	}
	
}
