package org.glytoucan.web.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String>{
	
	private Pattern pattern;
	private Matcher matcher;

	/**
	 * pattern: min 5, at least 1 lowercase, 1 uppercase letter, 1 numeric and 1 special character
	 */
	public static final String PASSWORD_PATTERN = 	
		"(?=^.{5,100}$)(?=.*\\d)(?=.*[!@#$%^&*]+)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";

	@Override
	public void initialize(Password arg0) {
		pattern = Pattern.compile(PASSWORD_PATTERN);
	}

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		matcher = pattern.matcher(password);
		return matcher.matches();
	}
}
