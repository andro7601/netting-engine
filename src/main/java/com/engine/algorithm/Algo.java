package com.engine.algorithm;

import com.engine.dto.CandidateTradeDto;
import com.engine.dto.RequestDto;
import com.engine.exception.InvalidArgumentException;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

public class Algo {

    public static HashSet<CandidateTradeDto> algorithm(RequestDto request) {
        List<CandidateTradeDto> trades = request.candidateTrades();
        BigDecimal maxMarginDecimal = request.maxMargin();

        int afterDecimal = maxMarginDecimal.stripTrailingZeros().scale();
        for (CandidateTradeDto trade : trades) {
            afterDecimal = Math.max(afterDecimal,
                    trade.marginRequired().stripTrailingZeros().scale());
        }


        BigDecimal scaled = maxMarginDecimal.movePointRight(afterDecimal);
        if (scaled.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0) {
            throw new InvalidArgumentException("maxMargin too large to process");
        }

        int capacity = scaled.intValue();
        int length = trades.size();

        BigDecimal[][] dp = new BigDecimal[length + 1][capacity + 1];


        for (int k = 0; k <= capacity; k++) {
            dp[0][k] = BigDecimal.ZERO;
        }

        for (int i = 1; i <= length; i++) {
            int margin = trades.get(i - 1).marginRequired()
                    .movePointRight(afterDecimal).intValue();
            BigDecimal pnl = trades.get(i - 1).expectedPnl();

            for (int k = 0; k <= capacity; k++) {
                BigDecimal skip = dp[i - 1][k];
                BigDecimal take = k >= margin
                        ? dp[i - 1][k - margin].add(pnl)
                        : BigDecimal.ZERO;
                dp[i][k] = skip.max(take);
            }
        }

        HashSet<CandidateTradeDto> selected = new HashSet<>();
        int tempMargin = capacity;
        for (int i = length; i > 0; i--) {

            if (dp[i][tempMargin].compareTo(dp[i - 1][tempMargin]) != 0) {
                selected.add(trades.get(i - 1));
                tempMargin -= trades.get(i - 1).marginRequired()
                        .movePointRight(afterDecimal).intValue();
                if (tempMargin == 0) break;
            }
        }
        return selected;
    }

}
