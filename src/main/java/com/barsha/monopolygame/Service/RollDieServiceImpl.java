package com.barsha.monopolygame.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.barsha.monopolygame.Constants.ApplicationConstant;
import com.barsha.monopolygame.Constants.CommonFunction;
import com.barsha.monopolygame.Constants.MonopolyGameError;
import com.barsha.monopolygame.Model.ActionTable;
import com.barsha.monopolygame.Model.CommonResponse;
import com.barsha.monopolygame.Model.DataTable;
import com.barsha.monopolygame.Model.GameBalanceTable;
import com.barsha.monopolygame.Model.GameDetailsTable;
import com.barsha.monopolygame.Model.MonopolyGameResponse;
import com.barsha.monopolygame.Model.OngoingGameTable;
import com.barsha.monopolygame.Model.RollDieRequest;
import com.barsha.monopolygame.Repository.Dao.ActionTableDao;
import com.barsha.monopolygame.Repository.Dao.DataTableDao;
import com.barsha.monopolygame.Repository.Dao.GameBalanceTableDao;
import com.barsha.monopolygame.Repository.Dao.GameDetailsTableDao;
import com.barsha.monopolygame.Repository.Dao.OngoingGameTableDao;

public class RollDieServiceImpl implements RollDieService{
    private final Logger logger = Logger.getLogger(RollDieServiceImpl.class);

    @Autowired
    GameDetailsTableDao gameDetailsTableDao;

    @Autowired
    DataTableDao dataTableDao;

    @Autowired
    ActionTableDao actionTableDao;

    @Autowired
    GameBalanceTableDao gameBalanceTableDao;

    @Autowired
    OngoingGameTableDao ongoingGameTableDao;

