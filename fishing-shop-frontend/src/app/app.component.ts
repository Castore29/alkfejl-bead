import {Component, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material';
import {ProductService} from './service/product.service';
import {UserService} from './service/user.service';
import {LoginDialogComponent} from './dialog/login-dialog/login-dialog.component';
import {NavigationEnd, Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  categories: string[];
  subCategories: { [key: string]: string[]; };
  selectedCategory: string;

  constructor(private dialog: MatDialog,
              private productService: ProductService,
              private userService: UserService,
              private router: Router) {
    this.subCategories = {};
  }

  ngOnInit(): void {
    this.productService.getCategories().subscribe(data => {
        this.categories = Object.keys(data);
        this.subCategories = data;
      },
      err => {
        console.log(err);
      });

    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };

    this.router.events.subscribe(evt => {
      if (evt instanceof NavigationEnd) {
        this.router.navigated = false;
        window.scrollTo(0, 0);
      }
    });
  }

  goToProducts(subCategory: string, sidenav: any): void {
    this.productService.searchProduct = {
      category: this.selectedCategory,
      subCategory: subCategory
    };
    sidenav.close();
    this.router.navigateByUrl('/products');
  }

  loginDialog(): void {
    this.dialog.open(LoginDialogComponent, {
      width: '300px',
      data: {email: '', password: ''}
    });
  }

  logout(): void {
    this.userService.logout().subscribe(data => {
      this.userService.setLoggedInUser(null);
      this.router.navigateByUrl('/');
    }, err => {
      console.log(err);
    });
  }

}
