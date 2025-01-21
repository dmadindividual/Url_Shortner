package topg.url_shortener.dto;

import lombok.Builder;

@Builder
public record UserResponseDto(
        boolean success,
        String message,
        Object data
) {
}
