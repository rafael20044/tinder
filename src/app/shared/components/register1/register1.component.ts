import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register1',
  templateUrl: './register1.component.html',
  styleUrls: ['./register1.component.scss'],
  standalone: false,
})
export class Register1Component  implements OnInit {

  @Input() nameControl = new FormControl('');
  @Input() lastNameControl = new FormControl('');
  @Input() emailControl = new FormControl('');
  @Input() passwordControl = new FormControl('');
  @Input() countryControl = new FormControl('');

  @Output() emitter = new EventEmitter<number>();

  constructor(private readonly router:Router) { }

  ngOnInit() {}

  emitNextId(){
    this.emitter.emit(2);
  }

  goToLogin(){
    this.router.navigate(['/login']);
  }

}
