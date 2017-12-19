import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Product} from '../models/product';
import {Pageable} from '../models/pageable';

@Injectable()
export class ProductService {
  host = 'http://localhost:8080/';

  searchProduct: Product;
  editProduct: Product;

  constructor(private http: HttpClient) {
    this.searchProduct = null;
    this.editProduct = null;
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

  saveProduct(product: Product, file?: any) {
    const productString = JSON.stringify(product);
    const formData: FormData = new FormData();
    formData.append('product', productString);
    if (file) {
      formData.append('file', file);
    }
    return this.http.post<Product>(this.host + 'api/product/save', formData);
  }

}
