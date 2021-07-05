package ya.dev.crm.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ya.dev.crm.model.Order;
import ya.dev.crm.exception.DaoException;
import ya.dev.crm.exception.UnknownResourceException;
import ya.dev.crm.repository.OrderRepository;
import ya.dev.crm.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{
	
	Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
	@Autowired
	OrderRepository orderRepository;
	
	@Override
	public Order getById(Integer id) throws DaoException {
		return orderRepository.findById(id).orElseThrow(UnknownResourceException::new);
	}

	@Override
	public List<Order> getAllOrders() throws DaoException {
		return orderRepository.findAll(Sort.by("label").ascending());
	}

	@Override
	public List<Order> getOrdersByCustomer(Integer id) throws DaoException {
		return orderRepository.findByCustomer(id);
	}

	@Override
	public Order createOrder(Order order) throws DaoException {
		log.debug("Attempting to create a new order...");
		return orderRepository.save(order);
	}

	@Override
	public Order updateOrder(Order order) throws DaoException {
		log.debug("Attempting to update order {}", order.getLabel());
		Order existingOrder = orderRepository.findById(order.getId()).orElseThrow(UnknownResourceException::new);
		existingOrder.setLabel(order.getLabel());
		existingOrder.setAdrEt(order.getAdrEt());
		existingOrder.setNumberOfDays(order.getNumberOfDays());
		existingOrder.setTva(order.getTva());
		existingOrder.setStatus(order.getStatus());
		existingOrder.setType(order.getType());
		existingOrder.setNotes(order.getNotes());
		existingOrder.setCustomer(order.getCustomer());
		
		return orderRepository.save(existingOrder);
	}

	@Override
	public void deleteOrder(Integer id) throws DaoException {
		log.debug("Attempting to delete order {}", id);
		Order order = orderRepository.findById(id).orElseThrow(UnknownResourceException::new);
		orderRepository.delete(order);
		
	}

	@Override
	public void patchOrderLabel(Integer id, String label) throws DaoException {
		log.debug("Attempting to update order {}", id);
		Order existingOrder = orderRepository.findById(id).orElseThrow(UnknownResourceException::new);
		existingOrder.setLabel(label);
		orderRepository.save(existingOrder);
	}
}
