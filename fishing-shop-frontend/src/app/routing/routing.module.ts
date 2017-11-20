import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {MainPageComponent} from '../main-page/main-page.component';
import {RegistrationPageComponent} from '../registration-page/registration-page.component';

const routes: Routes = [
  {
    path: '',
    component: MainPageComponent
  },
  {
    path: 'register',
    component: RegistrationPageComponent
  },
  {
    path: 'profile',
    component: RegistrationPageComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  declarations: []
})
export class RoutingModule {
}
