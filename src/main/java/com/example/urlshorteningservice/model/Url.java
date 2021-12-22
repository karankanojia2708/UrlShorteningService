package com.example.urlshorteningservice.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Url
{
    @Id
    @GeneratedValue
    private long id;
    @Lob
    private String originalUrl;

    private String shortLink;
    private LocalDateTime createdDate;
    private LocalDateTime expirationDate;
}
