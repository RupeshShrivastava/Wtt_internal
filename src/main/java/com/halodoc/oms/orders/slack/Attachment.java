package com.halodoc.oms.orders.slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attachment {
    private String pretext;
    private String color;
    private String title;
    private String text;
    public String image_url;
    public String thumb_url;
    public String footer;
    public String footer_icon;
    private List<Fields> fields;

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class Fields {
        private String title;
        private String value;
        private boolean short_;
    }
}
