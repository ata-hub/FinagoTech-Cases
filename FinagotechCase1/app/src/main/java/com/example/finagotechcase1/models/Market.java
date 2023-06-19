package com.example.finagotechcase1.models;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
public class Market {
    @SerializedName("baseAssetId")
    private String baseAssetId;
    @SerializedName("id")
    private String id;
    @SerializedName("quoteAssetId")
    private String quoteId;

    public Market() {

    }

    public String getBaseAssetId() {
        return baseAssetId;
    }

    public void setBaseAssetId(String baseAssetId) {
        this.baseAssetId = baseAssetId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    public static Market fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Market.class);
    }
}
