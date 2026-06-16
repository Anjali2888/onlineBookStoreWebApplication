package onlinebookstore.application.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import onlinebookstore.application.dto.BookstoreDTO;
import onlinebookstore.application.entity.User;
import onlinebookstore.application.exception.BookstoreExceptions;
import onlinebookstore.application.repositories.UserRepository;

/**
 * Authorisation service class for handling login and register user details.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * register method for user.
	 * 
	 * @param request
	 * @return
	 */
	public BookstoreDTO.UserResponse register(BookstoreDTO.RegisterRequest request) {
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new BookstoreExceptions.BadRequestException("Username already taken: " + request.getUsername());
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new BookstoreExceptions.BadRequestException("Email already registered: " + request.getEmail());
		}

		User user = User.builder().username(request.getUsername()).email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword())).role(User.Role.USER).build();

		User saved = userRepository.save(user);
		return toResponse(saved);
	}

	/**
	 * reading current logged in user details.
	 * 
	 * @param username
	 * @return
	 */
	public BookstoreDTO.UserResponse getCurrentUser(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new BookstoreExceptions.ResourceNotFoundException("User not found"));
		return toResponse(user);
	}

	/**
	 * Response handler
	 * 
	 * @param user
	 * @return
	 */
	private BookstoreDTO.UserResponse toResponse(User user) {
		return BookstoreDTO.UserResponse.builder().id(user.getId()).username(user.getUsername()).email(user.getEmail())
				.role(user.getRole()).build();
	}
}
