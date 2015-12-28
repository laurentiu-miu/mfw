package ro.devwfw.mfw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author LaurentiuM
 * @version createdOn: 12/20/15
 */
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Timestamp updtimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getUpdtimestamp() {
        return updtimestamp;
    }

    public void setUpdtimestamp(Timestamp updtimestamp) {
        this.updtimestamp = updtimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity be = (BaseEntity) o;
        return Objects.equals(id, be.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + id + "]";
    }
}
