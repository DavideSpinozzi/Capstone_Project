import { Component, OnInit } from '@angular/core';
import { CarService } from 'src/app/service/car.service';
import { Car } from 'src/app/interface/car';
import { UserService } from 'src/app/service/user.service';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/auth/auth.service';

@Component({
  selector: 'app-cars',
  template: `
  <div class="row">
    <div class="container-fluid px-0 col-12 col-md-10">
      <div class="bg-light px-4 py-3 align-items-center ">
        <h4 class="text-primary flex-grow-1 ms-3">Ricerca Auto</h4>
        <div class="d-flex">
          <div class="form-group mx-1 mx-md-4">
            <label class="text-primary" for="marca">Marca</label>
            <input
              type="text"
              class="form-control"
              [(ngModel)]="marca"
              id="marca"
              (input)="onSearch()"
            />
          </div>
          <div class="form-group mx-1 mx-md-4">
            <label class="text-primary" for="modello">Modello</label>
            <input
              type="text"
              class="form-control"
              [(ngModel)]="modello"
              id="modello"
              (input)="onSearch()"
            />
          </div>
          <div class="form-group mx-1 mx-md-4">
            <label class="text-primary" for="colore">Colore</label>
            <input
              type="text"
              class="form-control"
              [(ngModel)]="colore"
              id="colore"
              (input)="onSearch()"
            />
          </div>
          <div class="btn-group align-items-end">
            <button
              type="button"
              class="btn btn-primary dropdown-toggle h-75"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              Ordina per
            </button>
            <ul class="dropdown-menu">
              <li>
                <a
                  class="dropdown-item text-black"
                  (click)="setSorting('costoGiornaliero', 'asc')"
                  >Costo Giornaliero
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="16"
                    height="16"
                    fill="black"
                    class="bi bi-caret-up-fill"
                    viewBox="0 0 16 16"
                  >
                    <path
                      d="m7.247 4.86-4.796 5.481c-.566.647-.106 1.659.753 1.659h9.592a1 1 0 0 0 .753-1.659l-4.796-5.48a1 1 0 0 0-1.506 0z"
                    /></svg></a>
              </li>
              <li>
                <a
                  class="dropdown-item text-black"
                  (click)="setSorting('costoGiornaliero', 'desc')"
                  >Costo Giornaliero
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="16"
                    height="16"
                    fill="black"
                    class="bi bi-caret-down-fill"
                    viewBox="0 0 16 16"
                  >
                    <path
                      d="M7.247 11.14 2.451 5.658C1.885 5.013 2.345 4 3.204 4h9.592a1 1 0 0 1 .753 1.659l-4.796 5.48a1 1 0 0 1-1.506 0z"
                    /></svg></a>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="row mt-3 px-3">
        <div class="col-md-4" *ngFor="let car of cars">
          <div class="pb-3 h-100">
          <div class="card h-100">
            <img [src]="car.foto" class="card-img-top cars-img" alt="..." />
            <div class="card-body row">
              <div class="col-9">
              <h5 class="card-title">{{ car.marca }} {{ car.modello }}</h5>
              <p class="card-text">€ {{ car.costoGiornaliero }}/giorno</p>
            </div>
            <div *ngIf="user" class="col-3 d-flex justify-content-center align-items-center">
            <button type="button" class="btn btn-primary fs-4">Prenota</button>
            </div>
            </div>
          </div>
        </div>
      </div>
      </div>
    </div>
    <div class="col-0 col-md-2 border carrello" *ngIf="user">
<div class="border row px-0 py-2"><div class="col-8 d-flex justify-content-center align-items-center px-0"><h2 class="text-center py-1">Carrello</h2></div><div class="col-4 d-flex justify-content-center align-items-center px-0"><button type="button" class="btn btn-success fs-5">Acquista</button></div></div>
 <div class="d-flex flex-column align-items-center">
 <div class="card my-2" *ngFor="let booking of bookings" style="width: 18rem;">
          <div class="card-header">
Nome modello: {{booking.nomeModello}}
          </div>
          <ul class="list-group list-group-flush">
            <li class="list-group-item">Inizio: {{booking.dataInizio}}</li>
            <li class="list-group-item">Fine: {{booking.dataFine}}</li>
            <li class="list-group-item">Costo totale: {{booking.costoTotale}}€</li>
            <li class="list-group-item d-flex justify-content-between">
              <a class="text-warning" (click)="modifyBooking(booking)">Modifica</a>
              <a class="text-danger" (click)="deleteBooking(booking)">Cancella</a>
            </li>
          </ul>
        </div>
 </div>
    </div>
  </div>
  `,
  styles: [
    `
      .bg-light {
        background-color: lightgray !important;
      }
      .dropdown-item {
        color: #007bff !important;
        background-color: white !important;
      }
      .text-black {
        color: black !important;
      }
      .carrello{
        position : sticky !important;
        top: 0;
        z-index: 1;
        margin: 0;
      }
      .cars-img{
        height :85%;
      }
    `,
  ],
})
export class CarsComponent implements OnInit {
  user: any = null;
  private subscription: Subscription | null = null;

  cars: Car[] = [];
  bookings: any[] = [];
  marca: string = '';
  modello: string = '';
  colore: string = '';
  sortBy: string = '';
  direction: string = '';

  constructor(private carService: CarService, private userService: UserService, private authSrv: AuthService) {}

  ngOnInit(): void {
    this.subscription = this.authSrv.currentUser$.subscribe((user) => {
      console.log('User:', user);
      this.user = user;
    });
    this.loadAllCars();
    this.loadUserBookings();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  loadAllCars(): void {
    this.carService.getAllCars().subscribe((cars) => (this.cars = cars));
  }

  onSearch(): void {
    if (this.marca)
      this.carService
        .getCarsByMarca(this.marca)
        .subscribe((cars) => (this.cars = cars));
    else if (this.modello)
      this.carService
        .getCarsByModello(this.modello)
        .subscribe((cars) => (this.cars = cars));
    else if (this.colore)
      this.carService
        .getCarsByColore(this.colore)
        .subscribe((cars) => (this.cars = cars));
    else if (this.sortBy && this.direction)
      this.carService
        .getAllCarsSorted(this.sortBy, this.direction)
        .subscribe((cars) => (this.cars = cars));
    else this.loadAllCars();
  }

  setSorting(sortBy: string, direction: string): void {
    this.sortBy = sortBy;
    this.direction = direction;
    this.onSearch();
  }

  loadUserBookings(): void {
    this.userService.getOpenBookingsForCurrentUser().subscribe(
      (bookings) => {
        this.bookings = bookings;
        bookings.forEach((booking: any) => {
          console.log('Booking:', booking);
        });
      },
      (error) => {
        console.error('Error loading user bookings', error);
      }
    );
  }


  modifyBooking(booking: any): void {
    // Implementa la logica per modificare un booking
  }

  deleteBooking(booking: any): void {
    // Implementa la logica per cancellare un booking
  }
}
