package com.barsha.monopolygame.Mapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.barsha.monopolygame.Model.ActionTable;

public class ActionTableMapper implements RowMapper<ActionTable>{
    private final Logger logger = Logger.getLogger(ActionTableMapper.class);

    @Override
    public ActionTable mapRow(ResultSet rs, int rowNum) throws SQLException {
        logger.debug("*** mapRow ActionTable *** - START");

        ActionTable         actionTable         = new ActionTable();

        actionTable.setUniqueID(BigInteger.valueOf(rs.getLong("unique_id")));
        actionTable.setGameID(rs.getString("game_id"));
        actionTable.setUserID(rs.getString("user_id"));
        actionTable.setUserAction(rs.getString("user_action"));
        actionTable.setPlaceName(rs.getString("place_name"));
        logger.debug("*** mapRow ActionTable *** - END ");
        return actionTable;
    }
    
}
