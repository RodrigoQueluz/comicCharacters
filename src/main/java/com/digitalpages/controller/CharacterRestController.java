package com.digitalpages.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.digitalpages.dao.ComicCharacterDAOImpl;
import com.digitalpages.dao.ComicDAOImpl;
import com.digitalpages.model.Comic;
import com.digitalpages.model.ComicCharacter;;

@RestController
public class CharacterRestController {

	@Autowired
	ComicCharacterDAOImpl comicCharacterDAOImpl;

	@RequestMapping(value = "/characters", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
		ComicCharacterDAOImpl impl = new ComicCharacterDAOImpl();

		List<ComicCharacter> list = impl.findAll();

//		ModelAndView model = new ModelAndView("characters");
//		model.addObject("lists", list);
//
//		return model;
		
		PagedListHolder<ComicCharacter> pagedListHolder = new PagedListHolder<ComicCharacter>(list);
		int page = ServletRequestUtils.getIntParameter(request, "p", 0);
		pagedListHolder.setPage(page);
		int pageSize = 5;
		pagedListHolder.setPageSize(pageSize);
		
		ModelAndView mav = new ModelAndView("characters");
//		model.addObject("lists", list);
//
//		return model;
		
		mav.addObject("pagedListHolder", pagedListHolder);
		return mav;
	}
	
	@RequestMapping(value = "/characters.json", method = RequestMethod.GET)
	public List<ComicCharacter> listJson(HttpServletRequest request, HttpServletResponse response) {
		ComicCharacterDAOImpl impl = new ComicCharacterDAOImpl();

		List<ComicCharacter> list = impl.findAll();

		return list;
	}
	
	@RequestMapping(value = "/comics", method = RequestMethod.GET)
	public List<Comic> listComics(HttpServletRequest request, HttpServletResponse response) {
		ComicDAOImpl impl = new ComicDAOImpl();

		List<Comic> list = impl.findAll();

		return list;
	}

	@RequestMapping(value = "/characters/{value}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ModelAndView getCharacter(@PathVariable(value = "value") int idCharacter) {
		ComicCharacter comicCharacter = comicCharacterDAOImpl.findByID(idCharacter);

		ModelAndView model = new ModelAndView("character");
		model.addObject("comicCharacter", comicCharacter);
		model.addObject("comics", comicCharacter.getComics());

		return model;
	}

}
