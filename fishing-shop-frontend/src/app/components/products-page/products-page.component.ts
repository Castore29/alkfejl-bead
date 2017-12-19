import {Component, OnInit} from '@angular/core';
import {ProductService} from '../../services/product.service';
import {Product} from '../../models/product';
import {Pageable} from '../../models/pageable';
import {OrderService} from '../../services/order.service';

@Component({
  selector: 'app-products-page',
  templateUrl: './products-page.component.html',
  styleUrls: ['./products-page.component.css']
})
export class ProductsPageComponent implements OnInit {
  pageable: Pageable;
  productList: Product[];
  pages: number[];

  constructor(private productService: ProductService,
              private orderService: OrderService,) {
    this.productList = [];
    this.pageable = new Pageable;
  }

  ngOnInit() {
    if (this.productService.searchProduct) {
      this.productService.getProducts().subscribe(result => {
        this.pageable = result;
        this.pages = Array(this.pageable.totalPages).fill(1, 0).map((x, i) => i + 1);
        this.productList = result.content;

        this.orderService.cart.products.forEach(cartProduct => {
          const idx = this.productList.findIndex(p => p.id === cartProduct.id);
          if (idx > -1) {
            this.productList[idx].stock--;
          }
        });

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
}
