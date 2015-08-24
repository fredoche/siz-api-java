package io.siz.domain.siz;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.siz.domain.AbstractAuditingEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Structure devant mapper { "_id" : ObjectId("553a39d820000016002f5ad8"),
 * "storyId" : "14298729093245d988982d6e", "tags" : [ "epic-win", "funny" ],
 * "viewerProfileId" : "553a14f1200000b8f52f5595", "date" :
 * 1429879256186.0000000000000000, "type" : "nope" }
 *
 * @author fred
 */
@Document(collection = "tokens")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CompoundIndexes({
    @CompoundIndex(name = "ip_1_date_-1", def = " { ip: 1, date: -1 }")})
public class Event extends AbstractAuditingEntity implements Serializable {

    @Id
    private String id;

    private String storyId;
    private List<String> tags;
    private String viewerProfileId;
    private Date date;

    @Pattern(regexp = "like|nope|anonymous-view", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String type;

}
