package com.barsha.monopolygame.Repository.Dao;

import java.util.List;

import com.barsha.monopolygame.Model.DataTable;

public interface DataTableDao {
    List<DataTable>         GetAllData                  ();
    List<DataTable>         GetDataTableByUniqueID      (int PlaceValue);
    
}
