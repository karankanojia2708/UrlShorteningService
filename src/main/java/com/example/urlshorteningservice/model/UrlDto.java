package com.example.urlshorteningservice.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UrlDto
{
    private String url;
    private String expirationDate;
}
