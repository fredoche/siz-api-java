package io.siz.web.rest.dto.siz;

import lombok.Data;

/**
 *
 * @author fred
 */
@Data
public class SizErrorDTO {

    public SizErrorDTO(String title) {
        this.title = title;
    }

    public SizErrorDTO(String id, String title, String detail) {
        this.title = title;
        this.id = id;
        this.detail = detail;
    }

    private String title;
    private String id;
    private String detail;

}
