import { HttpErrorResponse, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import Swal from 'sweetalert2';

export const meuhttpInterceptor: HttpInterceptorFn = (request: HttpRequest<any>, next) => {
  const router = inject(Router);

  // Pega token do localStorage
  const token = localStorage.getItem('token');
  console.log('[Interceptor] Token atual:', token);

  // Adiciona header Authorization se existir token
  if (token) {
    request = request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  // Log da requisição
  console.log('[Interceptor] Requisição HTTP:', {
    url: request.url,
    method: request.method,
    headers: request.headers
  });

  return next(request).pipe(
    catchError((err: HttpErrorResponse) => {
      console.error('[Interceptor] Erro HTTP capturado:', err);

      if (err.status === 401) {
        Swal.fire('Sessão expirada', 'Faça login novamente.', 'warning');
        localStorage.clear();
        router.navigate(['/login']);
      }

      if (err.status === 403) {
        Swal.fire('Acesso negado', `403 Forbidden.\nDetalhes: ${JSON.stringify(err.error)}`, 'error');
        // Não redireciona automaticamente para teste
        console.log('[Interceptor] 403 recebido do backend. Verifique token/roles.');
      }

      return throwError(() => err);
    })
  );
};
