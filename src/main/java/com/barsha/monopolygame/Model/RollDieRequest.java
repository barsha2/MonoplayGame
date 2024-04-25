package com.barsha.monopolygame.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RollDieRequest {
    private String userID;
    private String gameID;
}
