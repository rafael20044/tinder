import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Const } from 'src/app/const/const';
import { LocalStorageService } from 'src/app/shared/services/local-storage';

export const welcomeGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const local = inject(LocalStorageService);
  let isGotIt = local.get<boolean>(Const.GOT_IT);
  if (!isGotIt) {
    router.navigate(['/welcome']);
    return false;
  }
  return true;
};
