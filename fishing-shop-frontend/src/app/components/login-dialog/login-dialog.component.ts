import {Component, OnInit, Inject, ViewChild} from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {FormControl, Validators} from '@angular/forms';
import {UserService} from '../../services/user.service';
import {User} from '../../models/user';

@Component({
  selector: 'app-login-dialog',
  templateUrl: './login-dialog.component.html',
  styleUrls: ['./login-dialog.component.css']
})
export class LoginDialogComponent implements OnInit {
  @ViewChild('loginForm') loginForm;

  emailFormControl = new FormControl('', [
    Validators.required,
    Validators.email,
  ]);

  passwordFormControl = new FormControl('', [Validators.required]);

  showPassword: boolean;
  loginError: string;

  constructor(public dialogRef: MatDialogRef<LoginDialogComponent>,
              private userService: UserService,
              @Inject(MAT_DIALOG_DATA) public data: User) {
  }

  ngOnInit(): void {
    this.loginForm.resetForm();
  }

  getEmailErrorMessage(): string {
    return this.emailFormControl.hasError('required') ? 'A mező kitöltése kötelező' :
      this.emailFormControl.hasError('email') ? 'Kérem valós e-mail címet adjon meg' :
        '';
  }

  getPasswordErrorMessage(): string {
    return this.passwordFormControl.hasError('required') ? 'A mező kitöltése kötelező' : '';
  }

  onSubmit(): void {
    this.userService.login(this.data).subscribe(user => {
        this.userService.setLoggedInUser(user);
        this.dialogRef.close(this.data);
      },
      err => {
        this.loginError = err.error;
      });
  }

  onReset(): void {
    this.dialogRef.close(null);
  }

}
