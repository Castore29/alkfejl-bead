import {Component, OnInit} from '@angular/core';
import {User} from '../../models/user';
import {Order} from '../../models/order';

@Component({
  selector: 'app-orders-page',
  templateUrl: './orders-page.component.html',
  styleUrls: ['./orders-page.component.css']
})
export class OrdersPageComponent implements OnInit {
  selectedUser: User;
  selectedOrder: Order;
  updatedOrder: Order;

  constructor() {
  }

  ngOnInit() {
    this.selectedUser = null;
    this.selectedOrder = null;
    this.updatedOrder = null;
  }

  handleSelectUser(user: User) {
    this.selectedUser = user;
    this.selectedOrder = null;
  }

  handleSelectOrder(order: Order) {
    this.selectedOrder = order;
  }

  handleUpdateSuccess(order: Order) {
    this.updatedOrder = order;
  }
}
