import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/shared/services/auth-service';

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrls: ['./home.page.scss'],
  standalone: false,
})
export class HomePage implements OnInit {

  constructor(private readonly auht:AuthService) { }

  ngOnInit() {
  }

  exit(){
    this.auht.mySingOut();
  }

}
