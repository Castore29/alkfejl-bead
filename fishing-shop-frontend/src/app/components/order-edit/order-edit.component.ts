import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {Order, Status} from '../../models/order';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {OrderService} from '../../services/order.service';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-order-edit',
  templateUrl: './order-edit.component.html',
  styleUrls: ['./order-edit.component.css']
})
export class OrderEditComponent implements OnInit, OnChanges {

  orderForm: FormGroup;
  @Input() order: Order;
  @Output() updateSuccess;
  statuses: Status[];

  constructor(private fb: FormBuilder,
              private orderService: OrderService,
              private snackBar: MatSnackBar) {
    this.statuses = Object.keys(Status).map(k => Status[k]);
    this.updateSuccess = new EventEmitter();
  }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    const order = changes['order'].currentValue;
    this.orderForm = this.fb.group({
      id: [order ? order.id : -1, Validators.min(0)],
      orderNumber: [order ? order.orderNumber : '', Validators.required],
      status: order ? order.status : '',
    });
  }

  onSubmit() {
    const order = {
      id: this.orderForm.value.id,
      orderNumber: this.orderForm.value.orderNumber,
      status: this.orderForm.value.status,
      user: {id: this.order.user.id},
      products: this.order.products.map(product => ({id: product.id}))
    };
    this.orderService.updateOrder(order).subscribe(result => {
      this.snackBar.open('Sikeres rendelés módosítás!', 'OK', {duration: 2000});
      this.updateSuccess.emit(result);
    }, err => {
      this.snackBar.open(err.error, 'OK', {duration: 2000});
    });
  }
}
