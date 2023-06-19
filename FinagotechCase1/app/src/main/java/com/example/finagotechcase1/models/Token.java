package com.example.finagotechcase1.models;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
public class Token {
    @SerializedName("access_token")
    private String access_token;
    @SerializedName("expires_in")
    private int expires_in;
    @SerializedName("scope")
    private String scope;
    @SerializedName("token_type")
    private String token_type;

    public Token() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public static Token fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Token.class);
    }
}
