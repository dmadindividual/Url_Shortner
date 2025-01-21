package topg.url_shortener.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import topg.url_shortener.models.UrlMapping;
import topg.url_shortener.repository.UrlMappingServiceRepository;
import topg.url_shortener.service.UrlMappingService;

@RestController
@RequiredArgsConstructor

public class RedirectController {
    private final UrlMappingService urlMappingService;


    @GetMapping("{shorturl}")
    public ResponseEntity<Void> redirectUrl(@PathVariable("shorturl") String shortUrl){
        UrlMapping urlMapping = urlMappingService.getOriginalUrl(shortUrl);
        if(urlMapping != null){
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Location", urlMapping.getOriginalUrl());
            return ResponseEntity.status(302).headers(httpHeaders).build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
