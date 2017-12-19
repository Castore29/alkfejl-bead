import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Order} from '../models/order';
import {Pageable} from '../models/pageable';
import {User} from '../models/user';

@Injectable()
export class OrderService {
  host = 'http://localhost:8080/';

  cart;

  constructor(private http: HttpClient) {
    this.cart = {
      products: [],
      total: 0
    };
  }

  sendOrder() {
    return this.http.post(this.host + 'api/order/save', this.cart);
  }

  getMyOrders() {
    return this.http.get<Pageable>(this.host + 'api/order/orders');
  }

  getOrders(user: User) {
    return this.http.get<Pageable>(this.host + 'api/order/all', {params: new HttpParams().set('user.id', user.id.toString())});
  }

  updateOrder(order: Order) {
    return this.http.post<Order>(this.host + 'api/order/update', order);
  }

}
