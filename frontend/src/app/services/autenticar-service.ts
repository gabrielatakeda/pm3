import { inject, Injectable } from '@angular/core';
import { Usuarios } from '../models/usuarios';
import { Observable} from 'rxjs';
import { UsuarioService } from './usuario-service';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Login } from '../models/login';
import { jwtDecode, JwtPayload } from "jwt-decode";
import { environment } from '../../environments/environment';

@Injectable({
	providedIn: 'root',
})
export class AutenticarService {
	http = inject(HttpClient);
	API = environment.API+'/api/usuarios';

	usuario: Usuarios = new Usuarios();
	usuarioService = inject(UsuarioService);
	router = inject(Router);

	login(login: Login): Observable<string> {
		return this.http.post<string>(this.API + '/login', login, { responseType: 'text' as 'json' });
	}
  

	getUsuarioAutenticado(): Usuarios | null {
    let user = this.jwtDecode() as Usuarios;
    return user;

	}

	logout() {
		localStorage.removeItem('token');
		this.router.navigate(['login']);
	}

  addToken(token: string) {
    localStorage.setItem('token', token);
  }

  removerToken() {
    localStorage.removeItem('token');
  }

  getToken() {
    return localStorage.getItem('token');
  }

  jwtDecode() {
    let token = this.getToken();
    if (token) {
      return jwtDecode<JwtPayload>(token);
    }
    return "";
  }

  hasPermission(role: string) {
    let user = this.jwtDecode() as Usuarios;
    if (user.role == role)
      return true;
    else
      return false;
  }

}