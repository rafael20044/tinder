import { CanActivateFn, Router } from '@angular/router';
import {Auth, onAuthStateChanged,} from '@angular/fire/auth';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const auth = inject(Auth);

  return new Promise<boolean>((resolve) =>{
    onAuthStateChanged(auth, (user) =>{
      if (user) {
        resolve(true);
      }else{
        router.navigate(['/login']);
        resolve(false);
      }
    });
  });
  
  //return true;
};
