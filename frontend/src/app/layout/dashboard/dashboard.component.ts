import { Component, inject } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import Swal from 'sweetalert2';

import { NgxChartsModule, Color, ScaleType } from '@swimlane/ngx-charts';

import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';

import { Emprestimos } from '../../models/emprestimos';
import { EmprestimoService } from '../../services/emprestimo-service';


interface ChartData {
  name: string;
  value: number;
}


@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    NgxChartsModule,
    MdbFormsModule,
    DatePipe,
    RouterLink
  ],
  providers: [DatePipe],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {

  lista: Emprestimos[] = [];

  emprestimosPorPatrimonio: ChartData[] = [];
  emprestimosPorCurso: ChartData[] = [];
  emprestimosPorAluno: ChartData[] = [];
  emprestimosPorDia: ChartData[] = [];

  // Indicadores do topo
  totalEmprestimos = 0;
  emAndamento = 0;
  encerrados = 0;
  equipamentosDistintos = 0;

  // Mesmo valor que vem selecionado no <select> do template
  periodoSelecionado = 30;

  datePipe = inject(DatePipe);

  emprestimoService = inject(EmprestimoService);


  // Tons de verde e marrom da identidade visual
  colorScheme: Color = {
    name: 'easynote',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: [
      '#2f6b3a',
      '#7a5236',
      '#3e8e4f',
      '#a47551',
      '#7fb98a',
      '#5a3d2b',
      '#c9b3a0',
      '#234f2b'
    ]
  };


  constructor() {
    this.findAll();
  }


  findAll() {

    this.emprestimoService.findAll().subscribe({

      next: (list) => {

        this.lista = list;

        this.processarIndicadores();
        this.processarEmprestimosPorPatrimonio();
        this.processarEmprestimosPorCurso();
        this.processarEmprestimosPorDia();

      },

      error: (erro) => {
        Swal.fire(
          'Erro',
          erro.error,
          'error'
        );
      }

    });

  }


  onPeriodoChange(event: Event) {

    const selectElement = event.target as HTMLSelectElement;

    this.periodoSelecionado = Number(selectElement.value);

    this.processarEmprestimosPorDia();

  }


  processarIndicadores() {
    this.totalEmprestimos = this.lista.length;
    this.emAndamento = this.lista.filter(e => e.situacao === 'Em Andamento').length;
    this.encerrados = this.totalEmprestimos - this.emAndamento;
    this.equipamentosDistintos = new Set(this.lista.map(e => e.equipamento?.patrimonio)).size;
  }


  processarEmprestimosPorPatrimonio() {

    const equipamentoCountMap = new Map<string, number>();

    this.lista.forEach((emprestimo) => {
      const patrimonio = emprestimo.equipamento.patrimonio;
      equipamentoCountMap.set(patrimonio, (equipamentoCountMap.get(patrimonio) || 0) + 1);
    });

    this.emprestimosPorPatrimonio = this.topCinco(equipamentoCountMap);

  }


  processarEmprestimosPorCurso() {

    const cursoCountMap = new Map<string, number>();

    this.lista.forEach((emprestimo) => {
      const curso = emprestimo.aluno.curso;
      cursoCountMap.set(curso, (cursoCountMap.get(curso) || 0) + 1);
    });

    this.emprestimosPorCurso = this.topCinco(cursoCountMap);

  }


  processarEmprestimosPorDia() {

    const hoje = new Date();
    const diasAtras = new Date();
    diasAtras.setDate(hoje.getDate() - this.periodoSelecionado);

    // Chave yyyy-MM-dd ordena corretamente como texto; o rotulo exibido e dd/MM
    const diaCountMap = new Map<string, number>();

    this.lista.forEach((emprestimo) => {

      const dataEmprestimo = new Date(emprestimo.dataRetirada);

      if (dataEmprestimo >= diasAtras && dataEmprestimo <= hoje) {
        const chave = this.datePipe.transform(emprestimo.dataRetirada, 'yyyy-MM-dd');
        if (chave) {
          diaCountMap.set(chave, (diaCountMap.get(chave) || 0) + 1);
        }
      }

    });

    this.emprestimosPorDia = Array.from(diaCountMap)
      .sort(([a], [b]) => a.localeCompare(b))
      .map(([chave, value]) => ({
        name: this.datePipe.transform(chave, 'dd/MM') ?? chave,
        value
      }));

  }


  private topCinco(mapa: Map<string, number>): ChartData[] {
    return Array.from(mapa, ([name, value]) => ({ name, value }))
      .sort((a, b) => b.value - a.value)
      .slice(0, 5);
  }

}
