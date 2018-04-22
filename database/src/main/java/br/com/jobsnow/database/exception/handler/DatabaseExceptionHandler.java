package br.com.jobsnow.database.exception.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.jobsnow.database.exception.dto.ApiError;

@ControllerAdvice
public class DatabaseExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LogManager.getLogger(DatabaseExceptionHandler.class);
	
    @ExceptionHandler(value = { IllegalArgumentException.class, NullPointerException.class, Exception.class})
    protected ResponseEntity<Object> handleIllegalArgumentException(RuntimeException ex, WebRequest request) {
    	logger.error("Erro ao processar requisição, motivo do erro: "+ex.getCause() + " : "+ex.getMessage(),ex);
    	ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<Object>(apiError,new HttpHeaders(), apiError.getStatus());
    }
    
    @ExceptionHandler(value = { BadSqlGrammarException.class})
    protected ResponseEntity<Object> handleBadSqlGrammarException(RuntimeException ex, WebRequest request) {
    	logger.error("Erro ao processar requisição, motivo do erro: "+ex.getCause() + " : "+ex.getMessage(),ex);
    	String message = "O sql descrito no parametro e invalido";
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message);
        return new ResponseEntity<Object>(apiError,new HttpHeaders(), apiError.getStatus());
    }
}