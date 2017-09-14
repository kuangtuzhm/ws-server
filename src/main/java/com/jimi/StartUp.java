package com.jimi;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class StartUp {

	public static void main(String[] args) {
		
		SpringApplication.run(StartUp.class, args);
	}
	
	@Bean  
	public EmbeddedServletContainerFactory createEmbeddedServletContainerFactory()  
	{  
	    TomcatEmbeddedServletContainerFactory tomcatFactory = new TomcatEmbeddedServletContainerFactory();  
	    //tomcatFactory.setPort(8081);  
	    tomcatFactory.addConnectorCustomizers(new MyTomcatConnectorCustomizer());  
	    return tomcatFactory;  
	} 

	class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer  
	{  
	    public void customize(Connector connector)  
	    {  
	        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();  
	        //设置最大连接数  
	        protocol.setMaxConnections(50000);  
	        //设置最大线程数  
	        protocol.setMaxThreads(2000);  
	        protocol.setConnectionTimeout(30000);  
	    }  
	}  
}
