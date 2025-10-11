import { Component, OnInit, ViewChild } from '@angular/core';
import { Capacitor } from '@capacitor/core';
import { IonTabs } from '@ionic/angular';
import { Const } from 'src/app/const/const';
import { IUserCreate } from 'src/app/interfaces/iuser-create';
import { IUserMatch } from 'src/app/interfaces/iuser-match';
import { ProfileComponent } from 'src/app/shared/components/profile/profile.component';
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

  @ViewChild(ProfileComponent) profile!: ProfileComponent;
  @ViewChild('tabs', { static: true }) tabs!: IonTabs;

  constructor(
    private readonly database: DatabaseService,
    private readonly local: LocalStorageService,
    private readonly toast: Toast
  ) { }

  ngOnInit() {

  }


  async openMatch() {
    // co]sole.log(usersF);
    if (Capacitor.isNativePlatform()) {
      this.toast.presentToast('bottom', 'wait a minute');
      const uid = this.local.get<string>('uid');
      const user = await this.database.findUserByUid(uid || '');
      if (user) {
        const fullName = `${user.name} ${user.lastName}`;
        await Matching.open({ uid: uid || '', name: fullName || ''});
      }
    }
  }

  async openChat() {
    if (Capacitor.isNativePlatform()) {
      this.toast.presentToast('bottom', 'wait a minute');
      const uid = this.local.get<string>('uid');
      await ChatPlugin.open({uid: uid || ''});
    }
  }

  onTabChange(event: any) {
    if (event.tab === 'profile' && this.profile) {
      this.profile.refresh();
    }
  }

  handleLogout() {
    this.tabs.select('home');
  }

}
