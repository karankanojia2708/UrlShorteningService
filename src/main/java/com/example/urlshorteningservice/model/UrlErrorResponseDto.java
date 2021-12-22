package com.example.urlshorteningservice.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UrlErrorResponseDto {
    private String status;
    private String message;
}
