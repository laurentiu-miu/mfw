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
 * The PathVariableToClassMapperImpl is used to map at runtime entities annotated withe @EntityName
 * so that you know what class to persist.
 *
 * @author LaurentiuM
 * @version createdOn: 12/27/15
 */
public class PathVariableToClassMapperImpl implements PathVariableToClassMapper {

    /**
     * pakeget to scan for annotation @EntityName
     */
    @Value("${packageToBeScanned.by.entityName}")
    private String packageToBeScanned;
    /**
     * map containing entities names and classes of the entities
     */
    private Map<String, Class<? extends BaseEntity>> mappings;

    /**
     * Returns the entitie class giving the entity name
     *
     * @param path the entity name from annotation @EntityName
     * @param <T> the class type of BaseEnity
     * @return The entity class by the entity name
     */
    @Override
    public <T extends BaseEntity> Class<T> getClassByPath(String path) {
        return (Class<T>) mappings.get(path);
    }

    /**
     * Construct at runtime a map which contains entity names annotated with @EntityName and the
     * class of the entity.
     *
     * @return A map contining fo entity name and class.
     * @throws ClassNotFoundException if the class is not found
     */
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

    /**
     * This is the map containing entities names and classes of the entities
     *
     * @return A map containing entity name and class
     */
    public Map<String, Class<? extends BaseEntity>> getMappings() {
        return mappings;
    }
}