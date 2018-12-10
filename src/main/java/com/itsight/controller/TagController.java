package com.itsight.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itsight.constants.ViewConstant;
import com.itsight.domain.Tag;
import com.itsight.repository.TipoTagRepository;
import com.itsight.service.TagService;

@Controller
@RequestMapping("/gestion/tag")
public class TagController {

	private TagService tagService;
	
	private TipoTagRepository tipoTagRepository;
	
	@Autowired
	public TagController(TagService tagService, TipoTagRepository tipoTagRepository) {
		// TODO Auto-generated constructor stub
		this.tagService = tagService;
		this.tipoTagRepository = tipoTagRepository;
	}
	
	@GetMapping(value = "")
	public ModelAndView principalTag(Model  model) {
		model.addAttribute("lstTipoTag", tipoTagRepository.findAll());
		return new ModelAndView(ViewConstant.MAIN_TAG);
	}
	
	@GetMapping(value = "/obtenerListado")
	public @ResponseBody List<Tag> listAllTags() throws JsonProcessingException{
		return tagService.listAll();
	}
	
	@PostMapping(value = "/agregar")
	public @ResponseBody String addTag(@ModelAttribute Tag tag, @RequestParam String tipo) {
		tag.setTipoTag(Integer.parseInt(tipo));

		if(tag.getId()> 0) {
			try {
				tagService.add(tag);
			} catch (ConstraintViolationException e) {
				// TODO: handle exception
				return "-1";
			} catch (Exception e) {
				return "-1";
			}
					
		}else {
			if(tagService.findOneByNombre(tag.getNombre()) == null) {
				tagService.add(tag);	
			}
		}
		return "1";
	}
	
	@DeleteMapping(value = "/eliminar/{id}")
	public @ResponseBody String eliminar(@PathVariable(value = "id") int id){
		tagService.delete(id);
		return "1";
	}
	
	@GetMapping(value = "/obtener")
	public @ResponseBody Tag getTagById(@RequestParam(value = "id") int tagId) {
		return tagService.findOneById(tagId);
	}
	
	@GetMapping(value = "/obtenerListado/{comodin}/{tipoTag}")
	public @ResponseBody List<Tag> listAllTags(
										@PathVariable("comodin") String comodin,
										@PathVariable("tipoTag") String tipoTagId) throws JsonProcessingException{
		
		List<Tag> lstTag = new ArrayList<Tag>();
		
		if(comodin.equals("0") && tipoTagId.equals("0") ) {
			lstTag= tagService.findAll();
		}else {
			if(comodin.equals("0") && !tipoTagId.equals("0")) {
					lstTag = tagService.findAllByTipoTagId(Integer.parseInt(tipoTagId));
			}else {
				List<Tag> lstTagFilter = new ArrayList<>();
				comodin = comodin.equals("0") ? "":comodin;
				
				lstTag = tagService.findAllByNombreContainingIgnoreCase(comodin);
				if(!tipoTagId.equals("0")) {
					for (Tag x : lstTag) {
						if(tipoTagId.equals(String.valueOf(x.getTipoTag().getId()))){
							lstTagFilter.add(x);
						}
					}
					
				}else {
					return lstTag;
				}
				return lstTagFilter;
			}
		}
		return lstTag;
	}
}
