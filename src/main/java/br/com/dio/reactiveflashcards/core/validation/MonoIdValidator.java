package br.com.dio.reactiveflashcards.core.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class MonoIdValidator implements ConstraintValidator<MongoId, String> {

    @Override
    public void initialize(final MongoId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        log.info("==== checking if {} is a valid mongoDB id", value);
        return StringUtils.isNotBlank(value) && ObjectId.isValid(value);
    }
}
