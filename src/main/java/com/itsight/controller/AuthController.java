
package com.itsight.controller;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.FlatColorBackgroundProducer;
import cn.apiclub.captcha.noise.CurvedLineNoiseProducer;
import cn.apiclub.captcha.noise.StraightLineNoiseProducer;
import cn.apiclub.captcha.servlet.CaptchaServletUtil;
import cn.apiclub.captcha.text.producer.DefaultTextProducer;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.itsight.constants.ViewConstant;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
public class AuthController {
	
	private static final Logger LOGGER = LogManager.getLogger(AuthController.class);

	private ServletContext context;

	public AuthController(ServletContext context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@GetMapping(value = "/login")
	public ModelAndView loginForm(@RequestParam(value = "error", required = false) String error, HttpSession session)
	{
		java.util.List<Color> textColors = Arrays.asList(
				Color.getHSBColor(198,68,66),
				Color.getHSBColor(175,228,216),
				Color.getHSBColor(222,129,94));
		List<Font> textFonts = Arrays.asList(
				new Font("Arial", Font.PLAIN, 40));

		Captcha captcha = new Captcha.Builder(126, 50)
				.addText(
						new DefaultTextProducer(),
						new DefaultWordRenderer(textColors, textFonts))
				.addBackground()
				.addNoise(new CurvedLineNoiseProducer())
				.build();
		session.setAttribute("codCaptcha", captcha.getAnswer());
		ModelAndView mav = new ModelAndView(ViewConstant.LOGIN);
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(captcha.getImage(), "png", bos);
			mav.addObject("captchaEnc", DatatypeConverter.printBase64Binary(bos.toByteArray()));
		} catch (IOException e) {
			mav.addObject("error","error");
			return mav;
		}

		if(error != null){
			if(error.equals("session-expired")) {
				mav.addObject("expired","expired");
			}else if(error.equals("error")) {
				mav.addObject("error","error");
			}
		}
		//mav.addObject("GOOGLE_API_KEY", context.getAttribute("GOOGLE_API_KEY").toString());
		return mav;
	}

	@GetMapping(value = {"/administracion/bienvenido","/"})
	public String welcome() {
		return "bienvenido";
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/validation/recaptcha-v2", method = RequestMethod.POST)
	public @ResponseBody String validationGoogleReCaptcha(@RequestParam String response){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("secret", "6Le0RDQUAAAAAKCePQERfk4RAFDyirh4_pYo5mf3");
		map.add("response", response);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
	    RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> res = restTemplate.postForEntity("https://www.google.com/recaptcha/api/siteverify", request , String.class);
		LOGGER.info(res.getBody());
		if(res != null){
			return "1";
		}else{
			return "-1";
		}
	}
	
	@GetMapping(value = "/session/idioma")
	public @ResponseBody String guardandoIdioma(HttpSession session) {
		if(session.getAttribute("idioma").equals("es")) {
			session.setAttribute("idioma","us");
			return "us";
		}else {
			session.setAttribute("idioma","es");
			return "es";
		}
	}
	
	@GetMapping(value = "/deniedAccess")
	public ModelAndView accesoDenegado() {
		return new ModelAndView(ViewConstant.ERROR403);
	}

	@GetMapping(value = "/imagen-captcha/nueva")
	public @ResponseBody String nuevaImagenCaptcha(HttpSession session){
		java.util.List<Color> textColors = Arrays.asList(
				Color.getHSBColor(198,68,66),
				Color.getHSBColor(175,228,216),
				Color.getHSBColor(222,129,94));
		List<Font> textFonts = Arrays.asList(
				new Font("Arial", Font.PLAIN, 40));

		Captcha captcha = new Captcha.Builder(126, 50)
				.addText(
						new DefaultTextProducer(),
						new DefaultWordRenderer(textColors, textFonts))
				.addBackground()
				.addNoise(new CurvedLineNoiseProducer())
				.build();
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(captcha.getImage(), "png", bos);
			session.setAttribute("codCaptcha", captcha.getAnswer());
			return DatatypeConverter.printBase64Binary(bos.toByteArray());
		} catch (Exception e){
			e.printStackTrace();
		}
		return "-1";
	}

}
