package com.barsha.monopolygame.Repository.Dao;

import java.util.List;

import com.barsha.monopolygame.Model.OngoingGameTable;

public interface OngoingGameTableDao {
    int                         InsertOngoingGameTable      (OngoingGameTable ongoingGameTable);
    List<OngoingGameTable>      GetOngoinGameTable          (String userID);
    String                      GenerateGameID              ();
    int                         ArchiveOngoingGameTable     (String gameID);
} 
