import { NgModule, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Capacitor } from '@capacitor/core';
import { FilePickerService } from './provider/file-picker-service';
import { StatusBarService } from './provider/status-bar';



@NgModule({
  declarations: [],
  providers: [FilePickerService],
  imports: [
    CommonModule
  ]
})
export class CoreModule implements OnInit { 

  constructor(private readonly file:FilePickerService, private readonly statusBar:StatusBarService){
    this.ngOnInit();
  }

  ngOnInit(): void {
    if (Capacitor.isNativePlatform()) {
      this.file.requestPermission();
      this.statusBar.setStatusBar();
    }
  }
}
