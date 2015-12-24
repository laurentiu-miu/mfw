package ro.devwfw.mfw.actuator.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ro.devwfw.mfw.model.BaseEntity;
import ro.devwfw.mfw.service.BaseService;

import java.util.Collection;

/**
 * The BaseEntityHealthIndicator is a custom Spring Boot Actuator HealthIndicator
 * implementation. HealthIndicator classes are invoked when the Actuator
 * 'health' endpoint is invoked. Each HealthIndicator class assesses some
 * portion of the application's health, returing a Health object which indicates
 * that status and, optionally, additional health attributes.
 *
 * @author laurentiumiu
 * @createdOn 12/20/15
 */
@Component
public class BaseEntityHealthIndicator implements HealthIndicator {

    /**
     * The BaseService business service.
     */
    @Autowired
    private BaseService baseService;

    @Override
    public Health health() {

        Collection<BaseEntity> baseEntities = baseService.findAll();

        if (baseEntities == null || baseEntities.size() == 0) {
            return Health.down().withDetail("count", 0).build();
        }

        return Health.up().withDetail("count", baseEntities.size()).build();
    }
}
