package br.com.seletivo.listener.ler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.seletivo.listener.DirectoryWatcherImpl;
import br.com.seletivo.listener.DiretorioWatchInter;
import br.com.seletivo.processa.ArquivoProcessa;

public class ArquivosManager {
	private static final Logger logger = LogManager.getLogger(ArquivosManager.class);
	public static boolean flag;
	public void lerArquivos() {
		Path dir = Paths.get(DiretorioWatchInter.DIRETORIO_IN);
		try(Stream<Path> walk = Files.walk(dir)){
			flag = true;
			List<String> resultado = walk.filter(Files::isRegularFile).map(x->x.toString()).collect(Collectors.toList());
			resultado.forEach(ArquivoProcessa::processaArquivos);
		}catch(IOException e) {
			e.printStackTrace();
			logger.error("Não foi possivel lista os arquivos de diretorio");
		}
	}
	//métodos de tratamento de tipos de arquivo.
}
