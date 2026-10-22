import { RouterLink } from '@angular/router';
import { Component, inject } from '@angular/core';
import { Alunos } from '../../../models/alunos';
import { AlunoService } from '../../../services/aluno-service';
import { FormsModule } from '@angular/forms';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import Swal from 'sweetalert2';
import { LoginService } from '../../../auth/login.service';

@Component({
  selector: 'app-aluno-list',
  standalone: true,
  imports: [RouterLink, FormsModule, MdbFormsModule],
  templateUrl: './aluno-list.component.html',
  styleUrl: './aluno-list.component.scss'
})
export class AlunoListComponent {
	loginService = inject(LoginService);
  	lista: Alunos[] = [];

  // Variáveis de paginação
	currentPage = 1; // Página atual (começa em 1 para o usuário)
	pageSize = 10; // Itens por página
	totalItems = 0; // Total de itens
	totalPages = 0; // Total de páginas
	pageSizeOptions = [5, 10, 25, 50, 100, 250, 500];

	//Variáveis de Filtro
	ra = '';
	curso = '';
	nome = '';


  alunoService = inject(AlunoService);

  constructor() {
		this.loadPage();
	  }
	
	  loadPage() {
		// Ajuste apenas para a chamada da API
		const pageToSend = this.currentPage - 1;
		
		// eslint-disable-next-line @typescript-eslint/no-explicit-any
		this.alunoService.findAllPage(pageToSend, this.pageSize).subscribe((data: any) => {
			this.lista = data.content;
			this.totalItems = data.totalElements;
			this.totalPages = data.totalPages || Math.ceil(this.totalItems / this.pageSize);
		});
	}
	
	  // Nos métodos de navegação, mantenha currentPage começando em 1
	  previousPage(): void {
		  if (this.currentPage > 1) { // Mude para 1 em vez de 0
			  this.currentPage--;
			  this.loadPage();
		  }
	  }
	
	  nextPage(): void {
		  if (this.currentPage < this.totalPages) { // Remova o -1
			  this.currentPage++;
			  this.loadPage();
		  }
	  }
	
	  onPageSizeChange() {
		this.currentPage = 1; // Reset para a primeira página quando mudar o tamanho
		this.loadPage();
	  }
	
	  onPageChange(page: number) {
		this.currentPage = page;
		this.loadPage();
	  }

  findAll(){
    this.alunoService.findAll().subscribe({
        next: list => { //Equivalente ao TRy
            this.lista = list;
        },
        error: erro => { // Equivalente ao CATCH ou EXCEPTIONS
            alert("Deu erro!"+erro);
        }
    })

 }

 filtrarCampos() {
	this.alunoService.findByFilter(
		this.ra, this.nome, this.curso).subscribe({
		next: (list: Alunos[]) => {
			this.lista = list;
			this.currentPage = 1; // Reinicia a paginação
			this.totalPages = 1; // Garante que tudo será exibido em uma única página
		},
		error: (erro) => {
			console.log(erro);
			Swal.fire("Erro", erro.error, 'error');
		}
	});
}

desativar(ra: string){
	const swalWithBootstrapButtons = Swal.mixin({
		customClass: {
		confirmButton: "btn btn-success",
		cancelButton: "btn btn-danger"
		},
		buttonsStyling: false
	});
	
	// Mostra o alerta antes de deletar
	swalWithBootstrapButtons.fire({
		title: "Tem certeza que deseja desativar este aluno?",
		icon: "warning",
		showCancelButton: true,
		confirmButtonText: "Sim",
		cancelButtonText: "Cancelar",
		reverseButtons: true
	}).then((result) => {
		// Se confirmado, chama o serviço de deleção
		if (result.isConfirmed) {
		this.alunoService.desativar(ra).subscribe({
			next: mensagem => {
			console.log(mensagem);
			console.log(JSON.stringify(mensagem));
			swalWithBootstrapButtons.fire({
				title: "Pronto!",
				text: typeof mensagem === 'string' ? mensagem : JSON.stringify(mensagem), // Verifica se é string
				icon: "success"
			});
			this.loadPage(); // Atualiza a lista de equipamentos após a exclusão
			},
			error: erro => {
			Swal.fire("Erro", erro.error, 'error'); // Trata erros ao tentar excluir
			}
		});
		} else if (result.dismiss === Swal.DismissReason.cancel) {
		// Mostra mensagem de cancelamento
		swalWithBootstrapButtons.fire({
			title: "Ação cancelada",
			text: "O aluno não foi desativado.",
			icon: "warning"
		});
		}
	});
}

reativar(ra:string){
	  const swalWithBootstrapButtons = Swal.mixin({
	  customClass: {
		confirmButton: "btn btn-success",
		cancelButton: "btn btn-danger"
	  },
	  buttonsStyling: false
	});
  
	// Mostra o alerta antes de deletar
	swalWithBootstrapButtons.fire({
	  title: "Tem certeza que deseja reativar este aluno?",
	  icon: "warning",
	  showCancelButton: true,
	  confirmButtonText: "Sim",
	  cancelButtonText: "Cancelar",
	  reverseButtons: true
	}).then((result) => {
	  // Se confirmado, chama o serviço de deleção
	  if (result.isConfirmed) {
		this.alunoService.reativar(ra).subscribe({
		  next: mensagem => {
			console.log(mensagem);
			console.log(JSON.stringify(mensagem));
			swalWithBootstrapButtons.fire({
			  title: "Pronto!",
			  text: typeof mensagem === 'string' ? mensagem : JSON.stringify(mensagem), // Verifica se é string
			  icon: "success"
			});
			this.loadPage(); // Atualiza a lista de equipamentos após a exclusão
		  },
		  error: erro => {
			Swal.fire("Erro", erro.error, 'error'); // Trata erros ao tentar excluir
		  }
		});
	  } else if (result.dismiss === Swal.DismissReason.cancel) {
		// Mostra mensagem de cancelamento
		swalWithBootstrapButtons.fire({
		  title: "Ação cancelada",
		  text: "O aluno não foi reativado.",
		  icon: "warning"
		});
	  }
	});
	}


	limparCampos(){
		this.ra = '';
		this.nome = '';
		this.curso = '';
		this.currentPage = 1;
		this.pageSize = 10;
   		this.loadPage();
	}
}
