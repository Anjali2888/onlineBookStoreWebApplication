package onlinebookstore.application.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlinebookstore.application.dto.BookstoreDTO;
import onlinebookstore.application.dto.BookstoreDTO.CreateOrderRequest;
import onlinebookstore.application.dto.BookstoreDTO.OrderResponse;
import onlinebookstore.application.entity.Book;
import onlinebookstore.application.entity.Order;
import onlinebookstore.application.entity.OrderItem;
import onlinebookstore.application.entity.User;
import onlinebookstore.application.exception.BookstoreExceptions;
import onlinebookstore.application.repositories.CartRepository;
import onlinebookstore.application.repositories.UserRepository;

/**
 * Cart service to manage all order of books.
 */
@Service
@Transactional
public class CartService {

	private CartRepository cartRepository;
	private final BookService bookService;
	private UserRepository userRepository;

	public CartService(CartRepository cartRepository, BookService bookService, UserRepository userRepository) {
		this.cartRepository = cartRepository;
		this.bookService = bookService;
		this.userRepository = userRepository;
	}

	/**
	 * method to read user order based on username and sort based on created date
	 * desc.
	 * 
	 * @param username
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<OrderResponse> getUserOrders(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new BookstoreExceptions.ResourceNotFoundException("User not found"));
		return cartRepository.findByUserOrderByCreatedAtDesc(user).stream().map(this::toResponse)
				.collect(Collectors.toList());
	}

	/**
	 * method to read all orders.
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BookstoreDTO.OrderResponse> getAllOrders() {
		return cartRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
	}

	/**
	 * method to create order.
	 * 
	 * @param request
	 * @param username
	 * @return
	 */
	@Transactional
	public BookstoreDTO.OrderResponse addToCart(CreateOrderRequest request, String username) {

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new BookstoreExceptions.ResourceNotFoundException("User not found"));
		Order order = Order.builder().user(user).status(Order.OrderStatus.PENDING).totalAmount(BigDecimal.ZERO).build();
		// check for stock quantity and reduce it
		List<OrderItem> items = request.getItems().stream().map(itemReq -> {
			Book book = bookService.findBookById(itemReq.getBookId());
			if (book.getStockQuantity() < itemReq.getQuantity()) {
				throw new BookstoreExceptions.InsufficientStockException("Insufficient stock for:" + book.getTitle());
			}
			book.setStockQuantity(book.getStockQuantity() - itemReq.getQuantity());

			return OrderItem.builder().order(order).book(book).quantity(itemReq.getQuantity())
					.priceAtPurchase(book.getPrice()).build();
		}).collect(Collectors.toList());

		BigDecimal total = items.stream().map(i -> i.getPriceAtPurchase().multiply(BigDecimal.valueOf(i.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		order.setOrderItems(items);
		order.setTotalAmount(total);

		return toResponse(cartRepository.save(order));
	}

	/**
	 * method to read order by order id.
	 * 
	 * @param id
	 * @param username
	 * @return
	 */
	@Transactional(readOnly = true)
	public BookstoreDTO.OrderResponse getOrderById(Long id, String username) {
		Order order = cartRepository.findById(id)
				.orElseThrow(() -> new BookstoreExceptions.ResourceNotFoundException("Order not found with id: " + id));

		boolean isAdmin = userRepository.findByUsername(username).map(u -> u.getRole() == User.Role.ADMIN)
				.orElse(false);

		if (!isAdmin && !order.getUser().getUsername().equals(username)) {
			throw new BookstoreExceptions.BadRequestException("Access denied to this order");
		}

		return toResponse(order);
	}

	/**
	 * method to update the order based on status.
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@Transactional
	public BookstoreDTO.OrderResponse updateOrderStatus(Long id, Order.OrderStatus status) {
		Order order = cartRepository.findById(id)
				.orElseThrow(() -> new BookstoreExceptions.ResourceNotFoundException("Order not found with id: " + id));
		order.setStatus(status);
		return toResponse(cartRepository.save(order));
	}

	private BookstoreDTO.OrderResponse toResponse(Order order) {
		List<BookstoreDTO.OrderItemResponse> items = order.getOrderItems().stream()
				.map(i -> BookstoreDTO.OrderItemResponse.builder().bookId(i.getBook().getId())
						.bookTitle(i.getBook().getTitle()).quantity(i.getQuantity())
						.priceAtPurchase(i.getPriceAtPurchase()).build())
				.collect(Collectors.toList());

		return BookstoreDTO.OrderResponse.builder().id(order.getId()).username(order.getUser().getUsername())
				.items(items).totalAmount(order.getTotalAmount()).status(order.getStatus())
				.createdAt(order.getCreatedAt()).build();
	}
}
