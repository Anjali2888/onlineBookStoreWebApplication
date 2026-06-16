package onlinebookstore.application.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import onlinebookstore.application.entity.Order;
import onlinebookstore.application.entity.User;

public interface CartRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserOrderByCreatedAtDesc(User user);

	List<Order> findByStatus(Order.OrderStatus status);
	
	
}
