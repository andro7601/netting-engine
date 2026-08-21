package com.engine.controller;

import com.engine.dto.RequestDto;
import com.engine.dto.ResponseDto;
import com.engine.service.OptimizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trades")
@RequiredArgsConstructor
public class OptimizationController {

    private final OptimizationService optimizationService;

    @PostMapping("/optimize")
    public ResponseEntity<ResponseDto> optimize(@RequestBody RequestDto requestDto) {
        ResponseDto responseDto = optimizationService.optimize(requestDto);
        URI location = URI.create("/api/v1/trades/" + responseDto.requestId());
        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ResponseDto> getOptimized(@PathVariable UUID requestId) {
        return ResponseEntity.ok().body(optimizationService.getOptimized(requestId));
    }

    @GetMapping
    public ResponseEntity<List<ResponseDto>> getAll(){
        return ResponseEntity.ok().body(optimizationService.getAll());
    }


}
