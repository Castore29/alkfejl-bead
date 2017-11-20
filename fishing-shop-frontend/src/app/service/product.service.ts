import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';

@Injectable()
export class ProductService {
  host = 'http://localhost:8080/';

  constructor(private http: HttpClient) {
  }

  getCategories() {
    return this.http.get<string[]>(this.host + 'api/product/categories');
  }

  getSubCategories(cat: string) {
    return this.http.get<string[]>(this.host + '/api/product/categories',
      {params: new HttpParams().set('cat', cat)});
  }

}
