package onlinebookstore.application.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onlinebookstore.application.dto.BookstoreDTO;
import onlinebookstore.application.services.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	/**
	 * Api to register the user.
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/register")
	public ResponseEntity<BookstoreDTO.ApiResponse<BookstoreDTO.UserResponse>> register(
			@Valid @RequestBody BookstoreDTO.RegisterRequest request) {
		BookstoreDTO.UserResponse user = authService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(BookstoreDTO.ApiResponse.ok("User registered successfully", user));
	}

	/**
	 * Api to authenticate and check if its existing user is logged in user.
	 * 
	 * @param userDetails
	 * @return
	 */
	@GetMapping("/me")
	public ResponseEntity<BookstoreDTO.ApiResponse<BookstoreDTO.UserResponse>> getCurrentUser(
			@AuthenticationPrincipal UserDetails userDetails) {
		BookstoreDTO.UserResponse user = authService.getCurrentUser(userDetails.getUsername());
		return ResponseEntity.ok(BookstoreDTO.ApiResponse.ok("User fetched", user));
	}
}
