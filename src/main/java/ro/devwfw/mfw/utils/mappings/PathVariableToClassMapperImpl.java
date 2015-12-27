package ro.devwfw.mfw.utils.mappings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import ro.devwfw.mfw.model.BaseEntity;
import ro.devwfw.mfw.utils.annotations.EntityName;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author laurentiumiu
 * @createdOn 12/27/15
 */
public class PathVariableToClassMapperImpl implements PathVariableToClassMapper {

    @Value("${packageToBeScanned.by.entityName}")
    private String packageToBeScanned;
    private Map<String, Class<? extends BaseEntity>> mappings;

    @Override
    public <T extends BaseEntity> Class<T> getClassByPath(String path) {
        return (Class<T>) mappings.get(path);
    }

    @PostConstruct
    private Map<String, Class<? extends BaseEntity>> createMappings() throws ClassNotFoundException {

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AssignableTypeFilter(BaseEntity.class));

        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(packageToBeScanned);
        if (candidateComponents.size() > 0) {
            mappings = new HashMap<>();
            for (BeanDefinition bd : candidateComponents) {
                Class clazz = Class.forName(bd.getBeanClassName());
                EntityName annotation = (EntityName) clazz.getAnnotation(EntityName.class);
                if (annotation != null)
                    mappings.put(annotation.value(), clazz);
            }
        }
        return mappings;
    }
}