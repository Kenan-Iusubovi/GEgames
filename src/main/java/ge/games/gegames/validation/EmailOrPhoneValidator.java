package ge.games.gegames.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailOrPhoneValidator implements ConstraintValidator<EmailOrPhone, String> {

    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    private static final String PHONE_REGEX = "^\\+[1-9]\\d{1,14}$\n";


    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isBlank()){
            return false;
        }
        return s.matches(EMAIL_REGEX) || s.matches(PHONE_REGEX);
    }
}
