package io.siz.domain.siz;

import io.siz.domain.siz.story.Loop;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.siz.domain.AbstractAuditingEntity;
import io.siz.domain.siz.story.Box;
import io.siz.domain.siz.story.Picture;
import io.siz.domain.siz.story.Source;
import io.siz.domain.util.ObjectIdSerializer;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author fred
 */
@Document(collection = "stories")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class Story extends AbstractAuditingEntity implements Serializable {

    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    private Source source;
    private List<Box> boxes;
    private Picture picture;
    private String title;
    @Indexed(direction = IndexDirection.DESCENDING)
    private Date creationDate;
    @Indexed(name = "slugUniqueIndex")
    private String slug;
    private List<String> tags;
    private String privacy;
    private Loop loop;

    @Transient
    public String getHref() {
        return "/stories/" + id;
    }
}
