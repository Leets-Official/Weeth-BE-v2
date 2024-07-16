package leets.weeth.domain.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RequestEvent (
     @NotBlank String title,
     @NotBlank String content,
     String location,
     @NotNull LocalDateTime startDateTime,
     @NotNull LocalDateTime endDateTime
) {}