package io.siz.domain.siz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.siz.domain.AbstractAuditingEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import org.springframework.data.annotation.Transient;

/**
 * A persistent token.
 */
@Document(collection = "tokens")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SizToken extends AbstractAuditingEntity implements Serializable {

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getViewerProfileId() {
        return viewerProfileId;
    }

    public void setViewerProfileId(String viewerProfileId) {
        this.viewerProfileId = viewerProfileId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoryIdToShow() {
        return storyIdToShow;
    }

    public void setStoryIdToShow(String storyIdToShow) {
        this.storyIdToShow = storyIdToShow;
    }
}
