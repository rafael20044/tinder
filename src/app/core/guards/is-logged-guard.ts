import { CanActivateFn, Router } from '@angular/router';
import {Auth, onAuthStateChanged,} from '@angular/fire/auth';
import { inject } from '@angular/core';

export const isLoggedGuard: CanActivateFn = (route, state) => {
  const auth = inject(Auth);
  const router = inject(Router);

  return new Promise<boolean>((resolve) =>{
    onAuthStateChanged(auth, (user) =>{
      if (!user) {
        resolve(true);
      }else{
        router.navigate(['/home']);
        resolve(false);
      }
    });
  });
  
  //return true;
};
