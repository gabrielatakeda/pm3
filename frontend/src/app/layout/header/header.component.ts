import { Component, ViewChild, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MdbCollapseDirective, MdbCollapseModule } from 'mdb-angular-ui-kit/collapse';
import { AutenticarService } from '../../services/autenticar-service';
import { LoginService } from '../../auth/login.service';

interface ItemMenu {
  titulo: string;
  rota: string;
  icone: string;
  admin?: boolean;
}

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [MdbCollapseModule, RouterLink, RouterLinkActive],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

  autenticarService = inject(AutenticarService);
  loginService = inject(LoginService);

  @ViewChild('basicNavbar') basicNavbar!: MdbCollapseDirective;

  menu: ItemMenu[] = [
    { titulo: 'Empréstimos',  rota: '/admin/emprestimo/new', icone: 'bi-arrow-left-right' },
    { titulo: 'Equipamentos', rota: '/admin/equipamento',    icone: 'bi-laptop' },
    { titulo: 'Alunos',       rota: '/admin/aluno',          icone: 'bi-mortarboard' },
    { titulo: 'Relatório',    rota: '/admin/relatorio',      icone: 'bi-file-earmark-bar-graph' },
    { titulo: 'Usuários',     rota: '/admin/usuario',        icone: 'bi-people', admin: true },
    { titulo: 'Sobre Nós',    rota: '/admin/about-us',       icone: 'bi-info-circle' }
  ];

  // No celular o menu fica aberto apos navegar; fecha ao escolher uma opcao
  fecharMenu() {
    if (this.basicNavbar && !this.basicNavbar.collapsed) {
      this.basicNavbar.hide();
    }
  }
}
