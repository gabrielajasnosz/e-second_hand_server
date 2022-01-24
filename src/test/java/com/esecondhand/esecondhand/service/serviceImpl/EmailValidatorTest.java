package com.esecondhand.esecondhand.service.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class EmailValidatorTest {

    @InjectMocks
    private EmailValidator emailValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @ParameterizedTest
    @MethodSource("validatorParameters")
    public void shouldValidateMail(String email, Boolean expectedValidation) {
        Boolean result = emailValidator.isValid(email, any(ConstraintValidatorContext.class));
        assertEquals(result, expectedValidation);
    }

    private static Stream<Arguments> validatorParameters() {
        return Stream.of(
                Arguments.of("januszkowalski@gmail.com", true),
                Arguments.of("janusz.kowalski@gmail.com", true),
                Arguments.of("januszkowalski.gmail.com", false),
                Arguments.of("janusz.kowalski@gmail", false),
                Arguments.of("123", false),
                Arguments.of("", false)
        );
    }
}
