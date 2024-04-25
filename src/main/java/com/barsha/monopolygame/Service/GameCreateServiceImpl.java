package com.barsha.monopolygame.Service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barsha.monopolygame.Constants.ApplicationConstant;
import com.barsha.monopolygame.Constants.MonopolyGameError;
import com.barsha.monopolygame.Model.CommonResponse;
import com.barsha.monopolygame.Model.GameBalanceTable;
import com.barsha.monopolygame.Model.GameCreateRequest;
import com.barsha.monopolygame.Model.GameDetailsTable;
import com.barsha.monopolygame.Model.MonopolyGameResponse;
import com.barsha.monopolygame.Model.OngoingGameTable;
import com.barsha.monopolygame.Model.UserTable;
import com.barsha.monopolygame.Repository.Dao.GameBalanceTableDao;
import com.barsha.monopolygame.Repository.Dao.GameDetailsTableDao;
import com.barsha.monopolygame.Repository.Dao.OngoingGameTableDao;
import com.barsha.monopolygame.Repository.Dao.UserTableDao;

@Service
public class GameCreateServiceImpl implements GameCreateService{

    private final Logger logger = Logger.getLogger(GameCreateServiceImpl.class);

    @Autowired
    UserTableDao userTableDao;

    @Autowired
    OngoingGameTableDao ongoingGameTableDao;

    @Autowired
    GameDetailsTableDao gameDetailsTableDao;

    @Autowired
    GameBalanceTableDao gameBalanceTableDao;

