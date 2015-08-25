package io.siz.domain.siz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.siz.service.util.EnumUtil;

/**
 * 3 types d'event pour le moment.
 */
public enum EventType {

    LIKE("like", 1), NOPE("nope", -1), ANONYMOUS_VIEW("anonymous-view", 0);
    private final String toValue;
    private final Integer tagsWeights;

    EventType(String toValue, Integer tagWeight) {
        this.toValue = toValue;
        this.tagsWeights = tagWeight;
    }

    /**
     * permet de mapper sans contrainte de casse ou de caracteres
     *
     * @param value
     * @return
     */
    @JsonCreator
    public static EventType forValue(String value) {
        return EnumUtil.valueOf(EventType.class, value);
    }

    @JsonValue
    public String toValue() {
        return toValue;
    }

    public Integer getTagsWeights() {
        return tagsWeights;
    }
}
