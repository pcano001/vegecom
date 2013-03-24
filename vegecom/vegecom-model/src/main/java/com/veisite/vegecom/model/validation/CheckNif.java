package com.veisite.vegecom.model.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.veisite.vegecom.model.validation.impl.CheckNifValidator;

@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CheckNifValidator.class)
@Documented
public @interface CheckNif {

    String message() default "{com.veisite.constraints.checknif}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
}
