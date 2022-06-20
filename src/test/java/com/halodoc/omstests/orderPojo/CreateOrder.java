package com.halodoc.omstests.orderPojo;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by shirisha
 * since  4/19/18.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "cart_id",
        "channel",
        "currency",
        "entity_id",
        "patient_id",
        "entity_type",
        "type",
        "items",
        "latitude",
        "longitude",
        "shipping_address",
        "city",
        "postal_code",
        "order_date",
        "attributes"
})
public class CreateOrder {
    @JsonProperty("cart_id")
    public String cartId;
    @JsonProperty("channel")
    public String channel;
    @JsonProperty("currency")
    public String currency;
    @JsonProperty("entity_id")
    public String entityId;
    @JsonProperty("patient_id")
    public String patientId;
    @JsonProperty("entity_type")
    public String entityType;
    @JsonProperty("type")
    public String type;
    @JsonProperty("items")
    public List<Item> items = null;
    @JsonProperty("latitude")
    public String latitude;
    @JsonProperty("longitude")
    public String longitude;
    @JsonProperty("shipping_address")
    public String shippingAddress;
    @JsonProperty("city")
    public Object city;
    @JsonProperty("postal_code")
    public Object postalCode;
    @JsonProperty("order_date")
    public Long orderDate;
    @JsonProperty("attributes")
    public Attributes attributes;


    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public void setPostalCode(Object postalCode) {
        this.postalCode = postalCode;
    }

    public void setOrderDate(Long orderDate) {
        this.orderDate = orderDate;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getCartId() {
        return cartId;
    }

    public String getChannel() {
        return channel;
    }

    public String getCurrency() {
        return currency;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getType() {
        return type;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public Object getCity() {
        return city;
    }

    public Object getPostalCode() {
        return postalCode;
    }

    public Long getOrderDate() {
        return orderDate;
    }

    public Attributes getAttributes() {
        return attributes;
    }



}
