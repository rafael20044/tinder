import { Component, OnInit } from '@angular/core';
import { Capacitor } from '@capacitor/core';
import { Const } from 'src/app/const/const';
import { IUserCreate } from 'src/app/interfaces/iuser-create';
import { IUserMatch } from 'src/app/interfaces/iuser-match';
import { Toast } from 'src/app/shared/provider/toast';
import { AuthService } from 'src/app/shared/services/auth-service';
import { DatabaseService } from 'src/app/shared/services/database-service';
import { LocalStorageService } from 'src/app/shared/services/local-storage';
import ChatPlugin from 'src/plugins/chat';
import Matching from 'src/plugins/matching';

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrls: ['./home.page.scss'],
  standalone: false,
})
export class HomePage implements OnInit {

  uid: string | null = "";

  constructor(
    private readonly database:DatabaseService, 
    private readonly local:LocalStorageService,
    private readonly toast:Toast
  ) { }

  ngOnInit() {
    this.uid = this.local.get<string>('uid');
  }


  async openMatch(){
    // co]sole.log(usersF);
    if (Capacitor.isNativePlatform()) {
      this.toast.presentToast('bottom', 'wait a minute');
      const users = await this.database.getAll<IUserCreate>(Const.COLLECTION_USERS);
      const usersF = users.filter(u => u.uid != this.uid);
      await Matching.open({users: usersF});
    }
  }

  async openChat(){
    if (Capacitor.isNativePlatform()) {
      this.toast.presentToast('bottom', 'wait a minute');
      await ChatPlugin.open();
    }
  }

}
