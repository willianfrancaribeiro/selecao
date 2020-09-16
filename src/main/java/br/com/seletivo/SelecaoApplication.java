package br.com.seletivo;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.seletivo.listener.DirectoryWatcherImpl;
import br.com.seletivo.listener.DiretorioEvento;
import br.com.seletivo.listener.EventoListenerInter;
import br.com.seletivo.listener.ler.ArquivosManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@SpringBootApplication
public class SelecaoApplication {
	static DirectoryWatcherImpl watcher;
	static ArquivosManager mgr;
	private static final Logger logger = LogManager.getLogger(SelecaoApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(SelecaoApplication.class, args);
		try {
			watcher = new DirectoryWatcherImpl();
			mgr = new ArquivosManager();
			watcher.addListener(new EventoListenerInter() {

				@Override
				public void dirMudou(DiretorioEvento evt) {
					mgr.lerArquivos();
					logger.info("mudou");
					
				}
				
			});
			watcher.processa();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
