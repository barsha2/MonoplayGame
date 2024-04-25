package com.barsha.monopolygame.Repository.Dao;

import java.math.BigInteger;
import java.util.List;

import com.barsha.monopolygame.Model.GameBalanceTable;

public interface GameBalanceTableDao {
    int                         InsertGameBalanceTable      (GameBalanceTable gameBalanceTable);
    List<GameBalanceTable>      GetGameBalanceTable         (String gameID);
    int                         ArchiveGameBalanceTable     (BigInteger uniqueID);
}
