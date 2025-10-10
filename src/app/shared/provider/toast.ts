import { Injectable } from '@angular/core';
import { ToastController } from '@ionic/angular';

@Injectable({
  providedIn: 'root'
})
export class Toast {

  constructor(private readonly toastController:ToastController) { }

  async presentToast(position: 'top' | 'middle' | 'bottom', message:string) {
    const toast = await this.toastController.create({
      message: message,
      duration: 1500,
      position: position,
    });

    await toast.present();
  }
}
