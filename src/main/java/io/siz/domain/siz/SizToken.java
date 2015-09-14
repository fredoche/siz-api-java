package io.siz.domain.siz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.siz.domain.AbstractAuditingEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * A persistent token.
 */
@Document(collection = "tokens")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class SizToken extends AbstractAuditingEntity implements Serializable, UserDetails {

    @Id
    private String id;

    private String viewerProfileId;

    @JsonIgnore
    private String userId;

    @JsonIgnore
    private String storyIdToShow;

    @Transient
    public String getHref() {
        return "href/" + id;
    }

    private final Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

    @Override
    @Transient
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /**
         * Les tokens ne donnent droit qu'au niveau d'authorité USER.
         */
        return authorities;
    }

    @Override
    @Transient
    @JsonIgnore
    public String getPassword() {
        return id;
    }

    @Override
    @Transient
    @JsonIgnore
    public String getUsername() {
        return id;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
