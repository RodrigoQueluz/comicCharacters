package com.digitalpages.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;


@Entity
public class ComicCharacter {

	private Integer id;
	private String name;
	private String description;
	private Date lastUpdate;
	private List<Comic> comics;
	private String thumb;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "COMIC CHARACTER [id=" + id +", description=" + name + ", last updated="+lastUpdate.toString()+"]";
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public List<Comic> getComics() {
		return comics;
	}

	public void setComics(List<Comic> comics) {
		this.comics = comics;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
}