package com.barsha.monopolygame.Mapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.barsha.monopolygame.Model.GameDetailsTable;

public class GameDetailsTableMapper implements RowMapper<GameDetailsTable>{
    private final Logger logger = Logger.getLogger(GameDetailsTableMapper.class);

    @Override
    public GameDetailsTable mapRow(ResultSet rs, int rowNum) throws SQLException {
        logger.debug("*** mapRow GameDetailsTable *** - START");

        GameDetailsTable        gameDetailsTable        = new GameDetailsTable();

        gameDetailsTable.setUniqueID(BigInteger.valueOf(rs.getLong("unique_id")));
        gameDetailsTable.setGameID(rs.getString("game_id"));
        gameDetailsTable.setPlayer1CurrentPosition(rs.getInt("player_1_current_position"));;
        gameDetailsTable.setPlayer2CurrentPosition(rs.getInt("player_2_current_positio"));
        gameDetailsTable.setCurrentStatus(rs.getInt("current_status"));

        logger.debug("*** mapRow GameDetailsTable *** - END ");
        return gameDetailsTable;
    }
    
}
