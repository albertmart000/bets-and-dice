package com.betsanddice.craps.helper;

import reactor.core.publisher.Mono;

public class OddCalculator {

    public Mono<Double> calculateOddResultInAttempts(int diceSum, int attempts) {
        if (diceSum < 2 || diceSum > 12) {
            return Mono.just(0.0);
        }
        int favorableCombinations = Math.min(diceSum - 1, 13 - diceSum);
        double probSum = favorableCombinations / 36.0;
        double probNotSum = Math.pow(1 - probSum, attempts);
        double probResultAndAttempts = 1 - probNotSum;
        return Mono.just(1 / probResultAndAttempts);
    }
}