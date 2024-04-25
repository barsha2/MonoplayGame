package com.barsha.monopolygame.Model;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionTable {
    private BigInteger  uniqueID;
    private String      gameID;
    private String      userID;
    private String      userAction;
    private String      placeName;
}
