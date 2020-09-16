package br.com.seletivo.listener;

import static java.nio.file.StandardWatchEventKinds.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchKey;
import java.nio.file.WatchEvent;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.seletivo.SelecaoApplication;


public class DirectoryWatcherImpl implements DiretorioWatchInter{
	private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private Path inicio;
    private Collection<EventoListenerInter> eventoListeners = new ArrayList<EventoListenerInter>();
	private static final Logger logger = LogManager.getLogger(DirectoryWatcherImpl.class);
    
	public DirectoryWatcherImpl() throws IOException {
		this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
        inicio = Paths.get(DIRETORIO_IN);
        registraDiretorio(inicio);
	}
	private void registraDiretorio(Path dir) {
		WatchKey key;
		try {
			if(Files.notExists(dir, LinkOption.NOFOLLOW_LINKS)) {
				
				Files.createDirectories(dir);
				logger.info("Criado em :"+dir.toString());
			}
			logger.info("Lendo em :"+dir.toString());
			key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
			keys.put(key, dir);
		} catch (IOException e) {
			logger.error("Erro ao verificar arquivos");
			e.printStackTrace();
		}
        
	}
	@Override
	public void registra(Path dir) throws IOException {
		Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            	registraDiretorio(dir);
                return FileVisitResult.CONTINUE;
            }
        });
	}

	@Override
	public boolean processa() {
		boolean flag;
		for (;;) {
			 
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return false;
            }
 
            Path dir = keys.get(key);
            if (dir == null) {
            	
                continue;
            }
            flag = false;
            for (WatchEvent<?> event : key.pollEvents()) {
                @SuppressWarnings("rawtypes")
                WatchEvent.Kind kind = event.kind();

                @SuppressWarnings("unchecked")
                Path name = ((WatchEvent<Path>)event).context();
                Path child = dir.resolve(name);
 
                logger.info(event.kind().name()+" "+child);
                //flegar???
                flag = true;
                
                if (kind == ENTRY_CREATE || kind == ENTRY_MODIFY) {
                    try {
                        if (Files.isDirectory(child)) {
                        	registra(child);
                        }
                    } catch (IOException x) {
                    	logger.error("Não foi possivel visitar novos diretorios");
                    }
                }
            }
            if(flag) {
            	disparaEvento();
            }
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
		return false;
	}
	public synchronized void addListener(EventoListenerInter l) {
        if (!eventoListeners.contains(l)) {
        	eventoListeners.add(l);
        }
    }
	
	private void disparaEvento() {
        Collection <EventoListenerInter> el;  
        synchronized (this) {  
            el = (Collection)(((ArrayList)eventoListeners).clone());  
        }  
        DiretorioEvento evento = new DiretorioEvento(this);                  
        for (EventoListenerInter e : el) {  
            e.dirMudou(evento);
        } 
    }

}
