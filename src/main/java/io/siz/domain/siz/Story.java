package io.siz.domain.siz;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.siz.domain.AbstractAuditingEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
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

    @Data
    private static class Format {

        private String href;
        private String type;
    }

    @Data
    private class Source {

        private Integer duration;
        private String type;
        private String id;

    }

    @Data
    private class Picture {

        private String href;

    }

    @Data
    private class Box {

        private Integer width;
        private Integer height;
        private List<Format> formats;

    }

}
