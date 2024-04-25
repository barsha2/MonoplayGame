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
import com.barsha.monopolygame.Mapper.GameDetailsTableMapper;
import com.barsha.monopolygame.Model.GameDetailsTable;
import com.barsha.monopolygame.Repository.Dao.GameDetailsTableDao;

public class GameDetailsTableDaoImpl implements GameDetailsTableDao{
    private final Logger logger = Logger.getLogger(GameDetailsTableDaoImpl.class);

    @Autowired
    private NamedParameterJdbcTemplate template;

    @Override
    public int InsertGameDetailsTable(GameDetailsTable gameDetailsTable) {
        logger.debug("*** InsertGameDetailsTable *** - START");
        int                     functionResult          = ApplicationConstant.ZERO;
        int                     rowsImpacted            = ApplicationConstant.ZERO;
        String                  sqlStatement            = SQLQuery.INSERT_GAME_DETAILS_TABLE;
        MapSqlParameterSource   parameterSource         = new MapSqlParameterSource();

        parameterSource.addValue("gameid", gameDetailsTable.getGameID());
        parameterSource.addValue("player1userid", gameDetailsTable.getPlayer1UserID());
        parameterSource.addValue("player1currentposition", gameDetailsTable.getPlayer1CurrentPosition());
        parameterSource.addValue("player2userid", gameDetailsTable.getPlayer2UserID());
        parameterSource.addValue("player2currentposition", gameDetailsTable.getPlayer2CurrentPosition());
        parameterSource.addValue("currentstatus", ApplicationConstant.CURRENT_STATUS_ACTIVE);

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

    @Override
    public List<GameDetailsTable> GetGameDetailsTable(String gameID) {
        logger.debug("*** GetGameDetailsTable *** - START");
        List<GameDetailsTable>      gameDetailsTableList        = new ArrayList<>();
        MapSqlParameterSource       parameterSource             = new MapSqlParameterSource();
        String                      sqlStatement                = SQLQuery.SELECT_GAME_DETAILS_TABLE;

        parameterSource.addValue("gameid", gameID);
        parameterSource.addValue("currentstatus", ApplicationConstant.CURRENT_STATUS_ACTIVE);

        try {
            gameDetailsTableList = template.query(sqlStatement, parameterSource, new GameDetailsTableMapper());
        }
        catch (DataAccessException e) {
            logger.error(e);
            return null;
        }

        logger.debug("*** GetGameDetailsTable *** - END ");
        return gameDetailsTableList;
    }
    
}
