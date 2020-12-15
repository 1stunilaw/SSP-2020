package ssp.marketplace.app.validation.uuid;

import javax.validation.*;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CollectionOfUiidValidator.class)
@Documented
public @interface CollectionOfUiid {
    String message() default "One of the Id's is invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
