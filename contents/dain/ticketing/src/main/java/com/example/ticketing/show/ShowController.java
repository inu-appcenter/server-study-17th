package com.example.ticketing.show;

import com.example.ticketing.show.dto.req.ShowCreateRequestDto;
import com.example.ticketing.show.dto.req.ShowUpdateRequestDto;
import com.example.ticketing.show.dto.res.ShowCreateResponseDto;
import com.example.ticketing.show.dto.res.ShowGetResponseDto;
import com.example.ticketing.show.dto.res.ShowUpdateResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowService showService;

    @PostMapping
    public ResponseEntity<ShowCreateResponseDto> create(@Valid @RequestBody ShowCreateRequestDto requestDto) {
        Show createdShow = showService.create(requestDto);
        ShowCreateResponseDto responseDto = ShowCreateResponseDto.from(createdShow);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowGetResponseDto> getShow(@PathVariable Long id) {
        Show show = showService.getShow(id);
        ShowGetResponseDto responseDto = ShowGetResponseDto.from(show);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ShowGetResponseDto>> getShowList() {
        List<ShowGetResponseDto> responseList = showService.getShowList();
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShowUpdateResponseDto> update(@PathVariable Long id,
                                                        @Valid @RequestBody ShowUpdateRequestDto requestDto) {
        Show updatedShow = showService.update(id, requestDto);
        ShowUpdateResponseDto responseDto = ShowUpdateResponseDto.from(updatedShow);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        showService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
