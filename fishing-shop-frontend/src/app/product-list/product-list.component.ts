import {Component, OnInit} from '@angular/core';
import {ProductService} from '../service/product.service';
import {Product} from '../model/product';
import {Pageable} from '../model/pageable';
import {OrderService} from '../service/order.service';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  pageable: Pageable;
  productList: Product[];
  pages: number[];

  constructor(private productService: ProductService,
              private orderService: OrderService,
              private snackBar: MatSnackBar) {
    this.productList = [];
    this.pageable = new Pageable;
  }

  ngOnInit() {
    if (this.productService.searchProduct) {
      this.productService.getProducts().subscribe(result => {
        this.pageable = result;
        this.pages = Array(this.pageable.totalPages).fill(1, 0).map((x, i) => i + 1 );
        this.productList = result.content;
      }, err => {
        console.log(err);
      });
    }
  }

  getPage(i: number): void {
    this.productService.getProductPage(i).subscribe(result => {
      this.pageable = result;
      this.productList = result.content;
    }, err => {
      console.log(err);
    });
  }

  putInCart(product: Product): void {
    this.orderService.cart.products.push(product);
    this.orderService.cart.total += product.price * (100 - product.discount) / 100;
    this.snackBar.open('Kosárhoz hozzáadva: ' + product.productName, '', { duration: 2000});
  }
}
