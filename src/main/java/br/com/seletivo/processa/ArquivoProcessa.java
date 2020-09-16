package br.com.seletivo.processa;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import br.com.seletivo.listener.ler.ArquivosManager;
import br.com.seletivo.salva.ArquivosSalvar;

public class ArquivoProcessa {
	static Integer vendedor;
	static Integer cliente;
	static Float valorVenda;
	static Integer idVenda;
	static Float valorTotalVenda;
	static Float valorTotalVendaAux;
	static String nomeVendedor;
	
	public static void processaArquivos(String nomeArquivo) {
		if(ArquivosManager.flag) {
			vendedor = new Integer(0);
			cliente = new Integer(0);
			valorVenda = new Float(0f);
			idVenda = new Integer(-1);
			valorTotalVendaAux = Float.MAX_VALUE;
			ArquivosManager.flag=false;
		}
		try (Stream<String> stream = Files.lines(Paths.get(nomeArquivo),Charset.forName("Cp1252"))) {
			stream.forEach(ArquivoProcessa::processaLinhas);
			ArquivosSalvar salvar = new ArquivosSalvar();
			salvar.gravarArquivo(montaResultado());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void processaLinhas(String linha) {
		String[] result = linha.split("ç");
		valorTotalVenda = new Float(0);
		if(result.length > 0) {
			switch(result[0]) {
			case "001":
				//vendedor
				vendedor++;
				break;
			case "002":
				//cliente
				cliente++;
				break;
			case "003":
				//venda
				venda(result);
				break;
			}
		}
	}

	/*
	 * Considerei que o pior vendedor é o menos rentável.
	 */
	private static void venda(String[] result) {
		//remove []
		String vendas = result[2];
		vendas = vendas.replace("[", "");
		vendas = vendas.replace("]", "");
		String[] vendasArray = vendas.split(",");
		for(String venda :vendasArray) {
			String dados[] = venda.split("-");
			Integer qtd = Integer.parseInt(dados[1]);
			Float valor = Float.parseFloat(dados[2]);
			valorTotalVenda+=new Float(valor*qtd);
			if(idVenda.equals(-1)) {
				idVenda = new Integer(dados[0]);
				valorVenda = new Float(valor*qtd);
			}else {
				Float f = new Float(valor*qtd);
				if(f.compareTo(valorVenda)>0) {
					idVenda = new Integer(dados[0]);
					valorVenda = new Float(valor*qtd);
				}
			}
		}
		if(valorTotalVendaAux.compareTo(valorTotalVenda)>0) {
			nomeVendedor = result[3];
			valorTotalVendaAux = valorTotalVenda;
		}
	}
	
	public static String montaResultado() {
		return "Resultado:\r\n"
				+ "Vendedores: "+vendedor+"\r\n"
				+ "Cliente: "+cliente+"\r\n"
				+ "Venda: "+idVenda+" - Valor: "+valorVenda+"\r\n"
				+ "Vendedor menos Lucrativo: "+nomeVendedor;
	}
	
	
	public static Integer getVendedor() {
		return vendedor;
	}
	public static Integer getCliente() {
		return cliente;
	}
	public static Integer getIdVenda() {
		return idVenda;
	}
	public static String getNomeVendedor() {
		return nomeVendedor;
	}
	
	
}
