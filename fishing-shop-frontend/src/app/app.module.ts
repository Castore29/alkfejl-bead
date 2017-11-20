import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {
  MatToolbarModule,
  MatButtonModule,
  MatIconModule,
  MatMenuModule,
  MatFormFieldModule,
  MatInputModule,
  MatButtonToggleModule,
  MatSidenavModule,
  MatListModule,
  MatDialogModule,
  MatSnackBarModule,
} from '@angular/material';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {FlexLayoutModule} from '@angular/flex-layout';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import {AppComponent} from './app.component';
import {MainPageComponent} from './main-page/main-page.component';
import {RoutingModule} from './routing/routing.module';

import {HttpClientModule} from '@angular/common/http';
import {LoginDialogComponent} from './dialog/login-dialog/login-dialog.component';
import {ConfirmDialogComponent} from './dialog/confirm-dialog/confirm-dialog.component';
import {UserService} from './service/user.service';
import {ProductService} from './service/product.service';
import {OrderService} from './service/order.service';
import {RegistrationPageComponent} from './registration-page/registration-page.component';

import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {AuthInterceptor} from './service/auth-interceptor';


@NgModule({
  declarations: [
    AppComponent,
    MainPageComponent,
    LoginDialogComponent,
    RegistrationPageComponent,
    ConfirmDialogComponent
  ],
  imports: [
    BrowserModule,
    RoutingModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonToggleModule,
    MatSidenavModule,
    MatListModule,
    MatDialogModule,
    MatSnackBarModule,
    FormsModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    BrowserAnimationsModule,
    HttpClientModule,
  ],
  providers: [OrderService, ProductService, UserService,
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true, } ],
  bootstrap: [AppComponent],
  entryComponents: [LoginDialogComponent, ConfirmDialogComponent]
})
export class AppModule {
}
