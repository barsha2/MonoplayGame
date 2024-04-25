package com.barsha.monopolygame.Service;

import com.barsha.monopolygame.Model.MonopolyGameResponse;
import com.barsha.monopolygame.Model.RollDieRequest;

public interface RollDieService {
    MonopolyGameResponse        RollDie         (RollDieRequest rollDieRequest);
}
