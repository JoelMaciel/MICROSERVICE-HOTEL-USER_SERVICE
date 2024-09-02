package com.msvc.user_service.api.exceptionhandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.msvc.user_service.domain.exceptions.BusinessException;
import com.msvc.user_service.domain.exceptions.EntityNotFoundException;
import com.msvc.user_service.domain.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String MSG_GENERIC_ERROR_END_USER = "An unexpected internal system error has occurred. " +
            "Try again and if the problem persists, contact your system administrator.";
    public static final String ENTITY_IN_USE = "Entity cannot be deleted because it is in use";

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.SYSTEM_ERROR;

        String detail = MSG_GENERIC_ERROR_END_USER;
        ex.printStackTrace();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex, WebRequest webRequest) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.RESOURCE_NOT_FOUND;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, webRequest);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusiness(BusinessException ex, WebRequest webRequest) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.BUSINESS_ERROR;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, webRequest);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleBusiness(UserAlreadyExistsException ex, WebRequest webRequest) {
        HttpStatus status = HttpStatus.CONFLICT;
        ProblemType problemType = ProblemType.INVALID_DATA;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, webRequest);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
        }

        ProblemType problemType = ProblemType.INCOMPREHENSIBLE_MESSAGE;
        String detail = "The request body is invalid. Check for syntax errors.";

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest webRequest) {
        HttpStatus status = HttpStatus.CONFLICT;
        ProblemType problemType = ProblemType.INVALID_DATA;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, webRequest);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<?> PropertyReference(PropertyReferenceException ex, WebRequest webRequest) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.INVALID_DATA;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, webRequest);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(ConstraintViolationException ex, WebRequest webRequest) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.INVALID_DATA;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatusCode status, WebRequest request) {

        if (ex instanceof MethodArgumentTypeMismatchException) {
            HttpStatus httpStatus = HttpStatus.valueOf(status.value());
            return handleMethodArgumentTypeMismatch(
                    (MethodArgumentTypeMismatchException) ex, headers, httpStatus, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }


    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ProblemType problemType = ProblemType.RESOURCE_NOT_FOUND;
        String detail = String.format("The resource %s, which you tried to access, is non-existent.", ex.getRequestURL());

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        return handleValidationInternal((Exception) ex, headers, status, request, ex.getBindingResult());
    }

    private ResponseEntity<Object> handleValidationInternal(Exception ex, HttpHeaders headers,
                                                            HttpStatusCode status, WebRequest request, BindingResult bindingResult) {
        ProblemType problemType = ProblemType.INVALID_DATA;
        String detail = "One or more fields are invalid. Fill in correctly and try again.";

        List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                    String name = objectError.getObjectName();

                    if (objectError instanceof FieldError) {
                        name = ((FieldError) objectError).getField();
                    }

                    return Problem.Object.builder()
                            .name(name)
                            .userMessage(message)
                            .build();
                })
                .toList();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .objects(problemObjects)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode status, WebRequest request) {
        if (body == null) {
            body = Problem.builder()
                    .timestamp(LocalDate.now())
                    .title(HttpStatus.valueOf(status.value()).getReasonPhrase())
                    .status(status.value())
                    .userMessage(MSG_GENERIC_ERROR_END_USER)
                    .build();
        } else if (body instanceof String) {
            body = Problem.builder()
                    .timestamp(LocalDate.now())
                    .title((String) body)
                    .status(status.value())
                    .userMessage(MSG_GENERIC_ERROR_END_USER)
                    .build();
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.INVALID_PARAMETER;

        String detail = String.format("The URL parameter '%s' was assigned the value '%s', " +
                        "which is of an invalid type. Please correct and enter a value compatible with the type %s.",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex,
                                                                HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.INCOMPREHENSIBLE_MESSAGE;
        String detail = String.format("The property '%s' received the value '%s'," +
                        "which is of an invalid type. Correct and enter a value compatible with the type %s.",
                path, ex.getValue(), ex.getTargetType().getSimpleName());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_GENERIC_ERROR_END_USER)
                .build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.INCOMPREHENSIBLE_MESSAGE;
        String detail = String.format("The property '%s' does not exist. " +
                "Please correct or remove this property and try again.", path);

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_GENERIC_ERROR_END_USER)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatusCode status,
                                                        ProblemType problemType, String detail) {
        return Problem.builder()
                .timestamp(LocalDate.now())
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail);
    }
}
