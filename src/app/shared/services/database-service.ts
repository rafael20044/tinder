import { Injectable } from '@angular/core';
import {addDoc, Firestore, collection} from '@angular/fire/firestore';

@Injectable({
  providedIn: 'root'
})
export class DatabaseService {
  
  constructor(private readonly database:Firestore){}

  async setData(nameCollection:string, data:any){
    try {
      const collectionRef = collection(this.database, nameCollection);
      const ref = await addDoc(collectionRef, data);
      return ref;
    } catch (error) {
      console.log(error);
      return;
    }
  }
}
