import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { Emprestimos } from '../../../models/emprestimos';
import { Alunos } from '../../../models/alunos';
import { Equipamentos } from '../../../models/equipamentos';
import { EmprestimoService } from '../../../services/emprestimo-service';
import { AlunoService } from '../../../services/aluno-service';
import { EquipamentoService } from '../../../services/equipamento-service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { UsuarioService } from '../../../services/usuario-service';
import { Usuarios } from '../../../models/usuarios';
import { AutenticarService } from '../../../services/autenticar-service';

@Component({
  selector: 'app-emprestimo-form',
  standalone: true,
  imports: [MdbFormsModule, FormsModule],
  templateUrl: './emprestimo-form.component.html',
  styleUrl: './emprestimo-form.component.scss'
})
export class EmprestimoFormComponent {

  emprestimo: Emprestimos = new Emprestimos();
  aluno: Alunos = new Alunos();
  equipamento: Equipamentos = new Equipamentos();
  usuario!: Usuarios | null;
  emprestimoEncontrado = false;

  emprestimoService = inject(EmprestimoService);
  alunoService = inject(AlunoService);
  equipamentoService = inject(EquipamentoService);
  usuarioService = inject(UsuarioService);
  usuarioAutenticado = inject(AutenticarService);
  router = inject(Router);

  ngOnInit() {
    this.usuario = this.usuarioAutenticado.getUsuarioAutenticado();

    if (this.usuario) {
      // Atribui o usuário autenticado ao empréstimo
      this.emprestimo.usuario = this.usuario;
    } else {
      console.warn("Usuário não autenticado.");
    }
  }

