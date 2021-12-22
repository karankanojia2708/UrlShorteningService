package com.example.urlshorteningservice.controller;

import com.example.urlshorteningservice.model.Url;
import com.example.urlshorteningservice.model.UrlDto;
import com.example.urlshorteningservice.model.UrlErrorResponseDto;
import com.example.urlshorteningservice.model.UrlResponse;
import com.example.urlshorteningservice.service.UrlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
public class UrlShorteningController {
    @Autowired
    private UrlService urlService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto){
        Url urlToRet = urlService.generateShortLink(urlDto);
        if(urlToRet != null){
            UrlResponse urlResponse = new UrlResponse();
            urlResponse.setOriginalUrl(urlToRet.getOriginalUrl());
            urlResponse.setShortLink(urlToRet.getShortLink());
            urlResponse.setExpirationDate(urlToRet.getExpirationDate());
            return new ResponseEntity<UrlResponse>(urlResponse, HttpStatus.OK);
        }
        UrlErrorResponseDto errorResponseDto = new UrlErrorResponseDto();
        errorResponseDto.setStatus("400");
        errorResponseDto.setMessage("There was a error processing! Please try again");
        return new ResponseEntity<UrlErrorResponseDto>(errorResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<?> redirectToOriginalUlr(@PathVariable String shortLink, HttpServletResponse response) throws IOException {
        if(StringUtils.isNotEmpty(shortLink)){
            Url urlToRet = urlService.getEncoded(shortLink);
            if(urlToRet == null){
                UrlErrorResponseDto errorResponseDto = new UrlErrorResponseDto();
                errorResponseDto.setStatus("400");
                errorResponseDto.setMessage("Url doesn't exist");
                return new ResponseEntity<UrlErrorResponseDto>(errorResponseDto, HttpStatus.OK);
            }
            if(urlToRet.getExpirationDate().isBefore(LocalDateTime.now())){
                urlService.deleteShortLink(urlToRet);
                UrlErrorResponseDto errorResponseDto = new UrlErrorResponseDto();
                errorResponseDto.setStatus("200");
                errorResponseDto.setMessage("Url expired");
                return new ResponseEntity<UrlErrorResponseDto>(errorResponseDto, HttpStatus.OK);
            }
            response.sendRedirect(urlToRet.getOriginalUrl());
            return null;
        }
        UrlErrorResponseDto errorResponseDto = new UrlErrorResponseDto();
        errorResponseDto.setStatus("400");
        errorResponseDto.setMessage("Invalid url");
        return new ResponseEntity<UrlErrorResponseDto>(errorResponseDto, HttpStatus.OK);
    }
}
