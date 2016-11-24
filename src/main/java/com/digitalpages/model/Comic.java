package com.digitalpages.model;

import javax.persistence.Entity;


@Entity
public class Comic {

	private Integer id;
	private String title;
	private Integer issue;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return "Comic [id=" + id +", title=" + title + ", issue="+issue+"]";
	}

	public Integer getIssue() {
		return issue;
	}

	public void setIssue(Integer issue) {
		this.issue = issue;
	}

}