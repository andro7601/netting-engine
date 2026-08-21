package com.engine.service;

import com.engine.algorithm.Algo;
import com.engine.dto.CandidateTradeDto;
import com.engine.dto.RequestDto;
import com.engine.dto.ResponseDto;
import com.engine.entity.OptimizationRequestEntity;
import com.engine.entity.TradeEntity;
import com.engine.repository.OptimizationRequestRepository;
import com.engine.repository.TradeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OptimizationService {

    private final OptimizationRequestRepository optimizationRequestRepository;
    private final TradeRepository tradeRepository;

    @Transactional
    public ResponseDto optimize(RequestDto requestDto) {
        int maxMargin = requestDto.maxMargin();
        List<CandidateTradeDto> input = requestDto.candidateTrades();

        HashSet<CandidateTradeDto> result = Algo.algorithm(requestDto);

        int totalMarginUsed = 0;
        int totalPnlExpected = 0;

        for (var e : result) {
            totalMarginUsed += e.marginRequired();
            totalPnlExpected += e.expectedPnl();
        }

        OptimizationRequestEntity requestEntity = new OptimizationRequestEntity();
        requestEntity.setMaxMargin(maxMargin);
        requestEntity.setTotalExpectedPnl(totalPnlExpected);
        requestEntity.setTotalMarginUsed(totalMarginUsed);

        List<TradeEntity> tradeEntities = input.stream().map(
                candidateTradeDto -> {
                    TradeEntity tradeEntity = new TradeEntity();
                    tradeEntity.setOptimizationRequestEntity(requestEntity);
                    tradeEntity.setTradeName(candidateTradeDto.tradeName());
                    tradeEntity.setExpectedPnl(candidateTradeDto.expectedPnl());
                    tradeEntity.setMarginRequired(candidateTradeDto.marginRequired());
                    tradeEntity.setSelected(result.contains(candidateTradeDto));
                    return tradeEntity;
                }
        ).toList();

        OptimizationRequestEntity flushedRequestEntity = optimizationRequestRepository.save(requestEntity);
        List<TradeEntity> flushedTradeEntities = tradeRepository.saveAll(tradeEntities);
        //not 100% flushed generates are done at hibernate level,not db level

        ResponseDto responseDto = new ResponseDto(
                flushedRequestEntity.getId(),
                result.stream().toList(),
                totalMarginUsed,
                totalPnlExpected,
                flushedRequestEntity.getCreatedAt()
        );

        return responseDto;
    }
}
