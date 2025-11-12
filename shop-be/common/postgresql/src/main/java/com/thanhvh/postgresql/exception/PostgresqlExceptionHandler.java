package com.thanhvh.postgresql.exception;

import com.thanhvh.exception.ErrorCode;
import com.thanhvh.exception.IErrorCode;
import com.thanhvh.rest.exception.GlobalExceptionHandler;
import com.thanhvh.rest.factory.response.IResponseFactory;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Postgresql Handler Exception
 */
@Slf4j
public class PostgresqlExceptionHandler extends GlobalExceptionHandler {

    /**
     * Map Postgresql State Codes
     */
    protected static Map<String, IErrorCode> psqlStateCodes;

    static {
        psqlStateCodes = new ConcurrentHashMap<>();
        psqlStateCodes.put(PSQLState.FOREIGN_KEY_VIOLATION.getState(), PostgresqlErrorCode.FOREIGN_KEY_VIOLATION);
        psqlStateCodes.put(PSQLState.BAD_DATETIME_FORMAT.getState(), PostgresqlErrorCode.BAD_DATETIME_FORMAT);
        psqlStateCodes.put(PSQLState.NOT_NULL_VIOLATION.getState(), PostgresqlErrorCode.NOT_NULL_VIOLATION);
        psqlStateCodes.put(PSQLState.UNIQUE_VIOLATION.getState(), PostgresqlErrorCode.UNIQUE_VIOLATION);
        psqlStateCodes.put(PSQLState.DATATYPE_MISMATCH.getState(), PostgresqlErrorCode.DATATYPE_MISMATCH);
        psqlStateCodes.put(PSQLState.INVALID_PARAMETER_VALUE.getState(), PostgresqlErrorCode.INVALID_PARAMETER_VALUE);
    }

    /**
     * PostgresqlException
     *
     * @param iResponseFactory {@link IResponseFactory}
     */
    public PostgresqlExceptionHandler(IResponseFactory iResponseFactory) {
        super(iResponseFactory);
    }

    private static IErrorCode getStateCode(PSQLException e) {
        IErrorCode errorCode = psqlStateCodes.get(e.getSQLState());
        return Objects.requireNonNullElse(errorCode, ErrorCode.SERVER_ERROR);
    }

    /**
     * Handle PSQLException
     *
     * @param ex      PSQLException exception
     * @param request the request
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<Object> handleQSQLException(PSQLException ex, WebRequest request) {
        log.error(ex.getMessage());
        IErrorCode errorCode = getStateCode(ex);
        HttpHeaders headers = new HttpHeaders();
        ProblemDetail body = createProblemDetail(
                ex,
                errorCode.getStatusMapping().getHttpStatus(),
                ex.getMessage(),
                errorCode.getDetailMessageCode(),
                null
                , request
        );
        return handleExceptionInternal(ex, body, headers, errorCode.getStatusMapping().getHttpStatus(), request);
    }
}
