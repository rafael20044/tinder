import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Const } from 'src/app/const/const';
import { LocalStorageService } from 'src/app/shared/services/local-storage';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.page.html',
  styleUrls: ['./welcome.page.scss'],
  standalone: false,
})
export class WelcomePage implements OnInit {

  constructor(
    private readonly local:LocalStorageService,
    private readonly router:Router  
  ) { }

  ngOnInit() {
  }

  ok(){
    this.local.set(Const.GOT_IT, true);
    this.router.navigate(['/login']);
  }

}
