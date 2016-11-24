package com.digitalpages.dao;

import java.util.List;

import com.digitalpages.model.Comic;

public interface ComicDAO {

    public Comic save(Comic character);

	public List<Comic> findAll();

	public Comic findByID(int id);
}