package ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ecommerce.entity.ItemCompra;

public class ItemCompraTest {

	@ParameterizedTest
	@CsvSource({"0",
				"-1"})
	public void deveLancarExceptionIdInvalido(Long id) {
		// Arrange
		ItemCompra item = new ItemCompra();
		
		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> {
				item.setId(id);
			});
	}
	
	@ParameterizedTest
	@CsvSource({"0",
				"-1"})
	public void deveLancarExceptionQuantidadeInvalida(Long quantidade) {
		// Arrange
		ItemCompra item = new ItemCompra();
		
		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> {
				item.setQuantidade(quantidade);
			});
	}
}
