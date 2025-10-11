import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { DatabaseService } from '../../services/database-service';
import { LocalStorageService } from '../../services/local-storage';
import { IUserCreate } from 'src/app/interfaces/iuser-create';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { FilePickerService } from 'src/app/core/provider/file-picker-service';
import { SupabaseService } from '../../services/supabase-service';
import { Const } from 'src/app/const/const';
import { Toast } from '../../provider/toast';
import { Ipassions } from 'src/app/interfaces/ipassions';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
  standalone: false,
})
export class ProfileComponent implements OnInit {

  @Output() onLogout = new EventEmitter<void>();

  uid: string | null = "";
  user: IUserCreate | null = null;
  nameControl = new FormControl('', [Validators.required]);
  lastNameControl = new FormControl('', [Validators.required]);
  countryControl = new FormControl('', [Validators.required]);
  formGroup = new FormGroup({
    name: this.nameControl,
    lastName: this.lastNameControl,
  });
  checked = false;
  passions: Ipassions[] = [
    { name: 'Music', selected: false },
    { name: 'Sports', selected: false },
    { name: 'Travel', selected: false },
    { name: 'Reading', selected: false },
    { name: 'Cooking', selected: false },
    { name: 'Movies', selected: false },
    { name: 'Fitness', selected: false },
    { name: 'Art', selected: false },
    { name: 'Gaming', selected: false },
    { name: 'Dancing', selected: false },
  ];
  passionsSelected: string[] = [];
  reload = false;

  constructor(
    private readonly auht: AuthService,
    private readonly database: DatabaseService,
    private readonly local: LocalStorageService,
    private readonly file: FilePickerService,
    private readonly supabase: SupabaseService,
    private readonly toast: Toast,
  ) { }

  async ngOnInit() {
    this.uid = this.local.get<string>('uid');
    await this.loadUser();
    this.initForm();
    if (this.user) {
      this.checked = this.user.showGenderProfile;
      this.passionsSelected = this.user.passions;
    }
    this.verifySelected();
    this.reload = false;
  }

  exit() {
    this.auht.mySingOut();
    this.onLogout.emit();
  }

  async doSubmit() {
    if (!this.formGroup.valid) {
      this.toast.presentToast('bottom', 'please fill all requered fields');
      return;
    }
    let data: IUserCreate = {
      name: this.nameControl.value || '',
      lastName: this.lastNameControl.value || '',
      birthDate: this.user?.birthDate || '',
      country: this.countryControl.value || '',
      email: this.user?.email || '',
      passions: this.passionsSelected || [],
      password: this.user?.password || '',
      photos: this.user?.photos || [],
      sex: this.user?.sex || '',
      showGenderProfile: this.checked,
      uid: this.user?.uid || '',
    }
    this.database.updateData(this.uid || '', data);
    if (this.user) {
      this.user.name = this.formGroup.value.name || '';
      this.user.lastName = this.formGroup.value.lastName || '';
    }
    this.toast.presentToast('bottom', 'updated');
  }

  deleteImg(path: string) {
    if (this.user) {
      this.user.photos = this.user.photos.filter(p => p.path != path);
    }
  }

  async getImg() {
    const file = await this.file.pickerPhoto();
    if (file) {
      const result = await this.supabase.upload(Const.BUCKET, 'photo', file.name, file.data || '', file.mimeType);
      if (result) {
        this.user?.photos.push(result);
      }
    }
  }

  setSex(sex: string) {
    if (this.user) {
      this.user.sex = sex;
    }
  }

  show() {
    if (this.user) {
      this.checked = !this.checked;
    }
  }


  select(index: number) {
    this.passions[index].selected = !this.passions[index].selected;
    this.passionsSelected = this.passions.filter(p => p.selected).map(p => p.name);
  }

  doDone(event: any) {
    const date = event.detail.value;
    if (this.user) {
      this.user.birthDate = date;
    }
  }

  refresh(){
    const reload = this.local.get<boolean>('reload');
    if (reload) {
      this.reload = reload;
      this.ngOnInit();
      this.local.set('reload', false);
    }
  }

  private async loadUser() {
    this.user = await this.database.findUserByUid(this.uid || '');
  }

  private initForm() {
    if (this.user) {
      this.nameControl.setValue(this.user.name);
      this.lastNameControl.setValue(this.user.lastName);
      this.countryControl.setValue(this.user.country);
    }
  }

  private verifySelected() {
    this.passions.forEach(p => {
      if (this.passionsSelected.includes(p.name)) {
        p.selected = true;
      }
    });
  }

}
