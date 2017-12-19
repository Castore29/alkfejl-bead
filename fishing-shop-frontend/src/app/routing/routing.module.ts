import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {MainPageComponent} from '../components/main-page/main-page.component';
import {ProfilePageComponent} from '../components/profile-page/profile-page.component';
import {ProductsPageComponent} from '../components/products-page/products-page.component';
import {CartPageComponent} from '../components/cart-page/cart-page.component';
import {ProductEditComponent} from '../components/product-edit/product-edit.component';
import {OrdersPageComponent} from '../components/orders-page/orders-page.component';

const routes: Routes = [
  {
    path: '',
    component: MainPageComponent
  },
  {
    path: 'register',
    component: ProfilePageComponent
  },
  {
    path: 'profile',
    component: ProfilePageComponent
  },
  {
    path: 'products',
    component: ProductsPageComponent
  },
  {
    path: 'cart',
    component: CartPageComponent
  },
  {
    path: 'product',
    component: ProductEditComponent
  },
  {
    path: 'orders',
    component: OrdersPageComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  declarations: []
})
export class RoutingModule {
}
