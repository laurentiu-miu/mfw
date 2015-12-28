package ro.devwfw.mfw.repository;

import ro.devwfw.mfw.model.BaseEntity;

import java.util.Collection;

/**
 * @author LaurentiuM
 * @version createdOn: 12/27/15
 */
public interface BaseRepository {
    <T extends BaseEntity> Collection<T> findAll(Class<T> clazz);

    <T extends BaseEntity> T findOne(Class<T> clazz, Long id);

    <T extends BaseEntity> T create(T baseEntity);

    <T extends BaseEntity> T update(T baseEntity);

    <T extends BaseEntity> void delete(Class<T> clazz, Long id);

}
