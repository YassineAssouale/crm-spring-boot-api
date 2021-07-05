package ya.dev.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ya.dev.crm.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{


	/**
	 * Get a list of orders for a type and a status
	 * @param type the type
	 * @param status the status
	 * @return a list of orders
	 */
	List<Order> findByStatusAndType(String status, String type);
	
	@Query("SELECT o FROM Order o WHERE o.numberOfDays > :nbOfDays")
	List<Order> findByNumberOfDays(@Param("nbOfDays") Double nbOfDays);
	
	List<Order> findByCustomer(Integer id);

}
