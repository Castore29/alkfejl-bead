import {Component, Input, OnInit} from '@angular/core';
import {Product} from '../../models/product';
import {ProductModalComponent} from '../product-modal/product-modal.component';
import {MatDialog} from '@angular/material';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

  @Input() products: Product[];

  constructor(private dialog: MatDialog) {
  }

  ngOnInit() {
  }

  openModal(product: Product): void {
    this.dialog.open(ProductModalComponent, {
      data: product,
    });
  }

}
