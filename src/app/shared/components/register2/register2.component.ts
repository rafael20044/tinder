import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-register2',
  templateUrl: './register2.component.html',
  styleUrls: ['./register2.component.scss'],
  standalone: false,
})
export class Register2Component  implements OnInit {

  @Input() sexControl = new FormControl();
  @Input() showGenderProfileControl = new FormControl();

  @Output() emitter = new EventEmitter<number>();
  sex = '';

  check = false;

  constructor() { }

  ngOnInit() {
    this.sex = this.sexControl.value;
    this.check = this.showGenderProfileControl.value;
  }

  checked(){
    this.check = !this.check;
    this.showGenderProfileControl.setValue(this.check);
  }

  goTo(id:number){
    this.emitter.emit(id);
  }

  selectSex(sex:string){
    this.sex = sex;
    this.sexControl.setValue(sex);
  }

}
