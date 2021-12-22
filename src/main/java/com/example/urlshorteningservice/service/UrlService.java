package com.example.urlshorteningservice.service;

import com.example.urlshorteningservice.model.Url;
import com.example.urlshorteningservice.model.UrlDto;
import org.springframework.stereotype.Service;

@Service
public interface UrlService
{
    public Url generateShortLink(UrlDto urlDto);
    public Url persistShortLink(Url url);
    public Url getEncoded(String url);
    public void deleteShortLink(Url url);
}
