import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

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
  passionControl = new FormControl([]);
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
  });

  registerId = 1;

  constructor() { }

  async ngOnInit() {
  }

  getEmitter(id:number){
    this.registerId = id;
  }

  doSubmit(){
    console.log('holaaa');
    console.log(this.formGroup.value);
  }

}
