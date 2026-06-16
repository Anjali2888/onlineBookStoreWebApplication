package onlinebookstore.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import onlinebookstore.application.dto.BookstoreDTO;

import java.util.stream.Collectors;

/**
 * Generic Exception Handler class for managing all runtime exception for Api.
 */
public class BookstoreExceptions {
	private BookstoreExceptions() {
		/* This utility class should not be instantiated */
	}

	public static class ResourceNotFoundException extends RuntimeException {
		public ResourceNotFoundException(String message) {
			super(message);
		}
	}

	public static class BadRequestException extends RuntimeException {
		public BadRequestException(String message) {
			super(message);
		}
	}

	public static class InsufficientStockException extends RuntimeException {
		public InsufficientStockException(String message) {
			super(message);
		}
	}

	@RestControllerAdvice
	public static class GlobalExceptionHandler {

		@ExceptionHandler(ResourceNotFoundException.class)
		public ResponseEntity<BookstoreDTO.ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BookstoreDTO.ApiResponse.error(ex.getMessage()));
		}

		@ExceptionHandler(BadRequestException.class)
		public ResponseEntity<BookstoreDTO.ApiResponse<Void>> handleBadRequest(BadRequestException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BookstoreDTO.ApiResponse.error(ex.getMessage()));
		}

		@ExceptionHandler(InsufficientStockException.class)
		public ResponseEntity<BookstoreDTO.ApiResponse<Void>> handleStock(InsufficientStockException ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(BookstoreDTO.ApiResponse.error(ex.getMessage()));
		}

		@ExceptionHandler(MethodArgumentNotValidException.class)
		public ResponseEntity<BookstoreDTO.ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
			String errors = ex.getBindingResult().getFieldErrors().stream()
					.map(e -> e.getField() + ": " + e.getDefaultMessage()).collect(Collectors.joining(", "));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BookstoreDTO.ApiResponse.error(errors));
		}

		@ExceptionHandler(Exception.class)
		public ResponseEntity<BookstoreDTO.ApiResponse<Void>> handleGeneral(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(BookstoreDTO.ApiResponse.error("Internal server error: " + ex.getMessage()));
		}
	}
}
