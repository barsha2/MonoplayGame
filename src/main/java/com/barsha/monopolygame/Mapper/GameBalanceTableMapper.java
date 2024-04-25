package com.barsha.monopolygame.Mapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.barsha.monopolygame.Model.GameBalanceTable;

public class GameBalanceTableMapper implements RowMapper<GameBalanceTable>{
    private final Logger logger = Logger.getLogger(GameBalanceTableMapper.class);

    @Override
    public GameBalanceTable mapRow(ResultSet rs, int rowNum) throws SQLException {
        logger.debug("*** mapRow GameBalanceTable *** - START");
        GameBalanceTable        gameBalanceTable        = new GameBalanceTable();

        gameBalanceTable.setUniqueID(BigInteger.valueOf(rs.getLong("unique_id")));
        gameBalanceTable.setGameID(rs.getString("game_id"));
        gameBalanceTable.setPlayer1UserID(rs.getString("player_1_user_id"));
        gameBalanceTable.setPlayer1Balance(rs.getDouble("player_1_balance"));
        gameBalanceTable.setPlayer2UserID(rs.getString("player_1_user_id"));
        gameBalanceTable.setPlayer2Balance(rs.getDouble("player_2_user_id"));
        gameBalanceTable.setCurrentstatus(rs.getInt("current_status"));

        logger.debug("*** mapRow GameBalanceTable *** - END ");
        return gameBalanceTable;
    }
    
}
