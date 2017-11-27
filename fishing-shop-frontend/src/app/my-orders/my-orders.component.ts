import { Component, OnInit } from '@angular/core';
import {OrderService} from '../service/order.service';
import {Order} from '../model/order';

@Component({
  selector: 'app-my-orders',
  templateUrl: './my-orders.component.html',
  styleUrls: ['./my-orders.component.css']
})
export class MyOrdersComponent implements OnInit {
  myOrders: Order[];

  constructor(private orderService: OrderService) {
    this.myOrders = [];
  }

  ngOnInit() {
    this.orderService.getMyOrders().subscribe(result => {
      this.myOrders = result.content;
    }, err => {
      console.log(err);
    });
  }

}
