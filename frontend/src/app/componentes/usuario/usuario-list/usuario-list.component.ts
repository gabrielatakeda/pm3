import { RouterLink } from '@angular/router';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { Usuarios } from '../../../models/usuarios';
import { UsuarioService } from '../../../services/usuario-service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-usuario-list',
  standalone: true,
  imports: [RouterLink, FormsModule, MdbFormsModule],
  templateUrl: './usuario-list.component.html',
  styleUrl: './usuario-list.component.scss'
})
export class UsuarioListComponent {

  usuarioService = inject(UsuarioService);
  lista: Usuarios[] = [];

  constructor(){
    this.findAll();
  }

  findAll(){
    this.usuarioService.findAll().subscribe({
        next: (list) => {
          this.lista = list;
        },
        error: (erro) => {
          // Equivalente ao CATCH ou EXCEPTIONS
          Swal.fire('Erro', erro.error, 'error');
        }
    });
  }

  desativar(id: number){
    const swalWithBootstrapButtons = Swal.mixin({
      customClass: {
        confirmButton: "btn btn-success",
        cancelButton: "btn btn-danger"
      },
      buttonsStyling: false
    });
  
    // Mostra o alerta antes de deletar
    swalWithBootstrapButtons.fire({
      title: "Tem certeza que deseja desativar este usuário?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sim",
      cancelButtonText: "Cancelar",
      reverseButtons: true
    }).then((result) => {
      // Se confirmado, chama o serviço de deleção
      if (result.isConfirmed) {
        this.usuarioService.desativar(id).subscribe({
          next: mensagem => {
            console.log(mensagem);
            console.log(JSON.stringify(mensagem));
            swalWithBootstrapButtons.fire({
              title: "Pronto!",
              text: typeof mensagem === 'string' ? mensagem : JSON.stringify(mensagem), // Verifica se é string
              icon: "success"
            });
            this.findAll(); // Atualiza a lista de equipamentos após a exclusão
          },
          error: erro => {
            Swal.fire("Erro", erro.error, 'error'); // Trata erros ao tentar excluir
          }
        });
      } else if (result.dismiss === Swal.DismissReason.cancel) {
        // Mostra mensagem de cancelamento
        swalWithBootstrapButtons.fire({
          title: "Ação cancelada",
          text: "O usuário não foi desativado.",
          icon: "warning"
        });
      }
    });
  }

  reativar(id:number){
    const swalWithBootstrapButtons = Swal.mixin({
      customClass: {
        confirmButton: "btn btn-success",
        cancelButton: "btn btn-danger"
      },
      buttonsStyling: false
    });
  
    // Mostra o alerta antes de deletar
    swalWithBootstrapButtons.fire({
      title: "Tem certeza que deseja reativar este usuário?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sim",
      cancelButtonText: "Cancelar",
      reverseButtons: true
    }).then((result) => {
      // Se confirmado, chama o serviço de deleção
      if (result.isConfirmed) {
        this.usuarioService.reativar(id).subscribe({
          next: mensagem => {
            console.log(mensagem);
            console.log(JSON.stringify(mensagem));
            swalWithBootstrapButtons.fire({
              title: "Pronto!",
              text: typeof mensagem === 'string' ? mensagem : JSON.stringify(mensagem), // Verifica se é string
              icon: "success"
            });
            this.findAll(); // Atualiza a lista de equipamentos após a exclusão
          },
          error: erro => {
            Swal.fire("Erro", erro.error, 'error'); // Trata erros ao tentar excluir
          }
        });
      } else if (result.dismiss === Swal.DismissReason.cancel) {
        // Mostra mensagem de cancelamento
        swalWithBootstrapButtons.fire({
          title: "Ação cancelada",
          text: "O usuário não foi reativado.",
          icon: "warning"
        });
      }
    });
  }

}
