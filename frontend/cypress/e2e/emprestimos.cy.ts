describe('Realizar empréstimo', () => {
  beforeEach(() => {
      cy.visit('/')
      cy.get('input[name="usuario"]').type('Admin');
      cy.get('input[name="senha"]').type('admin');
      cy.contains('button', 'Entrar').click();
    })


  it('Realizar um empréstimo válido',() =>{
      cy.contains('a', 'Empréstimos').click();
      cy.get('input[name="ra"]').type('505233');
      cy.focused().blur();
      cy.get('input[name="patrimonio"]').type('NB004');
      cy.focused().blur();
      cy.get('input[name="observacao"]').type('Ok');
      cy.wait(1000);
      cy.contains('button', 'Iniciar Empréstimo').click();
  })

  it('Finalizar um empréstimo pela aba de empréstimos',() =>{
      cy.contains('a', 'Empréstimos').click();
      cy.get('input[name="patrimonio"]').type('NB004');
      cy.focused().blur();
      cy.wait(1000);
      cy.contains('button', 'Encerrar Empréstimo').click();
  })

  it('Realizar empréstimo com aluno inexistente',() =>{
      cy.contains('a', 'Empréstimos').click();
      cy.get('input[name="ra"]').type('578965');
      cy.focused().blur();
      cy.get('input[name="patrimonio"]').type('NB004');
      cy.focused().blur();
      cy.get('input[name="observacao"]').type('Ok');
      cy.wait(1000);
      cy.contains('button', 'Iniciar Empréstimo').should('be.disabled');
  })

   it('Realizar empréstimo com equipamento inexistente',() =>{
      cy.contains('a', 'Empréstimos').click();
      cy.get('input[name="ra"]').type('505233');
      cy.focused().blur();
      cy.get('input[name="patrimonio"]').type('856974');
      cy.focused().blur();
      cy.get('input[name="observacao"]').type('Ok');
      cy.wait(1000);
      cy.contains('button', 'Iniciar Empréstimo').should('be.disabled');
  })


})