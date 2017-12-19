import {Component, OnInit} from '@angular/core';
import {OrderService} from '../../services/order.service';
import {UserService} from '../../services/user.service';
import {Product} from '../../models/product';
import {MatDialog, MatSnackBar} from '@angular/material';
import {Router} from '@angular/router';
import {ProductModalComponent} from '../product-modal/product-modal.component';


@Component({
  selector: 'app-cart-page',
  templateUrl: './cart-page.component.html',
  styleUrls: ['./cart-page.component.css']
})
export class CartPageComponent implements OnInit {

  constructor(private orderService: OrderService,
              private userService: UserService,
              private snackBar: MatSnackBar,
              private router: Router,
              private dialog: MatDialog) {
  }

  ngOnInit() {
  }

  removeProduct(product: Product): void {
    const index = this.orderService.cart.products.indexOf(product, 0);
    if (index > -1) {
      this.orderService.cart.products.splice(index, 1);
    }
    this.orderService.cart.total -= product.price * (100 - product.discount) / 100;
  }

  makeOrder(): void {
    if (this.userService.loggedInUser) {
      this.orderService.sendOrder().subscribe(() => {
        this.orderService.cart.products = [];
        this.orderService.cart.total = 0;
        this.snackBar.open('Sikeres rendelés!', 'OK', {duration: 5000});
        this.router.navigateByUrl('/');
      }, err => {
        console.log(err);
      });
    } else {
      this.snackBar.open('A rendelés elküldéséhez kérjük jelentkezzen be!', 'OK', {duration: 5000});
    }
  }

  openModal(product: Product): void {
    this.dialog.open(ProductModalComponent, {
      data: product,
    });
  }
}
