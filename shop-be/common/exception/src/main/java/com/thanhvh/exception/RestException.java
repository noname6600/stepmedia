package com.thanhvh.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;

/**
 * Extent RuntimeException and CustomException
 * <br>
 * Often using in last step REST
 */
public class RestException extends ErrorResponseException implements CustomException, ErrorResponse {

    /**
     * Error code
     */
    private final transient IErrorCode errorCode;

    /**
     * @param errorCode              error code
     * @param messageDetailArguments an array of arguments that will
     *                               be filled in for params within
     *                               the message (params look like "{0}", "{1,date}",
     *                               "{2,time}" within a message),
     *                               or null if none
     * @param cause                  cause
     */
    private RestException(IErrorCode errorCode, @Nullable Object[] messageDetailArguments, Throwable cause) {
        super(
                errorCode.getStatusMapping().getHttpStatus(),
                ProblemDetail.forStatus(errorCode.getCode()),
                cause,
                errorCode.getDetailMessageCode(),
                messageDetailArguments
        );
        this.errorCode = errorCode;
    }

    /**
     * @param errorCode implement of IErrorCode
     * @return RestException
     */
    public static RestException create(IErrorCode errorCode) {
        return create(errorCode, null, null);
    }

    /**
     * Create
     *
     * @param customException implement of CustomException
     * @return RestException
     */
    public static RestException create(CustomException customException) {
        return create(
                customException.getErrorCode(),
                customException.getDetailMessageArguments(),
                customException.getCause() != null ?
                        customException.getCause() :
                        (Exception) customException
        );
    }

    /**
     * @param errorCode implement of IErrorCode
     * @param cause     cause
     * @return RestException
     */
    public static RestException create(IErrorCode errorCode, Throwable cause) {
        return create(errorCode, null, cause);
    }

    /**
     * @param errorCode              error code
     * @param messageDetailArguments an array of arguments that will
     *                               be filled in for params within
     *                               the message (params look like "{0}", "{1,date}",
     *                               "{2,time}" within a message),
     *                               or null if none
     * @return RestException
     */
    public static RestException create(IErrorCode errorCode, @Nullable Object[] messageDetailArguments) {
        return create(errorCode, messageDetailArguments, null);
    }

    /**
     * Create RestException
     *
     * @param errorCode              Error Code
     * @param messageDetailArguments an array of arguments that will
     *                               be filled in for params within
     *                               the message (params look like "{0}", "{1,date}",
     *                               "{2,time}" within a message),
     *                               or null if none
     * @param cause                  cause
     * @return RestException
     */
    public static RestException create(IErrorCode errorCode, @Nullable Object[] messageDetailArguments, Throwable cause) {
        return switch (errorCode.getStatusMapping().getHttpStatus()) {
            case BAD_REQUEST -> new BadRequest(errorCode, messageDetailArguments, cause);
            case UNAUTHORIZED -> new Unauthorized(errorCode, messageDetailArguments, cause);
            case FORBIDDEN -> new Forbidden(errorCode, messageDetailArguments, cause);
            case NOT_FOUND -> new NotFound(errorCode, messageDetailArguments, cause);
            case METHOD_NOT_ALLOWED -> new MethodNotAllowed(errorCode, messageDetailArguments, cause);
            case NOT_ACCEPTABLE -> new NotAcceptable(errorCode, messageDetailArguments, cause);
            case CONFLICT -> new Conflict(errorCode, messageDetailArguments, cause);
            case GONE -> new Gone(errorCode, messageDetailArguments, cause);
            case UNSUPPORTED_MEDIA_TYPE -> new UnsupportedMediaType(errorCode, messageDetailArguments, cause);
            case TOO_MANY_REQUESTS -> new TooManyRequests(errorCode, messageDetailArguments, cause);
            case UNPROCESSABLE_ENTITY -> new UnProcessableEntity(errorCode, messageDetailArguments, cause);
            case INTERNAL_SERVER_ERROR -> new InternalServerError(errorCode, messageDetailArguments, cause);
            default -> RestException.create(errorCode, messageDetailArguments, cause);
        };
    }


