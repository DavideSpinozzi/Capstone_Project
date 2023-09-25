import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CarsComponent } from './components/cars/cars.component';
import { HomeComponent } from './components/home/home.component';
import { AuthGuard } from './auth/auth.guard';
import { ProfileComponent } from './components/profile/profile.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { DetailsComponent } from './components/details/details.component';

const routes: Routes = [
  {
      path: '',
      component: HomeComponent,
  },
  {
      path: 'cars',
      component: CarsComponent,
  },
  {
    path: 'details/:id',
    component: DetailsComponent,
},
  {
      path: 'profile',
      component: ProfileComponent,
      canActivate: [AuthGuard],
  },
  {
      path: 'login',
      component: LoginComponent,
  },
  {
      path: 'register',
      component: RegisterComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
