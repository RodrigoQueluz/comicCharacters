package com.digitalpages.dao;

import java.util.List;

import com.digitalpages.model.ComicsCharacters;

public interface ComicsCharactersDAO {

    public ComicsCharacters save(ComicsCharacters comicsCharacters);

	public List<ComicsCharacters> findAll();

	public ComicsCharacters findByID(int id);
}