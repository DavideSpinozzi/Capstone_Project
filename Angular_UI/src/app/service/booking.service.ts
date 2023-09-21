import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BookingPayload } from '../interface/booking-payload';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private baseUrl = 'http://localhost:4000/bookings';

  constructor(private http: HttpClient) { }

  createBooking(payload: BookingPayload): Observable<any> {
    return this.http.post(`${this.baseUrl}/create`, payload);
  }

  getAllBookings(): Observable<any> {
    return this.http.get(`${this.baseUrl}/all`);
  }

  getBookingById(id: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  updateBooking(id: string, payload: BookingPayload): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}`, payload);
  }

  deleteBooking(id: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  closeBooking(id: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}/close`, {});
  }
}
