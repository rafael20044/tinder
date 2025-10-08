import { Injectable } from '@angular/core';
import axios from 'axios';

@Injectable({
  providedIn: 'root'
})
export class HttpProvider {
  constructor(){}

  async get<T>(url:string){
    try {
      const res = await axios.get<T>(url);
      return res.data as T;
    } catch (error) {
      console.error(error);
      return;
    }
  }
}
