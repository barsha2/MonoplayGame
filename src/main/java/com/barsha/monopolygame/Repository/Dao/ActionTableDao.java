package com.barsha.monopolygame.Repository.Dao;


import java.util.List;

import com.barsha.monopolygame.Model.ActionTable;

public interface ActionTableDao {
    int                 InsertActionTable       (ActionTable actionTable);
    List<ActionTable>   GetActionTable          (String gameID, String place);
}
