import { Component, ElementRef, ViewChild, OnInit, inject } from '@angular/core';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { Login } from '../../models/login';
import { Usuarios } from '../../models/usuarios';
import { Router } from '@angular/router';
import { UsuarioService } from '../../services/usuario-service';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { AutenticarService } from '../../services/autenticar-service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [MdbFormsModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {

  @ViewChild('loginVideo', { static: true }) loginVideo!: ElementRef<HTMLVideoElement>;

  login: Login = new Login();
  mostrarSenha = false;
  carregando = false;
  usuario: Usuarios = new Usuarios();
  router = inject(Router);
  usuarioService = inject(UsuarioService);
  autenticarService = inject(AutenticarService);

  ngOnInit() {
    this.autenticarService.logout();
    this.tryPlayVideo();
  }

  tryPlayVideo() {
    const video = this.loginVideo.nativeElement;

    // Força o vídeo a estar em 'muted' para permitir autoplay
    video.muted = true;

    video.play().catch(error => {
      console.error("Erro ao tentar reproduzir o vídeo:", error);

      // Listener para interação do usuário se o autoplay falhar
      document.addEventListener('click', () => {
        video.play();
      }, { once: true });
    });
  }

  autenticar() {
    if (this.carregando) return;
    this.carregando = true;

    this.autenticarService.login(this.login).subscribe({
      next: token => {
        this.carregando = false;
        this.autenticarService.addToken(token);
        
        // Exibir Toast de sucesso
        Swal.fire({
          toast: true,
          position: "bottom-end",
          icon: "success",
          title: "Autenticado com sucesso!",
          showConfirmButton: false,
          timer: 3000,
          timerProgressBar: true,
          didOpen: toast => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
          }
        });
  
        // Simulando um usuário (caso o serviço de autenticação retorne um)
        const user = { login: this.login.usuario }; // Ajuste conforme necessário
        localStorage.setItem('usuario', JSON.stringify(user));
  
        // Redirecionar para o painel administrativo
        this.router.navigate(['admin/dashboard']);
      },
      error: erro => {
        this.carregando = false;
        // Exibir Toast de erro
        Swal.fire({
          toast: true,
          position: "top-end",
          icon: "error",
          title: "Usuário ou senha incorretos!",
          showConfirmButton: false,
          timer: 3000,
          timerProgressBar: true,
          didOpen: toast => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
          }
        });
  
        console.error("Erro de login:", erro);
      }
    });
  }
  
}