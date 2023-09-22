import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { NewUserPayload } from 'src/app/interface/new-user-payload';

@Component({
  selector: 'app-register',
  template: `
    <div id='main' class="container mt-5">
      <div class="row justify-content-center">
        <div class="col-md-6">
          <div class="card bg-white p-4">
            <h4 class="text-primary mb-3 text-center">Registrati</h4>
            <form (ngSubmit)="onSubmit()">
              <div class="form-group">
                <label class="text-primary mb-1">Nome</label>
                <input type="text" class="form-control" [(ngModel)]="name" name="name" required>
              </div>
              <div class="form-group">
                <label class="text-primary my-1">Cognome</label>
                <input type="text" class="form-control" [(ngModel)]="surname" name="surname" required>
              </div>
              <div class="form-group">
                <label class="text-primary my-1">Email</label>
                <input type="email" class="form-control" [(ngModel)]="email" name="email" required>
              </div>
              <div class="form-group">
                <label class="text-primary my-1">Password</label>
                <input type="password" class="form-control" [(ngModel)]="password" name="password" required>
              </div>
              <button type="submit" class="btn btn-primary btn-block mt-4">Registrati</button>
            </form>
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
export class RegisterComponent implements OnInit {
  name: string = '';
  surname: string = '';
  email: string = '';
  password: string = '';

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    const newUser: NewUserPayload = {
      name: this.name,
      surname: this.surname,
      email: this.email,
      password: this.password
    };
    this.authService.register(newUser).subscribe(
      response => {
        alert("Registrato con successo!");
        this.router.navigate(['/login']);
      },
      error => {
        console.error("Errore durante la registrazione!", error);
      }
    );
  }

}

