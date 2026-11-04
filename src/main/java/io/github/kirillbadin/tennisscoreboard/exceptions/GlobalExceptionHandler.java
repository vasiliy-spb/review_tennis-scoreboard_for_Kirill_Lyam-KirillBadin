package io.github.kirillbadin.tennisscoreboard.exceptions;

import io.github.kirillbadin.tennisscoreboard.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseDto> handleApi(ApiException e) {
        return ResponseEntity.status(e.getStatus()).body(new ErrorResponseDto(e.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException e){
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst() // Лучше возвращать все ошибки, а не только первую, чтобы пользователь сразу мог исправить все недочёты
                .map(FieldError::getDefaultMessage) // Возвращает только текст сообщения без имени поля — лучше что-то вроде field + ": " + message
                .orElse("Ошибка валидации");
        return ResponseEntity.badRequest().body(new ErrorResponseDto(message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllExceptions(Exception ex) {
        log.error("Необработанное исключение", ex);
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                "Произошла непредвиденная ошибка на сервере"
        );

        // Можно так: return ResponseEntity.internalServerError().body(errorResponse);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
