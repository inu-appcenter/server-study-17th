package com.example.ticketing.show;

import com.example.ticketing.show.dto.req.ShowCreateRequestDto;
import com.example.ticketing.show.dto.req.ShowUpdateRequestDto;
import com.example.ticketing.show.dto.res.ShowGetResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepository;

    // 공연 생성
    @Transactional
    public Show create(ShowCreateRequestDto requestDto) {
        Show show = Show.builder()
                .showTitle(requestDto.getShowTitle())
                .posterImage(requestDto.getPosterImage())
                .detailedImage(requestDto.getDetailedImage())
                .ticketingDate(requestDto.getTicketingDate())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .showRate(requestDto.getShowRate())
                .showState(requestDto.getShowState())
                .casts(requestDto.getCasts())
                .build();
        return showRepository.save(show);
    }

    // 개별 공연 조회
    @Transactional(readOnly = true)
    public Show getShow(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 공연입니다."));
    }

    // 전체 공연 리스트 조회
    @Transactional(readOnly = true)
    public List<ShowGetResponseDto> getShowList() {
        List<Show> showEntities = showRepository.findAll();
        List<ShowGetResponseDto> responseList = new ArrayList<>();
        for (Show show : showEntities) {
            responseList.add(ShowGetResponseDto.from(show));
        }
        return responseList;
    }

    // 공연 수정
    @Transactional
    public Show update(Long id, ShowUpdateRequestDto requestDto) {
        Show existingShow = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 공연입니다."));

        existingShow.update(requestDto);
        return showRepository.save(existingShow);
    }

    // 공연 삭제
    @Transactional
    public void delete(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 공연입니다."));

        showRepository.delete(show);
    }
}
