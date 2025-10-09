import { Injectable } from '@angular/core';
import { 
  Auth, 
  createUserWithEmailAndPassword, 
  signInWithEmailAndPassword,
  signOut,
} from '@angular/fire/auth'

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private readonly auth: Auth) { }

  async createUserEmailAndPassword(email: string, password: string) {
    try {
      const result = await createUserWithEmailAndPassword(this.auth, email, password);
      return result.user.uid;
    } catch (error) {
      console.error(error);
      return;
    }
  }

  async loginWithEmailAndPassword(email: string, password: string) {
    try {
      const result = await signInWithEmailAndPassword(this.auth, email, password);
      return result.user;
    } catch (error) {
      // this.toastService.presentToast('Incorrect password or email', 'top', 'danger');
      console.log(error);
      return;
    }
  }

  async mySingOut() {
    signOut(this.auth)
  }
}
