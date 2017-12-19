import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Product} from '../../models/product';
import {UserService} from '../../services/user.service';
import {OrderService} from '../../services/order.service';
import {ProductService} from '../../services/product.service';
import {Router} from '@angular/router';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {
  @Input() product: Product;
  @Output() editMode;

  constructor(private userService: UserService,
              private productService: ProductService,
              private orderService: OrderService,
              private router: Router,
              private snackBar: MatSnackBar) {
    this.editMode = new EventEmitter();
  }

  ngOnInit() {
  }

  putInCart(product: Product): void {
    product.stock--;
    this.orderService.cart.products.push(product);
    this.orderService.cart.total += product.price * (100 - product.discount) / 100;
    this.snackBar.open('Kosárhoz hozzáadva: ' + product.productName, '', {duration: 2000});
  }

  edit(product: Product): void {
    this.productService.editProduct = product;
    this.router.navigateByUrl('/product');
    this.editMode.emit();
  }

}
