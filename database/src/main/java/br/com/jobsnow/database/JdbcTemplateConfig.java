package br.com.jobsnow.database;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcTemplateConfig {

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate;
	}
	
	@Bean
	public DataSource dataSource() {
		PGSimpleDataSource source = new PGSimpleDataSource();
		source.setServerName("localhost");
		source.setDatabaseName("jobsnow");
		source.setUser("postgres");
		source.setPassword("banco");
		source.setUrl("jdbc:postgresql://localhost:5432/jobsnow");
		return source;
	}
}