package io.siz.domain.siz;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.siz.domain.AbstractAuditingEntity;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Mappe un document de ce type: { "_id" : ObjectId("54bd23101900001900f0afdc"),
 * "likeStoryIds" : [ "1433337162644db76e0cb3b2", ... "1439200351278ce801f45a83"
 * ], "tagsWeights" : { "meaningful-videos" : 8.0000000000000000, ...
 * "best-vines" : 1 }, "nopeStoryIds" : [ "1433493250531cf3980c8a0d", ...
 * "14392036071591ea935cd868" ] }
 *
 * @author fred
 */
@Document(collection = "viewerprofiles")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViewerProfile extends AbstractAuditingEntity implements Serializable {

    @Id
    private String id;

    @Field(value = "likeStoryIds")
    private List<String> likedStories;

    @Field(value = "nopeStoryIds")
    private List<String> nopeStoryIds;

    private Map<String, Integer> tagsWeights;

}
