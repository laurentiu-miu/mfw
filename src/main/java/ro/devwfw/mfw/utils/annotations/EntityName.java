package ro.devwfw.mfw.utils.annotations;

/**
 * @author LaurentiuM
 * @version createdOn: 12/27/15
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to map the name of the entity withe the class of the entity.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityName {
    String value() default "";
}
