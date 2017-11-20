import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../service/user.service';
import {Router} from '@angular/router';
import {MatSnackBar, MatDialog} from '@angular/material';
import {ConfirmDialogComponent} from '../dialog/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-registration-page',
  templateUrl: './registration-page.component.html',
  styleUrls: ['./registration-page.component.css']
})
export class RegistrationPageComponent implements OnInit {
  registrationForm: FormGroup;

  constructor(private fb: FormBuilder,
              private userService: UserService,
              private router: Router,
              private snackBar: MatSnackBar,
              private dialog: MatDialog) {
    const user = this.userService.getLoggedInUser();
    this.registrationForm = this.fb.group({
      userName: [user ? user.userName : '', Validators.required],
      email: [user ? user.email : '', [Validators.required, Validators.email]],
      password: [user ? user.password : '', Validators.required],
      address: user ? user.address : '',
      phoneNumber: user ? user.phoneNumber : '',
      role: user ? user.role : 'USER',
    });
  }

  ngOnInit() {
  }

  onSubmit() {
    if (this.userService.getLoggedInUser()) {
      this.registrationForm.value['id'] = this.userService.getLoggedInUser().id;
    }
    this.userService.register(this.registrationForm.value).subscribe(data => {
      let msg: string;
      if (this.userService.getLoggedInUser()) {
        msg = 'Sikeres módosítás!';
      } else {
        msg = 'Sikeres regisztráció!';
      }
      this.userService.setLoggedInUser(data);
      this.snackBar.open(msg, 'OK', {duration: 2000});
      this.router.navigateByUrl('/');
    }, err => {
      console.log(err);
    });
  }

  deregister(): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '250px'
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.userService.deregister().subscribe(data => {
          this.userService.setLoggedInUser(null);
          this.snackBar.open('Regisztráció törölve!', 'OK', {duration: 2000});
          this.router.navigateByUrl('/');
        }, err => {
          console.log(err);
        });
      }
    });
  }

  getEmailErrorMessage(): string {
    return this.registrationForm.controls['email'].hasError('required') ? 'A mező kitöltése kötelező!' :
      this.registrationForm.controls['email'].hasError('email') ? 'Kérem valós e-mail címet adjon meg!' :
        '';
  }

}
