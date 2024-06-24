package org.job.hostfully.common.util.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    private String getMessage(String code) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, null, code, currentLocale);
    }

    private String getMessage(Exception ex) {
        return getMessage(ex.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponseDTO handleNotFoundException(NotFoundException ex) {
        log.error("NotFoundException: {}", ex.getMessage());
        return new ErrorResponseDTO(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), getMessage(ex));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(OverlapException.class)
    public ErrorResponseDTO handleOverlapException(OverlapException ex) {
        log.error("OverlapException: {}", ex.getMessage());
        return new ErrorResponseDTO(LocalDateTime.now(), HttpStatus.CONFLICT.value(), getMessage(ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("MethodArgumentNotValidException: {}", ex.getMessage());
        List<ErrorResponseDTO> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String localizedErrorMessage = getMessage(Objects.requireNonNull(error.getDefaultMessage()));
                    return new ErrorResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), localizedErrorMessage);
                })
                .toList();
        ProblemDetail problemDetail = ex.getBody();
        problemDetail.setProperty("validation-errors", errors);
        return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public List<ErrorResponseDTO> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("ConstraintViolationException: {}", ex.getMessage());
        return ex.getConstraintViolations().stream()
                .map(violation -> {
                    String localizedMessage = getMessage(violation.getMessage());
                    return new ErrorResponseDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), localizedMessage);
                })
                .collect(Collectors.toList());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponseDTO handleGenericException(Exception ex) {
        log.error("Exception: {}", ex.getMessage(), ex);
        return new ErrorResponseDTO(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), getMessage(ex));
    }
}
