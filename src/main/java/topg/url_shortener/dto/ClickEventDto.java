package topg.url_shortener.dto;

import java.time.LocalDateTime;

public record ClickEventDto(
        LocalDateTime clickDate,
        Long count
) {
}
