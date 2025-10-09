import { Injectable } from '@angular/core';
import {addDoc, Firestore, collection, getDocs} from '@angular/fire/firestore';

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

    async getAll<T>(nameCollection: string) {
    try {
      const collectionRef = collection(this.database, nameCollection);
      const snapshot = await getDocs(collectionRef);

      const data = snapshot.docs.map(doc => ({
        ...(doc.data() as T)
      }));

      return data;
    } catch (error) {
      console.error('Error al obtener documentos:', error);
      return [];
    }
  }
}
