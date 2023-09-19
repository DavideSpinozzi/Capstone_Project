import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  currentUser: any = null;

  constructor(private http: HttpClient) { }

  login(email: string, password: string): Observable<any> {
    const credentials = { email, password };
    return this.http.post<any>('http://localhost:4000/auth/login', credentials)
      .pipe(map(response => {
        console.log('Server Response:', response);
        if (response.accessToken) {
          console.log('Token:', response.accessToken);
          localStorage.setItem('token', response.accessToken);
        }
        return response;
      }));
  }

  register(
    username: string,
    name: string,
    surname: string,
    email: string,
    password: string
    ): Observable<any> {
      const newUser = { username, name, surname, email, password };
    return this.http.post<any>('http://localhost:4000/auth/register', newUser);
  }

  getCurrentUserInfo(): Observable<any> {
    return this.http.get<any>('http://localhost:4000/users/current');
  }

  logout() {
    localStorage.removeItem('token');
    return this.http.post<any>('http://localhost:4000/auth/logout', null);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getToken(): String | null {
    return localStorage.getItem('token');
  }

}
