package com.barsha.monopolygame.Repository.Dao;

import java.util.List;

import com.barsha.monopolygame.Model.GameDetailsTable;

public interface GameDetailsTableDao {
    int                             InsertGameDetailsTable      (GameDetailsTable gameDetailsTable);
    List<GameDetailsTable>          GetGameDetailsTable         (String gameID);
}