    @Override
    public IErrorCode getErrorCode() {
        return errorCode;
    }


    @Override
    @NonNull
    public String getTitleMessageCode() {
        return errorCode.getTitleMessageCode();
    }

    @Override
    @NonNull
    public String getDetailMessageCode() {
        return errorCode.getDetailMessageCode();
    }

    @Override
    public Object[] getDetailMessageArguments() {
        return super.getDetailMessageArguments();
    }

    /**
     * Bad request
     */
    public static class BadRequest extends RestException {
        private BadRequest(IErrorCode errorCode, @Nullable Object[] messageDetailArguments, Throwable cause) {
            super(errorCode, messageDetailArguments, cause);
        }
    }

    /**
     * Unauthorized
     */
    public static class Unauthorized extends RestException {
        private Unauthorized(IErrorCode errorCode, @Nullable Object[] messageDetailArguments, Throwable cause) {
            super(errorCode, messageDetailArguments, cause);
        }
    }

    /**
     * Forbidden
     */
    public static class Forbidden extends RestException {
        private Forbidden(IErrorCode errorCode, @Nullable Object[] messageDetailArguments, Throwable cause) {
            super(errorCode, messageDetailArguments, cause);
        }
    }

    /**
     * NotFound
     */
    public static class NotFound extends RestException {
        private NotFound(IErrorCode errorCode, @Nullable Object[] messageDetailArguments, Throwable cause) {
            super(errorCode, messageDetailArguments, cause);
        }
    }

    /**
     * MethodNotAllowed
     */
    public static class MethodNotAllowed extends RestException {
        private MethodNotAllowed(IErrorCode errorCode, @Nullable Object[] messageDetailArguments, Throwable cause) {
            super(errorCode, messageDetailArguments, cause);
        }
    }

    /**
     * NotAcceptable
     */
    public static class NotAcceptable extends RestException {
        private NotAcceptable(IErrorCode errorCode, @Nullable Object[] messageDetailArguments, Throwable cause) {
            super(errorCode, messageDetailArguments, cause);
        }
    }

    /**
     * Conflict
     */
    public static class Conflict extends RestException {
        private Conflict(IErrorCode errorCode, @Nullable Object[] messageDetailArguments, Throwable cause) {
            super(errorCode, messageDetailArguments, cause);
        }
    }

    /**
     * InternalServerError
     */
    public static class InternalServerError extends RestException {
        private InternalServerError(IErrorCode errorCode, @Nullable Object[] messageDetailArguments, Throwable cause) {
            super(errorCode, messageDetailArguments, cause);
        }
    }

    /**
     * Gone
     */
    public static class Gone extends RestException {
        private Gone(IErrorCode errorCode, @Nullable Object[] messageDetailArguments, Throwable cause) {
            super(errorCode, messageDetailArguments, cause);
        }
    }

    /**
     * UnsupportedMediaType
     */
    public static class UnsupportedMediaType extends RestException {
        private UnsupportedMediaType(IErrorCode errorCode, @Nullable Object[] messageDetailArguments, Throwable cause) {
            super(errorCode, messageDetailArguments, cause);
        }
    }

    /**
     * UnProcessableEntity
     */
    public static class UnProcessableEntity extends RestException {
        private UnProcessableEntity(IErrorCode errorCode, @Nullable Object[] messageDetailArguments, Throwable cause) {
            super(errorCode, messageDetailArguments, cause);
        }
    }

    /**
     * TooManyRequests
     */
    public static class TooManyRequests extends RestException {
        private TooManyRequests(IErrorCode errorCode, @Nullable Object[] messageDetailArguments, Throwable cause) {
            super(errorCode, messageDetailArguments, cause);
        }
    }
}
