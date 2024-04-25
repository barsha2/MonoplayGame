package com.barsha.monopolygame.Constants;

public class SQLQuery {
    public static final String INSERT_ONGOING_GAME_TABLE            = "INSERT TABLE tbl_ongoing_game (game_id, host_id, co_player_id, game_status, winner_id) VALUE (:gameid, :hostid, :coplayerid, :gamestatus, :winnerid)";
    public static final String SELECT_USER_TABLE                    = "SELECT * FROM tbl_user WHERE user_id = :userid";
    public static final String UPDATE_USER_STATUS                   = "UPDATE tbl_user SET user_status = :userstatus WHERE user_id = :userid";
    public static final String SELECT_ONGOINT_GAME_TABLE            = "SELECT * FROM tbl_ongoing_game WHERE host_id = :userid OR co_player_id = :userid AND game_status = :gamestatus AND current_status = :currentstatus";
    public static final String ARCHIVE_ONGOING_GAME_TABLE           = "UPDATE TABLE tbl_ongoing_table SET current_status = :currentstaus WHERE game_id = :gameid";
    public static final String INSERT_GAME_DETAILS_TABLE            = "INSERT INTO tbl_game_details (game_id, player_1_user_id, player_1_current_position, player_2_user_id, player_2_current_position, current_status) VALUES (:gameid, :player1userid, :player1currentposition, :player2userid, :player2currentposition, :currentstatus)";
    public static final String INSERT_GAME_BALANCE_TABLE            = "INSERT INTO tbl_game_balance (game_id, player_1_user_id, player_1_balance, player_2_user_id, player_2_balance, current_status) VALUES (:gameid, player1userid, :player1balance, player2userid, :player2balance, currentstatus)";
    public static final String SELECT_GAME_DETAILS_TABLE            = "SELECT tbl_game_details WHERE game_id = :gameid AND current_status = :currentstatus";
    public static final String SELECT_DATA_TABLE                    = "SEELECT * FROM tbl_data";
    public static final String SELECT_DATA_TABLE_BY_PLACE_VALUE     = "SELECT * FROM tbl_data WHERE place_value = :placevalue";
    public static final String INSERT_ACTION_TABLE                  = "INSERT INTO tbl_action (game_id, user_id, user_action, place_name) VALUES (:gameid, :userid, :useraction, :placename)";
    public static final String SELECT_ACTION_TABLE                  = "SELECT * FROM tbl_action WHERE game_id = :gameid AND place_name = :placename";
    public static final String SELECT_GAME_BALANCE_TABLE_BY_GAME_ID = "SELECT * FROM tbl_game_balance WHER game_id = :gameid";
    public static final String ARCHIVE_GAME_BALANCE_TABLE           = "UPDATE TABLE tbl_game_balance SET current_status = :currentstatus WHERE unique_id = :uniqueid";
}
