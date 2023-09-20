import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-login',
  template: `
    <div id='main' class="container mt-5">
      <div class="row justify-content-center">
        <div class="col-md-6">
          <div class="card">
            <div class="card-body">
              <h3 class="text-primary text-center">Login</h3>

              <form (ngSubmit)="onSubmit()">
                <div class="mb-3">
                  <label for="email" class="form-label">Email address</label>
                  <input type="email" class="form-control" id="email" [(ngModel)]="email" name="email" required>
                </div>
                <div class="mb-3">
                  <label for="password" class="form-label">Password</label>
                  <input type="password" class="form-control" id="password" [(ngModel)]="password" name="password" required>
                </div>
                <button type="submit" class="btn btn-primary btn-block">Submit</button>
              </form>

            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [
    `#main{
      height: 60vh;
    }`
  ]
})
export class LoginComponent implements OnInit {

  email: string = '';
  password: string = '';

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void { }

  onSubmit(): void {
    this.authService.login(this.email, this.password).subscribe(
      response => {
        alert("Logged in successfully!");
        this.router.navigate(['']);
      },
      error => {
        console.error("Login error!", error);
      }
    );
  }

}
