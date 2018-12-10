package com.itsight.configuration;
 
import org.springframework.context.annotation.Configuration;
 
@Configuration
public class HibernateConfiguration {
     
//    @Bean
//	@Profile(value = "development")
//    public DataSource dataSource() {
//    	return DataSourceBuilder.create()
//    	        .username("root")
//    	        .password("")
//    	        .url("jdbc:mysql://localhost:3306/itsightadmin_runfit")
//    	        .driverClassName("com.mysql.jdbc.Driver")
//    	        .build();
//    }
    
//    @Bean
//	@Profile(value = "production")
//    @ConditionalOnMissingBean(name="dataSourceJndi")
//    @Primary
//    public DataSource dataSourceJndi() {
//        DataSource dataSource = null;
//        JndiTemplate jndi = new JndiTemplate();
//        try {
//            dataSource = jndi.lookup("java:comp/env/jdbc/Workout", DataSource.class);
//        } catch (NamingException e) {
//            System.out.println("********************************* CAUSE:" + "\n" + e.getMessage());
//        }
//        return dataSource;
//    }
}