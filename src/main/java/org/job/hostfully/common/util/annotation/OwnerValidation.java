package org.job.hostfully.common.util.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.job.hostfully.common.util.validator.OwnerValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = OwnerValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OwnerValidation {
    String message() default "Owner information must be provided";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}