    @Override
    public MonopolyGameResponse CreateGame(GameCreateRequest gameCreateRequest) {
        logger.debug("*** CreateGame *** - START");

        OngoingGameTable        ongoingGameTable    = new OngoingGameTable();
        List<UserTable>         userTableList       = new ArrayList<>();
        UserTable               userTable           = new UserTable();
        List<String>            errorList           = new ArrayList<>();
        List<OngoingGameTable>  ongoingGameTableList= new ArrayList<>();
        GameDetailsTable        gameDetailsTable    = new GameDetailsTable();
        GameBalanceTable        gameBalanceTable    = new GameBalanceTable();
        CommonResponse          commonResponse      = new CommonResponse();
        MonopolyGameResponse    monopolyGameResponse= new MonopolyGameResponse();
        
        int     functionResult      = ApplicationConstant.ZERO;
        String  hostID              = ApplicationConstant.SPACES;
        String  coPlayerID          = ApplicationConstant.SPACES;
        String  errorCode           = ApplicationConstant.SPACES;
        String  gameID              = ApplicationConstant.SPACES;
        String  nextStep            = ApplicationConstant.NEXT_STEP_TO_CONTINUE;

        hostID      = gameCreateRequest.getHostID();
        coPlayerID  = gameCreateRequest.getCoPlayerID();

        userTableList   = userTableDao.GetUserTable(hostID);

        if (userTableList != null) {
            if (userTableList.size()>0) {
                userTable = userTableList.get(0);

                if (userTable.getUserStatus() == ApplicationConstant.USER_STATUS_PLAYING){
                    ongoingGameTableList = ongoingGameTableDao.GetOngoinGameTable(hostID);

                    if (ongoingGameTableList != null) {
                        if (ongoingGameTableList.size()>0){
                            ongoingGameTable    = ongoingGameTableList.get(0);
                            gameID              = ongoingGameTable.getGameID();

                            functionResult      = ongoingGameTableDao.ArchiveOngoingGameTable(gameID);

                            switch (functionResult) {
                                case ApplicationConstant.ARCHIVE_SUCCESSFUL :
                                    nextStep            = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                                break;
                                case ApplicationConstant.ARCHIVE_UNSUCCESSFUL :
                                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                    errorCode       = MonopolyGameError.ARCHIVE_ONGOING_GAME_TABLE_NOT_SUCCESSFUL;
                                    errorList.add(errorCode);
                                break;
                                case ApplicationConstant.ARCHIVE_MULTIPLE_RECORDS :
                                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                    errorCode       = MonopolyGameError.ARCHIVE_ONGOING_GAME_TABLE_INCORRECT;
                                    errorList.add(errorCode);
                                break;
                                case ApplicationConstant.ARCHIVE_DATA_ACCESS_ERROR :
                                    errorCode       = MonopolyGameError.DATA_ACCESS_ERROR;
                                    errorList.add(errorCode);
                                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                break;
                            }
                        }
                        else{
                            nextStep = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                        }
                    }
                    else {
                        errorCode       = MonopolyGameError.DATA_ACCESS_ERROR;
                        errorList.add(errorCode);
                        nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                    }

                    if (nextStep.equals(ApplicationConstant.NEXT_STEP_TO_CONTINUE)) {
                        functionResult = userTableDao.UpdateUserStatus(hostID, ApplicationConstant.USER_STATUS_NOT_PLAYING);

                        switch (functionResult) {
                            case ApplicationConstant.UPDATE_SUCCESSFUL :
                                nextStep            = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                            break;
                            case ApplicationConstant.UPDATE_UNSUCCESSFUL :
                                nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                errorCode       = MonopolyGameError.UPDATE_USER_TABLE_NOT_SUCCESSFUL;
                                errorList.add(errorCode);
                            break;
                            case ApplicationConstant.UPDATE_MULTIPLE_RECORDS :
                                nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                errorCode       = MonopolyGameError.UPDATE_USER_TABLE_INCORRECT;
                                errorList.add(errorCode);
                            break;
                            case ApplicationConstant.UPDATE_DATA_ACCESS_ERROR :
                                errorCode       = MonopolyGameError.DATA_ACCESS_ERROR;
                                errorList.add(errorCode);
                                nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                            break;
                        }
                    }
                }
                else {
                    nextStep = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                }
            }
            else {
                nextStep = ApplicationConstant.NEXT_STEP_TO_STOP;
            }
        }
        else {
            errorCode       = MonopolyGameError.DATA_ACCESS_ERROR;
            errorList.add(errorCode);
            nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
        }

        if (nextStep.equals(ApplicationConstant.NEXT_STEP_TO_CONTINUE)) {
            ongoingGameTable    = new OngoingGameTable();

            gameID = ongoingGameTableDao.GenerateGameID();

            ongoingGameTable.setGameID(gameID);
            ongoingGameTable.setHostID(hostID);
            ongoingGameTable.setCoPlayerID(coPlayerID);
            ongoingGameTable.setGameStatus(ApplicationConstant.GAME_STATUS_ONGOING);
            
            functionResult = ongoingGameTableDao.InsertOngoingGameTable(ongoingGameTable);

            switch (functionResult) {
                case ApplicationConstant.INSERT_SUCCESSFUL :
                    nextStep            = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                break;
                case ApplicationConstant.INSERT_UNSUCCESSFUL :
                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                    errorCode       = MonopolyGameError.INSERT_ONGOING_GAME_TABLE_UNSUCCESSFUL;
                    errorList.add(errorCode);
                break;
                case ApplicationConstant.INSERT_MULTIPLE_RECORDS :
                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                    errorCode       = MonopolyGameError.INSERT_ONGOING_GAME_TABLE_INCORRECT;
                    errorList.add(errorCode);
                break;
                case ApplicationConstant.INSERT_DATA_ACCESS_ERROR:
                    errorCode       = MonopolyGameError.DATA_ACCESS_ERROR;
                    errorList.add(errorCode);
                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                break;
            }
        }

        if (nextStep.equals(ApplicationConstant.NEXT_STEP_TO_CONTINUE)) {
            gameDetailsTable.setGameID(gameID);
            gameDetailsTable.setPlayer1UserID(hostID);
            gameDetailsTable.setPlayer1CurrentPosition(ApplicationConstant.ZERO);
            gameDetailsTable.setPlayer2UserID(coPlayerID);
            gameDetailsTable.setPlayer2CurrentPosition(ApplicationConstant.ZERO);
            gameDetailsTable.setCurrentStatus(ApplicationConstant.CURRENT_STATUS_ACTIVE);

            functionResult = gameDetailsTableDao.InsertGameDetailsTable(gameDetailsTable);

            switch (functionResult) {
                case ApplicationConstant.INSERT_SUCCESSFUL :
                    nextStep            = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                break;
                case ApplicationConstant.INSERT_UNSUCCESSFUL :
                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                    errorCode       = MonopolyGameError.INSERT_GAME_DETAILS_TABLE_NOT_SUCCESSFUL;
                    errorList.add(errorCode);
                break;
                case ApplicationConstant.INSERT_MULTIPLE_RECORDS :
                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                    errorCode       = MonopolyGameError.INSERT_GAME_DETAILS_TABLE_INCORRECT;
                    errorList.add(errorCode);
                break;
                case ApplicationConstant.INSERT_DATA_ACCESS_ERROR:
                    errorCode       = MonopolyGameError.DATA_ACCESS_ERROR;
                    errorList.add(errorCode);
                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                break;
            }
        }

        if (nextStep.equals(ApplicationConstant.NEXT_STEP_TO_CONTINUE)) {
            gameBalanceTable.setGameID(gameID);
            gameBalanceTable.setPlayer1UserID(hostID);
            gameBalanceTable.setPlayer1Balance(1000);
            gameBalanceTable.setPlayer2UserID(coPlayerID);
            gameBalanceTable.setPlayer2Balance(1000);

            functionResult = gameBalanceTableDao.InsertGameBalanceTable(gameBalanceTable);

            switch (functionResult) {
                case ApplicationConstant.INSERT_SUCCESSFUL :
                    nextStep            = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                break;
                case ApplicationConstant.INSERT_UNSUCCESSFUL :
                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                    errorCode       = MonopolyGameError.INSERT_GAME_BALNCE_TABLE_NOT_SUCCESSFUL;
                    errorList.add(errorCode);
                break;
                case ApplicationConstant.INSERT_MULTIPLE_RECORDS :
                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                    errorCode       = MonopolyGameError.INSERT_GAME_DETAILS_TABLE_INCORRECT;
                    errorList.add(errorCode);
                break;
                case ApplicationConstant.INSERT_DATA_ACCESS_ERROR:
                    errorCode       = MonopolyGameError.DATA_ACCESS_ERROR;
                    errorList.add(errorCode);
                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                break;
            }
        }

        if (nextStep.equals(ApplicationConstant.NEXT_STEP_TO_CONTINUE)) {
            commonResponse.setTransactionResult(ApplicationConstant.TRANSACTION_RESULT_SUCCESS);;
            commonResponse.setMessege("Game Created Successfully");
        }
        else {
            commonResponse.setTransactionResult(ApplicationConstant.TRANSACTION_RESULT_FAILURE);
        }

        monopolyGameResponse.setApiResponse(commonResponse);;
        monopolyGameResponse.setErrorList(errorList);
        logger.debug("*** CreateGame *** - END ");
        return monopolyGameResponse;
    }
    
}
