package com.example.urlshorteningservice.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UrlResponse
{
    private String originalUrl;
    private String shortLink;
    private LocalDateTime expirationDate;

}
