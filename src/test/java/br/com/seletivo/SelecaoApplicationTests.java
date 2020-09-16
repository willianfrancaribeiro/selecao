package br.com.seletivo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.seletivo.listener.DiretorioWatchInter;
import br.com.seletivo.listener.ler.ArquivosManager;
import br.com.seletivo.processa.ArquivoProcessa;


@SpringBootTest
class SelecaoApplicationTests {

	@Test
	public void testeProcessamento() {
		ArquivosManager.flag = true;
		ArquivoProcessa.processaArquivos(DiretorioWatchInter.DIRETORIO_IN+"\\teste.txt");
		assertEquals(ArquivoProcessa.getCliente(),2);
		assertEquals(ArquivoProcessa.getVendedor(),2);
		assertEquals(ArquivoProcessa.getIdVenda(),1);
	}
	@Test
	public void testeProcessamentoMultiplo() {
		ArquivosManager.flag = true;
		ArquivoProcessa.processaArquivos(DiretorioWatchInter.DIRETORIO_IN+"\\teste.txt");
		assertEquals(ArquivoProcessa.getCliente(),2);
		assertEquals(ArquivoProcessa.getVendedor(),2);
		assertEquals(ArquivoProcessa.getIdVenda(),1);
		ArquivosManager.flag = true;
		ArquivoProcessa.processaArquivos(DiretorioWatchInter.DIRETORIO_IN+"\\teste2.txt");
		assertEquals(ArquivoProcessa.getCliente(),3);
		assertEquals(ArquivoProcessa.getVendedor(),1);
		assertEquals(ArquivoProcessa.getIdVenda(),5);
	}

}
