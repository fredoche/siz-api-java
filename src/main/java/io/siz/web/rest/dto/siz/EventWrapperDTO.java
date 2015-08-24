package io.siz.web.rest.dto.siz;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.siz.domain.siz.Event;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author fred
 */
@Data
@NoArgsConstructor
public class EventWrapperDTO {

    @JsonProperty("events") // keep typo because its used in the interface.
    private Event event;

    public EventWrapperDTO(Event event) {
        this.event = event;
    }

}
