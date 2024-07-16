package leets.weeth.domain.event.service;

import jakarta.persistence.EntityNotFoundException;
import leets.weeth.domain.calendar.entity.Calendar;
import leets.weeth.domain.calendar.service.CalendarService;
import leets.weeth.domain.event.dto.RequestEvent;
import leets.weeth.domain.event.dto.ResponseEvent;
import leets.weeth.domain.event.entity.Event;
import leets.weeth.domain.event.mapper.EventMapper;
import leets.weeth.domain.event.repository.EventRepository;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static leets.weeth.domain.event.entity.enums.ErrorMessage.*;


@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final CalendarService calendarService;

    private final EventMapper eventMapper;

    // 일정 생성
    @Transactional
    public void createEvent(RequestEvent requestEvent, Long userId) throws BusinessLogicException {
        // 기간 입력 검증
        validateDateRange(requestEvent.startDateTime(), requestEvent.endDateTime());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND.getMessage()));

        // 날짜 정보를 이용해 해당 캘린더와 연결하여 저장
        int year = requestEvent.startDateTime().getYear();
        int month = requestEvent.startDateTime().getMonthValue();
        Calendar calendar = calendarService.getCalendar(year, month);

        eventRepository.save(Event.fromDto(requestEvent, user, calendar));
    }

    // 일정 상세 조회
    @Transactional(readOnly = true)
    public ResponseEvent getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND.getMessage()));
        return eventMapper.toDto(event);
    }

    // 기간 별 일정 조회
    @Transactional(readOnly = true)
    public List<ResponseEvent> getEventsBetweenDate(LocalDateTime startDate, LocalDateTime endDate) throws BusinessLogicException {
        // 기간 입력 검증
        validateDateRange(startDate, endDate);

        List<Event> events = eventRepository.findByStartDateTimeBetween(startDate, endDate);
        return events.stream()
                .map(eventMapper::toDto)
                .toList();
    }

    // 일정 수정
    @Transactional
    public void updateEvent(Long eventId, RequestEvent updatedEvent, Long userId) throws BusinessLogicException {
        // 일정을 생성한 사용자인지 확인
        Event oldEvent = validateEventOwner(eventId, userId);
        oldEvent.updateFromDto(updatedEvent);
    }

    // 일정 삭제
    @Transactional
    public void deleteEvent(Long eventId, Long userId) throws BusinessLogicException {
        // 일정을 생성한 사용자인지 확인
        Event oldEvent = validateEventOwner(eventId, userId);
        eventRepository.delete(oldEvent);
    }

    // 해당 일정을 생성한 사용자와 같은지 검증
    private Event validateEventOwner(Long eventId, Long userId) throws BusinessLogicException {
        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND.getMessage()));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND.getMessage()));

        // 일정을 생성한 사용자와 같은지 확인
        if(!user.getId().equals(oldEvent.getUser().getId())){
            throw new BusinessLogicException(USER_NOT_MATCH.getMessage());
        }
        return oldEvent;
    }

    // 시작 날짜가 종료 날짜 보다 느린지 검증
    private void validateDateRange(LocalDateTime start, LocalDateTime end) throws BusinessLogicException {
        if (start.isAfter(end)) {
            throw new BusinessLogicException(INVALID_DATE.getMessage());
        }
    }
}
