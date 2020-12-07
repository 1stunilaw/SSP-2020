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
        String regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";
        return collection.stream().allMatch(x -> {
            log.info("Validation, TagId:" + x + " " + Pattern.matches(regexp, x));
            return Pattern.matches(regexp, x);
        });
    }
}
