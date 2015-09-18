package io.siz.domain.siz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.siz.domain.AbstractAuditingEntity;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Email;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author fred
 */
@Document(collection = "users")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * Modèle servant uniquement à se logger. Tout le reste est associé au siztoken.
 */
public class SizUser extends AbstractAuditingEntity implements Serializable {

    @Id
    private String id;

    @Indexed(unique = true, sparse = true)
    @Email
    @Size(min = 5, max = 100)
    private String email;

    @Indexed(unique = true, sparse = true)
    private String username;

    @Transient
    public String getHref() {
        return "/users/" + id;
    }

    @JsonIgnore
    private String passwordHash;

    @JsonIgnore
    private String facebookToken;

    @JsonIgnore
    @Indexed(unique = true, sparse = true)
    private String facebookUserId;

    @JsonIgnore
    private Date creationDate = DateTime.now().toDate();

    @JsonIgnore
    private String state;
}
