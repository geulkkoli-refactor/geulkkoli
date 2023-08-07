package com.geulkkoli.application.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

/**
 * ControllerAdvice 의 Rest API 버전
 * AOP 를 이용한 예외처리 방식
 * 모든 RestController 에서 이 클래스에 선언된 예외가 발생했다면 이 클래스에서 처리한다.
 * try-catch 를 대체할 수 있으며 그로 인해 코드 가독성이 좋아지는 이점이 있다.
 *
 * RestController 에서 ajax 로 validation 처리를 위해 작성하게 되었다.
 * validation 을 통과하지 못하면 errors.properties 의 String 을 반환하고
 * 통과한다면 Controller 에 선언된 값을 반환한다.
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class AjaxBadResponseHandler {

    MessageSource messageSource;

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<List<String>> validException(MethodArgumentNotValidException methodArgumentNotValidException) {

        List<String> validMessage = new ArrayList<>();

        for (ObjectError error : methodArgumentNotValidException.getBindingResult().getAllErrors()) {
            try {
                validMessage.add(messageSource.getMessage(Objects.requireNonNull(error.getCodes())[0], error.getArguments(), LocaleContextHolder.getLocale()));
            } catch (Exception e) {
                validMessage.add(error.getDefaultMessage());
            }
        }

        return new ResponseEntity<>(
                validMessage,
                HttpStatus.BAD_REQUEST);
    }
}
