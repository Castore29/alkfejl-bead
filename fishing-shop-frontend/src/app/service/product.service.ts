import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Product} from '../model/product';
import {Pageable} from '../model/pageable';

@Injectable()
export class ProductService {
  host = 'http://localhost:8080/';

  searchProduct: Product;

  constructor(private http: HttpClient) {
    this.searchProduct = null;
  }

  getCategories() {
    return this.http.get<{ [key: string]: string[]; }>(this.host + 'api/product/categories');
  }

  getProducts() {
    return this.http.get<Pageable>(this.host + 'api/product/list',
      {
        params: new HttpParams().append('category', this.searchProduct.category)
          .append('subCategory', this.searchProduct.subCategory)
      });
  }

  getProductPage(page: number) {
    return this.http.get<Pageable>(this.host + 'api/product/list',
      {
        params: new HttpParams()
          .append('category', this.searchProduct.category)
          .append('subCategory', this.searchProduct.subCategory)
          .append('page', page.toString())
      });
  }

}
