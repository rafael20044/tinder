import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FilePickerService } from 'src/app/core/provider/file-picker-service';

@Component({
  selector: 'app-register5',
  templateUrl: './register5.component.html',
  styleUrls: ['./register5.component.scss'],
  standalone: false,
})
export class Register5Component  implements OnInit {

  @Output() emitter = new EventEmitter<number>();

  constructor(private readonly file:FilePickerService) { }

  ngOnInit() {}

  doEmitter(id:number){
    this.emitter.emit(id);
  }

  async pick(){
    const file = await this.file.pickerPhoto();
    console.log(file);
  }

}
