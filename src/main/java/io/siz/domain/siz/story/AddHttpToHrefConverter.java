package io.siz.domain.siz.story;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 *
 * @author fred
 */
public class AddHttpToHrefConverter extends StdConverter<String, String> {

    @Override
    public String convert(String value) {
        return "http:" + value;
    }
}
