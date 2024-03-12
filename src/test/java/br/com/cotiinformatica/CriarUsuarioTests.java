package br.com.cotiinformatica;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import br.com.cotiinformatica.dtos.CriarUsuarioRequestDto;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CriarUsuarioTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private static String emailUsuario;
	private static String senhaUsuario;

	@Test
	@Order(1)
	public void criarUsuarioComSucessoTest() throws Exception {
		Faker faker = new Faker();

		// criando um usuário para realização do cadastro
		CriarUsuarioRequestDto dto = new CriarUsuarioRequestDto();
		dto.setNome(faker.name().fullName());
		dto.setEmail(faker.internet().emailAddress());
		dto.setSenha("@Teste123");

		// enviando uma requisição POST para
		// o endpoint de cadastro de usuário na API.
		MvcResult result = mockMvc.perform(post("/api/usuarios/criar").contentType("application/json")
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isCreated()).andReturn();

		// capturando o conteudo obtido da API
		String content = result.getResponse().getContentAsString();
		// verificando a mensagem
		assertTrue(content.contains("Usuário cadastrado com sucesso."));

		// armazenar os dados do usuário criado para usar no próximo teste
		emailUsuario = dto.getEmail();
		senhaUsuario = dto.getSenha();
	}

	@Test
	@Order(2)
	public void emailJaCadastradoTest() throws Exception {
		Faker faker = new Faker();

		CriarUsuarioRequestDto dto = new CriarUsuarioRequestDto();
		dto.setNome(faker.name().fullName());
		dto.setEmail(emailUsuario);
		dto.setSenha(senhaUsuario);

		// enviando uma requisição POST para
		// o endpoint de cadastro de usuário na API.
		MvcResult result = mockMvc
				.perform(post("/api/usuarios/criar").contentType("application/json")
						.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isUnprocessableEntity()).andReturn();

		// capturando o conteudo obtido da API
		String content = result.getResponse().getContentAsString();
		// verificando a mensagem
		assertTrue(content.contains("O email informado já está cadastrado. Tente outro."));
	}
}