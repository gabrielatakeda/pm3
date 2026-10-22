package app.controller.test;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class EquipamentosControllerTest {

    /*@Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipamentosService equipamentosService;
    
    @Autowired
    private EquipamentosController equipamentoController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSaveEquipamento() throws Exception {
        Equipamentos equipamento = new Equipamentos();
        equipamento.setPatrimonio("12345");
        equipamento.setMarca("Dell");
        equipamento.setModelo("XPS 13");
        equipamento.setDataAquisicao(LocalDate.of(2022, 5, 20));

        when(equipamentosService.save(any(Equipamentos.class))).thenReturn("Equipamento salvo com sucesso!");

        mockMvc.perform(post("/api/equipamentos/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(equipamento)))
                .andExpect(status().isOk())
                .andExpect(content().string("Equipamento salvo com sucesso!"));
    }
    
    @Test
    void testSaveEquipamentoNotValid() throws Exception {
        Equipamentos equipamento = new Equipamentos();
        equipamento.setPatrimonio("");
        equipamento.setMarca("");
        equipamento.setModelo("");
        equipamento.setDataAquisicao(LocalDate.of(2022, 5, 20));

        //when(equipamentosService.save(any(Equipamentos.class))).thenReturn("Equipamento salvo com sucesso!");
        
        assertThrows(Exception.class, () -> {
        	equipamentoController.save(equipamento);
        });
    }
	
	@Test
	void testSaveEquipamento_Exception() throws Exception {
		Equipamentos equipamento = new Equipamentos();
		equipamento.setPatrimonio("12345");
		equipamento.setMarca("Dell");
		equipamento.setModelo("XPS 13");
		equipamento.setDataAquisicao(LocalDate.of(2022, 5, 20));

		when(equipamentosService.save(any(Equipamentos.class)))
				.thenThrow(new RuntimeException(""));

		mockMvc.perform(post("/api/equipamentos/save")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(equipamento)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Deu erro!"));
	}


    @Test
    void testUpdateEquipamento() throws Exception {
        Equipamentos equipamento = new Equipamentos();
        equipamento.setPatrimonio("12345");
        equipamento.setMarca("Dell");
        equipamento.setModelo("XPS 13");
        equipamento.setDataAquisicao(LocalDate.of(2022, 5, 20));

        when(equipamentosService.update(any(Equipamentos.class), eq(1L))).thenReturn("Equipamento salvo com sucesso!");

        mockMvc.perform(put("/api/equipamentos/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(equipamento)))
                .andExpect(status().isOk())
                .andExpect(content().string("Equipamento salvo com sucesso!"));
    }
    
    @Test
    void testUpdateEquipamentoNotValid() throws Exception {
        Equipamentos equipamento = new Equipamentos();
        equipamento.setId(1);
        equipamento.setPatrimonio("");
        equipamento.setMarca("");
        equipamento.setModelo("");
        equipamento.setDataAquisicao(LocalDate.of(2022, 5, 20));

        //when(equipamentosService.save(any(Equipamentos.class))).thenReturn("Equipamento salvo com sucesso!");
        
        assertThrows(Exception.class, () -> {
        	equipamentoController.update(equipamento, 1);
        });
    }
	
	@Test
	void testUpdateEquipamento_Exception() throws Exception {
		Equipamentos equipamento = new Equipamentos();
		equipamento.setPatrimonio("12345");
		equipamento.setMarca("Dell");
		equipamento.setModelo("XPS 13");
		equipamento.setDataAquisicao(LocalDate.of(2022, 5, 20));

		when(equipamentosService.update(any(Equipamentos.class), eq(1L)))
				.thenThrow(new RuntimeException("Deu erro!Erro ao atualizar equipamento"));

		mockMvc.perform(put("/api/equipamentos/update/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(equipamento)))
		.andExpect(status().isBadRequest());

	}


    @Test
    void testFindById() throws Exception {
        Equipamentos equipamento = new Equipamentos();
        equipamento.setId(1L);
        equipamento.setPatrimonio("12345");
        equipamento.setMarca("Dell");
        equipamento.setModelo("XPS 13");
        equipamento.setDataAquisicao(LocalDate.of(2022, 5, 20));

        when(equipamentosService.findById(1L)).thenReturn(equipamento);

        mockMvc.perform(get("/api/equipamentos/findById/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.patrimonio").value("12345"))
                .andExpect(jsonPath("$.marca").value("Dell"))
                .andExpect(jsonPath("$.modelo").value("XPS 13"));
    }
	
	@Test
	void testFindById_Exception() throws Exception {
		when(equipamentosService.findById(1L)).thenThrow(new RuntimeException(""));

		mockMvc.perform(get("/api/equipamentos/findById/{id}", 1L))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(""));
	}


    @Test
    void testFindAllEquipamentos() throws Exception {
        Equipamentos equipamento1 = new Equipamentos();
        equipamento1.setId(1L);
        equipamento1.setPatrimonio("12345");
        equipamento1.setMarca("Dell");
        equipamento1.setModelo("XPS 13");
        equipamento1.setDataAquisicao(LocalDate.of(2022, 5, 20));

        Equipamentos equipamento2 = new Equipamentos();
        equipamento2.setId(2L);
        equipamento2.setPatrimonio("67890");
        equipamento2.setMarca("HP");
        equipamento2.setModelo("Spectre");
        equipamento2.setDataAquisicao(LocalDate.of(2021, 3, 15));

        List<Equipamentos> equipamentosList = Arrays.asList(equipamento1, equipamento2);

        when(equipamentosService.findAll()).thenReturn(equipamentosList);

        mockMvc.perform(get("/api/equipamentos/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].patrimonio").value("12345"))
                .andExpect(jsonPath("$[0].marca").value("Dell"))
                .andExpect(jsonPath("$[0].modelo").value("XPS 13"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].patrimonio").value("67890"))
                .andExpect(jsonPath("$[1].marca").value("HP"))
                .andExpect(jsonPath("$[1].modelo").value("Spectre"));
    }
	
	@Test
	void testFindAllEquipamentos_Exception() throws Exception {
		when(equipamentosService.findAll()).thenThrow(new RuntimeException(""));

		mockMvc.perform(get("/api/equipamentos/findAll"))
		.andExpect(status().isBadRequest())
				.andExpect(content().string(""));
	}


    @Test
    void testFindByPatrimonio() throws Exception {
        Equipamentos equipamento = new Equipamentos();
        equipamento.setId(1L);
        equipamento.setPatrimonio("12345");
        equipamento.setMarca("Dell");
        equipamento.setModelo("XPS 13");
        equipamento.setDataAquisicao(LocalDate.of(2022, 5, 20));

        when(equipamentosService.findByPatrimonio("12345")).thenReturn(equipamento);

        mockMvc.perform(get("/api/equipamentos/findByPatrimonio")
                .param("patrimonio", "12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.patrimonio").value("12345"))
                .andExpect(jsonPath("$.marca").value("Dell"))
                .andExpect(jsonPath("$.modelo").value("XPS 13"));
    }
	
	@Test
	void testFindByPatrimonio_Exception() throws Exception {
		when(equipamentosService.findByPatrimonio("12345")).thenThrow(new RuntimeException(""));

		mockMvc.perform(get("/api/equipamentos/findByPatrimonio")
				.param("patrimonio", "12345"))
		.andExpect(status().isBadRequest())
				.andExpect(content().string(""));
	}


    @Test
    void testFindBySituacao() throws Exception {
        Equipamentos equipamento1 = new Equipamentos();
        equipamento1.setId(1L);
        equipamento1.setPatrimonio("12345");
        equipamento1.setMarca("Dell");
        equipamento1.setModelo("XPS 13");
        equipamento1.setSituacao("Em uso");

        Equipamentos equipamento2 = new Equipamentos();
        equipamento2.setId(2L);
        equipamento2.setPatrimonio("67890");
        equipamento2.setMarca("HP");
        equipamento2.setModelo("Spectre");
        equipamento2.setSituacao("Em uso");

        List<Equipamentos> equipamentosList = Arrays.asList(equipamento1, equipamento2);

        when(equipamentosService.findBySituacao("Em uso")).thenReturn(equipamentosList);

        mockMvc.perform(get("/api/equipamentos/findBySituacao")
                .param("situacao", "Em uso"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].situacao").value("Em uso"))
                .andExpect(jsonPath("$[1].situacao").value("Em uso"));
    }
	
	@Test
	void testFindBySituacao_Exception() throws Exception {
		when(equipamentosService.findBySituacao("Em uso")).thenThrow(new RuntimeException(""));

		mockMvc.perform(get("/api/equipamentos/findBySituacao")
				.param("situacao", "Em uso"))
		.andExpect(status().isBadRequest())
				.andExpect(content().string(""));
	}

    @Test
    void testFindByMarca() throws Exception {
        Equipamentos equipamento1 = new Equipamentos();
        equipamento1.setId(1L);
        equipamento1.setPatrimonio("12345");
        equipamento1.setMarca("Dell");
        equipamento1.setModelo("XPS 13");

        Equipamentos equipamento2 = new Equipamentos();
        equipamento2.setId(2L);
        equipamento2.setPatrimonio("67890");
        equipamento2.setMarca("Dell");
        equipamento2.setModelo("Inspiron");

        List<Equipamentos> equipamentosList = Arrays.asList(equipamento1, equipamento2);

        when(equipamentosService.findByMarca("Dell")).thenReturn(equipamentosList);

        mockMvc.perform(get("/api/equipamentos/findByMarca")
                .param("marca", "Dell"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].marca").value("Dell"))
                .andExpect(jsonPath("$[1].marca").value("Dell"));
    }
	
	@Test
	void testFindByMarca_Exception() throws Exception {
		when(equipamentosService.findByMarca("Dell")).thenThrow(new RuntimeException(""));

		mockMvc.perform(get("/api/equipamentos/findByMarca")
				.param("marca", "Dell"))
		.andExpect(status().isBadRequest())
				.andExpect(content().string(""));
	}


    @Test
    void testFindByModelo() throws Exception {
        Equipamentos equipamento1 = new Equipamentos();
        equipamento1.setId(1L);
        equipamento1.setPatrimonio("12345");
        equipamento1.setMarca("Dell");
        equipamento1.setModelo("XPS 13");

        Equipamentos equipamento2 = new Equipamentos();
        equipamento2.setId(2L);
        equipamento2.setPatrimonio("67890");
        equipamento2.setMarca("HP");
        equipamento2.setModelo("XPS 13");

        List<Equipamentos> equipamentosList = Arrays.asList(equipamento1, equipamento2);

        when(equipamentosService.findByModelo("XPS 13")).thenReturn(equipamentosList);

        mockMvc.perform(get("/api/equipamentos/findByModelo")
                .param("modelo", "XPS 13"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].modelo").value("XPS 13"))
                .andExpect(jsonPath("$[1].modelo").value("XPS 13"));
    }
	
	@Test
	void testFindByModelo_Exception() throws Exception {
		when(equipamentosService.findByModelo("XPS 13")).thenThrow(new RuntimeException());

		mockMvc.perform(get("/api/equipamentos/findByModelo")
				.param("modelo", "XPS 13"))
		.andExpect(status().isBadRequest())
				.andExpect(content().string(""));
	}

	
    @Test
    void testFindByDataAquisicao() throws Exception {
        Equipamentos equipamento1 = new Equipamentos();
        equipamento1.setId(1L);
        equipamento1.setPatrimonio("12345");
        equipamento1.setMarca("Dell");
        equipamento1.setModelo("XPS 13");
        equipamento1.setDataAquisicao(LocalDate.of(2022, 5, 20));

        Equipamentos equipamento2 = new Equipamentos();
        equipamento2.setId(2L);
        equipamento2.setPatrimonio("67890");
        equipamento2.setMarca("HP");
        equipamento2.setModelo("Spectre");
        equipamento2.setDataAquisicao(LocalDate.of(2022, 6, 15));

        List<Equipamentos> equipamentosList = Arrays.asList(equipamento1, equipamento2);

        when(equipamentosService.findByDataAquisicao(LocalDate.of(2022, 5, 1), LocalDate.of(2022, 6, 30)))
                .thenReturn(equipamentosList);

        mockMvc.perform(get("/api/equipamentos/findByDataAquisicao")
                .param("data1", "2022-05-01")
                .param("data2", "2022-06-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].dataAquisicao").value("2022-05-20"))
                .andExpect(jsonPath("$[1].dataAquisicao").value("2022-06-15"));
    }
	
	@Test
	void testFindByDataAquisicao_Exception() throws Exception {
		when(equipamentosService.findByDataAquisicao(LocalDate.of(2022, 5, 1), LocalDate.of(2022, 6, 30)))
				.thenThrow(new RuntimeException("Erro ao buscar equipamentos por data de aquisição"));

		mockMvc.perform(get("/api/equipamentos/findByDataAquisicao")
				.param("data1", "2022-05-01")
				.param("data2", "2022-06-30"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(""));
	}


    @Test
    void testFindByEquipamentoAtivo() throws Exception {
        Equipamentos equipamento1 = new Equipamentos();
        equipamento1.setId(1L);
        equipamento1.setPatrimonio("12345");
        equipamento1.setAtivo(true);

        Equipamentos equipamento2 = new Equipamentos();
        equipamento2.setId(2L);
        equipamento2.setPatrimonio("67890");
        equipamento2.setAtivo(true);

        List<Equipamentos> equipamentosList = Arrays.asList(equipamento1, equipamento2);

        when(equipamentosService.findEquipamentosAtivos()).thenReturn(equipamentosList);

        mockMvc.perform(get("/api/equipamentos/findByEquipamentoAtivo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].ativo").value(true))
                .andExpect(jsonPath("$[1].ativo").value(true));
    }
	
	@Test
	void testFindByEquipamentoAtivo_Exception() throws Exception {
		when(equipamentosService.findEquipamentosAtivos()).thenThrow(new RuntimeException(""));

		mockMvc.perform(get("/api/equipamentos/findByEquipamentoAtivo"))
		.andExpect(status().isBadRequest())
				.andExpect(content().string(""));
	}


    @Test
    void testFindByEquipamentoInativo() throws Exception {
        Equipamentos equipamento1 = new Equipamentos();
        equipamento1.setId(3L);
        equipamento1.setPatrimonio("99999");
        equipamento1.setAtivo(false);

        List<Equipamentos> equipamentosList = Arrays.asList(equipamento1);

        when(equipamentosService.findEquipamentosInativos()).thenReturn(equipamentosList);

        mockMvc.perform(get("/api/equipamentos/findByEquipamentoInativo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ativo").value(false));
    }

	@Test
	void testFindByEquipamentoInativo_Exception() throws Exception {
		when(equipamentosService.findEquipamentosInativos()).thenThrow(new RuntimeException(""));

		mockMvc.perform(get("/api/equipamentos/findByEquipamentoInativo"))
		.andExpect(status().isBadRequest())
				.andExpect(content().string(""));
	}


    @Test
    void testDesativarEquipamento() throws Exception {
        when(equipamentosService.delete("12345")).thenReturn("Equipamento desativado com sucesso!");

        mockMvc.perform(put("/api/equipamentos/desativarEquipamento")
                .param("patrimonio", "12345"))
                .andExpect(status().isOk())
                .andExpect(content().string("Equipamento desativado com sucesso!"));
    }
	
	@Test
	void testDesativarEquipamento_Exception() throws Exception {
		when(equipamentosService.delete("12345")).thenThrow(new RuntimeException("Erro ao desativar equipamento"));

		mockMvc.perform(put("/api/equipamentos/desativarEquipamento")
				.param("patrimonio", "12345"))
		.andExpect(status().isBadRequest())
				.andExpect(content().string("Erro ao desativar equipamento"));
	}


    @Test
    void testReativarEquipamento() throws Exception {
        when(equipamentosService.reativarEquipamento("12345")).thenReturn("Equipamento reativado com sucesso!");

        mockMvc.perform(put("/api/equipamentos/reativarEquipamento")
                .param("patrimonio", "12345"))
                .andExpect(status().isOk())
                .andExpect(content().string("Equipamento reativado com sucesso!"));
    }
	
	@Test
	void testReativarEquipamento_Exception() throws Exception {
		when(equipamentosService.reativarEquipamento("12345")).thenThrow(new RuntimeException("Erro ao reativar equipamento"));

		mockMvc.perform(put("/api/equipamentos/reativarEquipamento")
				.param("patrimonio", "12345"))
		.andExpect(status().isBadRequest())
				.andExpect(content().string("Erro ao reativar equipamento"));
	}*/

}
