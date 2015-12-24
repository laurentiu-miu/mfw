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

/**
 * The BaseServiceImpl encapsulates all business behaviors operating on the
 * Greeting entity model object.
 *
 * @author laurentiumiu
 * @createdOn 12/20/15
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
     * The Spring Data repository for Greeting entities.
     */
    @Autowired
    private BaseRepository baseRepository;

    @Override
    public Collection<BaseEntity> findAll() {
        logger.info("> findAll");

        counterService.increment("method.invoked.baseServiceImpl.findAll");

        Collection<BaseEntity> baseEntities = baseRepository.findAll();

        logger.info("< findAll");
        return baseEntities;
    }

    @Override
    @Cacheable(
            value = "baseEntities",
            key = "#id")
    public BaseEntity findOne(Long id) {
        logger.info("> findOne id:{}", id);

        counterService.increment("method.invoked.baseServiceImpl.findOne");

        BaseEntity greeting = baseRepository.findOne(id);

        logger.info("< findOne id:{}", id);
        return greeting;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CachePut(
            value = "baseEntities",
            key = "#result.id")
    public BaseEntity create(BaseEntity baseEntity) {
        logger.info("> create");

        counterService.increment("method.invoked.baseServiceImpl.create");

        // Ensure the entity object to be created does NOT exist in the
        // repository. Prevent the default behavior of save() which will update
        // an existing entity if the entity matching the supplied id exists.
        if (baseEntity.getId() != null) {
            // Cannot create BaseEntity with specified ID value
            logger.error(
                    "Attempted to create a BaseEntity, but id attribute was not null.");
            throw new EntityExistsException(
                    "The id attribute must be null to persist a new entity.");
        }

        BaseEntity savedBaseEntity = baseRepository.save(baseEntity);

        logger.info("< create");
        return savedBaseEntity;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CachePut(
            value = "baseEntities",
            key = "#baseEntity.id")
    public BaseEntity update(BaseEntity baseEntity) {
        logger.info("> update id:{}", baseEntity.getId());

        counterService.increment("method.invoked.baseServiceImpl.update");

        // Ensure the entity object to be updated exists in the repository to
        // prevent the default behavior of save() which will persist a new
        // entity if the entity matching the id does not exist
        BaseEntity baseEntityToUpdate = findOne(baseEntity.getId());
        if (baseEntityToUpdate == null) {
            // Cannot update BaseEntity that hasn't been persisted
            logger.error(
                    "Attempted to update a BaseEntity, but the entity does not exist.");
            throw new NoResultException("Requested entity not found.");
        }

        baseEntityToUpdate.setDescription(baseEntity.getDescription());
        BaseEntity updatedBaseEntity = baseRepository.save(baseEntityToUpdate);

        logger.info("< update id:{}", baseEntity.getId());
        return updatedBaseEntity;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CacheEvict(
            value = "baseEntities",
            key = "#id")
    public void delete(Long id) {
        logger.info("> delete id:{}", id);

        counterService.increment("method.invoked.baseServiceImpl.delete");

        baseRepository.delete(id);

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
