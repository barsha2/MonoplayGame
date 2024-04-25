package com.barsha.monopolygame.Model;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameBalanceTable {
    private BigInteger  uniqueID;
    private String      gameID;
    private String      player1UserID;
    private double      player1Balance;
    private String      player2UserID;
    private double      player2Balance;
    private int         currentstatus;
}
