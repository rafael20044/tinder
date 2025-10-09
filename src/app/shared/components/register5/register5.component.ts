import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';
import { FilePickerService } from 'src/app/core/provider/file-picker-service';
import { SupabaseService } from '../../services/supabase-service';
import { Const } from 'src/app/const/const';
import { IPhoto } from 'src/app/interfaces/iphoto';

@Component({
  selector: 'app-register5',
  templateUrl: './register5.component.html',
  styleUrls: ['./register5.component.scss'],
  standalone: false,
})
export class Register5Component  implements OnInit {

  @Input() fileControl = new FormControl();

  @Output() emitter = new EventEmitter<number>();

  files:IPhoto[] = [];

  constructor(private readonly file:FilePickerService, private readonly supabase:SupabaseService) { }

  ngOnInit() {
    this.loadFiles();
  }

  doEmitter(id:number){
    this.emitter.emit(id);
  }

  async pick(){
    const file = await this.file.pickerPhoto();
    if (file) {
      const result = await this.supabase.upload(Const.BUCKET, 'photo', file.name, file.data || '', file.mimeType);
      if (result) {
        this.files.push(result);
        this.fileControl.setValue(this.files);
      }
    }
  }

  private loadFiles(){
    this.files = this.fileControl.value || [];
  }

}
