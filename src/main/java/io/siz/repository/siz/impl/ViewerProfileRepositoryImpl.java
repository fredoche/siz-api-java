package io.siz.repository.siz.impl;

import com.mongodb.WriteResult;
import io.siz.domain.siz.Event;
import io.siz.domain.siz.EventType;
import io.siz.domain.siz.ViewerProfile;
import io.siz.repository.siz.ViewerProfileRepositoryCustom;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 * @author fred
 */
public class ViewerProfileRepositoryImpl implements ViewerProfileRepositoryCustom {

    @Inject
    private MongoTemplate template;

    @Override
    public WriteResult updateFromEvent(Event e) {
        final String storyId = e.getStoryId();
        /**
         * query
         * Json.obj(
        "_id" -> Json.obj(
          "$oid" -> event.viewerProfileId
        ),
        "LikeStoryIds" -> Json.obj(
          "$not" -> Json.obj(
            "$elemMatch" -> Json.obj("$eq" -> event.storyId)
          )
        ),
        "NopeStoryIds" -> Json.obj(
          "$not" -> Json.obj(
            "$elemMatch" -> Json.obj("$eq" -> event.storyId)
          )
        )
      )
         */

        final Query query = new Query(
                where("id").is(e.getViewerProfile().getId())
                .and("LikeStoryIds").nin(storyId)
                .and("NopeStoryIds").nin(storyId)
        );

        /**
         * update
         *  val updateQuery = if (event.tags.isEmpty) {
      Json.obj(
        "$pull" -> Json.obj((if(event._type == "Nope") "Like" else "Nope") + "StoryIds" -> event.storyId),
        "$addToSet" -> Json.obj(event._type + "StoryIds" -> event.storyId)
      )
    } else {
      Json.obj(
        "$pull" -> Json.obj((if(event._type == "Nope") "Like" else "Nope") + "StoryIds" -> event.storyId),
        "$addToSet" -> Json.obj(event._type + "StoryIds" -> event.storyId),
        "$inc" -> JsObject(event.tags.map(tag => ("tagsWeights." + tag, JsNumber(event.tagsWeight))))
      )
    }
         */
        String arrayToAddTo = e.getType() + "StoryIds";
        String arrayToRemoveFrom = e.getType() == EventType.NOPE ? "LikeStoryIds" : "NopeStoryIds";
        final Update update = new Update()
                .addToSet(arrayToAddTo, storyId)
                .pull(arrayToRemoveFrom, storyId);

        Optional.ofNullable(e.getTags()).map(tags
                -> tags.stream().map(tag
                        -> update.inc("tagsWeights." + tag, e.getType().getTagsWeights())
                )
        );

        return template.updateMulti(
                query,
                update,
                ViewerProfile.class);

    }

}
