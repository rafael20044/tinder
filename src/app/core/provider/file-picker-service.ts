import { Injectable } from '@angular/core';
import { FilePicker } from '@capawesome/capacitor-file-picker';

@Injectable({
  providedIn: 'root'
})
export class FilePickerService {

  constructor(){}

  async requestPermission(){
    try {
      await FilePicker.requestPermissions();
    } catch (error) {
      console.error(error);
    }
  }

  async pickerPhoto(){
    try {
      const result = await FilePicker.pickImages({
        readData: true,
        limit: 1,
      });
      return result.files[0];
    } catch (error) {
      console.error(error);
      return;
    }
  }
}
