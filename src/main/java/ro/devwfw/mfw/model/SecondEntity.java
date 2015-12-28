package ro.devwfw.mfw.model;

import ro.devwfw.mfw.utils.annotations.EntityName;

import javax.persistence.Entity;

/**
 * @author LaurentiuM
 * @version createdOn: 12/28/15
 */
@Entity
@EntityName("second")
public class SecondEntity extends BaseEntity {
    private String extraProperty;

    public String getExtraProperty() {
        return extraProperty;
    }

    public void setExtraProperty(String extraProperty) {
        this.extraProperty = extraProperty;
    }
}
