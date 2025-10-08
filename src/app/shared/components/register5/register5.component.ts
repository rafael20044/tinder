import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-register5',
  templateUrl: './register5.component.html',
  styleUrls: ['./register5.component.scss'],
  standalone: false,
})
export class Register5Component  implements OnInit {

  @Output() emitter = new EventEmitter<number>();

  constructor() { }

  ngOnInit() {}

  doEmitter(id:number){
    this.emitter.emit(id);
  }

}
