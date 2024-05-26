package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccountLoginResponseModel {
    @JsonProperty("created_date")
    String createdDate;
    String expires, isActive, password, token, userId, username;
}
