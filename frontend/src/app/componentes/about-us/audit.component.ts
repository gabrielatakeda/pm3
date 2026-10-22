// audit.component.ts
import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Auditoria } from '../../models/auditoria';
import { AuditoriaService } from '../../services/auditoria-service';
import { DataService } from '../../services/data-service';
import { NgxMaskDirective } from 'ngx-mask';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { MdbModalModule } from 'mdb-angular-ui-kit/modal';

@Component({
  selector: 'app-audit',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DatePipe,
    NgxMaskDirective,
    MdbFormsModule,
    MdbModalModule
  ],
  templateUrl: './audit.component.html',
  styleUrl: './audit.component.scss'
})
export class AuditComponent implements OnInit {

  // Filtros
  entidade = '';
  createdBy = '';
  lastModifiedBy = '';
  modifiedDate1 = '';
  modifiedDate2 = '';

  // Paginação
  currentPage: number = 1;
  pageSize: number = 10;
  totalItems: number = 0;
  totalPages: number = 0;
  pageSizeOptions: number[] = [5, 10, 25, 50, 100];

  // Lista de auditorias
  lista: Auditoria[] = [];

  // Injeções
  auditoriaService = inject(AuditoriaService);
  dataService = inject(DataService);

  ngOnInit() {
    this.buscarAuditorias();
  }

  // Constrói filtros e chama o serviço
  buscarAuditorias() {
    const filtros = {
      entidade: this.entidade || null,
      criadoPor: this.createdBy || null,
      modificadoPor: this.lastModifiedBy || null,
      dataInicio: this.dataService.converterInputParaLocalDateTime(this.modifiedDate1),
      dataFim: this.dataService.converterInputParaLocalDateTime(this.modifiedDate2)
    };

    this.auditoriaService.listarTudo(filtros, this.currentPage - 1, this.pageSize)
      .subscribe({
        next: res => {
          this.lista = res.content;
          //this.totalItems = res.totalPages;
          this.totalPages = res.totalPages;
        },
        error: err => console.error("Erro ao carregar auditorias", err)
      });
  }

  // Chamado pelo botão de filtro
  onFilter() {
    this.currentPage = 1;
    this.buscarAuditorias();
  }

   // Nos métodos de navegação, mantenha currentPage começando em 1
	  previousPage(): void {
		  if (this.currentPage > 1) { // Mude para 1 em vez de 0
			  this.currentPage--;
			  this.buscarAuditorias();
		  }
	  }
	
	  nextPage(): void {
		  if (this.currentPage < this.totalPages) { // Remova o -1
			  this.currentPage++;
			  this.buscarAuditorias();
		  }
	  }

  onPageSizeChange() {
    this.currentPage = 1;
    this.buscarAuditorias();
  }

  onPageChange(page: number) {
		this.currentPage = page;
		this.buscarAuditorias();
	}

  limparCampos() {
    this.entidade = '';
    this.createdBy = '';
    this.lastModifiedBy = '';
    this.modifiedDate1 = '';
    this.modifiedDate2 = '';
    this.currentPage = 1;
    this.pageSize = 10;
    this.buscarAuditorias();
  }
}
