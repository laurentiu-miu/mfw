package ro.devwfw.mfw.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ro.devwfw.mfw.model.BaseEntity;
import ro.devwfw.mfw.repository.BaseRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.List;

/**
 * The BaseServiceImpl encapsulates all business behaviors operating on the
 * BaseEntities entity model object.
 *
 * @author LaurentiuM
 * @version createdOn: 12/20/15
 */
@Service
@Transactional(
        propagation = Propagation.SUPPORTS,
        readOnly = true)
public class BaseServiceImpl implements BaseService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The <code>CounterService</code> captures metrics for Spring Actuator.
     */
    @Autowired
    private CounterService counterService;

    /**
     * The Spring Data repository for BaseEntity entities.
     */
    @Autowired
    private BaseRepository baseRepository;

    /**
     * Find all BaseEntities entities.
     *
     * @param <T> the class type of BaseEnity
     * @return A Collection of BaseEnity objects.
     */
    @Override
    public <T extends BaseEntity> Collection<T> findAll(Class<T> clazz) {
        logger.info("> findAll");

        counterService.increment("method.invoked.baseServiceImpl.findAll" + " on " + clazz);

        Collection<T> baseEntities = baseRepository.findAll(clazz);

        logger.info("< findAll");
        return baseEntities;
    }

    /**
     * Finds the persisted object giving the primary key identifier and the class type.
     *
     * @param clazz of the entity that is persisted
     * @param id    A Long primary key identifier.
     * @param <T> the class type of BaseEnity
     * @return The object of the type class and identified by id
     */
    @Override
    @Cacheable(
            value = "baseEntities",
            key = "#id")
    public <T extends BaseEntity> T findOne(Class<T> clazz, Long id) {
        logger.info("> findOne id:{}", id);

        counterService.increment("method.invoked.baseServiceImpl.findOne" + " on " + clazz);

        T t = (T) baseRepository.findOne(clazz, id);

        logger.info("< findOne id:{}", id);
        return t;
    }

    /**
     * Persists a BaseEntity entity in the data store.
     *
     * @param objT A BaseEntity object to be persisted.
     * @param <T> the class type of BaseEnity
     * @return The persisted BaseEntity entity.
     */
    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CachePut(
            value = "baseEntities",
            key = "#result.id")
    public <T extends BaseEntity> T create(T objT) {
        logger.info("> create");

        counterService.increment("method.invoked.baseServiceImpl.create" + " on " + objT.getClass());

        if (objT.getId() != null) {
            // Cannot create BaseEntity with specified ID value
            logger.error(
                    "Attempted to create a BaseEntity, but id attribute was not null.");
            throw new EntityExistsException(
                    "The id attribute must be null to persist a new entity.");
        }

        T objTsaved = (T) baseRepository.create(objT);

        logger.info("< create");
        return objTsaved;
    }

    /**
     * Updates a previously persisted BaseEntity entity in the data store.
     *
     * @param objT A BaseEntity object to be updated.
     * @param <T> the class type of BaseEnity
     * @return The updated BaseEntity entity.
     */
    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CachePut(
            value = "baseEntities",
            key = "#objT.id")
    public <T extends BaseEntity> T update(T objT) {
        logger.info("> update id:{}", objT.getId());

        counterService.increment("method.invoked.baseServiceImpl.update" + " on " + objT.getClass());

        T objTfind = (T) findOne(objT.getClass(), objT.getId());
        if (objTfind == null) {
            // Cannot update BaseEntity that hasn't been persisted
            logger.error(
                    "Attempted to update a BaseEntity, but the entity does not exist.");
            throw new NoResultException("Requested entity not found.");
        }

        T objTupdated = (T) baseRepository.update(objTfind);

        logger.info("< update id:{}", objTupdated.getId());
        return objTupdated;
    }

    /**
     * Removes a previously persisted BaseEntity entity from the data store.
     *
     * @param id A Long primary key identifier.
     * @param <T> the class type of BaseEnity
     * @param clazz Of the entity that is persisted
     */
    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CacheEvict(
            value = "baseEntities",
            key = "#id")
    public <T extends BaseEntity> void delete(Class<T> clazz, Long id) {
        logger.info("> delete id:{}", id);

        counterService.increment("method.invoked.baseServiceImpl.delete" + " on " + clazz);

        baseRepository.delete(clazz, id);

        logger.info("< delete id:{}", id);
    }

    /**
     * Evicts all members of the "baseEntities" cache.
     */
    @Override
    @CacheEvict(
            value = "baseEntities",
            allEntries = true)
    public void evictCache() {
        logger.info("> evictCache");
        logger.info("< evictCache");
    }
}
