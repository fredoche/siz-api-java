package io.siz.domain.siz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.siz.domain.AbstractAuditingEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Structure devant mapper { "_id" : ObjectId("553a39d820000016002f5ad8"),
 * "storyId" : "14298729093245d988982d6e", "tags" : [ "epic-win", "funny" ],
 * "viewerProfileId" : "553a14f1200000b8f52f5595", "date" :
 * 1429879256186.0000000000000000, "type" : "nope" }
 *
 * @author fred
 */
@Document(collection = "events")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CompoundIndexes({
    @CompoundIndex(name = "ip_1_type_1", def = " { ip: 1, type: 1 }")})
public class Event extends AbstractAuditingEntity implements Serializable {

    @Id
    private String id;

    private String storyId;
    private List<String> tags;
    /**
     * An event of a non-registered user is associated to an abstract
     * viewerprofile, which is a document-id without any actual document in the
     * viewerprofile collection.
     */
    @Field("viewerProfileId")
    @JsonIgnore
    private String viewerProfileId;

    private Date date = DateTime.now().toDate();

    private EventType type;

    private String ip;
}
