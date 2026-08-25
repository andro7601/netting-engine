package com.engine.service;

import com.engine.dto.CandidateTradeDto;
import com.engine.dto.RequestDto;
import com.engine.exception.InvalidArgumentException;
import com.engine.repository.OptimizationRequestRepository;
import com.engine.repository.TradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
        RequestDto requestDto = new RequestDto(new BigDecimal("15"), List.of(
                new CandidateTradeDto("Trade Alpha", new BigDecimal("5"), new BigDecimal("120")),
                new CandidateTradeDto("Trade Beta", new BigDecimal("10"), new BigDecimal("200")),
                new CandidateTradeDto("Trade Gamma", new BigDecimal("3"), new BigDecimal("80")),
                new CandidateTradeDto("Trade Delta", new BigDecimal("8"), new BigDecimal("160"))
        ));

        when(optimizationRequestRepository.saveAndFlush(any()))
                .thenAnswer(inv -> inv.getArgument(0));
        when(tradeRepository.saveAll(anyList()))
                .thenAnswer(inv -> inv.getArgument(0));

        optimizationService.optimize(requestDto);

        verify(optimizationRequestRepository, times(1)).saveAndFlush(any());
        verify(tradeRepository, times(1)).saveAll(anyList());

    }

    @Test
    void rejectNonUnique(){
        RequestDto requestDto = new RequestDto(new BigDecimal("15"), List.of(
                new CandidateTradeDto("Trade", new BigDecimal("5"), new BigDecimal("120")),
                new CandidateTradeDto("Trade", new BigDecimal("10"), new BigDecimal("200"))
        ));
        assertThrows(InvalidArgumentException.class,()->optimizationService.optimize(requestDto));
    }
}
