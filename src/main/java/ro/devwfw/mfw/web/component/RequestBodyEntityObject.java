package ro.devwfw.mfw.web.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LaurentiuM
 * @version createdOn: 1/5/16
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestBodyEntityObject {
    String value() default "";

    boolean required() default true;
}
