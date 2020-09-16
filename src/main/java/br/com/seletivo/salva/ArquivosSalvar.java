package br.com.seletivo.salva;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.seletivo.listener.DirectoryWatcherImpl;
import br.com.seletivo.listener.DiretorioWatchInter;

public class ArquivosSalvar {
	private static final Logger logger = LogManager.getLogger(ArquivosSalvar.class);
	public void gravarArquivo(String linha) {
		Path dir = Paths.get(DiretorioWatchInter.DIRETORIO_OUT);
		if(Files.notExists(dir, LinkOption.NOFOLLOW_LINKS)) {
			
			try {
				//Files.createDirectories(dir);
				Files.createFile(dir);
			} catch (IOException e) {
				logger.error("Não foi possivel escrever em :"+dir.toString());
				e.printStackTrace();
			}
			logger.info("Criado em :"+dir.toString());
		}
		
		try {
			Files.write(dir, linha.getBytes());
		} catch (IOException e) {
			logger.error("Não foi possivel escrever em :"+dir.toString());
		}
		
	}
}
