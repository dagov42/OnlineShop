package ru.ellada.ecommerce.repos;

import ru.ellada.ecommerce.domain.Order;
import ru.ellada.ecommerce.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * A repository for {@link Order} objects providing a set of JPA methods for working with the database.
 * Inherits interface {@link JpaRepository}.
 *
 * @author Govorukhin Dmitriy
 * @version 1.0
 * @see Order
 * @see JpaRepository
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Returns list of orders authenticated user.
     *
     * @param user name of authenticated user.
     * @return An object of type {@link List} is a list of orders of authenticated user.
     */
    List<Order> findOrderByUser(User user);
}