import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { Emprestimos } from '../../../models/emprestimos';
import { EmprestimoService } from '../../../services/emprestimo-service';
import Swal from 'sweetalert2';
import { DatePipe } from '@angular/common';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';
import { DataService } from '../../../services/data-service';
import { LoginService } from '../../../auth/login.service';
import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver';
import { MdbModalModule } from 'mdb-angular-ui-kit/modal';


@Component({
	selector: 'app-emprestimo-list',
	standalone: true,
	imports: [FormsModule, MdbFormsModule, DatePipe, NgxMaskDirective, MdbModalModule],
	templateUrl: './emprestimo-list.component.html',
	styleUrl: './emprestimo-list.component.scss',
	providers: [provideNgxMask()]
})
export class EmprestimoListComponent {
	lista: Emprestimos[] = [];
	emprestimo!: Emprestimos;
	dataRetirada: Date | undefined;
	dataDevolucao: Date | undefined;
	situacao = '';
	patrimonio = '';
	ra = '';
	usuario = '';

	// Variáveis de paginação
	currentPage = 1; // Página atual (começa em 1 para o usuário)
	pageSize = 10; // Itens por página
	totalItems = 0; // Total de itens
	totalPages = 0; // Total de páginas
	pageSizeOptions = [5, 10, 25, 50, 100, 250, 500];


	emprestimoService = inject(EmprestimoService);
	dataService = inject(DataService);
	loginService = inject(LoginService);

	constructor() {
		this.loadPage();
	  }
	
	  loadPage() {
		// Ajuste apenas para a chamada da API
		const pageToSend = this.currentPage - 1;
		
		// eslint-disable-next-line @typescript-eslint/no-explicit-any
		this.emprestimoService.findAllPage(pageToSend, this.pageSize).subscribe((data: any) => {
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

	  
	filtrarCampos() {
		this.emprestimoService.findByFilter(
			this.dataRetirada, this.dataDevolucao, this.situacao, this.ra, this.usuario, this.patrimonio
		).subscribe({
			next: (list: Emprestimos[]) => {
				this.lista = list.sort((a: Emprestimos, b: Emprestimos) => 
					new Date(b.dataRetirada).getTime() - new Date(a.dataRetirada).getTime()
				);
				this.currentPage = 1; // Reinicia a paginação
				this.totalPages = 1; // Garante que tudo será exibido em uma única página
			},
			error: (erro) => {
				console.log(erro);
				Swal.fire("Erro", erro.error, 'error');
			}
		});
	}
	
	
	
	
		confirmarEncerramento(emprestimo: Emprestimos) {
		Swal.fire({
			title: "Tem certeza?",
			text: `Deseja realmente encerrar o empréstimo para o aluno ${emprestimo.aluno.nome}?`,
			icon: "warning",
			showCancelButton: true,
			confirmButtonColor: "#3085d6",
			cancelButtonColor: "#d33",
			confirmButtonText: "Sim, encerrar!",
			cancelButtonText: "Cancelar"
		}).then((result) => {
			if (result.isConfirmed) {
				this.encerrarEmprestimo(emprestimo);
			}
		});
	}

	encerrarEmprestimo(emprestimo: Emprestimos) {
		this.emprestimoService.encerrar(emprestimo.id, emprestimo).subscribe({
			next: () => {
				Swal.fire({
					toast: true,
					position: "bottom-end",
					icon: "success",
					title: "Empréstimo encerrado com sucesso!",
					showConfirmButton: false,
					timer: 3000
				});
				this.loadPage(); // Atualiza a lista após encerrar
			},
			error: erro => {
				console.error("Erro ao encerrar empréstimo:", erro);
				Swal.fire("Erro", "Não foi possível encerrar o empréstimo.", "error");
			}
		});
	}

		exportarParaXLSX() {
			// Verifica se algum campo de filtro está preenchido
			const filtrosPreenchidos = this.dataRetirada || this.dataDevolucao || this.situacao || this.ra || this.usuario || this.patrimonio;
		  
			// Se algum filtro estiver preenchido, faz a busca filtrada
			if (filtrosPreenchidos) {
				this.filtrarCampos();
			  this.emprestimoService.findByFilter(
				this.dataRetirada, this.dataDevolucao, this.situacao, this.ra, this.usuario, this.patrimonio
			  ).subscribe(emprestimos => {
				// eslint-disable-next-line @typescript-eslint/no-explicit-any
				const dadosFormatados = emprestimos.map((e: any) => {
				  return {
					'Patrimônio': e.equipamento?.patrimonio || '',
					'RA': e.aluno?.ra || '',
					'Nome': e.aluno?.nome || '',
					'Curso': e.aluno?.curso || '',
					'Data de Retirada': this.dataService.formatarDataHora(e.dataRetirada),
					'Data de Devolução': this.dataService.formatarDataHora(e.dataDevolucao)
				  };
				});
		  
				const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(dadosFormatados);
				const workbook: XLSX.WorkBook = { Sheets: { 'Empréstimos': worksheet }, SheetNames: ['Empréstimos'] };
				const excelBuffer: ArrayBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
		  
				const blob = new Blob([excelBuffer], { type: 'application/octet-stream' });
				saveAs(blob, 'relatorio_emprestimos.xlsx');
			  });
			} else {
			  // Se nenhum filtro estiver preenchido, retorna todos os dados
			  this.emprestimoService.findAll().subscribe(emprestimos => {
				// eslint-disable-next-line @typescript-eslint/no-explicit-any
				const dadosFormatados = emprestimos.map((e: any) => {
				  return {
					'Patrimônio': e.equipamento?.patrimonio || '',
					'RA': e.aluno?.ra || '',
					'Nome': e.aluno?.nome || '',
					'Curso': e.aluno?.curso || '',
					'Data de Retirada': this.dataService.formatarDataHora(e.dataRetirada),
					'Data de Devolução': this.dataService.formatarDataHora(e.dataDevolucao)
				  };
				});
		  
				const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(dadosFormatados);
				const workbook: XLSX.WorkBook = { Sheets: { 'Empréstimos': worksheet }, SheetNames: ['Empréstimos'] };
				const excelBuffer: ArrayBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
		  
				const blob = new Blob([excelBuffer], { type: 'application/octet-stream' });
				saveAs(blob, 'relatorio_emprestimos.xlsx');
			  });
			}
		  }
		  


	limparCampos(){
		this.dataRetirada = undefined;
		this.dataDevolucao = undefined;
		this.situacao = '';
		this.patrimonio = '';
		this.ra = '';
		this.usuario = '';
		this.loadPage();
	}
}
