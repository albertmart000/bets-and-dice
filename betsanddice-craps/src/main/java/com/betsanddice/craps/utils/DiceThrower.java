package com.betsanddice.craps.utils;

import java.util.Random;

public class DiceThrower {

    private Random diceValue = new Random();

    public Integer getDiceValue () {
        return diceValue.nextInt(6);
    }

}

