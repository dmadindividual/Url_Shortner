package topg.url_shortener.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import topg.url_shortener.dto.ClickEventDto;
import topg.url_shortener.dto.UrlMappingDto;
import topg.url_shortener.models.User;
import topg.url_shortener.service.UrlMappingService;
import topg.url_shortener.service.UserService;

import javax.swing.text.DateFormatter;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlMappingController {
    private final UrlMappingService urlMappingService;
    private  final UserService userService;

    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDto> createShortUrl(@RequestBody Map<String, String> request, Principal principal){
         String originalUrl = request.get("OriginalUrl");
     User user = userService.findByUsername(principal.getName());
        UrlMappingDto message  = urlMappingService.createShortUrl(originalUrl, user);
        return ResponseEntity.ok(message);

    }

    @GetMapping("/myUrls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDto>> getUserUrls(Principal principal){
        User user = userService.findByUsername(principal.getName());
        List<UrlMappingDto> message = urlMappingService.getUserUrls(user);
        return ResponseEntity.ok(message);

    }
    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClickEventDto>> getUrlAnalyticsByDate(
            @PathVariable("shortUrl") String url,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, dateTimeFormatter);
        LocalDateTime end = LocalDateTime.parse(endDate, dateTimeFormatter);

        List<ClickEventDto> message = urlMappingService.getUrlAnalyticsByDate(url, start, end);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/totalclicks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<LocalDate, Long>> getAllUrlAnalytics(
            Principal principal,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate ){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;
        LocalDate start = LocalDate.parse(startDate, dateTimeFormatter);
        LocalDate end = LocalDate.parse(endDate, dateTimeFormatter);
        User user = userService.findByUsername(principal.getName());
       Map<LocalDate, Long> message = urlMappingService.getAllUrlAnalytics(user, start, end);
        return ResponseEntity.ok(message);
    }

}
