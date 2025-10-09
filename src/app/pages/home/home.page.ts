import { Component, OnInit } from '@angular/core';
import { Capacitor } from '@capacitor/core';
import { Const } from 'src/app/const/const';
import { IUserCreate } from 'src/app/interfaces/iuser-create';
import { IUserMatch } from 'src/app/interfaces/iuser-match';
import { AuthService } from 'src/app/shared/services/auth-service';
import { DatabaseService } from 'src/app/shared/services/database-service';
import Matching from 'src/plugins/matching';

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrls: ['./home.page.scss'],
  standalone: false,
})
export class HomePage implements OnInit {

  constructor(private readonly auht:AuthService, private readonly database:DatabaseService) { }

  ngOnInit() {
  }

  exit(){
    this.auht.mySingOut();
  }

  async openMatch(){
    const users = await this.database.getAll<IUserCreate>(Const.COLLECTION_USERS);
    // console.log(users);
    if (Capacitor.isNativePlatform()) {
      await Matching.open({users: users});
    }
  }

}
