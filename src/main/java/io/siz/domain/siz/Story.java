package io.siz.domain.siz;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.siz.domain.AbstractAuditingEntity;
import io.siz.domain.siz.story.Box;
import io.siz.domain.siz.story.Picture;
import io.siz.domain.siz.story.Source;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
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
    private String id;
    private Source source;
    private List<Box> boxes;
    private Picture picture;
    private String title;
    private Date creationDate;
    private String slug;
    private List<String> tags;
    private String privacy;

    @Transient
    public String getHref() {
        return "/stories/" + id;
    }
}
