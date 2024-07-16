package leets.weeth.domain.event.dto;

import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record ResponseEvent (
        Long id,
        String title,
        String content,
        String location,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String userName
) {}