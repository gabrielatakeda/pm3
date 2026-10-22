import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { LoginService } from './login.service';

export const guardaGuard: CanActivateFn = (route, state) => {

  const loginService = inject(LoginService);
  const router = inject(Router);

  // Quem não é admin nao acessa a rota
  if(!loginService.hasPermission('Admin') && state.url == '/admin/usuario'){
    router.navigate(['/admin/dashboard']);
    return false
  }


  return true;
};
