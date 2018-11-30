package com.ebao.hospitaldapp.rest.entity;

import lombok.Data;
import lombok.NonNull;

@Data
public class TokenEntity {
    @NonNull
    private String deviceId;
    @NonNull
    private String token;
}
