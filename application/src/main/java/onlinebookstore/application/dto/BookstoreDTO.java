package onlinebookstore.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import onlinebookstore.application.entity.Order;
import onlinebookstore.application.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Class to have all data objects.
 */
public class BookstoreDTO {

	// ─── Auth DTOs ───────────────────────────────────────────
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class RegisterRequest {
		@NotBlank
		private String username;
		@NotBlank
		@Email
		private String email;
		@NotBlank
		@Size(min = 6)
		private String password;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class UserResponse {
		private Long id;
		private String username;
		private String email;
		private User.Role role;
	}

	// ─── Book DTOs ───────────────────────────────────────────
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class BookRequest {
		@NotBlank
		private String title;
		@NotBlank
		private String author;
		private String isbn;
		private String description;
		@NotNull
		@DecimalMin("0.0")
		private BigDecimal price;
		@Min(0)
		private Integer stockQuantity;
		@NotBlank
		private String category;
		private String imageUrl;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class BookResponse {
		private Long id;
		private String title;
		private String author;
		private String isbn;
		private String description;
		private BigDecimal price;
		private Integer stockQuantity;
		private String category;
		private String imageUrl;
	}

	// ─── Order DTOs ──────────────────────────────────────────
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class OrderItemRequest {
		@NotNull
		private Long bookId;
		@Min(1)
		private Integer quantity;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class CreateOrderRequest {
		@NotEmpty
		private List<OrderItemRequest> items;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class OrderItemResponse {
		private Long bookId;
		private String bookTitle;
		private Integer quantity;
		private BigDecimal priceAtPurchase;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class OrderResponse {
		private Long id;
		private String username;
		private List<OrderItemResponse> items;
		private BigDecimal totalAmount;
		private Order.OrderStatus status;
		private LocalDateTime createdAt;
	}

	// ─── Common ──────────────────────────────────────────────
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ApiResponse<T> {
		private boolean success;
		private String message;
		private T data;

		public static <T> ApiResponse<T> ok(String message, T data) {
			return new ApiResponse<>(true, message, data);
		}

		public static <T> ApiResponse<T> error(String message) {
			return new ApiResponse<>(false, message, null);
		}
	}
}
