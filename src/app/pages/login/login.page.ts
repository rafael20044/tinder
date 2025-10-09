import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/shared/services/auth-service';

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  standalone: false,
})
export class LoginPage implements OnInit {

  emailControl = new FormControl('', [Validators.required, Validators.email]);
  passwordControl = new FormControl('', [Validators.required]);
  formGroup = new FormGroup({
    email: this.emailControl,
    password: this.passwordControl
  });

  constructor(private readonly router:Router, private readonly auth:AuthService) { }

  ngOnInit() {
  }

  goToRegister(){
    this.router.navigate(['/register']);
  }

  async doSubmit(){
    if (!this.formGroup.valid) {
      console.log('fill all inputs');
      return;
    }
    const {email, password} = this.formGroup.value;
    const user = await this.auth.loginWithEmailAndPassword(email || '', password || '');
    if (user) {
      this.router.navigate(['/home']);
    }
  }

}
