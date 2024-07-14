package leets.weeth.domain.attendance.controller;

import jakarta.validation.Valid;
import leets.weeth.domain.attendance.dto.RequestAttendance;
import leets.weeth.domain.attendance.dto.RequestPenalty;
import leets.weeth.domain.attendance.dto.ResponseMeeting;
import leets.weeth.domain.attendance.dto.ResponseStatistics;
import leets.weeth.domain.attendance.service.AttendanceService;
import leets.weeth.domain.attendance.service.AttendanceStatisticsService;
import leets.weeth.domain.attendance.service.MeetingService;
import leets.weeth.domain.attendance.service.PenaltyService;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final AttendanceStatisticsService statisticsService;
    private final MeetingService meetingService;
    private final PenaltyService penaltyService;

    @GetMapping("/meetings")
    public CommonResponse<List<ResponseMeeting>> getAllMeetings() {
        List<ResponseMeeting> meetings = meetingService.getAllMeetings();
        return CommonResponse.createSuccess(meetings);
    }

    @PostMapping("/check-in")
    public CommonResponse<String> checkInAttendance(@RequestBody @Valid RequestAttendance attendanceDTO, @RequestParam(value = "email") String email) {
        attendanceService.checkInAttendance(attendanceDTO, email);
        return CommonResponse.createSuccess("출석이 완료되었습니다.");
    }

    @GetMapping("/statistics/{userId}") //특정 사용자 출석통계
    public CommonResponse<ResponseStatistics> getAttendanceStatistics(@PathVariable Long userId) {
        ResponseStatistics statistics = statisticsService.getAttendanceStatistics(userId);
        return CommonResponse.createSuccess(statistics);
    }

    @PostMapping("/penalty")
    public CommonResponse<String> recordPenalty(@RequestBody @Valid RequestPenalty requestPenalty, @RequestParam(value = "email") String email) {
        penaltyService.recordPenalty(requestPenalty, email);
        return CommonResponse.createSuccess("패널티가 기록되었습니다.");
    }
}
