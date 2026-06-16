package onlinebookstore.application.basic;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onlinebookstore.application.entity.Book;
import onlinebookstore.application.entity.User;
import onlinebookstore.application.repositories.BookRepository;
import onlinebookstore.application.repositories.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

	private final UserRepository userRepository;
	private final BookRepository bookRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) {
		seedUsers();
		seedBooks();
		log.info(" Sample data seeded successfully!");
		log.info(" Admin credentials: username=admin, password=admin123");
		log.info(" User credentials:  username=ram, password=user123");
	}

	/**
	 * adding user details as username admin and ram.
	 */
	private void seedUsers() {
		if (userRepository.count() == 0) {
			userRepository.saveAll(List.of(
					User.builder().username("admin").email("admin@bookstore.com")
							.password(passwordEncoder.encode("admin123")).role(User.Role.ADMIN).build(),
					User.builder().username("ram").email("ram@example.com").password(passwordEncoder.encode("user123"))
							.role(User.Role.USER).build()));
		}
	}

	/**
	 * Adding books details for ordering some dummy data.
	 */
	private void seedBooks() {
		if (bookRepository.count() == 0) {
			bookRepository.saveAll(List.of(Book.builder().title("Clean Code").author("Robert C. Martin")
					.isbn("978-0132350884").description("A handbook of agile software craftsmanship.")
					.price(new BigDecimal("39.99")).stockQuantity(50).category("Programming").build(),
					Book.builder().title("Basic Python Programming for Beginner").author("Mr. K. Varada Rajkumar")
							.isbn("978-0135957059").description("Your journey to mastery.")
							.price(new BigDecimal("44.99")).stockQuantity(30).category("Programming").build(),
					Book.builder().title("Programming Beyond Practices : Be More Than Just A Code Monkey")
							.author("Gregory T Brown").isbn("978-0201633610")
							.description(
									"Programming Beyond Practices Writing code is the easy part of your work as a software developer.")
							.price(new BigDecimal("54.99")).stockQuantity(20).category("Programming").build(),
					Book.builder().title("Atomic Habits").author("James Clear").isbn("978-0735211292")
							.description("An easy and proven way to build good habits.").price(new BigDecimal("24.99"))
							.stockQuantity(100).category("Self-Help").build(),
					Book.builder().title("History Of The World").author("YArjun dev, Indira Arjun Dev")
							.isbn("978-0062316097")
							.description(
									" The book, Towards Freedom Documents On The Movement For Independence in India.")
							.price(new BigDecimal("19.99")).stockQuantity(75).category("History").build(),
					Book.builder().title("The Harry Potter").author("J K Rowling").isbn("978-0743273565")
							.description("A classic for younger generation who fancy magic.")
							.price(new BigDecimal("12.99")).stockQuantity(60).category("Fiction").build()));
		}
	}
}
