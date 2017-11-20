import {Component, OnInit} from '@angular/core';
import {MatDialog, MatSnackBar} from '@angular/material';
import {Router} from '@angular/router';
import {ProductService} from './service/product.service';
import {UserService} from './service/user.service';
import {LoginDialogComponent} from './dialog/login-dialog/login-dialog.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  categories: string[] = ['egy', 'ketto'];
  subCategories: { [key: string]: string[]; };
  selectedCategory: string;

  constructor(private dialog: MatDialog,
              private productService: ProductService,
              private userService: UserService,
              private snackBar: MatSnackBar,
              private router: Router) {
    this.subCategories = {};
  }

  ngOnInit(): void {
    this.productService.getCategories().subscribe(data => {
        this.categories = data;
      },
      err => {
        console.log(err);
      });
  }

  getSubCat(cat: string): void {
    this.selectedCategory = cat;
    if (!this.subCategories[cat]) {
      this.productService.getSubCategories(cat).subscribe(data => {
          this.subCategories[cat] = data;
        },
        err => {
          console.log(err);
        });
    }
  }

  openSnackBar(message: string, action: string): void {
    this.snackBar.open(message, action, { duration: 5000 });
  }

  loginDialog(): void {
    const dialogRef = this.dialog.open(LoginDialogComponent, {
      width: '300px',
      data: {email: '', password: ''}
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.userService.login(result).subscribe(data => {
            this.userService.setLoggedInUser(data);
            this.router.navigateByUrl('');
          },
          err => {
            this.openSnackBar('Hibás bejelentkezési adatok', 'Bezárás');
          });
      }
    });
  }

  logout(): void {
    this.userService.logout().subscribe(data => {
      this.userService.setLoggedInUser(null);
    }, err => {
      console.log(err);
    });
  }

}
