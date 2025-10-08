import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss'],
  standalone: false,
})
export class ButtonComponent  implements OnInit {

  @Input() text:string = '';
  @Input() type:ButtonType = 'button';

  constructor() { }

  ngOnInit() {}

}

type ButtonType = 'button' | 'submit';
