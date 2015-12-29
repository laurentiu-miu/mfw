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
     * Construct at runtime a map which contains entity names annotated with @EntityName and the
     * class of the entity.
     *
     * @return A map contining fo entity name and class.
     * @throws ClassNotFoundException
     */
    <T extends BaseEntity> Class<T> getClassByPath(String path);

    /**
     * This is the map containing entities names and classes of the entities
     *
     * @return A map containing entity name and class
     */
    public Map<String, Class<? extends BaseEntity>> getMappings();
}
