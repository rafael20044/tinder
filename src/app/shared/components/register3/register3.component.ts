import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-register3',
  templateUrl: './register3.component.html',
  styleUrls: ['./register3.component.scss'],
  standalone: false,
})
export class Register3Component  implements OnInit {

  @Input() birthDateControl = new FormControl();

  @Output() emitter = new EventEmitter<number>();

  constructor() { }

  ngOnInit() {}

  doDone(event:any){
    const date = event.detail.value;
    this.birthDateControl.setValue(date);
  }

  doEmitter(id:number){
    this.emitter.emit(id);
  }

}
