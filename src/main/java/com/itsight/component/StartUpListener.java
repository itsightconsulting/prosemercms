package com.itsight.component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import com.itsight.domain.*;
import com.itsight.domain.en.*;
import com.itsight.service.*;
import com.itsight.service.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.itsight.constants.Parseador;
import com.itsight.constants.Utilitarios;
import com.itsight.repository.SecurityUserRepository;
import com.itsight.repository.TipoContenidoRepository;
import com.itsight.repository.TipoTagRepository;

@Component
public class StartUpListener implements ApplicationListener<ContextRefreshedEvent>{
	
	private static final Logger LOGGER = LogManager.getLogger(StartUpListener.class);
    
    @Autowired
    private ServletContext context;
    
    @Autowired
    private String mainRoute;
    
    @Autowired
    private ParametroService parametroService;
    
    @Autowired
    private PerfilService perfilService;
    
    @Autowired
    private BeneficiarioService beneficiarioService;
    
    @Autowired
    private TipoEstudioService tipoEstudioService;

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private TipoContenidoRepository tipoContenidoRepository;
    
    @Autowired
    private SecurityUserRepository userRepository;
    
    @Autowired
    private TipoTagRepository tipoTagRepository;
    
    @Autowired
    private TagService tagService;
    
    @Autowired
    private TipoImagenService tipoImagenService;
    
    @Autowired
    private QuienesSomosService quienesSomosService;
    
    @Autowired
    private EnQuienesSomosService enQuienesSomosService;
    
    @Autowired
    private MemoriaService memoriaService;
    
    @Autowired
    private EnMemoriaService enMemoriaService;
    
    @Autowired
    private ContenidoWebService contenidoWebService;
    
    @Autowired
    private EnContenidoWebService enContenidoWebService;

    @Autowired
    private ResumenService resumenService;

    @Autowired
    private EnResumenService enResumenService;

    @Autowired
    private EstudioService estudioService;

	@Autowired
	private EnEstudioService enEstudioService;

