package com.barsha.monopolygame.Model;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDetailsTable {
    private BigInteger  uniqueID;
    private String      gameID;
    private int         player1CurrentPosition;
    private int         player2CurrentPosition;
    private int         currentStatus;
}
