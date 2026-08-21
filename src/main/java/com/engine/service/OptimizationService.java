package com.engine.service;

import com.engine.algorithm.Algo;
import com.engine.dto.CandidateTradeDto;
import com.engine.dto.RequestDto;
import com.engine.dto.ResponseDto;
import com.engine.entity.OptimizationRequestEntity;
import com.engine.entity.TradeEntity;
import com.engine.exception.NotFoundException;
import com.engine.repository.OptimizationRequestRepository;
import com.engine.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

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

        List<TradeEntity> tradeEntities = input.stream().map(candidateTradeDto -> {
            TradeEntity tradeEntity = new TradeEntity();
            tradeEntity.setOptimizationRequestEntity(requestEntity);
            tradeEntity.setTradeName(candidateTradeDto.tradeName());
            tradeEntity.setExpectedPnl(candidateTradeDto.expectedPnl());
            tradeEntity.setMarginRequired(candidateTradeDto.marginRequired());
            tradeEntity.setSelected(result.contains(candidateTradeDto));
            return tradeEntity;
        }).toList();

        OptimizationRequestEntity flushedRequestEntity = optimizationRequestRepository.save(requestEntity);
        tradeRepository.saveAll(tradeEntities);
        //not 100% flushed generates are done at hibernate level,not db level

        ResponseDto responseDto = new ResponseDto(flushedRequestEntity.getId(), result.stream().toList(), totalMarginUsed, totalPnlExpected, flushedRequestEntity.getCreatedAt());

        return responseDto;
    }

    @Transactional(readOnly = true)
    public ResponseDto getOptimized(UUID requestId) {
        OptimizationRequestEntity requestEntity = optimizationRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("No request with id: " + requestId));

        List<CandidateTradeDto> tradeDtos = requestEntity.getTradeEntities().stream()
                .filter(TradeEntity::isSelected)
                .map(entity -> {
                    CandidateTradeDto candidateTradeDto = new CandidateTradeDto(entity.getTradeName(), entity.getMarginRequired(), entity.getExpectedPnl());
                    return candidateTradeDto;
                }).toList();

        ResponseDto responseDto = new ResponseDto(
                requestId,
                tradeDtos,
                requestEntity.getTotalMarginUsed(),
                requestEntity.getTotalExpectedPnl(),
                requestEntity.getCreatedAt()
        );
        return responseDto;
    }

    @Transactional(readOnly = true)
    public List<ResponseDto> getAll() {
        List<OptimizationRequestEntity> requestEntities = optimizationRequestRepository.findAll();
        return requestEntities.stream().map(entity ->
                new ResponseDto(
                        entity.getId(),
                        entity.getTradeEntities().stream()
                                .filter(TradeEntity::isSelected)
                                .map(candidateEntity ->
                                        new CandidateTradeDto(
                                                candidateEntity.getTradeName(),
                                                candidateEntity.getMarginRequired(),
                                                candidateEntity.getExpectedPnl()
                                        )
                                ).toList(),
                        entity.getTotalMarginUsed(),
                        entity.getTotalExpectedPnl(),
                        entity.getCreatedAt()
                )
        ).toList();
    }
}
