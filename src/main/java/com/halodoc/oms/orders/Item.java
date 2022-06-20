package java.com.halodoc.oms.orders;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by shirisha
 * since  4/19/18.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "currency",
        "description",
        "listing_id",
        "name",
        "requested_price",
        "requested_quantity"
})
public class Item {
    @JsonProperty("currency")
    public String currency;
    @JsonProperty("description")
    public String description;
    @JsonProperty("listing_id")
    public String listingId;
    @JsonProperty("name")
    public String name;
    @JsonProperty("requested_price")
    public String requestedPrice;
    @JsonProperty("requested_quantity")
    public String requestedQuantity;


    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRequestedPrice(String requestedPrice) {
        this.requestedPrice = requestedPrice;
    }

    public void setRequestedQuantity(String requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getListingId() {
        return listingId;
    }

    public String getName() {
        return name;
    }

    public String getRequestedPrice() {
        return requestedPrice;
    }

    public String getRequestedQuantity() {
        return requestedQuantity;
    }




}
