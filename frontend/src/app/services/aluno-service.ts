import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Alunos } from "../models/alunos";
import { environment } from "../../environments/environment";
import { ListFormat } from "typescript";

@Injectable({
    providedIn: "root"
})
export class AlunoService {

    http = inject(HttpClient);
    API = environment.API+"/api/alunos";

    // Busca alunos paginados
    findAllPage(page: number, size: number): Observable<ListFormat> {
        const params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString());
        return this.http.get<ListFormat>(`${this.API}/findAllPage`, { params });
    }

    findAll(): Observable<Alunos[]>{
        return this.http.get<Alunos[]>(this.API+"/findAll");
    }

    findByRa(ra: string): Observable<Alunos>{
        return this.http.get<Alunos>(`${this.API}/findByRa?ra=${ra}`);
    }

    findById(id: number): Observable<Alunos>{
        return this.http.get<Alunos>(this.API+"/findById/"+id);
    }

    findByFilter(ra: string, nome: string, curso: string): Observable<Alunos[]>{
        
        const httpParams = new HttpParams()
        .set('ra', ra)
        .set('nome', nome)
        .set('curso', curso);
        
        return this.http.get<Alunos[]>(`${this.API}/findByFilter`, { params: httpParams });
    }

    save(aluno: Alunos): Observable<string>{
        return this.http.post<string>(this.API+"/save", aluno, {responseType: 'text' as 'json'});
    }

    update(aluno: Alunos): Observable<string>{
        return this.http.put<string>(this.API+"/update/"+aluno.id, aluno, {responseType: 'text' as 'json'});
    }

    desativar(ra: string): Observable<string>{
        return this.http.put<string>(`${this.API}/desativarAluno?ra=${ra}`, {}, {responseType: 'text' as 'json'})
    }

    reativar(ra: string): Observable<string>{
        return this.http.put<string>(`${this.API}/reativarAluno?ra=${ra}`, {}, {responseType: 'text' as 'json'})
    }

}
