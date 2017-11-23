import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Order} from '../model/order';
import {Pageable} from '../model/pageable';

@Injectable()
export class OrderService {
  host = 'http://localhost:8080/';

  cart: Order;

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

}
