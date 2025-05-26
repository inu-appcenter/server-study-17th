package com.example.ticketing.ticket;

import com.example.ticketing.exception.CustomException;
import com.example.ticketing.exception.ErrorCode;
import com.example.ticketing.ticket.dto.req.TicketCreateRequestDto;
import com.example.ticketing.ticket.dto.req.TicketUpdateRequestDto;
import com.example.ticketing.ticket.dto.res.TicketCreateResponseDto;
import com.example.ticketing.ticket.dto.res.TicketGetResponseDto;
import com.example.ticketing.ticket.dto.res.TicketUpdateResponseDto;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    //티켓 생성
    @Transactional
    public TicketCreateResponseDto create(TicketCreateRequestDto requestDto) {
        Ticket ticket = Ticket.builder()
                .ticketNumber(requestDto.getTicketNumber())
                .ticketSeat(requestDto.getTicketSeat())
                .ticketState(requestDto.getTicketState())
                .build();
        ticketRepository.save(ticket);
        return TicketCreateResponseDto.from(ticket);
    }

    //개별 티켓 조회
    @Transactional(readOnly = true)
    public TicketGetResponseDto getTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));
        return TicketGetResponseDto.from(ticket);
    }

    //티켓 전체 조회
    @Transactional(readOnly = true)
    public List<TicketGetResponseDto> getTicketList() {
        return ticketRepository.findAll().stream()
                .map(TicketGetResponseDto::from)
                .toList();
    }

    //티켓 수정
    @Transactional
    public TicketUpdateResponseDto update(Long id, TicketUpdateRequestDto requestDto) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));
        ticket.update(requestDto);
        return TicketUpdateResponseDto.from(ticket);
    }

    //티켓 삭제
    @Transactional
    public void delete(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));
        ticketRepository.delete(ticket);
    }
}