	private final Long currentVersion = new Date().getTime();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    	//Main Seeders
    	addingApplicationParameters();
    	addingToContextSession();
    	addingProfilesToTable();
    	addingInitUsers();
    	addingBeneficiariesToTable();
    	addingStudyTypesToTable();
    	addingContentTypeToTable();
    	addingTagTypesToTable();
    	addingTagsToTable();
    	addingImageTypeTable();
    	addingWhoAreUsTable();
    	addingWhoAreUsTableEN();
    	addingProjectMemory();
    	addingProjectMemoryEN();
    	creatingFileDirectories();
    	/*fixEstudios();*/
    }
    
    private void addingApplicationParameters() {
    	if(parametroService.findByClave("MAIN_ROUTE") == null) {
    		parametroService.add(new Parametro("MAIN_ROUTE", mainRoute));
    	}
    	if(parametroService.findByClave("GOOGLE_API_KEY") == null) {
    		parametroService.add(new Parametro("GOOGLE_API_KEY", "6Le0RDQUAAAAAF7HWOT2J2eIE7R1Xc3-YQYGbxy0"));
    	}
    	if(parametroService.findByClave("INDEX_PAGE_STUDIES_1") == null) {
    		parametroService.add(new Parametro("INDEX_PAGE_STUDIES_1",  "0"));
    	}
    	if(parametroService.findByClave("INDEX_PAGE_STUDIES_2") == null) {
    		parametroService.add(new Parametro("INDEX_PAGE_STUDIES_2",  "0"));
    	}
    	if(parametroService.findByClave("INDEX_PAGE_TRAININGS") == null) {
    		parametroService.add(new Parametro("INDEX_PAGE_TRAININGS",  "0"));
    	}
    }
    
    private void addingToContextSession() {
    	
    	context.setAttribute("MAIN_ROUTE", parametroService.findByClave("MAIN_ROUTE").getValor());
    	context.setAttribute("GOOGLE_API_KEY", parametroService.findByClave("GOOGLE_API_KEY").getValor());
		context.setAttribute("version", currentVersion);
    	//Agregando variables al contexto si el valor del parámetro tiene algun valor diferente a vacio o NULL
    	Parametro pGenerico = parametroService.findByClave("INDEX_PAGE_STUDIES_1");
    	if(pGenerico.getValor()!= null && !pGenerico.getValor().equals("")) {
        	context.setAttribute("INDEX_PAGE_STUDIES_1", Parseador.stringArrayToIntArray(pGenerico.getValor().split(",")));
    	}
    	
    	pGenerico = parametroService.findByClave("INDEX_PAGE_STUDIES_2");
    	if(pGenerico.getValor() != null && !pGenerico.getValor().equals("")) {
        	context.setAttribute("INDEX_PAGE_STUDIES_2", Parseador.stringArrayToIntArray(pGenerico.getValor().split(",")));
    	}
    	
    	pGenerico = parametroService.findByClave("INDEX_PAGE_TRAININGS");
    	if(pGenerico.getValor() != null && !pGenerico.getValor().equals("")) {
        	context.setAttribute("INDEX_PAGE_TRAININGS", Parseador.stringArrayToIntArray(pGenerico.getValor().split(",")));
    	}
    }
    
    private void addingProfilesToTable() {
    	if(perfilService.findOneById(1) == null) { perfilService.add(new Perfil("Administrador"));}
    	if(perfilService.findOneById(2) == null) { perfilService.add(new Perfil("Publicador de Contenido"));}
    }
    
    private void addingBeneficiariesToTable() {
    	if(beneficiarioService.findOneById(1) == null) { beneficiarioService.add(new Beneficiario("MINEM","Ministerio de Energía y Minas"));}
    	if(beneficiarioService.findOneById(2) == null) { beneficiarioService.add(new Beneficiario("OSINERGMIN","Organismo Supervisor de la inversión en Energía y Minería"));}
    	if(beneficiarioService.findOneById(3) == null) { beneficiarioService.add(new Beneficiario("FONAFE","Corporación FONAFE"));}
    	if(beneficiarioService.findOneById(4) == null) { beneficiarioService.add(new Beneficiario("PETROPERU","PETROPERU"));}
    }
    
    private void addingStudyTypesToTable() {
    	if(tipoEstudioService.findOneById(1) == null) { tipoEstudioService.add(new TipoEstudio("Estudios para el desarrollo del Planeamiento Energético,y la promoción de las Energías Renovables y la Eficiencia Energética"));}
    	if(tipoEstudioService.findOneById(2) == null) { tipoEstudioService.add(new TipoEstudio("Estudios para la mejora de la gestión de las empresas públicas del sector energía"));}
    }

    private void fixEstudios(){
    	int[] fixIds = {6,7,9};
		int[] estudioFixIds = {4,5,7};
    	for(int i=0; i<fixIds.length;i++){
    		Resumen qResumen = resumenService.findResumenById(fixIds[i]);
    		if(qResumen == null){
    			ContenidoWeb contenidoWeb = contenidoWebService.findOneById(fixIds[i]);

    			Resumen resumen = new Resumen();
    			Estudio qEstudio = estudioService.findOneById(estudioFixIds[i]);
    			if(qEstudio.getAlcance()==null){
					resumen.setResumen("Por modificar");
    			}else{
					if(qEstudio.getAlcance().length()<252){
						resumen.setResumen(qEstudio.getAlcance());
					}else{
						resumen.setResumen(qEstudio.getAlcance().substring(0,252));
					}
    			}

    			contenidoWeb.addResumen(resumen);
    			contenidoWeb.setFlagResumen(true);
    			contenidoWebService.update(contenidoWeb);
    		}
    	}

		for(int i=0; i<fixIds.length;i++){
			EnResumen qResumen = enResumenService.findResumenById(fixIds[i]);
			if(qResumen == null){
				EnContenidoWeb contenidoWeb = enContenidoWebService.findOneById(fixIds[i]);

				EnResumen resumen = new EnResumen();
				EnEstudio qEstudio = enEstudioService.findOneById(estudioFixIds[i]);
				if(qEstudio.getAlcance()==null){
					resumen.setResumen("Por modificar");

				}else{
					if(qEstudio.getAlcance().length()<252){
						resumen.setResumen(qEstudio.getAlcance());
					}else{
						resumen.setResumen(qEstudio.getAlcance().substring(0,252));
					}
				}

				contenidoWeb.addResumen(resumen);
				contenidoWeb.setFlagResumen(true);
				enContenidoWebService.update(contenidoWeb);
			}
		}
		Integer[] idsI = {6,7,9};

		for (Resumen r: resumenService.findAllByIdIn(idsI)){
			System.out.println("-------->"+r.getId());
		}
    }
    
    private void addingContentTypeToTable() {
    	if(tipoContenidoRepository.findOne(1) == null) { tipoContenidoRepository.save(new TipoContenido("Estudio"));}
    	if(tipoContenidoRepository.findOne(2) == null) { tipoContenidoRepository.save(new TipoContenido("Capacitación"));}
    	if(tipoContenidoRepository.findOne(3) == null) { tipoContenidoRepository.save(new TipoContenido("Evento"));}
    }
    
    private void addingTagTypesToTable() {
    	if(tipoTagRepository.findOne(1) == null) { tipoTagRepository.save(new TipoTag("Español"));}
    	if(tipoTagRepository.findOne(2) == null) { tipoTagRepository.save(new TipoTag("Inglés"));}
    }
    
    private void addingTagsToTable() {
    	//TAG IN SPANISH
    	if(tagService.findOneById(1) == null) { tagService.add(new Tag("Estudio", 1));}
    	if(tagService.findOneById(2) == null) { tagService.add(new Tag("Capacitación", 1));}
    	if(tagService.findOneById(3) == null) { tagService.add(new Tag("Logro",1));}
    	if(tagService.findOneById(4) == null) { tagService.add(new Tag("Evento",1));}
    	//TAG IN ENGLISH
    	if(tagService.findOneById(5) == null) { tagService.add(new Tag("Research",2));}
    	if(tagService.findOneById(6) == null) { tagService.add(new Tag("Training",2));}
    	if(tagService.findOneById(7) == null) { tagService.add(new Tag("Achievement",2));}
    	if(tagService.findOneById(8) == null) { tagService.add(new Tag("Event",2));}
    }
    
    public void addingImageTypeTable() {
    	if(tipoImagenService.findOneById(1) == null) { tipoImagenService.add(new TipoImagen("Para Desktop", 1085.0, 395.0));}
    	if(tipoImagenService.findOneById(2) == null) { tipoImagenService.add(new TipoImagen("Para Mobile", 360.0, 241.0));}
    }
    
    public void addingWhoAreUsTable() {
    	ContenidoWeb cw = new ContenidoWeb();
    	cw.setTitulo("Contenido Web Historia");
    	cw.setUrl("/web/historia");
    	if(quienesSomosService.findByNombreMenuContaining("Historia") == null) {
    		quienesSomosService.add(new QuienesSomos("Historia",  new Date(), contenidoWebService.add(cw)));
    	}
    	if(quienesSomosService.findByNombreMenuContaining("Objetivos") == null) {
    		quienesSomosService.add(new QuienesSomos("Objetivos",  new Date()));
    	}
    	if(quienesSomosService.findByNombreMenuContaining("Estructura Organizacional") == null) {
    		quienesSomosService.add(new QuienesSomos("Estructura Organizacional",  new Date()));
    	}
    	if(quienesSomosService.findByNombreMenuContaining("Marco Legal") == null) {
    		quienesSomosService.add(new QuienesSomos("Marco Legal",  new Date()));
    	}
    }
    
    public void addingWhoAreUsTableEN() {
    	EnContenidoWeb cw = new EnContenidoWeb();
    	cw.setTitulo("Contenido Web Historia");
    	cw.setUrl("/web/en/historia");
    	if(enQuienesSomosService.findByNombreMenuContaining("History") == null) {
    		enQuienesSomosService.add(new EnQuienesSomos("History",  new Date(), enContenidoWebService.add(cw)));
    	}
    	if(enQuienesSomosService.findByNombreMenuContaining("Objectives") == null) {
    		enQuienesSomosService.add(new EnQuienesSomos("Objectives",  new Date()));
    	}
    	if(enQuienesSomosService.findByNombreMenuContaining("Organizational Structure") == null) {
    		enQuienesSomosService.add(new EnQuienesSomos("Organizational Structure",  new Date()));
    	}
    	if(enQuienesSomosService.findByNombreMenuContaining("Legal Framework") == null) {
    		enQuienesSomosService.add(new EnQuienesSomos("Legal Framework",  new Date()));
    	}
    }
    
    public void addingProjectMemory() {
    	
    	if(memoriaService.findOneById(1) == null) {
    		ContenidoWeb cw = new ContenidoWeb();
        	cw.setTitulo("Contenido Memoria del Programa");
        	cw.setUrl("/web/memoria-del-proyecto");
        	//Resumen
        	Resumen resumen = new Resumen();
			resumen.setResumen("Texto provisional");
			cw.addResumen(resumen);
    		memoriaService.add(new Memoria("Prosemer 2012 - 2018", contenidoWebService.add(cw)));
    	}
    }
    
    public void addingProjectMemoryEN() {
    	
    	if(enMemoriaService.findOneById(1) == null) {
    		EnContenidoWeb cw = new EnContenidoWeb();
        	cw.setTitulo("Contenido Memoria del Programa");
        	cw.setUrl("/web/en/project-memory");
        	//Resumen
        	EnResumen resumen = new EnResumen();
			resumen.setResumen("Texto provisional");
			cw.addResumen(resumen);
    		enMemoriaService.add(new EnMemoria("Prosemer 2012 - 2018", enContenidoWebService.add(cw)));
    	}
    }
    
    private void addingInitUsers() {
    	SecurityUser securityUser = userRepository.findByUsername("admin@prosemer.com");
		if(securityUser == null) {
			SecurityUser secUser = new SecurityUser();
			secUser.setUsername("admin@prosemer.com");
			secUser.setPassword(new BCryptPasswordEncoder().encode("@dmin@2018"));
			secUser.setEnabled(true);
			
			//Roles
			SecurityRole sr = new SecurityRole();
			sr.setRole("ROLE_ADMIN");	
			
			//Privileges
			SecurityPrivilege sp = new SecurityPrivilege();
			sp.setPrivilege("READ_PRIVILEGE");
			sp.setSecurityRole(sr);
			SecurityPrivilege sp1 = new SecurityPrivilege();
			sp1.setPrivilege("WRITE_PRIVILEGE");
			sp1.setSecurityRole(sr);
			//Set Privileges
			Set<SecurityPrivilege> listSp = new HashSet<>();
			listSp.add(sp);
			listSp.add(sp1);
			//Adding to Role father
			sr.setPrivileges(listSp);
			//Set Roles(Only 1)
			Set<SecurityRole> listSr = new HashSet<>();
			listSr.add(sr);
			//Adding to User
			secUser.setRoles(listSr);
			userRepository.save(secUser);
			
			//TB_Usuario
			Usuario usuario = new Usuario();
			usuario.setFechaCreacion();
			usuario.setNombres("Prosemer");
			usuario.setApellidoMaterno("App");
			usuario.setApellidoPaterno("Administrador");
			usuario.setFlagActivo(true);
			usuario.setUsername("admin@prosemer.com");
			usuario.setPassword("$2a$10$YWIc/LM3MYKxXQ8oBdLQGOgMiI/5ehw54BSTDXVafo8UqwWA0SFVi");
			usuario.setPerfil(1);
	    	if(usuarioService.getUsuarioByUsername("admin@prosemer.com") == null) { usuarioService.add(usuario);}

		}else {
			LOGGER.info("> Init user already exists(USER: admin@prosemer.com | PASSWORD: @dmin@2018 <");
		}
    }
    
    private void creatingFileDirectories() {
    	String[] childPaths = {
    			"/Estudios",
    			"/Estudios/Imagenes",
    			"/Estudios/Archivos",
    			"/Capacitaciones",
    			"/Capacitaciones/Imagenes",
    			"/Capacitaciones/Archivos",
    			"/Eventos",
    			"/Eventos/Imagenes",
    			"/Eventos/Archivos",
    			"/Quienes",
    			"/Quienes/Historia",
    			"/Quienes/Historia/Imagenes",
    			"/Slider",
    			"/MemoriaProyecto",
    			"/Media",
    			"/Media/Imagenes",
    			"/Media/Archivos"
    			};
	  	Utilitarios.createDirectoryStartUp(mainRoute, childPaths);	  	
    }
}
