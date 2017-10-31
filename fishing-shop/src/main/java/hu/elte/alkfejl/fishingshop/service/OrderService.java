package hu.elte.alkfejl.fishingshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

import hu.elte.alkfejl.fishingshop.model.Order;
import hu.elte.alkfejl.fishingshop.model.Product;
import hu.elte.alkfejl.fishingshop.model.QOrder;

import static hu.elte.alkfejl.fishingshop.model.Order.Status.*;
import hu.elte.alkfejl.fishingshop.model.User;
import hu.elte.alkfejl.fishingshop.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	public Order createOrUpdate(Order order) {
		Order o = orderRepository.findOne(order.getId());
		if (o != null) {
			if (o.getStatus() != PROCESSED && order.getStatus() == PROCESSED) {
				for (Product p : order.getProducts()) {
					p.setStock(p.getStock() - 1);
				}
			} else if (o.getStatus() != CANCELLED && order.getStatus() == CANCELLED) {
				for (Product p : order.getProducts()) {
					p.setStock(p.getStock() + 1);
				}
			}
		} else {
			order.setStatus(RECEIVED);
			for (Product p : order.getProducts()) {
				order.setTotal(order.getTotal() + p.getPrice() - (p.getPrice() * (p.getDiscount() / 100)));
			}
		}
		return orderRepository.save(order);
	}

	public void delete(Order order) {
		orderRepository.delete(order);
	}
	
	public Iterable<Order> listByUser(User user, Predicate predicate, Pageable pageable) {
		predicate = ExpressionUtils.allOf(predicate, QOrder.order.user.id.eq(user.getId()));
		return list(predicate, pageable);
	}

	public Iterable<Order> list(Predicate predicate, Pageable pageable) {
		predicate = ExpressionUtils.allOf(predicate, QOrder.order.active.eq(true));
		return orderRepository.findAll(predicate, pageable);
	}

}
