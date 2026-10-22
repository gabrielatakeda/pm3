import { Alunos } from "./alunos";
import { Equipamentos } from "./equipamentos";
import { Usuarios } from "./usuarios";

export class Emprestimos {
    id!: number;
    aluno!: Alunos;
    equipamento!: Equipamentos;
    observacao = '';
    usuario!: Usuarios;
    dataRetirada!: Date;
    dataDevolucao!: Date;
    situacao = '';
}

