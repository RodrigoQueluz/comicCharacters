package com.digitalpages;

import java.sql.SQLException;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.digitalpages.dao.ComicCharacterDAOImpl;
import com.digitalpages.dao.ComicDAOImpl;
import com.digitalpages.dao.ComicsCharactersDAOImpl;
import com.digitalpages.model.Comic;
import com.digitalpages.model.ComicCharacter;
import com.digitalpages.model.ComicsCharacters;


public class DataBaseTest {

	private static final int DATABASE_ID_TEST = 1;
	private static final String DATABASE_TEST = "Database Test";
	EmbeddedDatabase db;
	ComicsCharacters comicsCharacters;
	ComicCharacter character;
	Comic comic;
	
	@Before
	public void setup() throws SQLException{
		db = new EmbeddedDatabaseBuilder()
	    		.setName("database")
	    		.setType(EmbeddedDatabaseType.HSQL)
	    		.addScript("db/sql/create-db.sql")
	    		.build();
	}
	
	@Test
	public void shouldInsertCharacter() throws SQLException{
		character = new ComicCharacter();
		character.setName(DATABASE_TEST);
		character.setLastUpdate(new Date());
		character.setDescription("Description");
		
		ComicCharacterDAOImpl impl = new ComicCharacterDAOImpl();
		impl.save(character);
		
		Assert.assertTrue(impl.findByID(DATABASE_ID_TEST).getName().equals(character.getName()));
	}

	@Test
	public void shouldInsertComic() throws SQLException{
		comic = new Comic();
		comic.setTitle("Comic Title");
		comic.setIssue(199);
		
		ComicDAOImpl impl = new ComicDAOImpl();
		impl.save(comic);
		
		Assert.assertTrue(impl.findByID(DATABASE_ID_TEST).getTitle().equals(comic.getTitle()));
	}
	
	@Test
	public void shouldInsertComicCharacter() throws SQLException{
		shouldInsertComic();
		shouldInsertCharacter();
		
		comicsCharacters = new ComicsCharacters();
		comicsCharacters.setCharacter(character);
		comicsCharacters.setComic(comic);
		
		ComicsCharactersDAOImpl impl = new ComicsCharactersDAOImpl();
		impl.save(comicsCharacters);

		Assert.assertTrue(impl.findByID(DATABASE_ID_TEST).getCharacter().getName().equals(character.getName()));
	}
	
	@After
	public void tearDown(){
		db.shutdown();

	}
}
