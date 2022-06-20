package com.halodoc.oms.orders;

/**
 * Created by shirisha
 * since  4/19/18.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "attribute_key",
        "attribute_value",
        "language"
})
public class MerchantAttributes {
    @JsonProperty("attribute_key")
    public String attributeKey;

    public String getAttributeKey() {
        return attributeKey;
    }

    public void setAttributeKey(String attributeKey) {
        this.attributeKey = attributeKey;
    }

    public Boolean getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(Boolean attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("attribute_value")
    public Boolean attributeValue;
    @JsonProperty("language")
    public String language;




}
