package com.barsha.monopolygame.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OngoingGameTable {
    private String gameID;
    private String hostID;
    private String coPlayerID;
    private String gameStatus;
    private String winnerID;
}
