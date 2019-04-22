package de.tarent.challenge.store.validator;

import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class NotEmptyElementsValidator implements ConstraintValidator<NotEmptyElements, Set<String>> {

    @Override
    public void initialize(NotEmptyElements notEmptyFields) {
    }

    @Override
    public boolean isValid(Set<String> objects, ConstraintValidatorContext context) {
        return objects.stream().allMatch(nef -> nef != null && !nef.trim().isEmpty());
    }

}

