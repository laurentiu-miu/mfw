package ro.devwfw.mfw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * @author: laurentiumiu
 * @createdOn: 12/20/15
 */
@Entity
public class BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Timestamp updtimestamp;
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
