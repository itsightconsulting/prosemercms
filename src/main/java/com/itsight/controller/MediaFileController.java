package com.itsight.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itsight.service.ArchivoService;
import com.itsight.service.ContenidoArchivoService;
import com.itsight.service.MemoriaService;
import com.itsight.service.en.EnMemoriaService;

@Controller
@RequestMapping("/media/file")
public class MediaFileController {
	
	private String mainRoute;
	
	private ContenidoArchivoService contenidoArchivoService;
	
	private ArchivoService archivoService;
	
	private MemoriaService memoriaService;
	
	private EnMemoriaService enMemoriaService;
	
	@Autowired
	public MediaFileController(
					String mainRoute, 
					ContenidoArchivoService contenidoArchivoService,
					ArchivoService archivoService,
					MemoriaService memoriaService,
					EnMemoriaService enMemoriaService) {
		this.mainRoute = mainRoute;
		this.contenidoArchivoService = contenidoArchivoService;
		this.archivoService = archivoService;
		this.memoriaService = memoriaService;
		this.enMemoriaService = enMemoriaService;
	}
	
	
	
	@RequestMapping(value = "/estudio/gt/{strategy}/{contenidoWebId}/{uuidName:.+}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getFileAsBytesArray(
												@PathVariable int strategy,
												@PathVariable String contenidoWebId,
												@PathVariable String uuidName) throws IOException {
		File serverFile = new File(
				mainRoute + "/Estudios/Archivos/" + contenidoWebId + "/" + uuidName);
		
		if(!serverFile.exists()) {
			return new ResponseEntity<>("No se ha encontrado el archivo, comuníquese con el administrador. Gracias por su compresión.".getBytes(), HttpStatus.NO_CONTENT);
		}
		byte[] contents = Files.readAllBytes(serverFile.toPath());
		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.parseMediaType("application/pdf;application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;application/vnd.ms-excel"));
		if(strategy == 0) {
			String uuid = serverFile.getName().split("\\.")[0];
			String extension = "."+serverFile.getName().split("\\.")[1];
			String documentName = contenidoArchivoService.getAliasByContenidoWebIdAndUuid(Integer.parseInt(contenidoWebId), uuid);
			headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
			headers.setContentDispositionFormData("Generic File", documentName+ extension);	
		}
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/capacitacion/gt/{strategy}/{contenidoWebId}/{uuidName:.+}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getFileAsBytesArrayCapacitacion(
												@PathVariable int strategy,
												@PathVariable String contenidoWebId,
												@PathVariable String uuidName) throws IOException {
		File serverFile = new File(
				mainRoute + "/Capacitaciones/Archivos/" + contenidoWebId + "/" + uuidName);
		
		if(!serverFile.exists()) {
			return new ResponseEntity<>("No se ha encontrado el archivo, comuníquese con el administrador. Gracias por su compresión.".getBytes(), HttpStatus.NO_CONTENT);
		}
		byte[] contents = Files.readAllBytes(serverFile.toPath());
		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.parseMediaType("application/pdf;application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;application/vnd.ms-excel"));
		if(strategy == 0) {
			String uuid = serverFile.getName().split("\\.")[0];
			String extension = "."+serverFile.getName().split("\\.")[1];
			String documentName = contenidoArchivoService.getAliasByContenidoWebIdAndUuid(Integer.parseInt(contenidoWebId), uuid);
			headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
			headers.setContentDispositionFormData("Generic File", documentName+ extension);	
		}
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/evento/gt/{strategy}/{contenidoWebId}/{uuidName:.+}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getFileAsBytesArrayEvento(
												@PathVariable int strategy,
												@PathVariable String contenidoWebId,
												@PathVariable String uuidName) throws IOException {
		File serverFile = new File(
				mainRoute + "/Eventos/Archivos/" + contenidoWebId + "/" + uuidName);
		if(!serverFile.exists()) {
			return new ResponseEntity<>("No se ha encontrado el archivo, comuníquese con el administrador. Gracias por su compresión.".getBytes(), HttpStatus.NO_CONTENT);
		}
		byte[] contents = Files.readAllBytes(serverFile.toPath());
		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.parseMediaType("application/pdf;application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;application/vnd.ms-excel"));
		if(strategy == 0) {
			String uuid = serverFile.getName().split("\\.")[0];
			String extension = "."+serverFile.getName().split("\\.")[1];
			String documentName = contenidoArchivoService.getAliasByContenidoWebIdAndUuid(Integer.parseInt(contenidoWebId), uuid);
			headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
			headers.setContentDispositionFormData("Generic File", documentName+ extension);	
		}
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/estudio/{uuidName:.+}", method = RequestMethod.GET)
	public @ResponseBody byte[] getAudioFromAudio(@PathVariable(value="uuidName",required=true) String uuidName) {
		try {
		    File serverFile = new File(
		    		mainRoute + "/Estudios/Archivos/" + uuidName);
		    
		    return Files.readAllBytes(serverFile.toPath());
		} catch (IOException e) {
			// TODO: handle exception
			return "No se ha encontrado el contrato, comuníquese con el administrador. Gracias por su compresión.".getBytes();
		}
	}
	
