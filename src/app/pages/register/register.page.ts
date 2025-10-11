import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Const } from 'src/app/const/const';
import { IUserCreate } from 'src/app/interfaces/iuser-create';
import { Toast } from 'src/app/shared/provider/toast';
import { AuthService } from 'src/app/shared/services/auth-service';
import { DatabaseService } from 'src/app/shared/services/database-service';
import { LocalStorageService } from 'src/app/shared/services/local-storage';

@Component({
  selector: 'app-register',
  templateUrl: './register.page.html',
  styleUrls: ['./register.page.scss'],
  standalone: false,
})
export class RegisterPage implements OnInit {

  nameControl = new FormControl('', [Validators.required]);
  lastNameControl = new FormControl('', [Validators.required]);
  emailControl = new FormControl('', [Validators.required, Validators.email]);
  passwordControl = new FormControl('', [Validators.required]);
  countryControl = new FormControl('', [Validators.required]);
  sexControl = new FormControl('', [Validators.required]);
  birthDateControl = new FormControl('', [Validators.required]);
  showGenderProfileControl = new FormControl(false);
  passionControl = new FormControl([], [Validators.required]);
  fileControl = new FormControl();
  formGroup = new FormGroup({
    name: this.nameControl,
    lastName: this.lastNameControl,
    email: this.emailControl,
    password: this.passwordControl,
    country: this.countryControl,
    sex: this.sexControl,
    birthDate: this.birthDateControl,
    showGenderProfile: this.showGenderProfileControl,
    passions: this.passionControl,
    photos: this.fileControl,
  });

  registerId = 1;

  constructor(
    private readonly auth:AuthService, 
    private readonly database:DatabaseService,
    private readonly router:Router,
    private readonly toast:Toast,
    private readonly local:LocalStorageService,
  ) { }

  async ngOnInit() {
  }

  getEmitter(id:number){
    this.registerId = id;
  }

  async doSubmit(){
    if (!this.formGroup.valid) {
      // console.log("please fill all requered fields")
      this.toast.presentToast('bottom', 'please fill all requered fields')
      return;
    }
    const {
      email, 
      password, 
      birthDate, 
      country, 
      lastName, 
      name, 
      passions, 
      sex, 
      showGenderProfile,
      photos
    } = this.formGroup.value;
    const uid = await this.auth.createUserEmailAndPassword(email || '', password || '');
    if (uid) {
      const user:IUserCreate = {
        uid: uid,
        birthDate: birthDate || '',
        country: country || '',
        email: email || '',
        lastName: lastName || '',
        name: name || '',
        passions: passions || [],
        password: password || '',
        sex: sex || '',
        showGenderProfile: showGenderProfile || false,
        photos: photos,
      }
      await this.database.setData(Const.COLLECTION_USERS, user);
      this.local.set('uid', user.uid);
      this.local.set('reload', true);
      this.formGroup.reset();
      this.router.navigate(['/home']);
    }
  }

}
