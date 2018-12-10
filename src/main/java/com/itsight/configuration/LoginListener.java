package com.itsight.configuration;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.itsight.domain.Usuario;
import com.itsight.service.UsuarioService;

@Component
public class LoginListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent>{

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private HttpSession session;
	
	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent login) {
		// TODO Auto-generated method stub
		String userName = login.getAuthentication().getName();

		Usuario secUser = usuarioService.getUsuarioByUsername(userName);
		secUser.setFechaUltimoAcceso(new Date());
		//Actualizando ultimo acceso
		usuarioService.add(secUser);
		//Seteando en session el valor inicial del idioma predilecto - ES
		session.setAttribute("idioma", "es");
		
	}
}
