import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Emprestimos } from "../models/emprestimos";
import { DataService } from "./data-service";
import { environment } from "../../environments/environment";

@Injectable({
    providedIn: "root"
})
export class EmprestimoService {
    http = inject(HttpClient);
    API = environment.API+"/api/emprestimos";

    dataService = inject(DataService);

    findAll(): Observable<Emprestimos[]>{
        return this.http.get<Emprestimos[]>(this.API+"/findAll");
    }

    // Busca equipamentos paginados
    findAllPage(page: number, size: number): Observable<any> {
        const params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString());
        return this.http.get<any>(`${this.API}/findAllPage`, { params });
    }


    save(emprestimo: Emprestimos): Observable<string>{
        console.log(emprestimo);
        return this.http.post<string>(this.API+"/save", emprestimo, {responseType: 'text' as 'json'});
    }

    findEmprestimoByEquipamento(patrimonio: string): Observable<Emprestimos>{
        return this.http.get<Emprestimos>(`${this.API}/findByEquipamentoPatrimonio?patrimonio=${patrimonio}`);
    }

    findByEquipamentoPorEmprestimoAtivo(patrimonio: string): Observable<Emprestimos>{
        return this.http.get<Emprestimos>(`${this.API}/findByEquipamentoPorEmprestimoAtivo?patrimonio=${patrimonio}`);
    }

    encerrar(id: number, emprestimo: Emprestimos): Observable<string>{
        return this.http.put<string>(this.API+"/encerrar/"+id, emprestimo, {responseType: 'text' as 'json'});
    }

    findByFilter(
        dataRetirada: Date | undefined,
        dataDevolucao: Date | undefined,
        situacao: string,
        ra: string,
        usuario: string,
        patrimonio: string,
      ): Observable<any> { // Retorna um objeto paginado
      
        const dataRetiradaFormatada = dataRetirada ? this.dataService.formatarDataParaSalvar(dataRetirada) : '';
        const dataDevolucaoFormatada = dataDevolucao ? this.dataService.formatarDataParaSalvar(dataDevolucao) : '';
      
        const httpParams = new HttpParams()
          .set('dataRetirada', dataRetiradaFormatada)
          .set('dataDevolucao', dataDevolucaoFormatada)
          .set('situacao', situacao || '')
          .set('ra', ra || '')
          .set('usuario', usuario || '')
          .set('patrimonio', patrimonio || '')
      
        return this.http.get<any>(`${this.API}/findByFilter`, { params: httpParams });
      }
      
}
