package com.thanhvh.audit;

import com.thanhvh.logging.LogContext;
import com.thanhvh.logging.LogType;
import com.thanhvh.security.DefaultUserDetail;
import com.thanhvh.util.constant.Constants;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

/**
 * Default auditor aware
 */
@Slf4j
public class DefaultAuditorAware implements AuditorAware<UUID> {

    @Override
    @NonNull
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof DefaultUserDetail defaultUserDetail) {
            return Optional.ofNullable(defaultUserDetail.getSubject() != null ? UUID.fromString(defaultUserDetail.getSubject()) : null);
        }
        String userId = null;
        try {
            userId = MDC.get(Constants.USERID);
            if (userId != null && !userId.isBlank() && !userId.equals(Constants.SYSTEM)) {
                return Optional.of(UUID.fromString(userId));
            }
        } catch (Exception e) {
            LogContext.push(LogType.TRACING, "Error when get Auditor: UserId " + userId + " from MDC " + e.getClass().getSimpleName() + " " + e.getMessage());
        }
        return Optional.empty();
    }

}
