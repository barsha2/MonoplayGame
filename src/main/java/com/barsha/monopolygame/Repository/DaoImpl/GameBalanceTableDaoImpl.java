package com.barsha.monopolygame.Repository.DaoImpl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.barsha.monopolygame.Constants.ApplicationConstant;
import com.barsha.monopolygame.Constants.SQLQuery;
import com.barsha.monopolygame.Model.GameBalanceTable;
import com.barsha.monopolygame.Repository.Dao.GameBalanceTableDao;

public class GameBalanceTableDaoImpl implements GameBalanceTableDao{
    private final Logger logger = Logger.getLogger(GameBalanceTableDaoImpl.class);

    @Autowired
    private NamedParameterJdbcTemplate template;

    @Override
    public int InsertGameBalanceTable(GameBalanceTable gameBalanceTable) {
        logger.debug("*** InsertGameDetailsTable *** - START");
        int                     functionResult          = ApplicationConstant.ZERO;
        int                     rowsImpacted            = ApplicationConstant.ZERO;
        String                  sqlStatement            = SQLQuery.INSERT_GAME_BALANCE_TABLE;    
        MapSqlParameterSource   parameterSource         = new MapSqlParameterSource();

        parameterSource.addValue("gameid", gameBalanceTable.getGameID());
        parameterSource.addValue("player1balance", gameBalanceTable.getPlayer1Balance());
        parameterSource.addValue("player2balance", gameBalanceTable.getPlayer2Balance());

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

        logger.debug("*** InsertGameDetailsTable *** - END ");
        return functionResult;
    }
    
}
