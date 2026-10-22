import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Auditoria } from '../models/auditoria';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuditoriaService {
  http = inject(HttpClient);
  API = environment.API+'/api/auditoria/audit';

  listarTudo(
    filtros: any,
    page: number,
    size: number
  ): Observable<{ content: Auditoria[]; totalPages: number }> {

    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    Object.keys(filtros).forEach(key => {
      if (filtros[key]) {
        params = params.set(key, filtros[key]);
      }
    });

    return this.http.get<any[]>(this.API, { params }).pipe(
      map(response => {
        const lista: Auditoria[] = response.map(item => ({
          entidade: item[0],
          id: item[1],
          criadoPor: item[2],
          criadoEm: item[3] ? new Date(item[3]) : null,
          alteradoPor: item[4],
          alteradoEm: item[5] ? new Date(item[5]) : null
        }));

        return {
          content: lista,
          totalPages: Math.ceil(lista.length / size)
        };
      })
    );
  }
}
