import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { UsuarioService } from '../../../services/usuario-service';
import { Usuarios } from '../../../models/usuarios';
import Swal from 'sweetalert2';
import { FormsModule } from '@angular/forms';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';

@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [RouterLink, FormsModule, MdbFormsModule, NgxMaskDirective],
  templateUrl: './usuario-form.component.html',
  styleUrl: './usuario-form.component.scss',
  providers: [provideNgxMask()]
})
export class UsuarioFormComponent {

  tituloComponente = "Novo Usuário";
  router = inject(Router);
  rotaAtivada = inject(ActivatedRoute);
  usuarioService = inject(UsuarioService);
  usuario: Usuarios = new Usuarios();

  constructor(){
    const id = this.rotaAtivada.snapshot.params['id'];
    if(id > 0){
      this.tituloComponente = "Editar Usuário";
      this.findById(id);
    }else{
      this.usuario.id = 0;
      this.usuario.role = 'Admin';
    }
  }

  findById(id: number){
    this.usuarioService.findById(id).subscribe({
      next: user => {
        this.usuario = user;
      },
      error: erro => {
        Swal.fire('Erro', erro.error, 'error');
      }
    })

  }

  save(){
    this.usuarioService.save(this.usuario).subscribe({
      next: mensagem => {
        Swal.fire({
          title: mensagem,
          icon: "success"
        }).then(() => {
          this.router.navigate(['admin/usuario']);
        });
      },
      error: erro => {
        Swal.fire('Erro', erro.error, 'error');
      }
    });
  }

  update(){
    this.usuarioService.update(this.usuario).subscribe({
      next: mensagem => {
        Swal.fire({
          title: mensagem,
          icon: "success"
        }).then(() => {
          this.router.navigate(['admin/usuario']);
        });
      },
      error: erro => {
        Swal.fire('Erro', erro.error, 'error');
      }
    });

  }

}
