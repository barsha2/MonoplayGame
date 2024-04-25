package com.barsha.monopolygame.Controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barsha.monopolygame.Constants.ApplicationConstant;
import com.barsha.monopolygame.Model.CommonResponse;
import com.barsha.monopolygame.Model.GameCreateRequest;
import com.barsha.monopolygame.Model.MonopolyGameResponse;
import com.barsha.monopolygame.Service.GameCreateService;

@RestController
@RequestMapping("/Game")
public class GameController {
    private final Logger logger = Logger.getLogger(GameController.class);

    @Autowired
    private GameCreateService gameCreateService;

    @PostMapping("/CreateGame")
    public ResponseEntity<MonopolyGameResponse>  CreateGame (
            @RequestBody(required = true) GameCreateRequest gameCreateRequest) {
     logger.debug("*** CreateGame *** - START");

        MonopolyGameResponse       monopolyGameResponse     = new MonopolyGameResponse();
        HttpStatus                 httpStatus               = ApplicationConstant.HTTP_STATUS_ERROR;

        monopolyGameResponse      = gameCreateService.CreateGame(gameCreateRequest);
        
        if (((CommonResponse) monopolyGameResponse.getApiResponse()).getTransactionResult().equals(ApplicationConstant.TRANSACTION_RESULT_SUCCESS)) {
            httpStatus  = ApplicationConstant.HTTP_STATUS_OK;
       }
       else {
            httpStatus  = ApplicationConstant.HTTP_STATUS_ERROR;
       }

       logger.debug("*** CreateGame *** - END");
       return new ResponseEntity<> (monopolyGameResponse, httpStatus);
    }
}