    @Override
    public MonopolyGameResponse RollDie(RollDieRequest rollDieRequest) {
        logger.debug("*** RollDie *** - START");

        List<GameDetailsTable>      gameDetailsTableList    = new ArrayList<>();
        GameDetailsTable            gameDetailsTable        = new GameDetailsTable();
        List<String>                errorList               = new ArrayList<>();
        List<DataTable>             dataTableList           = new ArrayList<>();
        DataTable                   dataTable               = new DataTable();
        List<ActionTable>           actionTableList         = new ArrayList<>();
        ActionTable                 actionTable             = new ActionTable();
        List<GameBalanceTable>      gameBalanceTableList    = new ArrayList<>();
        GameBalanceTable            gameBalanceTable        = new GameBalanceTable();
        List<OngoingGameTable>      ongoingGameTableList    = new ArrayList<>();
        OngoingGameTable            ongoingGameTable        = new OngoingGameTable();
        CommonResponse              commonResponse          = new CommonResponse();
        MonopolyGameResponse        monopolyGameResponse    = new MonopolyGameResponse();

        BigInteger  uniqueID         = BigInteger.ZERO;
        double      buyPrice        = ApplicationConstant.ZERO;
        double      rentPrice       = ApplicationConstant.ZERO;
        double      previousBalance = ApplicationConstant.ZERO;
        double      currentBalance  = ApplicationConstant.ZERO;
        int         die1Value       = ApplicationConstant.ZERO;
        int         die2Value       = ApplicationConstant.ZERO;
        int         totalValue      = ApplicationConstant.ZERO;
        int         totalData       = ApplicationConstant.ZERO;
        int         position        = ApplicationConstant.ZERO;
        int         nextPosition    = ApplicationConstant.ZERO;
        int         placeValue      = ApplicationConstant.ZERO;
        int         functionResult  = ApplicationConstant.ZERO;
        String      errorCode       = ApplicationConstant.SPACES;
        String      gameID          = ApplicationConstant.SPACES;
        String      userID          = ApplicationConstant.SPACES;
        String      place           = ApplicationConstant.SPACES;
        String      nextStep        = ApplicationConstant.NEXT_STEP_TO_CONTINUE;

        gameID      = rollDieRequest.getGameID();
        userID      = rollDieRequest.getUserID();

        die1Value   = CommonFunction.GenerateRandomNumber(1, 6);
        die2Value   = CommonFunction.GenerateRandomNumber(1, 6);

        totalValue  = die1Value + die2Value;
        
        dataTableList       = dataTableDao.GetAllData();
        if (dataTableList != null) {
            if (dataTableList.size()>0) {
                nextStep = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                totalData = dataTableList.size();
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
            gameDetailsTableList = gameDetailsTableDao.GetGameDetailsTable(gameID);

            if (gameDetailsTableList != null) {
                if (gameDetailsTableList.size() > 0) {
                    gameDetailsTable = gameDetailsTableList.get(0);

                    if (gameDetailsTable.getPlayer1UserID().equals(userID)){
                        nextStep = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                        position = gameDetailsTable.getPlayer1CurrentPosition();
                    }
                    else if (gameDetailsTable.getPlayer2UserID().equals(userID)) {
                        nextStep = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                        position = gameDetailsTable.getPlayer2CurrentPosition();
                    }
                    else {
                        //NO OPERATIONS
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
        }
        
        if (nextStep.equals(ApplicationConstant.NEXT_STEP_TO_CONTINUE)) {
            nextPosition = totalValue + position;

            if (nextPosition > totalData) {
                placeValue = nextPosition % totalData;
            }
            else {
                placeValue = nextPosition;
            }


            dataTableList = dataTableDao.GetDataTableByUniqueID(placeValue);

            if (dataTableList != null) {
                if (dataTableList.size() > 0) {
                    dataTable   = dataTableList.get(0);
                    place       = dataTable.getPlace();
                    buyPrice    = dataTable.getBuyPrice();
                    rentPrice   = dataTable.getRentPrice();
                    nextStep    = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                }
                else {
                    nextStep = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                }
            }
            else {
                errorCode       = MonopolyGameError.DATA_ACCESS_ERROR;
                errorList.add(errorCode);
                nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
            }
        }

        if (nextStep.equals(ApplicationConstant.NEXT_STEP_TO_CONTINUE)) {
            actionTableList = actionTableDao.GetActionTable(gameID, place);

            if (actionTableList != null) {
                if (actionTableList.size()>0) {
                    actionTable = actionTableList.get(0);

                    if (actionTable.getUserAction().equals(ApplicationConstant.USER_ACTION_BUY)) {
                        //NO OPERATIONS
                    }
                    else {
                        gameBalanceTableList = gameBalanceTableDao.GetGameBalanceTable(gameID);

                        if (gameBalanceTableList != null) {
                            if (gameBalanceTableList.size()> 0) {
                                gameBalanceTable = gameBalanceTableList.get(0);
                                uniqueID        = gameBalanceTable.getUniqueID();
                                
                                if (gameBalanceTable.getPlayer1UserID().equals(userID)) {
                                    previousBalance = gameBalanceTable.getPlayer1Balance();
                                    currentBalance  = previousBalance - rentPrice;
                                    if (currentBalance > 50) {
                                        functionResult  = gameBalanceTableDao.ArchiveGameBalanceTable(uniqueID);
    
                                        switch (functionResult) {
                                            case ApplicationConstant.ARCHIVE_SUCCESSFUL :
                                                nextStep            = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                                            break;
                                            case ApplicationConstant.ARCHIVE_UNSUCCESSFUL :
                                                nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                                errorCode       = MonopolyGameError.ARCHIVE_GAME_BALNCE_TABLE_UNSUCCESSFUL;
                                                errorList.add(errorCode);
                                            break;
                                            case ApplicationConstant.ARCHIVE_MULTIPLE_RECORDS :
                                                nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                                errorCode       = MonopolyGameError.ARCHIVE_GAME_BALANCE_TABLE_INCORRECT;
                                                errorList.add(errorCode);
                                            break;
                                            case ApplicationConstant.ARCHIVE_DATA_ACCESS_ERROR :
                                                errorCode       = MonopolyGameError.DATA_ACCESS_ERROR;
                                                errorList.add(errorCode);
                                                nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                            break;
                                        }
    
                                        if (nextStep.equals(ApplicationConstant.NEXT_STEP_TO_CONTINUE)) {
                                            gameBalanceTable.setPlayer1Balance(currentBalance);
                                            
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
                                            actionTable.setGameID(gameID);
                                            actionTable.setUserID(userID);
                                            actionTable.setUserAction(ApplicationConstant.USER_ACTION_RENT);
                                            actionTable.setPlaceName(place);
    
                                            functionResult = actionTableDao.InsertActionTable(actionTable);
    
                                            switch (functionResult) {
                                                case ApplicationConstant.INSERT_SUCCESSFUL :
                                                    nextStep            = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                                                break;
                                                case ApplicationConstant.INSERT_UNSUCCESSFUL :
                                                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                                    errorCode       = MonopolyGameError.INSERT_INTO_ACTION_TABLE_NOT_SUCCESSFUL;
                                                    errorList.add(errorCode);
                                                break;
                                                case ApplicationConstant.INSERT_MULTIPLE_RECORDS :
                                                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                                    errorCode       = MonopolyGameError.INSERT_INTO_ACTION_TABLE_INCORRECT;
                                                    errorList.add(errorCode);
                                                break;
                                                case ApplicationConstant.INSERT_DATA_ACCESS_ERROR:
                                                    errorCode       = MonopolyGameError.DATA_ACCESS_ERROR;
                                                    errorList.add(errorCode);
                                                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                                break;
                                            }
                                        }
                                    }
                                    else {
                                        ongoingGameTableList = ongoingGameTableDao.GetOngoinGameTable(userID);
    
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
    
                                                if (nextStep.equals(ApplicationConstant.NEXT_STEP_TO_CONTINUE)) {
                                                    ongoingGameTable.setGameStatus(ApplicationConstant.GAME_STATUS_END);
        
                                                    functionResult = ongoingGameTableDao.InsertOngoingGameTable(ongoingGameTable);
    
                                                    switch (functionResult) {
                                                        case ApplicationConstant.INSERT_SUCCESSFUL :
                                                            commonResponse.setTransactionResult(ApplicationConstant.TRANSACTION_RESULT_SUCCESS);
                                                            commonResponse.setMessege("Game Over! You losse " + userID);
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
                                    } 
                                }
                                else if (gameBalanceTable.getPlayer2UserID().equals(userID)) {
                                    previousBalance = gameBalanceTable.getPlayer2Balance();
                                    currentBalance  = previousBalance - rentPrice;

                                    if (currentBalance > 50) {
                                        functionResult  = gameBalanceTableDao.ArchiveGameBalanceTable(uniqueID);
    
                                        switch (functionResult) {
                                            case ApplicationConstant.ARCHIVE_SUCCESSFUL :
                                                nextStep            = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                                            break;
                                            case ApplicationConstant.ARCHIVE_UNSUCCESSFUL :
                                                nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                                errorCode       = MonopolyGameError.ARCHIVE_GAME_BALNCE_TABLE_UNSUCCESSFUL;
                                                errorList.add(errorCode);
                                            break;
                                            case ApplicationConstant.ARCHIVE_MULTIPLE_RECORDS :
                                                nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                                errorCode       = MonopolyGameError.ARCHIVE_GAME_BALANCE_TABLE_INCORRECT;
                                                errorList.add(errorCode);
                                            break;
                                            case ApplicationConstant.ARCHIVE_DATA_ACCESS_ERROR :
                                                errorCode       = MonopolyGameError.DATA_ACCESS_ERROR;
                                                errorList.add(errorCode);
                                                nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                            break;
                                        }
    
                                        if (nextStep.equals(ApplicationConstant.NEXT_STEP_TO_CONTINUE)) {
                                            gameBalanceTable.setPlayer1Balance(currentBalance);
                                            
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
                                            actionTable.setGameID(gameID);
                                            actionTable.setUserID(userID);
                                            actionTable.setUserAction(ApplicationConstant.USER_ACTION_RENT);
                                            actionTable.setPlaceName(place);
    
                                            functionResult = actionTableDao.InsertActionTable(actionTable);
    
                                            switch (functionResult) {
                                                case ApplicationConstant.INSERT_SUCCESSFUL :
                                                    nextStep            = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                                                break;
                                                case ApplicationConstant.INSERT_UNSUCCESSFUL :
                                                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                                    errorCode       = MonopolyGameError.INSERT_INTO_ACTION_TABLE_NOT_SUCCESSFUL;
                                                    errorList.add(errorCode);
                                                break;
                                                case ApplicationConstant.INSERT_MULTIPLE_RECORDS :
                                                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                                    errorCode       = MonopolyGameError.INSERT_INTO_ACTION_TABLE_INCORRECT;
                                                    errorList.add(errorCode);
                                                break;
                                                case ApplicationConstant.INSERT_DATA_ACCESS_ERROR:
                                                    errorCode       = MonopolyGameError.DATA_ACCESS_ERROR;
                                                    errorList.add(errorCode);
                                                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                                break;
                                            }
                                        }
                                    }
                                    else {
                                        ongoingGameTableList = ongoingGameTableDao.GetOngoinGameTable(userID);
    
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
    
                                                if (nextStep.equals(ApplicationConstant.NEXT_STEP_TO_CONTINUE)) {
                                                    ongoingGameTable.setGameStatus(ApplicationConstant.GAME_STATUS_END);
        
                                                    functionResult = ongoingGameTableDao.InsertOngoingGameTable(ongoingGameTable);
    
                                                    switch (functionResult) {
                                                        case ApplicationConstant.INSERT_SUCCESSFUL :
                                                            commonResponse.setTransactionResult(ApplicationConstant.TRANSACTION_RESULT_SUCCESS);
                                                            commonResponse.setMessege("Game Over! You lose " + userID);
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
                                    } 
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
                    }
                    
                }
                else {

                    if (gameBalanceTable.getPlayer1UserID().equals(userID)) {
                        previousBalance = gameBalanceTable.getPlayer1Balance();
                        currentBalance  = previousBalance - buyPrice;
                        if (currentBalance > 50) {
                            functionResult  = gameBalanceTableDao.ArchiveGameBalanceTable(uniqueID);

                            switch (functionResult) {
                                case ApplicationConstant.ARCHIVE_SUCCESSFUL :
                                    nextStep            = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                                break;
                                case ApplicationConstant.ARCHIVE_UNSUCCESSFUL :
                                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                    errorCode       = MonopolyGameError.ARCHIVE_GAME_BALNCE_TABLE_UNSUCCESSFUL;
                                    errorList.add(errorCode);
                                break;
                                case ApplicationConstant.ARCHIVE_MULTIPLE_RECORDS :
                                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                    errorCode       = MonopolyGameError.ARCHIVE_GAME_BALANCE_TABLE_INCORRECT;
                                    errorList.add(errorCode);
                                break;
                                case ApplicationConstant.ARCHIVE_DATA_ACCESS_ERROR :
                                    errorCode       = MonopolyGameError.DATA_ACCESS_ERROR;
                                    errorList.add(errorCode);
                                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                break;
                            }

                            if (nextStep.equals(ApplicationConstant.NEXT_STEP_TO_CONTINUE)) {
                                gameBalanceTable.setPlayer1Balance(currentBalance);
                                
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
                                actionTable.setGameID(gameID);
                                actionTable.setUserID(userID);
                                actionTable.setUserAction(ApplicationConstant.USER_ACTION_BUY);
                                actionTable.setPlaceName(place);

                                functionResult = actionTableDao.InsertActionTable(actionTable);

                                switch (functionResult) {
                                    case ApplicationConstant.INSERT_SUCCESSFUL :
                                        nextStep            = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                                    break;
                                    case ApplicationConstant.INSERT_UNSUCCESSFUL :
                                        nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                        errorCode       = MonopolyGameError.INSERT_INTO_ACTION_TABLE_NOT_SUCCESSFUL;
                                        errorList.add(errorCode);
                                    break;
                                    case ApplicationConstant.INSERT_MULTIPLE_RECORDS :
                                        nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                        errorCode       = MonopolyGameError.INSERT_INTO_ACTION_TABLE_INCORRECT;
                                        errorList.add(errorCode);
                                    break;
                                    case ApplicationConstant.INSERT_DATA_ACCESS_ERROR:
                                        errorCode       = MonopolyGameError.DATA_ACCESS_ERROR;
                                        errorList.add(errorCode);
                                        nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                    break;
                                }
                            }
                        }
                        else {
                            ongoingGameTableList = ongoingGameTableDao.GetOngoinGameTable(userID);

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

                                    if (nextStep.equals(ApplicationConstant.NEXT_STEP_TO_CONTINUE)) {
                                        ongoingGameTable.setGameStatus(ApplicationConstant.GAME_STATUS_END);

                                        functionResult = ongoingGameTableDao.InsertOngoingGameTable(ongoingGameTable);

                                        switch (functionResult) {
                                            case ApplicationConstant.INSERT_SUCCESSFUL :
                                                commonResponse.setTransactionResult(ApplicationConstant.TRANSACTION_RESULT_SUCCESS);
                                                commonResponse.setMessege("Game Over! You lose " +userID);
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
                        } 
                    }
                    else if (gameBalanceTable.getPlayer2UserID().equals(userID)) {
                        previousBalance = gameBalanceTable.getPlayer2Balance();
                        currentBalance  = previousBalance - buyPrice;

                        if (currentBalance > 50) {
                            functionResult  = gameBalanceTableDao.ArchiveGameBalanceTable(uniqueID);

                            switch (functionResult) {
                                case ApplicationConstant.ARCHIVE_SUCCESSFUL :
                                    nextStep            = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                                break;
                                case ApplicationConstant.ARCHIVE_UNSUCCESSFUL :
                                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                    errorCode       = MonopolyGameError.ARCHIVE_GAME_BALNCE_TABLE_UNSUCCESSFUL;
                                    errorList.add(errorCode);
                                break;
                                case ApplicationConstant.ARCHIVE_MULTIPLE_RECORDS :
                                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                    errorCode       = MonopolyGameError.ARCHIVE_GAME_BALANCE_TABLE_INCORRECT;
                                    errorList.add(errorCode);
                                break;
                                case ApplicationConstant.ARCHIVE_DATA_ACCESS_ERROR :
                                    errorCode       = MonopolyGameError.DATA_ACCESS_ERROR;
                                    errorList.add(errorCode);
                                    nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                break;
                            }

                            if (nextStep.equals(ApplicationConstant.NEXT_STEP_TO_CONTINUE)) {
                                gameBalanceTable.setPlayer1Balance(currentBalance);
                                
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
                                actionTable.setGameID(gameID);
                                actionTable.setUserID(userID);
                                actionTable.setUserAction(ApplicationConstant.USER_ACTION_BUY);
                                actionTable.setPlaceName(place);

                                functionResult = actionTableDao.InsertActionTable(actionTable);

                                switch (functionResult) {
                                    case ApplicationConstant.INSERT_SUCCESSFUL :
                                        nextStep            = ApplicationConstant.NEXT_STEP_TO_CONTINUE;
                                    break;
                                    case ApplicationConstant.INSERT_UNSUCCESSFUL :
                                        nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                        errorCode       = MonopolyGameError.INSERT_INTO_ACTION_TABLE_NOT_SUCCESSFUL;
                                        errorList.add(errorCode);
                                    break;
                                    case ApplicationConstant.INSERT_MULTIPLE_RECORDS :
                                        nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                        errorCode       = MonopolyGameError.INSERT_INTO_ACTION_TABLE_INCORRECT;
                                        errorList.add(errorCode);
                                    break;
                                    case ApplicationConstant.INSERT_DATA_ACCESS_ERROR:
                                        errorCode       = MonopolyGameError.DATA_ACCESS_ERROR;
                                        errorList.add(errorCode);
                                        nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
                                    break;
                                }
                            }
                        }
                        else {
                            ongoingGameTableList = ongoingGameTableDao.GetOngoinGameTable(userID);

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

                                    if (nextStep.equals(ApplicationConstant.NEXT_STEP_TO_CONTINUE)) {
                                        ongoingGameTable.setGameStatus(ApplicationConstant.GAME_STATUS_END);

                                        functionResult = ongoingGameTableDao.InsertOngoingGameTable(ongoingGameTable);

                                        switch (functionResult) {
                                            case ApplicationConstant.INSERT_SUCCESSFUL :
                                                commonResponse.setTransactionResult(ApplicationConstant.TRANSACTION_RESULT_SUCCESS);
                                                commonResponse.setMessege("Game Over! You lose " + userID);
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
                        } 
                    }
                    
                }
            }
            else {
                errorCode       = MonopolyGameError.DATA_ACCESS_ERROR;
                errorList.add(errorCode);
                nextStep        = ApplicationConstant.NEXT_STEP_TO_STOP;
            }
        }

        monopolyGameResponse.setApiResponse(monopolyGameResponse);
        monopolyGameResponse.setErrorList(errorList);
        logger.debug("*** RollDie *** - END ");
        return monopolyGameResponse;
    }
    
}
