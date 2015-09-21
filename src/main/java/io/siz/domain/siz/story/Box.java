package io.siz.domain.siz.story;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.siz.domain.siz.VideoFormat;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Box {

    private Integer width;
    private Integer height;
    private Integer start;
    private Integer stop;
    private List<VideoFormat> formats;

}
