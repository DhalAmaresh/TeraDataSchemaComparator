package com.ad.de.config;

import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import com.ad.de.domain.TableInfo;
import com.ad.de.domain.Tables;


@Configuration
@Component
public class AppConfig {

	public AppConfig(){
		
	}
	@Value("${spring.teradataDatasource.url}")
	private String url;
	
	@Value("${spring.teradataDatasource.username}")
	private String username;
	@Value("${spring.teradataDatasource.password}")
	private String password;
	@Value("${spring.teradataDatasource.driver-class-name}")
	private String driverName;
	@Value("${spring.teradataDatasource.maxActive}")
	private String maxActive;
	@Value("${spring.teradataDatasource.minIdle}")
	private String minIdle;
	@Value("${spring.teradataDatasource.maxIdle}")
	private String maxIdle;
	@Value("${spring.teradataDatasource.maxWait}")
	private String maxWait;
	
	
	@Bean
	@Primary
	public DataSource dataSource(){
		return DataSourceBuilder.create().url(url).username(username).password(password)
				.driverClassName(driverName)
				.build();
	}
	
	@Bean
	public Jaxb2Marshaller jaxb2Marshaller(){
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setClassesToBeBound(new Class[]{Tables.class,TableInfo.class});
		jaxb2Marshaller.setMarshallerProperties(new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;

			{
				put("jaxb.formatted.output", Boolean.TRUE);
			}
		});
		return jaxb2Marshaller;
	}
}
