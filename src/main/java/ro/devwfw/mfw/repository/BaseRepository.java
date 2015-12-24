package ro.devwfw.mfw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.devwfw.mfw.model.BaseEntity;

/**
 * @author laurentiumiu
 * @createdOn 12/20/15
 */
@Repository
public interface BaseRepository extends JpaRepository<BaseEntity, Long> {
}
