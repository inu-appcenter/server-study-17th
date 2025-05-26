package com.example.ticketing.ticket;

import com.example.ticketing.ticket.dto.req.TicketCreateRequestDto;
import com.example.ticketing.ticket.dto.req.TicketUpdateRequestDto;
import com.example.ticketing.ticket.dto.res.TicketCreateResponseDto;
import com.example.ticketing.ticket.dto.res.TicketGetResponseDto;
import com.example.ticketing.ticket.dto.res.TicketUpdateResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "티켓 생성")
    @PostMapping
    public ResponseEntity<TicketCreateResponseDto> create(@Valid @RequestBody TicketCreateRequestDto requestDto) {
        return ResponseEntity.ok(ticketService.create(requestDto));
    }
    
    @Operation(summary = "티켓 조회")
    @GetMapping("/{id}")
    public ResponseEntity<TicketGetResponseDto> getTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicket(id));
    }
    
    @Operation(summary = "티켓 전체 조회")
    @GetMapping
    public ResponseEntity<List<TicketGetResponseDto>> getTicketList() {
        return ResponseEntity.ok(ticketService.getTicketList());
    }
    
    @Operation(summary = "티켓 수정")
    @PutMapping("/{id}")
    public ResponseEntity<TicketUpdateResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody TicketUpdateRequestDto requestDto) {
        return ResponseEntity.ok(ticketService.update(id, requestDto));
    }

    @Operation(summary = "티켓 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}