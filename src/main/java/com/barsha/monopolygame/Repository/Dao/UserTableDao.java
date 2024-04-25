package com.barsha.monopolygame.Repository.Dao;

import java.util.List;

import com.barsha.monopolygame.Model.UserTable;

public interface UserTableDao {
    List<UserTable>         GetUserTable        (String userID);
    int                     UpdateUserStatus    (String userID, int userStatus);
}
