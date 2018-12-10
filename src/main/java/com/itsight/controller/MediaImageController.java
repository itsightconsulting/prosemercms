package com.itsight.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itsight.annotations.NoLogging;

@Controller
@RequestMapping(value = "/media/image")
public class MediaImageController {

	private String mainRoute;
				
	@Autowired
	public MediaImageController(String mainRoute) {
		this.mainRoute = mainRoute;
	}
	
	@NoLogging
	@RequestMapping(value = "/slider/{uuidName:.+}", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
	public ResponseEntity<byte[]> getImageFromSlider(
				@PathVariable String uuidName) {
		try {
			HttpHeaders headers = new HttpHeaders();
			File serverFile = new File(mainRoute + "/Slider/" + uuidName);
			if(serverFile.length() > 0) {
				headers.setETag("\""+uuidName.replace("-", "").substring(0,8)+"\"");
			    headers.setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic().getHeaderValue());
			    headers.setContentType(MediaType.IMAGE_JPEG);
			    return new ResponseEntity<>(Files.readAllBytes(serverFile.toPath()), headers, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			// TODO: handle exception
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@NoLogging
	@RequestMapping(value = "/estudio/{contenidoWebId}/{uuidName:.+}", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
	public ResponseEntity<byte[]> getImageFromEstudio(@PathVariable String contenidoWebId,
			@PathVariable String uuidName) {
		try {
			HttpHeaders headers = new HttpHeaders();
			File serverFile = new File(mainRoute + "/Estudios/Imagenes/" + contenidoWebId + "/" + uuidName);
			if(serverFile.length() > 0) {	
				headers.setETag("\""+uuidName.replace("-", "").substring(0,8)+"\"");
			    headers.setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic().getHeaderValue());
			    return new ResponseEntity<>(Files.readAllBytes(serverFile.toPath()), headers, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			// TODO: handle exception
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@NoLogging
	@RequestMapping(value = "/capacitacion/{contenidoWebId}/{uuidName:.+}", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
	public ResponseEntity<byte[]> getImageFromCapacitacion(@PathVariable String contenidoWebId,
			@PathVariable String uuidName) {
		try {
			HttpHeaders headers = new HttpHeaders();
			File serverFile = new File(mainRoute + "/Capacitaciones/Imagenes/" + contenidoWebId + "/" + uuidName);
			if(serverFile.length() > 0) {	
				headers.setETag("\""+uuidName.replace("-", "").substring(0,8)+"\"");
			    headers.setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic().getHeaderValue());
			    return new ResponseEntity<>(Files.readAllBytes(serverFile.toPath()), headers, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			// TODO: handle exception
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@NoLogging
	@RequestMapping(value = "/evento/{contenidoWebId}/{uuidName:.+}", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
	public ResponseEntity<byte[]> getImageFromEvento(@PathVariable String contenidoWebId,
			@PathVariable String uuidName) {
		try {
			HttpHeaders headers = new HttpHeaders();
			File serverFile = new File(mainRoute + "/Eventos/Imagenes/" + contenidoWebId + "/" + uuidName);
			if(serverFile.length() > 0) {	
				headers.setETag("\""+uuidName.replace("-", "").substring(0,8)+"\"");
			    headers.setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic().getHeaderValue());
			    return new ResponseEntity<>(Files.readAllBytes(serverFile.toPath()), headers, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			// TODO: handle exception
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@NoLogging
	@RequestMapping(value = "/quienes-somos/historia/{contenidoWebId}/{uuidName:.+}", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
	public ResponseEntity<byte[]> getImageFromHistoria(@PathVariable String contenidoWebId,
			@PathVariable String uuidName) {
		try {
			HttpHeaders headers = new HttpHeaders();
			File serverFile = new File(mainRoute + "/Quienes/Historia/Imagenes/" + contenidoWebId + "/" + uuidName);
			if(serverFile.length() > 0) {	
				headers.setETag("\""+uuidName.replace("-", "").substring(0,8)+"\"");
			    headers.setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic().getHeaderValue());
			    return new ResponseEntity<>(Files.readAllBytes(serverFile.toPath()), headers, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			// TODO: handle exception
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@NoLogging
	@RequestMapping(value = "/memoria/{uuidName:.+}", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
	public ResponseEntity<byte[]> getImageFromProjectMemory(
			@PathVariable String uuidName) {
		try {
			HttpHeaders headers = new HttpHeaders();
			File serverFile = new File(mainRoute + "/MemoriaProyecto/" + uuidName);
			if(serverFile.length() > 0) {	
				headers.setETag("\""+uuidName.replace("-", "").substring(0,8)+"\"");
			    headers.setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic().getHeaderValue());
			    return new ResponseEntity<>(Files.readAllBytes(serverFile.toPath()), headers, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			// TODO: handle exception
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@NoLogging
	@RequestMapping(value = "/libre/{uuidName:.+}", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
	public ResponseEntity<byte[]> getImageLibre(
			@PathVariable String uuidName) {
		try {
			HttpHeaders headers = new HttpHeaders();
			File serverFile = new File(mainRoute + "/Media/Imagenes/" + uuidName);
			if(serverFile.length() > 0) {
				headers.setETag("\""+uuidName.replace("-", "").substring(0,8)+"\"");
			    headers.setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic().getHeaderValue());
			    return new ResponseEntity<>(Files.readAllBytes(serverFile.toPath()), headers, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			// TODO: handle exception
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
