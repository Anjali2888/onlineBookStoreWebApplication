package onlinebookstore.application.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onlinebookstore.application.dto.BookstoreDTO;
import onlinebookstore.application.entity.Order;
import onlinebookstore.application.services.CartService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CartService cartService;
/**
 * Api to create order.
 * @param userDetails
 * @param request
 * @return
 */
    @PostMapping
    public ResponseEntity<BookstoreDTO.ApiResponse<BookstoreDTO.OrderResponse>> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BookstoreDTO.CreateOrderRequest request) {
        BookstoreDTO.OrderResponse order = cartService.addToCart(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BookstoreDTO.ApiResponse.ok("Order placed successfully", order));
    }
/**
 * Api to read all books order placed by user itself.
 * @param userDetails
 * @return
 */
    @GetMapping
    public ResponseEntity<BookstoreDTO.ApiResponse<List<BookstoreDTO.OrderResponse>>> getMyOrders(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<BookstoreDTO.OrderResponse> orders = cartService.getUserOrders(userDetails.getUsername());
        return ResponseEntity.ok(BookstoreDTO.ApiResponse.ok("Orders fetched", orders));
    }
/**
 * Api to read order with order id.
 * @param id
 * @param userDetails
 * @return
 */
    @GetMapping("/{id}")
    public ResponseEntity<BookstoreDTO.ApiResponse<BookstoreDTO.OrderResponse>> getOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        BookstoreDTO.OrderResponse order = cartService.getOrderById(id, userDetails.getUsername());
        return ResponseEntity.ok(BookstoreDTO.ApiResponse.ok("Order fetched", order));
    }
/**
 * Api to read all orders which include all user orders.
 * @return
 */
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookstoreDTO.ApiResponse<List<BookstoreDTO.OrderResponse>>> getAllOrders() {
        return ResponseEntity.ok(BookstoreDTO.ApiResponse.ok("All orders fetched", cartService.getAllOrders()));
    }
/**
 * Api to update the order with status.
 * @param id
 * @param status
 * @return
 */
    @PatchMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookstoreDTO.ApiResponse<BookstoreDTO.OrderResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam Order.OrderStatus status) {
        return ResponseEntity.ok(BookstoreDTO.ApiResponse.ok("Status updated", cartService.updateOrderStatus(id, status)));
    }
}
