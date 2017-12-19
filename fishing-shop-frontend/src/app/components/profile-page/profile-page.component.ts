import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../../services/user.service';
import {Router} from '@angular/router';
import {MatSnackBar, MatDialog} from '@angular/material';
import {ConfirmDialogComponent} from '../confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})
export class ProfilePageComponent implements OnInit {
  registrationForm: FormGroup;
  showPassword: boolean;

  constructor(private fb: FormBuilder,
              private userService: UserService,
              private router: Router,
              private snackBar: MatSnackBar,
              private dialog: MatDialog) {
    const user = this.userService.loggedInUser;
    this.registrationForm = this.fb.group({
      userName: [user ? user.userName : '', Validators.required],
      email: [user ? user.email : '', [Validators.required, Validators.email]],
      password: [user ? user.password : '', Validators.required],
      address: user ? user.address : '',
      phoneNumber: user ? user.phoneNumber : '',
      role: user ? user.role : 'USER'
    });
  }

  ngOnInit() {
  }

  onSubmit() {
    const isUpdate: boolean = this.userService.loggedInUser != null;
    this.userService.register(this.registrationForm.value).subscribe(data => {
      this.userService.setLoggedInUser(data);

      const msg = isUpdate ? 'Sikeres módosítás!' : 'Sikeres regisztráció!';
      this.snackBar.open(msg, 'OK', {duration: 2000});
      if (!isUpdate) {
        this.router.navigateByUrl('/');
      }
    }, err => {
      this.snackBar.open(err.error, 'OK', {duration: 2000});
    });
  }

  deactivate(): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '250px',
      data: 'Biztos törölni akarod a profilod?'
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.userService.deactivate().subscribe(data => {
          this.userService.setLoggedInUser(null);
          this.snackBar.open('Regisztráció törölve!', 'OK', {duration: 2000});
          this.router.navigateByUrl('/');
        }, err => {
          this.snackBar.open(err.error, 'OK', {duration: 2000});
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
