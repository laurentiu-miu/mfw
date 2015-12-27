package ro.devwfw.mfw.model;

import ro.devwfw.mfw.utils.annotations.EntityName;

import javax.persistence.Entity;

/**
 * @author laurentiumiu
 * @createdOn 12/27/15
 */
@Entity
@EntityName("demo")
public class DemoEntity extends BaseEntity {

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
