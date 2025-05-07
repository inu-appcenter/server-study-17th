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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowService showService;

    @PostMapping
    public ResponseEntity<ShowCreateResponseDto> create(@Valid @RequestBody ShowCreateRequestDto requestDto) {
        return ResponseEntity.ok(showService.create(requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowGetResponseDto> getShow(@PathVariable Long id) {
        return ResponseEntity.ok(showService.getShow(id));
    }

    @GetMapping
    public ResponseEntity<List<ShowGetResponseDto>> getShowList() {
        return ResponseEntity.ok(showService.getShowList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShowUpdateResponseDto> update(@PathVariable Long id,
                                                        @Valid @RequestBody ShowUpdateRequestDto requestDto) {
        return ResponseEntity.ok(showService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        showService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
