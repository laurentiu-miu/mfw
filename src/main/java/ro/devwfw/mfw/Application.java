package ro.devwfw.mfw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ro.devwfw.mfw.utils.mappings.PathVariableToClassMapper;
import ro.devwfw.mfw.utils.mappings.PathVariableToClassMapperImpl;
import ro.devwfw.mfw.web.component.EntityObjectArgumentResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Boot main application class. Serves as both the runtime application
 * entry point and the central Java configuration class.
 *
 * @author LaurentiuM
 * @version createdOn: 12/20/15
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public class Application {
    /**
     * Entry point for the application.
     *
     * @param args Command line arguments.
     * @throws Exception Thrown when an unexpected Exception is thrown from the
     *                   application.
     */
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Create a CacheManager implementation class to be used by Spring where
     * <code>@Cacheable</code> annotations are applied.
     *
     * @return A CacheManager instance.
     */
    @Bean
    public CacheManager cacheManager() {

        GuavaCacheManager cacheManager = new GuavaCacheManager("baseEntities");

        return cacheManager;
    }

    /**
     * Create a PathVariableToClassMapper implementation class to be used in Controllers where
     * you need to find the class by entity name
     *
     * @return A PathVariableToClassMapper instance
     */
    @Bean
    public PathVariableToClassMapper pathVariableToClassMapper() {

        PathVariableToClassMapper pathVariableToClassMapper = new PathVariableToClassMapperImpl();

        return pathVariableToClassMapper;
    }

}
