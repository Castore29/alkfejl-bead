import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {User} from '../../models/user';
import {Order} from '../../models/order';
import {OrderService} from '../../services/order.service';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css']
})
export class OrderListComponent implements OnInit, OnChanges {

  orders: Order[];
  @Input() user: User;
  @Input() updatedOrder: Order;
  @Output() selectOrder;

  constructor(private orderService: OrderService) {
    this.selectOrder = new EventEmitter();
  }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['user']) {
      if (!changes['user'].currentValue) {
        this.user = null;
        this.orders = [];
      } else if (!changes['user'].previousValue || changes['user'].currentValue.id !== changes['user'].previousValue.id) {
        this.orderService.getOrders(this.user).subscribe(result => {
          this.orders = result.content;
        }, err => {
          console.log(err.error);
        });
      }
    }

    if (changes['updatedOrder']) {
      const order = changes['updatedOrder'].currentValue;
      const idx = this.orders.findIndex(o => o.id === order.id);
      if (idx > -1) {
        this.orders[idx] = order;
      }
    }
  }

  select(order: Order) {
    this.selectOrder.emit(order);
  }

}
