package com.barsha.monopolygame.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameCreateRequest {
    private String hostID;
    private String coPlayerID;
}
