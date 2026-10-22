package app.config;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	  @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
	        Map<String, String> errors = new HashMap<>();
	        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
	            errors.put(error.getField(), error.getDefaultMessage());
	        }
	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	    }
	  
	  @ExceptionHandler(AccessDeniedException.class)
	  public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
	      Map<String, String> errors = new HashMap<>();
	      errors.put("error", "Acesso Negado");
	      errors.put("message", ex.getMessage());
	      return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
	  }
	  
	  @ExceptionHandler(AuthenticationException.class)
	  public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException ex) {
	      Map<String, String> errors = new HashMap<>();
	      errors.put("error", "Unauthorized");
	      errors.put("message", ex.getMessage());
	      return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
	  }
}