package com.example.ticketing.show;

import com.example.ticketing.exception.CustomException;
import com.example.ticketing.exception.ErrorCode;
import com.example.ticketing.show.dto.req.ShowCreateRequestDto;
import com.example.ticketing.show.dto.req.ShowUpdateRequestDto;
import com.example.ticketing.show.dto.res.ShowCreateResponseDto;
import com.example.ticketing.show.dto.res.ShowGetResponseDto;
import com.example.ticketing.show.dto.res.ShowUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepository;

    // 공연 생성
    @Transactional
    public ShowCreateResponseDto create(ShowCreateRequestDto requestDto) {
        Show show = Show.builder()
                .showTitle(requestDto.getShowTitle())
                .posterImage(requestDto.getPosterImage())
                .ticketingDate(requestDto.getTicketingDate())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .showState(requestDto.getShowState())
                .build();
        showRepository.save(show);
        return ShowCreateResponseDto.from(show);
    }


    // 개별 공연 조회
    @Transactional(readOnly = true)
    public ShowGetResponseDto getShow(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SHOW_NOT_FOUND));
        return ShowGetResponseDto.from(show);
    }

    // 전체 공연 리스트 조회
    @Transactional(readOnly = true)
    public List<ShowGetResponseDto> getShowList() {
        return showRepository.findAll().stream() //Stream API 활용
                .map(ShowGetResponseDto::from)
                .toList();
    }

    // 공연 수정
    @Transactional
    public ShowUpdateResponseDto update(Long id, ShowUpdateRequestDto requestDto) {
        Show existingShow = showRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SHOW_NOT_FOUND));
        existingShow.update(requestDto);
        return ShowUpdateResponseDto.from(existingShow);
    }

    // 공연 삭제
    @Transactional
    public void delete(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SHOW_NOT_FOUND));

        showRepository.delete(show);
    }
}
