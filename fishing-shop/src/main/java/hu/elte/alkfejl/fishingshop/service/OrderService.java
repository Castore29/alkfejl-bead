package hu.elte.alkfejl.fishingshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.elte.alkfejl.fishingshop.model.Order;
import hu.elte.alkfejl.fishingshop.model.User;
import hu.elte.alkfejl.fishingshop.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	public Order createOrUpdate(Order order) {
		return orderRepository.save(order);
	}

	public void delete(Order order) {
		orderRepository.delete(order);
	}

	public Iterable<Order> listByUser(User user) {
		return orderRepository.findByActiveAndUser(true, user);
	}

	public Iterable<Order> list() {
		return orderRepository.findAll();
	}

}
