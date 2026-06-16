package onlinebookstore.application.services;

import java.util.List;
/**
 * Book Service class for managing creating, reading and updating book details in repository.
 */
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import onlinebookstore.application.dto.BookstoreDTO;
import onlinebookstore.application.entity.Book;
import onlinebookstore.application.exception.BookstoreExceptions;
import onlinebookstore.application.repositories.BookRepository;

@Service
public class BookService {
	private BookRepository bookRepository;

	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}
/**
 * Method to read all books details from repository.
 * @return
 */
	public List<BookstoreDTO.BookResponse> getAllBooks() {
		return bookRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
	}

	public BookstoreDTO.BookResponse toResponse(Book book) {
		return BookstoreDTO.BookResponse.builder().id(book.getId()).title(book.getTitle()).author(book.getAuthor())
				.isbn(book.getIsbn()).description(book.getDescription()).price(book.getPrice())
				.stockQuantity(book.getStockQuantity()).category(book.getCategory()).imageUrl(book.getImageUrl())
				.build();
	}
/**
 * Method to find the book with Id.
 * @param id
 * @return
 */
	public Book findBookById(Long id) {
		return bookRepository.findById(id)
				.orElseThrow(() -> new BookstoreExceptions.ResourceNotFoundException("Book not found with id: " + id));
	}
/**
 * delete method to remove book with id.
 * @param id
 */
	public void deleteBook(Long id) {
		bookRepository.delete(findBookById(id));
	}
/**
 * read book with bookid.
 * @param id
 * @return
 */
	public BookstoreDTO.BookResponse getBookById(Long id) {
		return toResponse(findBookById(id));
	}
/**
 * Method to search book with specific book author , title or category.
 * @param keyword
 * @return
 */
	public List<BookstoreDTO.BookResponse> searchBooks(String keyword) {
		return bookRepository.searchBooks(keyword).stream().map(this::toResponse).collect(Collectors.toList());
	}
/**
 * method to read book by category.
 * @param category
 * @return
 */
	public List<BookstoreDTO.BookResponse> getBooksByCategory(String category) {
		return bookRepository.findByCategory(category).stream().map(this::toResponse).collect(Collectors.toList());
	}
/**
 * method to create book details.
 * @param request
 * @return
 */
	public BookstoreDTO.BookResponse addBook(BookstoreDTO.BookRequest request) {
		if (request.getIsbn() != null && bookRepository.findByIsbn(request.getIsbn()).isPresent()) {
			throw new BookstoreExceptions.BadRequestException("Book with ISBN already exists: " + request.getIsbn());
		}
		Book book = Book.builder().title(request.getTitle()).author(request.getAuthor()).isbn(request.getIsbn())
				.description(request.getDescription()).price(request.getPrice())
				.stockQuantity(request.getStockQuantity()).category(request.getCategory())
				.imageUrl(request.getImageUrl()).build();
		return toResponse(bookRepository.save(book));
	}
/**
 * method to update the book with book details.
 * @param id
 * @param request
 * @return
 */
	public BookstoreDTO.BookResponse updateBook(Long id, BookstoreDTO.BookRequest request) {
		Book book = findBookById(id);
		book.setTitle(request.getTitle());
		book.setAuthor(request.getAuthor());
		book.setIsbn(request.getIsbn());
		book.setDescription(request.getDescription());
		book.setPrice(request.getPrice());
		book.setStockQuantity(request.getStockQuantity());
		book.setCategory(request.getCategory());
		book.setImageUrl(request.getImageUrl());
		return toResponse(bookRepository.save(book));
	}
}
