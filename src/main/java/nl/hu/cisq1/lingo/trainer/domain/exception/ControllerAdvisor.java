package nl.hu.cisq1.lingo.trainer.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());
        body.put("status code", HttpStatus.BAD_REQUEST.toString());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LingoGameException.class)
    public ResponseEntity<Object> handleGameException(LingoGameException ex) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());
        body.put("status code", HttpStatus.BAD_REQUEST.toString());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFeedbackException.class)
    public ResponseEntity<Object> handleInvalidFeedbackException() {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status code", HttpStatus.BAD_REQUEST.toString());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<Object> handleGameNotFound() {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status code", HttpStatus.NOT_FOUND.toString());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }




}
