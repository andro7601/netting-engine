package com.engine.service;

import com.engine.dto.CandidateTradeDto;
import com.engine.dto.RequestDto;
import com.engine.repository.OptimizationRequestRepository;
import com.engine.repository.TradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OptimizationServiceTest {
    @Mock
    private OptimizationRequestRepository optimizationRequestRepository;
    @Mock
    private TradeRepository tradeRepository;
    @InjectMocks
    private OptimizationService optimizationService;

    @Test
    void requestAndTradesSaved() {
        RequestDto requestDto = new RequestDto(15, List.of(
                new CandidateTradeDto("Trade Alpha", 5, 120),
                new CandidateTradeDto("Trade Beta", 10, 200),
                new CandidateTradeDto("Trade Gamma", 3, 80),
                new CandidateTradeDto("Trade Delta", 8, 160)
        ));

        when(optimizationRequestRepository.saveAndFlush(any()))
                .thenAnswer(inv -> inv.getArgument(0));
        when(tradeRepository.saveAll(anyList()))
                .thenAnswer(inv -> inv.getArgument(0));

        optimizationService.optimize(requestDto);

        verify(optimizationRequestRepository, times(1)).saveAndFlush(any());
        verify(tradeRepository, times(1)).saveAll(anyList());

    }
}
