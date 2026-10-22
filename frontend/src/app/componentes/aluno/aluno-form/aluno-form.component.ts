import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';
import { NgxPaginationModule } from 'ngx-pagination';
import { Alunos } from '../../../models/alunos';
import { EquipamentoService } from '../../../services/equipamento-service';
import { DatePipe } from '@angular/common';
import { AlunoService } from '../../../services/aluno-service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-aluno-form',
  standalone: true,
  imports: [RouterLink, MdbFormsModule, FormsModule, NgxMaskDirective, NgxPaginationModule],
  templateUrl: './aluno-form.component.html',
  styleUrl: './aluno-form.component.scss',
  providers: [DatePipe, provideNgxMask()]
})
export class AlunoFormComponent {
  tituloComponente = "Novo Aluno"; 
  aluno: Alunos = new Alunos();
  situacaoAtual = '';
  alunoService: AlunoService = new AlunoService();
  

  router = inject(Router);
  rotaAtivada = inject(ActivatedRoute);
  equipamentoService = inject(EquipamentoService);
  datePipe = inject(DatePipe);

  constructor(){
    const id = this.rotaAtivada.snapshot.params['id'];
    if(id > 0){
      this.tituloComponente = "Editar Aluno";
      this.findById(id);
    }else {
      this.aluno.id = 0; // Ou algo similar para indicar um novo aluno
    }
  }

  findById(id: number){
    this.alunoService.findById(id).subscribe({
      next: aluno => {
        this.aluno = aluno;
      },
      error: erro => {
        Swal.fire('Erro', erro.error, 'error');
      }
    })
  }

  save(){
      this.alunoService.save(this.aluno).subscribe({
        next: mensagem => {
          Swal.fire({
            title: mensagem,
            icon: "success"
          }).then(() => {
            this.router.navigate(['admin/aluno']);
          });
        },
        error: erro => {
          Swal.fire('Erro', erro.error, 'error');
        }
      });
    }

update(){
    this.alunoService.update(this.aluno).subscribe({
      next: mensagem => {
        Swal.fire({
          title: mensagem,
          icon: "success"
        }).then(() => {
          this.router.navigate(['admin/aluno']);
        });
      },
      error: erro => {
        Swal.fire('Erro', erro.error, 'error');
      }
    });

  }

  reativar(){
      this.alunoService.reativar(this.aluno.ra).subscribe({
        next: mensagem => {
          Swal.fire({
            title: mensagem,
            icon: "success"
          }).then(() => {
            this.router.navigate(['admin/equipamento']);
          });
        },
        error: erro => {
          console.log(erro);
          Swal.fire('Erro', erro.error, 'error');
        }
      });
    }
  
}
