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
import com.barsha.monopolygame.Mapper.UserTableMapper;
import com.barsha.monopolygame.Model.UserTable;
import com.barsha.monopolygame.Repository.Dao.UserTableDao;

public class UserTableDaoImpl implements UserTableDao{
    private final Logger logger = Logger.getLogger(UserTableDaoImpl.class);

    @Autowired
    private NamedParameterJdbcTemplate template;

    @Override
    public List<UserTable> GetUserTable(String userID) {
        logger.debug("*** GetUserTable *** - START");
        List<UserTable>         userTableList           = new ArrayList<>();
        MapSqlParameterSource   mapSqlParameterSource   = new MapSqlParameterSource();
        String                  sqlStatement            = SQLQuery.SELECT_USER_TABLE;

        mapSqlParameterSource.addValue("userid", userID);

        try {
            userTableList = template.query(sqlStatement, mapSqlParameterSource, new UserTableMapper());
        }
        catch (DataAccessException e) {
            logger.error(e);
            return null;
        }

        logger.debug("*** GetUserTable *** - END ");
        return userTableList;
    }

    @Override
    public int UpdateUserStatus(String userID, int userStatus) {
        logger.debug("*** UpdateUserStatus*** - START");
        int     rowImpacted     = ApplicationConstant.ZERO;
        int     functionResult  = ApplicationConstant.ZERO;
        String  sqlStatement    = ApplicationConstant.SPACES;

        MapSqlParameterSource   parameterSource     = new MapSqlParameterSource();

        sqlStatement = SQLQuery.UPDATE_USER_STATUS;

        parameterSource.addValue("userstatus", userStatus);
        parameterSource.addValue("userid", userID);

        try {
            rowImpacted = template.update(sqlStatement, parameterSource);
            if (rowImpacted == ApplicationConstant.ZERO) {
                functionResult = ApplicationConstant.UPDATE_UNSUCCESSFUL;
            }
            else {
                if (rowImpacted == ApplicationConstant.ONE) {
                    functionResult = ApplicationConstant.UPDATE_SUCCESSFUL;
                }
                else {
                    functionResult = ApplicationConstant.UPDATE_MULTIPLE_RECORDS;
                }
            }
        }
        catch (DataAccessException e) {
            logger.error(e);
            functionResult = ApplicationConstant.UPDATE_DATA_ACCESS_ERROR;
        }
        logger.debug("*** UpdateUserStatus*** - END ");
        return functionResult;
    }
    
}
