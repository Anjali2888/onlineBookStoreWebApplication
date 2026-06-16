package onlinebookstore.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onlinebookstore.application.dto.BookstoreDTO;
import onlinebookstore.application.services.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

	private final BookService bookService;

	/**
	 * Api to get all books from repository.
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<BookstoreDTO.ApiResponse<List<BookstoreDTO.BookResponse>>> getAllBooks() {
		return ResponseEntity.ok(BookstoreDTO.ApiResponse.ok("Books fetched", bookService.getAllBooks()));
	}

	/**
	 * Api to get book with bookId.
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public ResponseEntity<BookstoreDTO.ApiResponse<BookstoreDTO.BookResponse>> getBook(@PathVariable Long id) {
		return ResponseEntity.ok(BookstoreDTO.ApiResponse.ok("Book fetched", bookService.getBookById(id)));
	}

	/**
	 * Api to search the book from book repository.
	 * 
	 * @param keyword
	 * @return
	 */
	@GetMapping("/search")
	public ResponseEntity<BookstoreDTO.ApiResponse<List<BookstoreDTO.BookResponse>>> searchBooks(
			@RequestParam String keyword) {
		return ResponseEntity.ok(BookstoreDTO.ApiResponse.ok("Search results", bookService.searchBooks(keyword)));
	}

	/**
	 * Api to get books based on category.
	 * 
	 * @param category
	 * @return
	 */
	@GetMapping("/category/{category}")
	public ResponseEntity<BookstoreDTO.ApiResponse<List<BookstoreDTO.BookResponse>>> getByCategory(
			@PathVariable String category) {
		return ResponseEntity
				.ok(BookstoreDTO.ApiResponse.ok("Books by category", bookService.getBooksByCategory(category)));
	}

	/**
	 * Api to create book and store it using admin role.
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BookstoreDTO.ApiResponse<BookstoreDTO.BookResponse>> addBook(
			@Valid @RequestBody BookstoreDTO.BookRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(BookstoreDTO.ApiResponse.ok("Book added", bookService.addBook(request)));
	}

	/**
	 * Api to update the book with book Id details like price and stockQunatity.
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BookstoreDTO.ApiResponse<BookstoreDTO.BookResponse>> updateBook(@PathVariable Long id,
			@Valid @RequestBody BookstoreDTO.BookRequest request) {
		return ResponseEntity.ok(BookstoreDTO.ApiResponse.ok("Book updated", bookService.updateBook(id, request)));
	}

	/**
	 * Api to delete to book with book id.
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BookstoreDTO.ApiResponse<Void>> deleteBook(@PathVariable Long id) {
		bookService.deleteBook(id);
		return ResponseEntity.ok(BookstoreDTO.ApiResponse.ok("Book deleted", null));
	}
}