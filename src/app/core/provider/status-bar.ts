import { Injectable } from '@angular/core';
import { StatusBar, Style } from '@capacitor/status-bar';

@Injectable({
  providedIn: 'root'
})
export class StatusBarService {
  
  constructor(){}

  async setStatusBar(){
    await StatusBar.setOverlaysWebView({overlay:false});
    await StatusBar.setStyle({style:Style.Dark});
  }

}
