package com.digitalpages.dao;

import java.util.List;

import com.digitalpages.model.ComicCharacter;

public interface ComicCharacterDAO {

    public ComicCharacter save(ComicCharacter character);

	public List<ComicCharacter> findAll();

	public ComicCharacter findByID(int id);
}