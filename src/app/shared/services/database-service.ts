import { Injectable } from '@angular/core';
import { addDoc, Firestore, collection, getDocs, where, query, doc, updateDoc } from '@angular/fire/firestore';
import { Const } from 'src/app/const/const';
import { IUserCreate } from 'src/app/interfaces/iuser-create';

@Injectable({
  providedIn: 'root'
})
export class DatabaseService {

  constructor(private readonly database: Firestore) { }

  async setData(nameCollection: string, data: any) {
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

  async findUserByUid(uid: string): Promise<IUserCreate | null> {
    const ref = collection(this.database, Const.COLLECTION_USERS);
    const q = query(ref, where('uid', '==', uid));
    const result = await getDocs(q);
    let data: IUserCreate | null = null;
    result.forEach(doc => data = doc.data() as IUserCreate);
    return data;
  }

  async updateData(uid: string, data: any) {
    const idDoc = await this.findDoc(uid) || '';
    if (idDoc) {
      const ref = doc(this.database, Const.COLLECTION_USERS, idDoc);
      await updateDoc(ref, data);
      return true;
    }
    return false;
  }

    private async findDoc(uid:string){
    const ref = collection(this.database, Const.COLLECTION_USERS);
    const q = query(ref, where('uid', '==', uid));
    const result = await getDocs(q);
    if (result.empty) {
      console.log('documento no encontrado');
      return;
    }
    let doc:string = '';
    result.forEach(d => doc = d.id);
    return doc;
  }
}
