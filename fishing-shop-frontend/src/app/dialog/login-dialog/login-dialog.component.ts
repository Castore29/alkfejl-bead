import {Component, OnInit, Inject, ViewChild} from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {FormControl, Validators} from '@angular/forms';

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

  constructor(public dialogRef: MatDialogRef<LoginDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: { email: string, password: string }) {
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
    this.dialogRef.close(this.data);
  }

  onReset(): void {
    this.dialogRef.close(null);
  }

}
