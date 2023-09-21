import { Component, OnInit } from '@angular/core';
import { CarService } from 'src/app/service/car.service';
import { Car } from 'src/app/interface/car';

@Component({
  selector: 'app-cars',
  template: `
   <div class="container-fluid px-0">
  <div class="bg-light p-3 align-items-center">
    <h4 class="text-primary flex-grow-1">Ricerca Auto</h4>
    <div class="d-flex">
    <div class="form-group ml-1">
      <label class="text-primary" for="marca">Marca</label>
      <input type="text" class="form-control" [(ngModel)]="marca" id="marca" (input)="onSearch()">
    </div>
    <div class="form-group mx-1 ml-3">
      <label class="text-primary" for="modello">Modello</label>
      <input type="text" class="form-control" [(ngModel)]="modello" id="modello" (input)="onSearch()">
    </div>
    <div class="form-group mx-1 ml-3">
      <label class="text-primary" for="colore">Colore</label>
      <input type="text" class="form-control" [(ngModel)]="colore" id="colore" (input)="onSearch()">
    </div>
    <div class="dropdown mr-1">
      <button class="btn btn-primary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        Ordina per
      </button>
      <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
        <a class="dropdown-item" (click)="setSorting('marca', 'asc')">Marca Ascendente</a>
        <a class="dropdown-item" (click)="setSorting('marca', 'desc')">Marca Discendente</a>
        <a class="dropdown-item" (click)="setSorting('modello', 'asc')">Modello Ascendente</a>
        <a class="dropdown-item" (click)="setSorting('modello', 'desc')">Modello Discendente</a>
        <a class="dropdown-item" (click)="setSorting('costoGiornaliero', 'asc')">Costo Giornaliero Ascendente</a>
        <a class="dropdown-item" (click)="setSorting('costoGiornaliero', 'desc')">Costo Giornaliero Discendente</a>
      </div>
    </div>
  </div>
  </div>
  <div class="row mt-3">
    <div class="col-md-4" *ngFor="let car of cars">
      <div class="card">
        <img [src]="car.foto" class="card-img-top" alt="...">
        <div class="card-body">
          <h5 class="card-title">{{ car.marca }} {{ car.modello }}</h5>
          <p class="card-text">€ {{ car.costoGiornaliero }}/giorno</p>
        </div>
      </div>
    </div>
  </div>
</div>

  `,
  styles: [
    `.bg-light {
      background-color: #f8f9fa !important;
    }`,
    `.dropdown-item {
      color: #007bff !important;
      background-color: white !important;
    }`
  ]
})
export class CarsComponent implements OnInit {
  cars: Car[] = [];
  marca: string = '';
  modello: string = '';
  colore: string = '';
  sortBy: string = '';
  direction: string = '';

  constructor(private carService: CarService) { }

  ngOnInit(): void {
    this.loadAllCars();
  }

  loadAllCars(): void {
    this.carService.getAllCars().subscribe(cars => this.cars = cars);
  }

  onSearch(): void {
    if (this.marca) this.carService.getCarsByMarca(this.marca).subscribe(cars => this.cars = cars);
    else if (this.modello) this.carService.getCarsByModello(this.modello).subscribe(cars => this.cars = cars);
    else if (this.colore) this.carService.getCarsByColore(this.colore).subscribe(cars => this.cars = cars);
    else if (this.sortBy && this.direction) this.carService.getAllCarsSorted(this.sortBy, this.direction).subscribe(cars => this.cars = cars);
    else this.loadAllCars();
  }

  setSorting(sortBy: string, direction: string): void {
    this.sortBy = sortBy;
    this.direction = direction;
    this.onSearch();
  }

}

