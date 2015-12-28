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

    @Override
    public <T extends BaseEntity> Collection<T> findAll(Class<T> clazz) {
        logger.info("> findAll");

        counterService.increment("method.invoked.baseServiceImpl.findAll" + " on " + clazz);

        Collection<T> baseEntities = baseRepository.findAll(clazz);

        logger.info("< findAll");
        return baseEntities;
    }

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

        // Ensure the entity object to be created does NOT exist in the
        // repository. Prevent the default behavior of save() which will update
        // an existing entity if the entity matching the supplied id exists.
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

        // Ensure the entity object to be updated exists in the repository to
        // prevent the default behavior of save() which will persist a new
        // entity if the entity matching the id does not exist
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

    @Override
    @CacheEvict(
            value = "baseEntities",
            allEntries = true)
    public void evictCache() {
        logger.info("> evictCache");
        logger.info("< evictCache");
    }
}
