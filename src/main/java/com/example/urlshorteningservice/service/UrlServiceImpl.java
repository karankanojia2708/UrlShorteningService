package com.example.urlshorteningservice.service;

import com.example.urlshorteningservice.model.Url;
import com.example.urlshorteningservice.model.UrlDto;
import com.example.urlshorteningservice.repository.UrlRepository;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class UrlServiceImpl implements UrlService{
    @Autowired
    private UrlRepository urlRepository;

    @Override
    public Url generateShortLink(UrlDto urlDto) {
        if(StringUtils.isNotEmpty(urlDto.getUrl())) {
            String encodedUrl = encodeUrl(urlDto.getUrl());
            Url urlToPersist = new Url();
            urlToPersist.setOriginalUrl(urlDto.getUrl());
            urlToPersist.setShortLink(encodedUrl);
            urlToPersist.setCreatedDate(LocalDateTime.now());
            urlToPersist.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(), urlToPersist.getCreatedDate()));
            Url urlToRet = persistShortLink(urlToPersist);
            if(urlToRet != null){
                return urlToRet;
            }
            return null;
        }
        return null;
    }

    private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime createdDate) {
        if(StringUtils.isBlank(expirationDate)){
            return createdDate.plusDays(10);
        }
        LocalDateTime expirationDateToRet = LocalDateTime.parse(expirationDate);
        return expirationDateToRet;
    }

    private String encodeUrl(String url) {
        LocalDateTime time = LocalDateTime.now();
        String encodedUrl = Hashing.murmur3_32_fixed()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return encodedUrl;
    }

    @Override
    public Url persistShortLink(Url url) {
        Url urlToRet = urlRepository.save(url);
        return urlToRet;
    }

    @Override
    public Url getEncoded(String url) {
        Url urlToRet = urlRepository.findByShortLink(url);
        return urlToRet;
    }

    @Override
    public void deleteShortLink(Url url) {
        urlRepository.delete(url);
    }
}
