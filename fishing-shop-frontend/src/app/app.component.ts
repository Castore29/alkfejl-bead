import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  categories: string[] = ['egy', 'ketto'];
  subCategories: { [key: string]: string[]; };
  selectedCategory: string;
  host = 'http://localhost:8080/';

  constructor(private http: HttpClient) {
    this.subCategories = {};
  }

  ngOnInit(): void {
    this.http.get<string[]>(this.host + '/api/product/categories').subscribe(data => {
        this.categories = data;
      },
      err => {
        console.log(err);
      });
  }

  getSubCat(cat): void {
    this.selectedCategory = cat;
    if (!this.subCategories[cat]) {
      this.http.get<string[]>(this.host + '/api/product/categories',
        {params: new HttpParams().set('cat', cat)}).subscribe(data => {
          this.subCategories[cat] = data;
        },
        err => {
          console.log(err);
        });
    }
  }
}
