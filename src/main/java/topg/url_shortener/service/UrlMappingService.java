package topg.url_shortener.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import topg.url_shortener.dto.ClickEventDto;
import topg.url_shortener.dto.UrlMappingDto;
import topg.url_shortener.models.ClickedEvent;
import topg.url_shortener.models.UrlMapping;
import topg.url_shortener.models.User;
import topg.url_shortener.repository.ClickEventRepository;
import topg.url_shortener.repository.UrlMappingServiceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UrlMappingService {
private final UrlMappingServiceRepository urlMappingServiceRepository;
private final ClickEventRepository clickEventRepository;
    public UrlMappingDto createShortUrl(String originalUrl, User user) {
        String shortUrl = generateUrl();
        UrlMapping urlMapping = UrlMapping.builder()
                .originalUrl(originalUrl)
                .shortUrl(shortUrl)
                .user(user)
                .createdDate(LocalDateTime.now().now())
                .build();
        urlMapping = urlMappingServiceRepository.save(urlMapping);
        return convertDto(urlMapping);

    }

    private UrlMappingDto convertDto(UrlMapping urlMapping) {
        return  new UrlMappingDto(
                urlMapping.getId(),
                urlMapping.getOriginalUrl(),
                urlMapping.getShortUrl(),
                urlMapping.getClickCount(),
                urlMapping.getCreatedDate(),
                urlMapping.getUser().getUsername()

        );
    }

    private String generateUrl() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(8);
        for (int i = 0; i < 8; i++){
            stringBuilder.append(characters.charAt(random.nextInt(characters.length())));

        }
        return stringBuilder.toString();
    }

    public List<UrlMappingDto> getUserUrls(User user) {
        return  urlMappingServiceRepository.findByUser(user).stream()
                .map(this::convertDto)
                .toList();
    }

    public List<ClickEventDto> getUrlAnalyticsByDate(String url, LocalDateTime start, LocalDateTime end) {
        // Find the URL mapping for the given short URL
        UrlMapping urlMapping = urlMappingServiceRepository.findByShortUrl(url);

        if (urlMapping == null) {
            // Return an empty list if the URL mapping is not found
            return Collections.emptyList();
        }

        // Group by LocalDate and count occurrences
        return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end).stream()
                .collect(Collectors.groupingBy(
                        click -> click.getClickDate().toLocalDate(), // Group by date
                        Collectors.counting() // Count clicks for each date
                ))
                .entrySet().stream()
                .map(entry -> new ClickEventDto(
                        entry.getKey().atStartOfDay(), // Convert LocalDate to LocalDateTime
                        entry.getValue() // Count of clicks
                ))
                .collect(Collectors.toList());
    }


    public Map<LocalDate, Long> getAllUrlAnalytics(User user, LocalDate start, LocalDate end) {
        List<UrlMapping> urlMappings = urlMappingServiceRepository.findByUser(user);
        List<ClickedEvent> clickedEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
        return  clickedEvents.stream()
                .collect(Collectors.groupingBy(click-> click.getClickDate().toLocalDate(), Collectors.counting()));
    }

    public UrlMapping getOriginalUrl(String shortUrl) {
        UrlMapping urlMapping = urlMappingServiceRepository.findByShortUrl(shortUrl);

        if(urlMapping != null){
            urlMapping.setClickCount(urlMapping.getClickCount() + 1);
            urlMappingServiceRepository.save(urlMapping);

            ClickedEvent clickedEvent = new ClickedEvent();
            clickedEvent.setClickDate(LocalDateTime.now());
            clickedEvent.setUrlMapping(urlMapping);
            clickEventRepository.save(clickedEvent);
        }
        return urlMapping;
    }
}







