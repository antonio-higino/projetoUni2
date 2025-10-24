package ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ecommerce.entity.Produto;

public class ProdutoTest {
	
	@ParameterizedTest
	@CsvSource({"0",
				"-1"})
	public void deveLancarExceptionIdInvalido(Long id) {
		// Arrange
		Produto produto = new Produto();
		
		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> {
				produto.setId(id);
			});
	}
	
	@ParameterizedTest
	@CsvSource({"-0.99",
				"-1"})
	public void deveLancarExceptionPrecoInvalido(BigDecimal preco) {
		// Arrange
		Produto produto = new Produto();
		
		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> {
				produto.setPreco(preco);
			});
	}
	
	@ParameterizedTest
	@CsvSource({"0",
				"-0.99"})
	public void deveLancarExceptionPesoFisicoInvalido(BigDecimal pesoFisico) {
		// Arrange
		Produto produto = new Produto();
		
		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> {
				produto.setPesoFisico(pesoFisico);
			});
	}
	
	@ParameterizedTest
	@CsvSource({"0",
				"-0.99"})
	public void deveLancarExceptionComprimentoInvalido(BigDecimal comprimento) {
		// Arrange
		Produto produto = new Produto();
		
		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> {
				produto.setComprimento(comprimento);
			});
	}
	
	@ParameterizedTest
	@CsvSource({"0",
				"-0.99"})
	public void deveLancarExceptionLarguraInvalida(BigDecimal largura) {
		// Arrange
		Produto produto = new Produto();
		
		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> {
				produto.setLargura(largura);
			});
	}
	
	@ParameterizedTest
	@CsvSource({"0",
				"-0.99"})
	public void deveLancarExceptionAlturaInvalida(BigDecimal altura) {
		// Arrange
		Produto produto = new Produto();
		
		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> {
				produto.setAltura(altura);
			});
	}
}
