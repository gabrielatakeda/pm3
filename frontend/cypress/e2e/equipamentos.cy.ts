describe('Realizar empréstimo', () => {
  beforeEach(() => {
      cy.visit('/')
      cy.get('input[name="usuario"]').type('Admin');
      cy.get('input[name="senha"]').type('admin');
      cy.contains('button', 'Entrar').click();
    })


/* it('Cadastrar um novo equipamento corretamente', () => {
    const patrimonio = `PA${Date.now()}`;

    cy.contains('a', 'Equipamentos').click();
    cy.contains('a', 'Novo').click();
    cy.location('pathname')
        .should('eq', '/admin/equipamento/new');
    cy.get('input[name="patrimonio"]').type(patrimonio);
    cy.get('input[name="marca"]').type('Dell');
    cy.get('input[name="modelo"]').type('XP');
    cy.get('input[name="observacao"]').type('Notebook');
    cy.get('select[name="situacao"]').select('Disponível');

    cy.contains('button', 'Salvar').click();

    cy.get('.swal2-confirm')
        .should('be.visible')
        .click();
    cy.location('pathname')
        .should('eq', '/admin/equipamento');
  })*/

  it('Cadastrar um novo equipamento sem data de aquisição', () => {
    const patrimonio = `PA${Date.now()}`;

    cy.contains('a', 'Equipamentos').click();
    cy.contains('a', 'Novo').click();
    cy.location('pathname')
        .should('eq', '/admin/equipamento/new');
    cy.get('input[name="patrimonio"]').type(patrimonio);
    cy.get('input[name="marca"]').type('Dell');
    cy.get('input[name="modelo"]').type('XP');
    cy.get('input[name="dataAquisicao"]').type('2025-10-12');
    cy.get('input[name="observacao"]').type('Erro na data');
    cy.get('select[name="situacao"]').select('Disponível');

    cy.contains('button', 'Salvar').click();

    cy.get('.swal2-popup')
  .should('be.visible');

    cy.get('.swal2-icon.swal2-error')
    .should('exist');

    cy.get('.swal2-html-container')
    .should('contain', 'Data inválida');

    cy.get('.swal2-confirm').click();
  })
  


})