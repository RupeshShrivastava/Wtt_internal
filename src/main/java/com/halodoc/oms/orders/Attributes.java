package java.com.halodoc.oms.orders;

/**
 * Created by shirisha
 * since  4/19/18.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "allocation_type",
        "check_inventory",
        "delivery_type",
        "consultation_id",
        "merchant_locations"
})
public class Attributes {
    @JsonProperty("allocation_type")
    public String allocationType;
    @JsonProperty("check_inventory")
    public Boolean checkInventory;
    @JsonProperty("delivery_type")
    public String deliveryType;
    @JsonProperty("consultation_id")
    public String consultationId;
    @JsonProperty("merchant_locations")
    public String merchantLocations;

    public void setAllocationType(String allocationType) {
        this.allocationType = allocationType;
    }

    public void setCheckInventory(Boolean checkInventory) {
        this.checkInventory = checkInventory;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public void setConsultationId(String consultationId) {
        this.consultationId = consultationId;
    }

    public void setMerchantLocations(String merchantLocations) {
        this.merchantLocations = merchantLocations;
    }

    public String getAllocationType() {
        return allocationType;
    }

    public Boolean getCheckInventory() {
        return checkInventory;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public String getConsultationId() {
        return consultationId;
    }

    public String getMerchantLocations() {
        return merchantLocations;
    }



}
