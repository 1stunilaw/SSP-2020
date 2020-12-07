package ssp.marketplace.app.validation.uuid;

import lombok.extern.slf4j.Slf4j;

import javax.validation.*;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
public class CollectionOfUiidValidator implements ConstraintValidator<CollectionOfUiid, Collection<String>> {
    @Override
    public boolean isValid(Collection<String> collection, ConstraintValidatorContext context) {
        if (collection == null){
            return true;
        }
        String regexp = "([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})";
        return collection.stream().allMatch(x -> {
            return Pattern.matches(regexp, x);
        });
    }
}
