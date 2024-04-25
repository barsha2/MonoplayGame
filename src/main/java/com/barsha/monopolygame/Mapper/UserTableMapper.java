package com.barsha.monopolygame.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.barsha.monopolygame.Model.UserTable;

public class UserTableMapper implements RowMapper<UserTable>{

    private final Logger logger = Logger.getLogger(UserTableMapper.class);

    @Override
    public UserTable mapRow(ResultSet rs, int arg1) throws SQLException {
        logger.debug("*** mapRow UserTable *** - START");

        UserTable       userTable       = new UserTable();

        userTable.setUserID(rs.getString("user_id"));
        userTable.setUserStatus(rs.getInt("user_status"));

        logger.debug("*** mapRow UserTable *** - END ");
        return userTable;
    }
    
}
