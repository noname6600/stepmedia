package com.thanhvh.security;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

/**
 * Default user detail
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class DefaultUserDetail implements UserDetails {
    /**
     * authorized party
     */
    @JsonProperty("azp")
    public String issuedFor;
    /**
     * Prefix role
     */
    @JsonIgnore
    @Builder.Default
    protected String prefixRole = "ROLE_";
    /**
     * Role anonymous
     */
    @JsonIgnore
    @Builder.Default
    protected String anonymous = "ROLE_ANONYMOUS";
    /**
     * Resource access
     */
    @JsonProperty("resource_access")
    protected transient Map<String, Access> resourceAccess;

    /**
     * Scope
     */
    @JsonProperty("scope")
    protected String scope;

    /**
     * Json web token id
     */
    @JsonProperty("jti")
    protected String id;
    /**
     * Expiration time
     */
    protected Long exp;
    /**
     * Not Before
     */
    protected Long nbf;
    /**
     * Issued At
     */
    protected Long iat;
    /**
     * Issuer
     */
    @JsonProperty("iss")
    protected String issuer;
    /**
     * subject
     */
    @JsonProperty("sub")
    protected String subject;
    /**
     * Type
     */
    @JsonProperty("typ")
    protected String type;
    /**
     * Value used to associate a Client session with an ID Token
     */
    @JsonProperty("nonce")
    protected String nonce;
    /**
     * Time when the authentication occurred
     */
    @JsonProperty("auth_time")
    protected Long authTime;
    /**
     * Session ID
     */
    @JsonProperty("session_state")
    @JsonAlias("sid")
    protected String sessionState;
    /**
     * Access Token hash value
     */
    @JsonProperty("at_hash")
    protected String accessTokenHash;
    /**
     * Code hash value
     */
    @JsonProperty("c_hash")
    protected String codeHash;
    /**
     * Full name
     */
    @JsonProperty("name")
    protected String name;
    /**
     * Given name(s) or first name(s)
     */
    @JsonProperty("given_name")
    protected String givenName;
    /**
     * Surname(s) or last name(s)
     */
    @JsonProperty("family_name")
    protected String familyName;
    /**
     * Middle name(s)
     */
    @JsonProperty("middle_name")
    protected String middleName;
    /**
     * Casual name
     */
    @JsonProperty("nickname")
    protected String nickName;
    /**
     * Shorthand name by which the End-User wishes to be referred to
     */
    @JsonProperty("preferred_username")
    protected String preferredUsername;
    /**
     * Profile page URL
     */
    @JsonProperty("profile")
    protected String profile;
    /**
     * Profile picture URL
     */
    @JsonProperty("picture")
    protected String picture;
    /**
     * Web page or blog URL
     */
    @JsonProperty("website")
    protected String website;
    /**
     * Preferred e-mail address
     */
    @JsonProperty("email")
    protected String email;
    /**
     * True if the e-mail address has been verified; otherwise false
     */
    @JsonProperty("email_verified")
    protected Boolean emailVerified;
    /**
     * Gender
     */
    @JsonProperty("gender")
    protected String gender;
    /**
     * Birthday
     */
    @JsonProperty("birthdate")
    protected String birthdate;
    /**
     * Time zone
     */
    @JsonProperty("zoneinfo")
    protected String zoneInfo;
    /**
     * Locale
     */
    @JsonProperty("locale")
    protected String locale;
    /**
     * Preferred telephone number
     */
    @JsonProperty("phone_number")
    protected String phoneNumber;
    /**
     * True if the phone number has been verified; otherwise false
     */
    @JsonProperty("phone_number_verified")
    protected Boolean phoneNumberVerified;
    /**
     * Preferred postal address
     */
    @JsonProperty("address")
    protected AddressClaimSet address;
    /**
     * Time the information was last updated
     */
    @JsonProperty("updated_at")
    protected Long updatedAt;
    /**
     * claims_locales
     */
    @JsonProperty("claims_locales")
    protected String claimsLocales;
    /**
     * Authentication Context Class Reference
     */
    @JsonProperty("acr")
    protected String acr;
    /**
     * State hash
     */
    @JsonProperty("s_hash")
    protected String stateHash;
    /**
     * Realm access
     */
    @JsonProperty("realm_access")
    protected Access realmAccess;
    /**
     * Claims
     */
    protected transient Map<String, Object> claims;

    /**
     * Get resource access
     *
     * @return {@link #resourceAccess}
     */
    public Map<String, Access> getResourceAccess() {
        return new HashMap<>(resourceAccess);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (Objects.nonNull(this.realmAccess)) {
            for (String role : realmAccess.getRoles()) {
                authorities.add(
                        DefaultGrantedAuthority
                                .builder()
                                .role(getPrefixRole().concat(role.toUpperCase()))
                                .build()
                );
            }
        }

        if (Objects.nonNull(this.resourceAccess)) {
            for (Map.Entry<String, Access> resource : resourceAccess.entrySet()) {
                for (String role : resource.getValue().getRoles()) {
                    authorities.add(DefaultGrantedAuthority
                            .builder()
                            .role(getPrefixRole().concat(role.toUpperCase()))
                            .build()
                    );
                }
            }
        }
        if (authorities.isEmpty()) {
            DefaultGrantedAuthority gameGrantedAuthority = DefaultGrantedAuthority
                    .builder()
                    .role(getAnonymous())
                    .build();
            authorities = Collections.singletonList(gameGrantedAuthority);
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return this.preferredUsername;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Get claims
     *
     * @return {@link #claims}
     */
    @JsonAnyGetter
    public Map<String, Object> getClaims() {
        return new HashMap<>(claims);
    }

    /**
     * Set prefix role
     *
     * @param prefix prefix role
     */
    void setPrefixRole(String prefix) {
        this.prefixRole = prefix;
    }

    /**
     * @return {@link #anonymous}
     */
    public String getAnonymous() {
        return anonymous;
    }

    /**
     * Set anonymous role
     *
     * @param anonymous anonymous
     */
    void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    /**
     * Access
     */
    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Access implements Serializable {
        /**
         * Roles
         */
        @JsonProperty("roles")
        protected transient Set<String> roles;
        /**
         * Verify caller
         */
        @JsonProperty("verify_caller")
        protected Boolean verifyCaller;

        /**
         * Get roles
         *
         * @return {@link #roles}
         */
        public Set<String> getRoles() {
            return roles;
        }

        /**
         * Set roles
         *
         * @param roles roles
         * @return {@link Access}
         */
        public Access roles(Set<String> roles) {
            this.roles = roles;
            return this;
        }

        /**
         * @param role role
         * @return True if user in role, else false
         */
        @JsonIgnore
        public boolean isUserInRole(String role) {
            if (roles == null) return false;
            return roles.contains(role);
        }

        /**
         * Add role
         *
         * @param role role
         * @return {@link Access}
         */
        public Access addRole(String role) {
            if (roles == null) roles = new HashSet<>();
            roles.add(role);
            return this;
        }

        /**
         * getVerifyCaller
         *
         * @return {@link #verifyCaller}
         */
        public Boolean getVerifyCaller() {
            return verifyCaller;
        }

        /**
         * verifyCaller
         *
         * @param required required
         * @return {@link Access}
         */
        public Access verifyCaller(Boolean required) {
            this.verifyCaller = required;
            return this;
        }
    }

    /**
     * AddressClaimSet
     */
    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class AddressClaimSet implements Serializable {
        /**
         * Formatted address
         */
        @JsonProperty("formatted")
        protected String formattedAddress;
        /**
         * Street address
         */
        @JsonProperty("street_address")
        protected String streetAddress;
        /**
         * locality
         */
        @JsonProperty("locality")
        protected String locality;
        /**
         * region
         */
        @JsonProperty("region")
        protected String region;
        /**
         * postal code
         */
        @JsonProperty("postal_code")
        protected String postalCode;
        /**
         * country
         */
        @JsonProperty("country")
        protected String country;
    }
}
