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
  MatCheckboxModule,
  MatTooltipModule,
  MatGridListModule,
  MatCardModule,
  MatExpansionModule,
  MatSelectModule,
} from '@angular/material';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {FlexLayoutModule} from '@angular/flex-layout';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import {AppComponent} from './components/app/app.component';
import {MainPageComponent} from './components/main-page/main-page.component';
import {RoutingModule} from './routing/routing.module';

import {HttpClientModule} from '@angular/common/http';
import {LoginDialogComponent} from './components/login-dialog/login-dialog.component';
import {ConfirmDialogComponent} from './components/confirm-dialog/confirm-dialog.component';
import {UserService} from './services/user.service';
import {ProductService} from './services/product.service';
import {OrderService} from './services/order.service';
import {ProfilePageComponent} from './components/profile-page/profile-page.component';

import {RequestInterceptor} from './services/request-interceptor';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {ProductsPageComponent} from './components/products-page/products-page.component';
import {CartPageComponent} from './components/cart-page/cart-page.component';
import {MyOrdersComponent} from './components/my-orders/my-orders.component';
import {StatusPipe} from './pipe/status.pipe';
import {ProductEditComponent} from './components/product-edit/product-edit.component';
import {OrderEditComponent} from './components/order-edit/order-edit.component';
import {UserListComponent} from './components/user-list/user-list.component';
import {OrdersPageComponent} from './components/orders-page/orders-page.component';
import {OrderListComponent} from './components/order-list/order-list.component';
import {ProductDetailsComponent} from './components/product-details/product-details.component';
import {ProductModalComponent} from './components/product-modal/product-modal.component';
import {ProductListComponent} from './components/product-list/product-list.component';


@NgModule({
  declarations: [
    AppComponent,
    MainPageComponent,
    LoginDialogComponent,
    ProfilePageComponent,
    ConfirmDialogComponent,
    ProductsPageComponent,
    CartPageComponent,
    MyOrdersComponent,
    ProductEditComponent,
    OrdersPageComponent,
    OrderEditComponent,
    UserListComponent,
    StatusPipe,
    OrderListComponent,
    ProductDetailsComponent,
    ProductModalComponent,
    ProductListComponent,
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
    MatCheckboxModule,
    MatTooltipModule,
    MatGridListModule,
    MatCardModule,
    MatExpansionModule,
    MatSelectModule,
    FormsModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    BrowserAnimationsModule,
    HttpClientModule,
  ],
  providers: [OrderService, ProductService, UserService,
    {provide: HTTP_INTERCEPTORS, useClass: RequestInterceptor, multi: true, }],
  bootstrap: [AppComponent],
  entryComponents: [LoginDialogComponent, ConfirmDialogComponent, ProductModalComponent]
})
export class AppModule {
}
