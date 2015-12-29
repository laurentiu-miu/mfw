package ro.devwfw.mfw.utils.mappings;

import ro.devwfw.mfw.model.BaseEntity;

import java.util.Map;

/**
 * The PathVariableToClassMapper is used to map at runtime entities annotated withe @EntityName
 * so that you know what class to persist
 *
 * @author LaurentiuM
 * @version createdOn: 12/27/15
 */
public interface PathVariableToClassMapper {

    /**
     * Returns the entitie class giving the entity name
     *
     * @param path the entity name from annotation @EntityName
     * @param <T> the class type of BaseEnity
     * @return The entity class by the entity name
     */
    <T extends BaseEntity> Class<T> getClassByPath(String path);

    /**
     * This is the map containing entities names and classes of the entities
     *
     * @return A map containing entity name and class
     */
    public Map<String, Class<? extends BaseEntity>> getMappings();
}
