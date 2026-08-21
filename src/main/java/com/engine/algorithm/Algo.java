package com.engine.algorithm;

import com.engine.dto.CandidateTradeDto;
import com.engine.dto.RequestDto;

import java.util.HashSet;
import java.util.List;

public class Algo {

    public static final HashSet<CandidateTradeDto> algorithm(RequestDto request) {
        List<CandidateTradeDto> trades = request.candidateTrades();
        int length = trades.size();
        int capacity = request.maxMargin();
        int[][] dp = new int[length + 1][capacity + 1];

        for (int i = 1; i <= length; i++) {
            int margin = trades.get(i - 1).marginRequired();
            int pnl = trades.get(i - 1).expectedPnl();
            for (int k = 0; k <= capacity; k++) {
                int skip = dp[i - 1][k];
                int take = k >= margin
                        ? dp[i - 1][k - margin] + pnl
                        : 0;
                dp[i][k] = Math.max(skip, take);
            }
        }
        HashSet<CandidateTradeDto> selected = new HashSet<>();

        int tempMargin = capacity;

        for (int i = length; i > 0; i--) {
            if (dp[i][tempMargin] != dp[i - 1][tempMargin]) {
                selected.add(trades.get(i-1));
                tempMargin-=trades.get(i-1).marginRequired();
                if(tempMargin==0)break;
            }
        }
        return selected;
    }

}
