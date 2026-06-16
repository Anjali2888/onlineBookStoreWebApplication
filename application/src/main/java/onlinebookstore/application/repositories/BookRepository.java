package onlinebookstore.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import onlinebookstore.application.entity.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findByCategory(String category);

	List<Book> findByAuthorContainingIgnoreCase(String author);

	Optional<Book> findByIsbn(String isbn);
/**
 * search book by title, author and category .
 * @param keyword
 * @return
 */
	@Query("SELECT b FROM Book b WHERE " + "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(b.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Book> searchBooks(@Param("keyword") String keyword);
}