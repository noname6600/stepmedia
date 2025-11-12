package com.thanhvh.rest.exception;

import com.thanhvh.exception.InvalidException;
import com.thanhvh.exception.RestException;
import com.thanhvh.rest.factory.response.IResponseFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Default handler exception
 */
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Response factory
     */
    protected final IResponseFactory iResponseFactory;

    /**
     * GlobalExceptionHandler
     *
     * @param iResponseFactory {@link IResponseFactory}
     */
    protected GlobalExceptionHandler(IResponseFactory iResponseFactory) {
        this.iResponseFactory = iResponseFactory;
    }


    /**
     * handleMethodArgumentMisMatchException
     *
     * @param ex      {@link MethodArgumentTypeMismatchException}
     * @param request the request
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentMisMatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        ProblemDetail body = createProblemDetail(ex, HttpStatus.BAD_REQUEST, "Failed to read request", null, null, request);
        return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Bad request
     *
     * @param e       {@link RestException.BadRequest}
     * @param request the request
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(RestException.BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> badRequestRestExceptionHandler(RestException.BadRequest e, WebRequest request) {
        return customRestExceptionHandler(e, request);
    }

    /**
     * Not found
     *
     * @param e       {@link RestException.NotFound}
     * @param request the request
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(RestException.NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> notFoundRestExceptionHandler(RestException.NotFound e, WebRequest request) {
        return customRestExceptionHandler(e, request);
    }

    /**
     * CONFLICT
     *
     * @param e       {@link RestException.Conflict}
     * @param request the request
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(RestException.Conflict.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> conflictRestExceptionHandler(RestException.Conflict e, WebRequest request) {
        return customRestExceptionHandler(e, request);
    }


    /**
     * customRestExceptionHandler
     *
     * @param ex      {@link RestException}
     * @param request the request
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(RestException.class)
    public ResponseEntity<Object> customRestExceptionHandler(RestException ex, WebRequest request) {
        return createResponseEntity(ex, request);
    }

    /**
     * customInvalidExceptionHandler
     *
     * @param ex      {@link InvalidException}
     * @param request the request
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<Object> customInvalidExceptionHandler(InvalidException ex, WebRequest request) {
        return createResponseEntity(RestException.create(ex), request);
    }

    /**
     * Create response entity response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @return the response entity
     */
    protected ResponseEntity<Object> createResponseEntity(
            RestException ex,
            WebRequest request
    ) {
        log.warn("{} {}", ex.getErrorCode().getClass().getSimpleName(), ex.getErrorCode().name());
        HttpHeaders headers = new HttpHeaders();
        return handleExceptionInternal(ex, null, headers, ex.getStatusCode(), request);
    }
}
