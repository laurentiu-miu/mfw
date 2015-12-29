package ro.devwfw.mfw.actuator.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ro.devwfw.mfw.model.BaseEntity;
import ro.devwfw.mfw.model.DemoEntity;
import ro.devwfw.mfw.service.BaseService;
import ro.devwfw.mfw.utils.mappings.PathVariableToClassMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The BaseEntityHealthIndicator is a custom Spring Boot Actuator HealthIndicator
 * implementation. HealthIndicator classes are invoked when the Actuator
 * 'health' endpoint is invoked. Each HealthIndicator class assesses some
 * portion of the application's health, returing a Health object which indicates
 * that status and, optionally, additional health attributes.
 *
 * @author LaurentiuM
 * @version createdOn: 12/20/15
 */
@Component
public class BaseEntityHealthIndicator implements HealthIndicator {

    /**
     * The BaseService business service.
     */
    @Autowired
    private BaseService baseService;

    /**
     * The PathVariableToClassMapper maps entity to model.
     */
    @Autowired
    private PathVariableToClassMapper pathVariableToClassMapper;

    /**
     * @return entityMap containing the total number of persistent entities group by class
     */
    @Override
    public Health health() {
        Map<String, Class<? extends BaseEntity>> entityMap = pathVariableToClassMapper.getMappings();
        Map<String, Integer> map = entityMap.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> baseService.findAll(entry.getValue()).size()));
        return Health.up().withDetail("entityMap", map).build();
    }

}
