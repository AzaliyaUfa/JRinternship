package com.game.service;

import com.game.PlayerDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Component
public class PlayerValidation implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PlayerDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        PlayerDTO playerDTO = (PlayerDTO) target;

        if (playerDTO.getName() == null ||
                playerDTO.getTitle() == null ||
                playerDTO.getRace() == null ||
                playerDTO.getProfession() == null ||
                playerDTO.getBirthday() == null ||
                playerDTO.getExperience() == null ||
                playerDTO.getName().isEmpty() ||
                playerDTO.getTitle().isEmpty()) {
            errors.reject("Указаны не все необходимые параметры.");
        }
        if (playerDTO.getName() != null && playerDTO.getName().length() > 12) {
            errors.reject("Имя персонажа должно быть до 12 знаков включительно.");
        }
        if (playerDTO.getTitle() != null && playerDTO.getTitle().length() > 30) {
            errors.reject("Титул персонажа должен быть до 30 знаков включительно.");
        }
        if (playerDTO.getExperience() != null && (playerDTO.getExperience() < 0 ||
                playerDTO.getExperience() > 10_000_000)) {
            errors.reject("Опыт персонажа выходит за границы допустимого.");
        }
        if (playerDTO.getBirthday() != null &&
                (playerDTO.getBirthday() < 0 ||
                 playerDTO.getBirthday() < (new GregorianCalendar(2000, Calendar.JANUARY, 1).getTimeInMillis()) ||
                playerDTO.getBirthday()> (new GregorianCalendar(3000, Calendar.DECEMBER, 31).getTimeInMillis()))) {
            errors.reject("Дата регистрации выходит за границы допустимого.");
        }
    }
}
