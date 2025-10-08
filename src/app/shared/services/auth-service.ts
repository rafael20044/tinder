import { Injectable } from '@angular/core';
import {Auth, createUserWithEmailAndPassword} from '@angular/fire/auth'

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private readonly auth:Auth){}

  async createUserEmailAndPassword(email:string, password:string){
    try {
      const result = await createUserWithEmailAndPassword(this.auth,email, password);
      return result.user.uid;
    } catch (error) {
      console.error(error);
      return;
    }
  }
}
