package com.barsha.monopolygame.Repository.DaoImpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.barsha.monopolygame.Constants.SQLQuery;
import com.barsha.monopolygame.Mapper.DataTableMapper;
import com.barsha.monopolygame.Model.DataTable;
import com.barsha.monopolygame.Repository.Dao.DataTableDao;

public class DataTableDaoImpl implements DataTableDao{
    private final Logger logger = Logger.getLogger(DataTableDaoImpl.class);

    @Autowired
    private NamedParameterJdbcTemplate template;

    @Override
    public List<DataTable> GetAllData() {
        logger.debug("*** GetAllData *** - START" );
        List<DataTable>         dataTableList       = new ArrayList<>();
        MapSqlParameterSource   parameterSource     = new MapSqlParameterSource();
        String                  sqlStatement        = SQLQuery.SELECT_DATA_TABLE;

        try {
            dataTableList = template.query(sqlStatement, parameterSource, new DataTableMapper());
        }
        catch (DataAccessException e) {
            logger.error(e);
            return null;
        }
        logger.debug("*** GetAllData *** - END " );
        return dataTableList;
    }

    @Override
    public List<DataTable> GetDataTableByUniqueID(int placeValue) {
        logger.debug("*** GetDataTableByUniqueID *** - START" );
        List<DataTable>         dataTableList       = new ArrayList<>();
        MapSqlParameterSource   parameterSource     = new MapSqlParameterSource();
        String                  sqlStatement        = SQLQuery.SELECT_DATA_TABLE_BY_PLACE_VALUE;

        parameterSource.addValue("placevalue", placeValue);

        try {
            dataTableList = template.query(sqlStatement, parameterSource, new DataTableMapper());
        }
        catch (DataAccessException e) {
            logger.error(e);
            return null;
        }
        
        logger.debug("*** GetDataTableByUniqueID *** - END " );
        return dataTableList;
    }
    
}
