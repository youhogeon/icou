package com.youhogeon.icou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.DispatcherServlet;

@EnableJpaAuditing
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Application.class, args);

        ((DispatcherServlet)ctx.getBean("dispatcherServlet")).setThrowExceptionIfNoHandlerFound(true);
	}

}
