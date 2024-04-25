package com.barsha.monopolygame.Repository.DaoImpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.barsha.monopolygame.Constants.ApplicationConstant;
import com.barsha.monopolygame.Constants.SQLQuery;
import com.barsha.monopolygame.Mapper.ActionTableMapper;
import com.barsha.monopolygame.Model.ActionTable;
import com.barsha.monopolygame.Repository.Dao.ActionTableDao;

public class ActionTabledaoImpl implements ActionTableDao{
    private final Logger logger = Logger.getLogger(ActionTabledaoImpl.class);

    @Autowired
    private NamedParameterJdbcTemplate template;

    @Override
    public int InsertActionTable(ActionTable actionTable) {
        logger.debug("*** InsertActionTable *** - START");
        int                     functionResult          = ApplicationConstant.ZERO;
        int                     rowsImpacted            = ApplicationConstant.ZERO;
        String                  sqlStatement            = SQLQuery.INSERT_ACTION_TABLE;
        MapSqlParameterSource   parameterSource         = new MapSqlParameterSource();

        parameterSource.addValue("gameid", actionTable.getGameID());
        parameterSource.addValue("userid", actionTable.getUserID());
        parameterSource.addValue("useraction", actionTable.getUserAction());
        parameterSource.addValue("placename", actionTable.getPlaceName());

        try {
            rowsImpacted = template.update(sqlStatement, parameterSource);
            if (rowsImpacted == ApplicationConstant.ZERO) {
                functionResult = ApplicationConstant.INSERT_UNSUCCESSFUL;
            }
            else {
                if (rowsImpacted == ApplicationConstant.ONE) {
                    functionResult = ApplicationConstant.INSERT_SUCCESSFUL;
                }
                else {
                    functionResult = ApplicationConstant.INSERT_MULTIPLE_RECORDS;
                }
            }
        }
        catch (DataAccessException e) {
            logger.error(e);
            functionResult = ApplicationConstant.INSERT_DATA_ACCESS_ERROR;
        }
        logger.debug("*** InsertActionTable *** - END");
        return functionResult;
    }

    @Override
    public List<ActionTable> GetActionTable(String gameID, String place) {
        logger.debug("*** GetActionTable *** - START");
        List<ActionTable>       actionTableList     = new ArrayList<>();
        MapSqlParameterSource   parameterSource     = new MapSqlParameterSource();
        String                  sqlStatement        = SQLQuery.SELECT_ACTION_TABLE;

        parameterSource.addValue("gameid", gameID);
        parameterSource.addValue("placename", place);

        try {
            actionTableList = template.query(sqlStatement, parameterSource, new ActionTableMapper());
        }
        catch (DataAccessException e) {
            logger.error(e);
            return null;
        }
        logger.debug("*** GetActionTable *** - END ");
        return actionTableList;
    }
    
}
