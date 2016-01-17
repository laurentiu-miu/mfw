package ro.devwfw.mfw;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import ro.devwfw.mfw.web.component.EntityObjectArgumentResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LaurentiuM
 * @version createdOn: 1/17/16
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        List<HttpMessageConverter<?>> httpMessageConverter = new ArrayList<HttpMessageConverter<?>>();
        httpMessageConverter.add(mappingJackson2HttpMessageConverter);
        final EntityObjectArgumentResolver entityObjectArgumentResolver = new EntityObjectArgumentResolver(httpMessageConverter);
        argumentResolvers.add(entityObjectArgumentResolver);
    }

}