	@RequestMapping(value = "/memoria/gt/{strategy}/{uuidName:.+}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getFileAsBytesArrayMemoria(
												@PathVariable int strategy,
												@PathVariable String uuidName) throws IOException {
		File serverFile = new File(
				mainRoute + "/MemoriaProyecto"+"/" + uuidName);
		
		if(!serverFile.exists()) {
			return new ResponseEntity<>("No se ha encontrado el archivo, comuníquese con el administrador. Gracias por su compresión.".getBytes(), HttpStatus.NO_CONTENT);
		}
		byte[] contents = Files.readAllBytes(serverFile.toPath());
		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.parseMediaType("application/pdf;application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;application/vnd.ms-excel"));
		if(strategy == 0) {
			String documentName = memoriaService.getNombreArchivoById(1);
			headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
			headers.setContentDispositionFormData("Generic File", documentName);	
		}
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/en/memoria/gt/{strategy}/{uuidName:.+}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getFileAsBytesArrayEnMemoria(
												@PathVariable int strategy,
												@PathVariable String uuidName) throws IOException {
		File serverFile = new File(
				mainRoute + "/MemoriaProyecto"+"/" + uuidName);
		
		if(!serverFile.exists()) {
			return new ResponseEntity<>("No se ha encontrado el archivo, comuníquese con el administrador. Gracias por su compresión.".getBytes(), HttpStatus.NO_CONTENT);
		}
		byte[] contents = Files.readAllBytes(serverFile.toPath());
		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.parseMediaType("application/pdf;application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;application/vnd.ms-excel"));
		if(strategy == 0) {
			String documentName = enMemoriaService.getNombreArchivoById(1);
			headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
			headers.setContentDispositionFormData("Generic File", documentName);	
		}
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/gt/{strategy}/{id}/{uuidName:.+}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getFileAsBytesArrayFree(
												@PathVariable int strategy,
												@PathVariable String id,
												@PathVariable String uuidName) throws IOException {
		File serverFile = new File(
				mainRoute + "/Media/Archivos"+"/" + uuidName);
		
		if(!serverFile.exists()) {
			return new ResponseEntity<>("No se ha encontrado el archivo, comuníquese con el administrador. Gracias por su compresión.".getBytes(), HttpStatus.NO_CONTENT);
		}
		byte[] contents = Files.readAllBytes(serverFile.toPath());
		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.parseMediaType("application/pdf;application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;application/vnd.ms-excel"));
		if(strategy == 0) {
			String uuid = serverFile.getName().split("\\.")[0];
			String extension = "."+serverFile.getName().split("\\.")[1];
			String documentName = archivoService.getAliasByIdAndUuid(Integer.parseInt(id), uuid);
			headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
			headers.setContentDispositionFormData("Generic File", documentName+extension);	
		}
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
		return response;
	}
}
