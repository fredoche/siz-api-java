package io.siz.domain.siz;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.siz.domain.AbstractAuditingEntity;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Structure correspondant au document auto-incrémental dans mongodb. On s'en sert pour générer des slugs avec un nombre
 * unique.
 *
 * @author fred
 */
@Document(collection = "counters")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Counter extends AbstractAuditingEntity implements Serializable {

    @Id
    private String id;

    private Integer seq;

}


/*
 public class SizUser extends AbstractAuditingEntity implements Serializable {

 /**
 * égal et positionné au viewerprofile id.
 *
 @Id
 private String id;*/