  findAlunoByRa() {
    this.alunoService.findByRa(this.aluno.ra).subscribe({
      next: aluno => {
        if (aluno) {
          this.aluno = aluno;
        } else {
          this.resetAlunoFields(); // Limpa os campos caso o aluno não seja encontrado
          Swal.fire({
            toast: true,
            position: "bottom-end", // Exibe no canto inferior direito
            icon: "error",
            title: "Aluno não encontrado!",
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            didOpen: toast => {
              toast.onmouseenter = Swal.stopTimer;
              toast.onmouseleave = Swal.resumeTimer;
            }
          });
        }
      },
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      error: erro => {
        this.resetAlunoFields();
        Swal.fire({
          toast: true,
          position: "bottom-end", // Exibe no canto inferior direito
          icon: "error",
          title: "Aluno não encontrado!",
          showConfirmButton: false,
          timer: 3000,
          timerProgressBar: true,
          didOpen: toast => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
          }
        });
      }
    });
  }
  

  resetAlunoFields() {
    this.aluno = { id: 0, ra: '', nome: '', curso: '', ativo: 0};
  }

  findEquipamentoByPatrimonio() {
    const patrimonio = this.equipamento.patrimonio;
  
    // Se o campo de patrimônio estiver vazio, não faz nada
    if (!patrimonio) {
      return;
    }
  
    // Verifica se o equipamento possui empréstimo em andamento
    this.emprestimoService.findByEquipamentoPorEmprestimoAtivo(patrimonio).subscribe(
      (emprestimo) => {
        if (emprestimo) {
          if (this.aluno.ra) { // Verifica se 'ra' já tem um valor válido
            Swal.fire({
              toast: true,
              position: "bottom-end", // Exibe no canto inferior direito
              icon: "error",
              title: "O equipamento informado já está emprestado!",
              showConfirmButton: false,
              timer: 3000,
              timerProgressBar: true,
              didOpen: toast => {
                toast.onmouseenter = Swal.stopTimer;
                toast.onmouseleave = Swal.resumeTimer;
              }
            });
  
            this.equipamento.patrimonio = ''; // Limpa o patrimônio se o equipamento já está emprestado
          } else {
            // Caso não haja erro, preenche os dados do empréstimo, aluno e equipamento
            this.emprestimo = emprestimo;
            this.aluno = emprestimo.aluno;
            this.equipamento = emprestimo.equipamento;
            this.emprestimoEncontrado = true;
          }
        } else {
          // Se não houver empréstimo ativo, busca os dados do equipamento
          this.equipamentoService.findByPatrimonio(patrimonio).subscribe(
            (equipamento) => {
              if (equipamento) {
                this.equipamento = equipamento;
              } else {
                this.resetEquipamentoFields(); // Limpa os campos caso o equipamento não seja encontrado
                Swal.fire({
                  toast: true,
                  position: "bottom-end", // Exibe no canto inferior direito
                  icon: "error",
                  title: "Equipamento não encontrado!",
                  showConfirmButton: false,
                  timer: 3000,
                  timerProgressBar: true,
                  didOpen: toast => {
                    toast.onmouseenter = Swal.stopTimer;
                    toast.onmouseleave = Swal.resumeTimer;
                  }
                });
              }
            },
            (error) => {
              Swal.fire({
                toast: true,
                position: "bottom-end", // Exibe no canto inferior direito
                icon: "error",
                title: error.error,
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true,
                didOpen: toast => {
                  toast.onmouseenter = Swal.stopTimer;
                  toast.onmouseleave = Swal.resumeTimer;
                }
              });
  
              this.resetEquipamentoFields();
            }
          );
        }
      },
      (error) => {
        console.error("Erro ao verificar empréstimo", error);
      }
    );
  }
  
  

  resetEquipamentoFields() {
    this.equipamento = {
      id: 0,  // ou 0, dependendo do tipo de dado
      patrimonio: '',
      modelo: '',
      marca: '',
      dataAquisicao: '',
      observacao: '',
      situacao: '',
      ativo: 0 // ou true, dependendo da lógica do seu sistema
    };
  }

  iniciarEmprestimo() {
    this.emprestimo.aluno = this.aluno;
    this.emprestimo.equipamento = this.equipamento;
  
    this.emprestimoService.save(this.emprestimo).subscribe({
      next: mensagem => {
        // Exibir Toast de sucesso
        Swal.fire({
          toast: true,
          position: "bottom-end",
          icon: "success",
          title: mensagem,
          showConfirmButton: false,
          timer: 3000,
          timerProgressBar: true,
          didOpen: toast => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
          }
        });
  
        this.limparCampo();
  
        // Retorna o foco para o campo RA do Aluno após limpar os campos
        setTimeout(() => {
          document.getElementById("raAluno")?.focus();
        }, 100);
      },
      error: erro => {
        // Exibir Toast de erro
        Swal.fire({
          toast: true,
          position: "bottom-end",
          icon: "error",
          title: erro.error,
          showConfirmButton: false,
          timer: 3000,
          timerProgressBar: true,
          didOpen: toast => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
          }
        });
  
        this.limparCampo();
  
        // Retorna o foco para o campo RA do Aluno após limpar os campos
        setTimeout(() => {
          document.getElementById("raAluno")?.focus();
        }, 100);
      }
    });
  }
  

  encerrarEmprestimo() {
    this.emprestimo.aluno = this.aluno;
    this.emprestimo.equipamento = this.equipamento;
    console.log(this.emprestimo.id);
  
    this.emprestimoService.encerrar(this.emprestimo.id, this.emprestimo).subscribe({
      next: mensagem => {
        // Exibir Toast de sucesso
        Swal.fire({
          toast: true,
          position: "bottom-end",
          icon: "success",
          title: mensagem,
          showConfirmButton: false,
          timer: 3000,
          timerProgressBar: true,
          didOpen: toast => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
          }
        });
  
        this.limparCampo();
        // this.router.navigate(['admin/emprestimo/new']);
      },
      error: erro => {
        // Exibir Toast de erro
        Swal.fire({
          toast: true,
          position: "bottom-end",
          icon: "error",
          title: erro.error,
          showConfirmButton: false,
          timer: 3000,
          timerProgressBar: true,
          didOpen: toast => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
          }
        });
      }
    });
  }  
  

  limparCampo(){
    this.aluno.ra = '';
    this.aluno.nome = '';
    this.aluno.curso = '';
    this.equipamento.patrimonio = '';
    this.equipamento.modelo = '';
    this.equipamento.marca = '';
    this.emprestimo.observacao = '';
    this.emprestimoEncontrado = false;
  }

}
