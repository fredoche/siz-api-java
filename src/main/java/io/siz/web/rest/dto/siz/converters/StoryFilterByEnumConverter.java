package io.siz.web.rest.dto.siz.converters;

import io.siz.web.rest.dto.siz.StoryFilterBy;
import java.beans.PropertyEditorSupport;

public class StoryFilterByEnumConverter extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(StoryFilterBy.valueOf(text.toUpperCase()));
    }
}
