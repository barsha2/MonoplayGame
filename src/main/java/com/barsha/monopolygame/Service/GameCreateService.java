package com.barsha.monopolygame.Service;

import com.barsha.monopolygame.Model.GameCreateRequest;
import com.barsha.monopolygame.Model.MonopolyGameResponse;

public interface GameCreateService {
    MonopolyGameResponse        CreateGame          (GameCreateRequest gameCreateRequest);
}
