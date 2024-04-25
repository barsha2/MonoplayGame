package com.barsha.monopolygame.Repository.DaoImpl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.barsha.monopolygame.Constants.ApplicationConstant;
import com.barsha.monopolygame.Constants.CommonFunction;
import com.barsha.monopolygame.Constants.SQLQuery;
import com.barsha.monopolygame.Mapper.OngoingGameTableMapper;
import com.barsha.monopolygame.Model.OngoingGameTable;
import com.barsha.monopolygame.Repository.Dao.OngoingGameTableDao;

public class OngoingGameTableDaoImpl implements OngoingGameTableDao{
    private final Logger logger = Logger.getLogger(OngoingGameTableDaoImpl.class);

    @Autowired
    private NamedParameterJdbcTemplate template;
    
    @Override
    public int InsertOngoingGameTable(OngoingGameTable ongoingGameTable) {
        logger.debug("*** InsertOngoingGameTable *** - START");
        int                     functionResult          = ApplicationConstant.ZERO;
        int                     rowsImpacted            = ApplicationConstant.ZERO;
        String                  sqlStatement            = SQLQuery.INSERT_ONGOING_GAME_TABLE;
        MapSqlParameterSource   mapSqlParameterSource   = new MapSqlParameterSource();
        
        mapSqlParameterSource.addValue("gameid", ongoingGameTable.getGameID());
        mapSqlParameterSource.addValue("hostid", ongoingGameTable.getHostID());
        mapSqlParameterSource.addValue("coplayerid", ongoingGameTable.getCoPlayerID());
        mapSqlParameterSource.addValue("gamestatus", ongoingGameTable.getGameStatus());
        mapSqlParameterSource.addValue("winnerid", ongoingGameTable.getWinnerID());

        try {
            rowsImpacted = template.update(sqlStatement, mapSqlParameterSource);
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
        logger.debug("*** InsertOngoingGameTable *** - END ");
        return functionResult;
    }

    @Override
    public List<OngoingGameTable> GetOngoinGameTable(String userID) {
        logger.debug("*** GetOngoinGameTable *** - START");
        List<OngoingGameTable>  ongoingGameTableList    = new ArrayList<>();
        MapSqlParameterSource   mapSqlParameterSource   = new MapSqlParameterSource();
        String                  sqlStatement            = SQLQuery.SELECT_ONGOINT_GAME_TABLE;

        mapSqlParameterSource.addValue("userid", userID);
        mapSqlParameterSource.addValue("currentstatus", ApplicationConstant.CURRENT_STATUS_ACTIVE);
        mapSqlParameterSource.addValue("gamestatus", ApplicationConstant.GAME_STATUS_ONGOING);

        try {
            ongoingGameTableList = template.query(sqlStatement, mapSqlParameterSource, new OngoingGameTableMapper());
        }
        catch (DataAccessException e) {
            logger.error(e);
            return null;
        }
        logger.debug("*** GetOngoinGameTable *** - END ");
        return ongoingGameTableList;
    }

    @Override
    public String GenerateGameID() {
        logger.debug("*** GenerateGameID *** - START");

        Date            date            = new Date(System.currentTimeMillis());
        String          dateString      = ApplicationConstant.SPACES;
        String          gameID          = ApplicationConstant.SPACES;
        String          randomString    = ApplicationConstant.SPACES;

        dateString      = String.valueOf(date);

        gameID          = dateString.substring(0,4) + dateString.substring(5, 7);
        randomString    = CommonFunction.GenerateRandomString(6);
        gameID          = gameID + randomString;

        logger.debug("*** GenerateGameID *** - END ");
        return gameID;
    }

    @Override
    public int ArchiveOngoingGameTable(String gameID) {
        logger.debug("*** DeleteOngoingGameTable *** - START");

        int                     functionResult      = ApplicationConstant.ZERO;
        int                     rowsImpacted        = ApplicationConstant.ZERO;
        String                  sqlStatement        = ApplicationConstant.SPACES;
        MapSqlParameterSource   parameterSource     = new MapSqlParameterSource();

        sqlStatement = SQLQuery.ARCHIVE_ONGOING_GAME_TABLE;
        parameterSource.addValue("currentstatus", ApplicationConstant.CURRENT_STATUS_ARCHIVE);
        parameterSource.addValue("gameid", gameID);
        
        try {
            rowsImpacted = template.update(sqlStatement, parameterSource);
            if (rowsImpacted == ApplicationConstant.ZERO) {
                functionResult = ApplicationConstant.ARCHIVE_UNSUCCESSFUL;
            }
            else if (rowsImpacted == ApplicationConstant.ONE) {
                functionResult = ApplicationConstant.ARCHIVE_SUCCESSFUL;
            }
            else {
                functionResult = ApplicationConstant.ARCHIVE_MULTIPLE_RECORDS;
            }
        }
        catch (DataAccessException e) {
            logger.error(e);
            functionResult = ApplicationConstant.ARCHIVE_DATA_ACCESS_ERROR;
        }

        logger.debug("*** DeleteOngoingGameTable *** - END ");
        return functionResult;
    }
    
}
