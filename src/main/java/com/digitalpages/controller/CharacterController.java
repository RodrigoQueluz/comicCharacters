package com.digitalpages.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.digitalpages.helper.Helper;;


@Controller
public class CharacterController {
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String home() throws IOException, ParseException{
		return "home";
	}
	@RequestMapping(value="/credenciais", method=RequestMethod.POST)
	public ModelAndView credenciais(HttpServletRequest request){
		String privateKey = request.getParameter("private_key");
		String publicKey = request.getParameter("public_key");
		Long ts = new Date().getTime();
		Helper helper = new Helper();
		try {
			helper.sendRequest(ts, publicKey, privateKey);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:characters");

	}
	
	public static void main(String[] args) throws IOException, ParseException {
		Long ts = new Date().getTime();
		String publicKey = "8cd6fdc39c0d5e3479e9b35a3dd505e2";
		String privateKey = "c34a70f52d253bff491c4ba7e7794254dd118b3b";
		
		Helper helper = new Helper();
		helper.sendRequest(ts, publicKey, privateKey);
	}

}
