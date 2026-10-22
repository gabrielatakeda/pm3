import { Injectable } from "@angular/core";

@Injectable({
    providedIn: "root"
})
export class DataService {


    formatarDataAtual(): string {
        const data = new Date();
        const dia = String(data.getDate()).padStart(2, '0');
        const mes = String(data.getMonth() + 1).padStart(2, '0'); // Meses começam em 0
        const ano = data.getFullYear();
        
        return `${dia}-${mes}-${ano}`;
      }

      formatarDataParaSalvar(data: Date | string | undefined) {

        if(data == undefined)
            return '';

        if (!data) {
            return ''; // Retorna string vazia se a data for undefined
        }
    
        let dataFormatada: Date;
    
        if (typeof data === 'string') {
            const partes = data.split('-'); // Esperado: 'dd-MM-yyyy'
            if (partes.length === 3) {
                const dia = Number(partes[0]);
                const mes = Number(partes[1]) - 1; // Meses em JavaScript começam do zero
                const ano = Number(partes[2]);
                dataFormatada = new Date(ano, mes, dia);
            } else {
                return ''; // Retorna string vazia se o formato for inválido
            }
        } else {
            dataFormatada = data;
        }
    
        // Verifica se a data é válida
        if (isNaN(dataFormatada.getTime())) {
            return ''; // Retorna string vazia para datas inválidas
        }
    
        return dataFormatada.toISOString().slice(0, 19); // Formato ISO sem milissegundos
    }

      
      // Função para formatar a data recebida do backend para o input no formato 'yyyy-MM-dd'
    formatarDataParaInput(data: string): string {
        const partes = data.split('-'); // 'dd-MM-yyyy'
        return `${partes[2]}-${partes[1]}-${partes[0]}`; // Converte para 'yyyy-MM-dd'
    }

    formatarDataHora(dataHora: string): string {
        const data = new Date(dataHora);
        if (isNaN(data.getTime())) return dataHora;
      
        const dia = String(data.getDate()).padStart(2, '0');
        const mes = String(data.getMonth() + 1).padStart(2, '0');
        const ano = data.getFullYear();
        const horas = String(data.getHours()).padStart(2, '0');
        const minutos = String(data.getMinutes()).padStart(2, '0');
      
        return `${dia}/${mes}/${ano} ${horas}:${minutos}`;
      }

    converterInputParaLocalDateTime(input: string): string | null {
        if (!input) return null;

        // Supondo input no formato dd-MM-yyyy
        const partes = input.split('-');
        if (partes.length !== 3) return null;

        const dia = Number(partes[0]);
        const mes = Number(partes[1]) - 1; // JS: meses começam do 0
        const ano = Number(partes[2]);

        const data = new Date(ano, mes, dia);

        if (isNaN(data.getTime())) return null;

        // Gera string ISO com horas zeradas
        return data.toISOString().slice(0, 19); // "yyyy-MM-ddTHH:mm:ss"
    }

}
