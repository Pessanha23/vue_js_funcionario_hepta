package com.hepta.funcionarios;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hepta.funcionarios.entity.Funcionario;
import com.hepta.funcionarios.entity.Setor;
import com.hepta.funcionarios.rest.FuncionarioService;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.jdkhttp.JdkHttpServerTestContainerFactory;
import org.junit.jupiter.api.*;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FuncionarioServiceTest extends JerseyTest {

	@Override
	protected Application configure() {
		return new ResourceConfig(FuncionarioService.class);
	}

	private JdkHttpServerTestContainerFactory containerFactory;

	@BeforeEach
	public void init() throws Exception {
		containerFactory = new JdkHttpServerTestContainerFactory();
		super.setUp();
	}

	@AfterEach
	public void close() throws Exception {
		super.tearDown();

		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/funcionarios_prova", "root", "root");

		Statement statement = connection.createStatement();
		statement.execute("TRUNCATE TABLE funcionario");

		connection.close();
	}
	@Test
	public void deve_retonar_ok_quando_passado_no_body_os_parametros_corretos_para_criacao_do_funcionario() {
		Funcionario funcionario = new Funcionario();
		Setor setor = new Setor();
		setor.setNome("RH");
		setor.setId(9);
		funcionario.setNome("gamiguel");
		funcionario.setIdade(18);
		funcionario.setEmail("gamiguel@gmail.com");
		funcionario.setSalario(3000.00);
		funcionario.setSetor(setor);

		Response resposta = target("/funcionarios/salvar")
				.request()
				.post(Entity.entity(funcionario, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(200, resposta.getStatus());
		resposta.close();
	}

	@Test
	public void deve_retonar_NotFoundExcpetion_quando_o_path_for_invalido() {
		Funcionario funcionario = new Funcionario();
		Setor setor = new Setor();
		setor.setNome("RH");
		setor.setId(9);
		funcionario.setNome("gamiguel");
		funcionario.setIdade(18);
		funcionario.setEmail("gamiguel@gmail.com");
		funcionario.setSalario(3000.00);
		funcionario.setSetor(setor);

		Response resposta = target("funcionario/xpto")
				.request()
				.post(Entity.entity(funcionario, MediaType.APPLICATION_JSON));

		assertEquals(404, resposta.getStatus());
		resposta.close();
	}


	@Test
	public void deve_retonar_ok_a_lista_de_funcionário() {

		Funcionario funcionario1 = new Funcionario();
		Setor setor1 = new Setor();
		setor1.setNome("DEV");
		setor1.setId(9);
		funcionario1.setNome("joao");
		funcionario1.setIdade(18);
		funcionario1.setEmail("joao@gmail.com");
		funcionario1.setSalario(3000.00);
		funcionario1.setSetor(setor1);

		Response resposta1 = target("/funcionarios/salvar")
				.request()
				.post(Entity.entity(funcionario1, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(200, resposta1.getStatus());
		resposta1.close();

		Funcionario funcionario2 = new Funcionario();
		Setor setor2 = new Setor();
		setor2.setNome("SEC");
		setor2.setId(8);
		funcionario2.setNome("gamiguel");
		funcionario2.setIdade(20);
		funcionario2.setEmail("gamiguel@gmail.com");
		funcionario2.setSalario(4000.00);
		funcionario2.setSetor(setor2);

		Response resposta2 = target("/funcionarios/salvar")
				.request()
				.post(Entity.entity(funcionario2, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(200, resposta2.getStatus());
		resposta2.close();


		Response resposta = target("/funcionarios/listar")
				.request()
				.get();

		assertEquals(200, resposta.getStatus());
		List<Funcionario> funcionarioLista = resposta.readEntity(new GenericType<List<Funcionario>>(){});

		assertEquals(2, funcionarioLista.size());
		assertEquals("gamiguel", funcionarioLista.get(0).getNome());
		assertEquals("SEC", funcionarioLista.get(0).getSetor().getNome());
		assertEquals("joao", funcionarioLista.get(1).getNome());
		assertEquals(3000.0, funcionarioLista.get(1).getSalario());

		resposta.close();
	}


	@Test
	public void deve_retonar_ok_quando_metodo_atualizar_tiver_no_body_os_parametros_corretos() {
		Funcionario funcionario1 = new Funcionario();
		Setor setor1 = new Setor();
		setor1.setNome("DEV");
		setor1.setId(9);
		funcionario1.setNome("joao");
		funcionario1.setIdade(18);
		funcionario1.setEmail("joao@gmail.com");
		funcionario1.setSalario(3000.00);
		funcionario1.setSetor(setor1);

		Response resposta1 = target("/funcionarios/salvar")
				.request()
				.post(Entity.entity(funcionario1, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(200, resposta1.getStatus());
		resposta1.close();

		Funcionario funcionario2 = new Funcionario();
		Setor setor2 = new Setor();
		setor2.setNome("SEC");
		setor2.setId(8);
		funcionario2.setNome("gamiguel");
		funcionario2.setIdade(20);
		funcionario2.setEmail("gamiguel@gmail.com");
		funcionario2.setSalario(4000.00);
		funcionario2.setSetor(setor2);

		Response resposta2 = target("/funcionarios/atualizar/1")
				.request()
				.put(Entity.entity(funcionario2, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(200, resposta2.getStatus());

		Response resposta = target("/funcionarios/listar")
				.request()
				.get();

		assertEquals(200, resposta.getStatus());
		List<Funcionario> funcionarioLista = resposta.readEntity(new GenericType<List<Funcionario>>(){});

		assertEquals(1, funcionarioLista.size());
		assertEquals("gamiguel", funcionarioLista.get(0).getNome());
		assertEquals("gamiguel@gmail.com", funcionarioLista.get(0).getEmail());

		resposta2.close();

	}
	@Test
	public void deve_retonar_BadRequestException_quando_numero_do_path_atualizar_for_0() {
		Funcionario funcionario1 = new Funcionario();
		Setor setor1 = new Setor();
		setor1.setNome("DEV");
		setor1.setId(9);
		funcionario1.setNome("joao");
		funcionario1.setIdade(18);
		funcionario1.setEmail("joao@gmail.com");
		funcionario1.setSalario(3000.00);
		funcionario1.setSetor(setor1);

		Response resposta1 = target("/funcionarios/salvar")
				.request()
				.post(Entity.entity(funcionario1, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(200, resposta1.getStatus());
		resposta1.close();

		Funcionario funcionario2 = new Funcionario();
		Setor setor2 = new Setor();
		setor2.setNome("SEC");
		setor2.setId(8);
		funcionario2.setNome("gamiguel");
		funcionario2.setIdade(20);
		funcionario2.setEmail("gamiguel@gmail.com");
		funcionario2.setSalario(4000.00);
		funcionario2.setSetor(setor2);

		Response resposta2 = target("/funcionarios/atualizar/0")
				.request()
				.put(Entity.entity(funcionario2, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(400, resposta2.getStatus());

		resposta2.close();
	}

	@Test
	public void deve_retonar_NotFoundException_quando_numero_do_path_atualizar_for_vazio() {
		Funcionario funcionario1 = new Funcionario();
		Setor setor1 = new Setor();
		setor1.setNome("DEV");
		setor1.setId(9);
		funcionario1.setNome("joao");
		funcionario1.setIdade(18);
		funcionario1.setEmail("joao@gmail.com");
		funcionario1.setSalario(3000.00);
		funcionario1.setSetor(setor1);

		Response resposta1 = target("/funcionarios/salvar")
				.request()
				.post(Entity.entity(funcionario1, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(200, resposta1.getStatus());
		resposta1.close();

		Funcionario funcionario2 = new Funcionario();
		Setor setor2 = new Setor();
		setor2.setNome("SEC");
		setor2.setId(8);
		funcionario2.setNome("gamiguel");
		funcionario2.setIdade(20);
		funcionario2.setEmail("gamiguel@gmail.com");
		funcionario2.setSalario(4000.00);
		funcionario2.setSetor(setor2);

		Response resposta2 = target("/funcionarios/atualizar/")
				.request()
				.put(Entity.entity(funcionario2, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(404, resposta2.getStatus());

		resposta2.close();
	}

	@Test
	public void deve_retonar_ok_quando_path_deletar_informado_corretamente() {
		Funcionario funcionario1 = new Funcionario();
		Setor setor1 = new Setor();
		setor1.setNome("DEV");
		setor1.setId(9);
		funcionario1.setNome("joao");
		funcionario1.setIdade(18);
		funcionario1.setEmail("joao@gmail.com");
		funcionario1.setSalario(3000.00);
		funcionario1.setSetor(setor1);

		Response resposta1 = target("/funcionarios/salvar")
				.request()
				.post(Entity.entity(funcionario1, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(200, resposta1.getStatus());
		resposta1.close();

		Response resposta2 = target("/funcionarios/deletar/1")
				.request()
				.delete();
		assertEquals(200, resposta2.getStatus());
		resposta2.close();

	}
	@Test
	public void deve_retonar_NotFoundException_quando_numero_do_path_for_vazio() {
		Funcionario funcionario1 = new Funcionario();
		Setor setor1 = new Setor();
		setor1.setNome("DEV");
		setor1.setId(9);
		funcionario1.setNome("joao");
		funcionario1.setIdade(18);
		funcionario1.setEmail("joao@gmail.com");
		funcionario1.setSalario(3000.00);
		funcionario1.setSetor(setor1);

		Response resposta1 = target("/funcionarios/salvar")
				.request()
				.post(Entity.entity(funcionario1, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(200, resposta1.getStatus());
		resposta1.close();

		Response resposta2 = target("/funcionarios/deletar/")
				.request()
				.delete();
		assertEquals(404, resposta2.getStatus());
		resposta2.close();
	}

	@Test
	public void deve_retonar_Funcionaddo_no_caminho_do_metodo_get_teste() {
		Response resposta1 = target("/funcionarios/teste")
				.request()
				.get();
		String mensagemRecebida = resposta1.readEntity(String.class);
		assertEquals("Funcionando.", mensagemRecebida);
		assertEquals(200, resposta1.getStatus());
	}

}





