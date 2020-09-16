package br.com.seletivo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.seletivo.listener.DiretorioWatchInter;
import br.com.seletivo.processa.ArquivoProcessa;


@SpringBootTest
class SelecaoApplicationTests {

	@Test
	public void testeProcessamento() {
		ArquivoProcessa.processaArquivos(DiretorioWatchInter.DIRETORIO_IN);
		assertEquals(ArquivoProcessa.getCliente(),2);
		assertEquals(ArquivoProcessa.getVendedor(),2);
		assertEquals(ArquivoProcessa.getIdVenda(),1);
	}
	@Test
	public void testeProcessamentoMultiplo() {
		
	}

}
