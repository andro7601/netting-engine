package com.engine.service;

import com.engine.algorithm.Algo;
import com.engine.dto.CandidateTradeDto;
import com.engine.dto.RequestDto;
import com.engine.dto.ResponseDto;
import com.engine.entity.OptimizationRequestEntity;
import com.engine.entity.TradeEntity;
import com.engine.exception.InvalidArgumentException;
import com.engine.exception.NotFoundException;
import com.engine.repository.OptimizationRequestRepository;
import com.engine.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptimizationService {

    private final OptimizationRequestRepository optimizationRequestRepository;
    private final TradeRepository tradeRepository;

    @Transactional
    public ResponseDto optimize(RequestDto requestDto) {
        if (requestDto.candidateTrades().stream().map(CandidateTradeDto::tradeName).collect(Collectors.toSet()).size() != requestDto.candidateTrades().size()) {
            throw new InvalidArgumentException("CandidateNames arent unique");
        }

        BigDecimal maxMarginDecimal=requestDto.maxMargin();
        List<CandidateTradeDto> inputTrades=requestDto.candidateTrades();



        HashSet<CandidateTradeDto> result = Algo.algorithm(requestDto);

        BigDecimal totalMarginUsed = new BigDecimal("0");
        BigDecimal totalPnlExpected = new BigDecimal("0");

        for (var e : result) {
            totalMarginUsed=totalMarginUsed.add(e.marginRequired());
            totalPnlExpected=totalPnlExpected.add(e.expectedPnl());
        }

        OptimizationRequestEntity requestEntity = new OptimizationRequestEntity();
        requestEntity.setMaxMargin(maxMarginDecimal);
        requestEntity.setTotalExpectedPnl(totalPnlExpected);
        requestEntity.setTotalMarginUsed(totalMarginUsed);

        List<TradeEntity> tradeEntities = inputTrades.stream().map(candidateTradeDto -> {
            TradeEntity tradeEntity = new TradeEntity();
            tradeEntity.setOptimizationRequestEntity(requestEntity);
            tradeEntity.setTradeName(candidateTradeDto.tradeName());
            tradeEntity.setExpectedPnl(candidateTradeDto.expectedPnl());
            tradeEntity.setMarginRequired(candidateTradeDto.marginRequired());
            tradeEntity.setSelected(result.contains(candidateTradeDto));
            return tradeEntity;
        }).toList();

        OptimizationRequestEntity flushedRequestEntity = optimizationRequestRepository.saveAndFlush(requestEntity);
        tradeRepository.saveAll(tradeEntities);

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
    public List<ResponseDto> getAllSorted(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        List<OptimizationRequestEntity> requestEntities = optimizationRequestRepository.findAllByOrderByCreatedAtDesc(pageable);

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
