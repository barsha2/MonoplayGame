package com.barsha.monopolygame.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.barsha.monopolygame.Model.OngoingGameTable;

public class OngoingGameTableMapper implements RowMapper<OngoingGameTable>{
    private final Logger logger = Logger.getLogger(OngoingGameTableMapper.class);

    @Override
    public OngoingGameTable mapRow(ResultSet rs, int arg1) throws SQLException {
        logger.debug("*** mapRow OngoingGameTable *** - START");

        OngoingGameTable        ongoingGameTable        = new OngoingGameTable();

        ongoingGameTable.setGameID(rs.getString("game_id"));
        ongoingGameTable.setHostID(rs.getString("host_id"));
        ongoingGameTable.setCoPlayerID(rs.getString("co_player_id"));
        ongoingGameTable.setGameStatus(rs.getString("game_status"));
        ongoingGameTable.setWinnerID(rs.getString("winner_id"));

        logger.debug("*** mapRow OngoingGameTable *** - END ");
        return ongoingGameTable;
    }
    
}
