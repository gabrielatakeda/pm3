export interface Auditoria {
  entidade: string;
  id: number;
  criadoPor: string | null;
  criadoEm: Date | null;
  alteradoPor: string | null;
  alteradoEm: Date | null;
}
