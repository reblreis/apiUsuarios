package br.com.cotiinformatica.components;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenComponent {
	/*
	 * Ler a configuração da chave secreta definida no arquivo
	 * /application.properties
	 */
	@Value("${jwt.secret}")
	private String jwtSecret;

	/*
	 * Método para gerar o TOKEN JWT
	 */
	public String generateToken(String email) throws Exception {
		return Jwts.builder().setSubject(email) // email do usuário autenticado
				.setIssuedAt(new Date()) // data de geração do token
				.signWith(SignatureAlgorithm.HS256, jwtSecret) // chave de assinatura
				.compact();
	}
}