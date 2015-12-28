package ro.devwfw.mfw.repository;

import org.springframework.stereotype.Repository;
import ro.devwfw.mfw.model.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;

/**
 * @author LaurentiuM
 * @version createdOn: 12/27/15
 */
@Repository
public class BaseRepositoryImpl implements BaseRepository {
    @PersistenceContext
    protected EntityManager entityManager;

    public <T extends BaseEntity> Collection<T> findAll(Class<T> clazz) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> criteria = criteriaBuilder.createQuery(clazz);

        final Root<T> root = criteria.from(clazz);
        criteria.select(root);

        final TypedQuery<T> query = entityManager.createQuery(criteria);
        return query.getResultList();
    }

    public <T extends BaseEntity> T findOne(Class<T> clazz, Long id) {
        return entityManager.find(clazz, id);
    }

    public <T extends BaseEntity> T create(T baseEntity) {
        entityManager.persist(baseEntity);
        return baseEntity;
    }

    public <T extends BaseEntity> T update(T baseEntity) {
        return entityManager.merge(baseEntity);
    }

    public <T extends BaseEntity> void delete(Class<T> clazz, Long id) {
        BaseEntity baseEntity = findOne(clazz, id);
        if (baseEntity != null) {
            if (!entityManager.contains(baseEntity))
                baseEntity = entityManager.merge(baseEntity);
            entityManager.remove(baseEntity);
        }
    }

}
