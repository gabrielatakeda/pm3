import { Component, inject } from '@angular/core';
import { Equipamentos } from '../../../models/equipamentos';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { EquipamentoService } from '../../../services/equipamento-service';
import Swal from 'sweetalert2'
import { FormsModule } from '@angular/forms';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { DatePipe } from '@angular/common';
import { NgxMaskDirective, NgxMaskPipe, provideNgxMask } from 'ngx-mask';
import { NgxPaginationModule } from 'ngx-pagination';

@Component({
  selector: 'app-equipamento-form',
  standalone: true,
  imports: [RouterLink, MdbFormsModule, FormsModule, NgxMaskPipe, NgxMaskDirective, NgxPaginationModule],
  templateUrl: './equipamento-form.component.html',
  styleUrl: './equipamento-form.component.scss',
  providers: [DatePipe, provideNgxMask()]
})
export class EquipamentoFormComponent {

  tituloComponente = "Novo Equipamento"; 
  equipamento: Equipamentos = new Equipamentos();
  situacaoAtual = '';
  

  router = inject(Router);
  rotaAtivada = inject(ActivatedRoute);
  equipamentoService = inject(EquipamentoService);
  datePipe = inject(DatePipe);

  constructor(){
    const id = this.rotaAtivada.snapshot.params['id'];
    if(id > 0){
      this.tituloComponente = "Editar Equipamento";
      this.findById(id);
    }else {
      this.equipamento.id = 0; // Ou algo similar para indicar um novo equipamento
      this.equipamento.situacao = "Disponível";
      this.equipamento.dataAquisicao = this.formatarDataAtual(); 
    }
  }

  ngOnInit(): void {
    this.equipamento.dataAquisicao = this.formatarDataAtual(); // Define a data atual
  }

  formatarDataAtual(): string {
    const data = new Date();
    const dia = String(data.getDate()).padStart(2, '0');
    const mes = String(data.getMonth() + 1).padStart(2, '0'); // Meses começam em 0
    const ano = data.getFullYear();
    
    return `${dia}-${mes}-${ano}`;
  }

  // Converte 'dd-MM-yyyy' (formato do input) para 'yyyy-MM-dd' (LocalDate do backend).
  // Retorna '' se a data estiver incompleta ou nao existir (ex.: 31-02-2026).
  formatarDataParaSalvar(): string {
    const [dia, mes, ano] = (this.equipamento.dataAquisicao ?? '').split('-').map(Number);
    const data = new Date(ano, mes - 1, dia);

    const valida = ano > 999 && data.getFullYear() === ano && data.getMonth() === mes - 1 && data.getDate() === dia;
    if (!valida) {
      return '';
    }

    return `${ano}-${String(mes).padStart(2, '0')}-${String(dia).padStart(2, '0')}`;
  }

  // Copia enviada ao backend: o objeto ligado ao formulario nao e alterado,
  // entao o campo de data continua exibindo dd-MM-yyyy durante e apos o envio.
  private montarPayload(): Equipamentos | null {
    const dataParaSalvar = this.formatarDataParaSalvar();
    if (!dataParaSalvar) {
      Swal.fire('Data inválida', 'Informe a data de aquisição no formato dd-mm-aaaa.', 'warning');
      return null;
    }
    return { ...this.equipamento, dataAquisicao: dataParaSalvar };
  }
  
  // Ao receber a data do backend
  formatarDataParaInput(data: string): string {
    const partes = data.split('-');
    return `${partes[2]}-${partes[1]}-${partes[0]}`; // Converte 'dd-MM-yyyy' para 'yyyy-MM-dd'
  }

  findById(id: number){
    this.equipamentoService.findById(id).subscribe({
      next: equip => {
        this.equipamento = equip;
        this.equipamento.dataAquisicao = this.formatarDataParaInput(this.equipamento.dataAquisicao); // Formata a data recebida
        console.log("Situação carregada:", this.equipamento.situacao);
      },
      error: erro => {
        Swal.fire('Erro', erro.error, 'error');
      }
    })

  }

  save(){
    const payload = this.montarPayload();

  if (payload) {
    this.equipamentoService.save(payload).subscribe({
      next: mensagem => {
        Swal.fire({
          title: mensagem,
          icon: "success"
        }).then(() => {
          this.router.navigate(['admin/equipamento']);
        });
      },
      error: erro => {
        Swal.fire('Erro', erro.error, 'error');
      }
    });
  }
  }

  update(){
    const payload = this.montarPayload();

  if (payload) {
    this.equipamentoService.update(payload).subscribe({
      
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

  reativar(){
    this.equipamentoService.reativar(this.equipamento.patrimonio).subscribe({
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
