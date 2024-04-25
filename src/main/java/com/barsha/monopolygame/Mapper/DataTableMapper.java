package com.barsha.monopolygame.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.barsha.monopolygame.Model.DataTable;

public class DataTableMapper implements RowMapper<DataTable>{
    private final Logger logger = Logger.getLogger(DataTableMapper.class);

    @Override
    public DataTable mapRow(ResultSet rs, int rowNum) throws SQLException {
        logger.debug("*** mapRow DataTable *** - START");
        DataTable   dataTable   = new DataTable();

        dataTable.setPlaceValue(rs.getInt("place_value"));
        dataTable.setPlace(rs.getString("place"));
        dataTable.setBuyPrice(rs.getDouble("buy_price"));
        dataTable.setRentPrice(rs.getDouble("rent_price"));

        logger.debug("*** mapRow DataTable *** - END ");
        return dataTable;
    }
    
}
