package br.com.seletivo.listener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import br.com.seletivo.SelecaoApplication;

public interface DiretorioWatchInter {
	static final Logger logger = LogManager.getLogger(DiretorioWatchInter.class);
	static final String DIRETORIO_HOME = System.getProperty("user.home");
	static final String DIRETORIO_IN = DIRETORIO_HOME+read("seletivo.in");
	static final String DIRETORIO_OUT = DIRETORIO_HOME+read("seletivo.out");
	

	public static String read(String property) {
		final Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("src/main/resources/application.properties"));
		} catch (IOException e) {
			logger.error("Arquivo de propriedades não encontrado.");
			e.printStackTrace();
		}
		return properties.getProperty(property);
	}
	public void registra(Path dir) throws IOException;
	public boolean processa();
	
}
