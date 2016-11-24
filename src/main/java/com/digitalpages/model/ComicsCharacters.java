package com.digitalpages.model;

import javax.persistence.Entity;


@Entity
public class ComicsCharacters {

	private Integer id;
	private Comic comic;
	private ComicCharacter character;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "COMIC/CHARACTER [Comic=" + comic.getTitle() +" - Character= "+ character.getName() +"]";
	}

	public Comic getComic() {
		return comic;
	}

	public void setComic(Comic comic) {
		this.comic = comic;
	}

	public ComicCharacter getCharacter() {
		return character;
	}

	public void setCharacter(ComicCharacter character) {
		this.character = character;
	}
}