package ro.devwfw.mfw.utils.mappings;

import ro.devwfw.mfw.model.BaseEntity;

/**
 * @author laurentiumiu
 * @createdOn 12/27/15
 */
public interface PathVariableToClassMapper {
    <T extends BaseEntity> Class<T> getClassByPath(String path);
}
