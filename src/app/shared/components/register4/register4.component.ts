import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Ipassions } from 'src/app/interfaces/ipassions';

@Component({
  selector: 'app-register4',
  templateUrl: './register4.component.html',
  styleUrls: ['./register4.component.scss'],
  standalone: false,
})
export class Register4Component  implements OnInit {

  @Input() passionControl = new FormControl();
  @Output() emitter = new EventEmitter<number>();

  passions: Ipassions[] = [
    {name: 'Music', selected: false},
    {name: 'Sports', selected: false},
    {name: 'Travel', selected: false},
    {name: 'Reading', selected: false},
    {name: 'Cooking', selected: false},
    {name: 'Movies', selected: false},
    {name: 'Fitness', selected: false},
    {name: 'Art', selected: false},
    {name: 'Gaming', selected: false},
    {name: 'Dancing', selected: false},
  ];

  passionsSelected:string[] = [];

  constructor() { }

  ngOnInit() {
    this.passionsSelected = this.passionControl.value;
    this.verifySelected();
  }

  select(index:number){
    this.passions[index].selected = !this.passions[index].selected;
    this.passionsSelected = this.passions.filter(p => p.selected).map(p => p.name);
    this.passionControl.setValue(this.passionsSelected);
  }

  doEmitter(id:number){
    this.emitter.emit(id);
  }

  private verifySelected(){
    this.passions.forEach(p => {
      if (this.passionsSelected.includes(p.name)) {
        p.selected = true;
      }
    });
  }

}
