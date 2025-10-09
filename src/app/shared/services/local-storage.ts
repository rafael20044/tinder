import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {
  constructor() { }

  set(key: string, data: any) {
    localStorage.setItem(key, JSON.stringify(data));
  }

  get<T>(key: string) {
    const data = localStorage.getItem(key) || null;
    return (data) ? JSON.parse(data) as T : null;
  }
}
