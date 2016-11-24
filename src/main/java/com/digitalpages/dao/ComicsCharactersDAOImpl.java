package com.digitalpages.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.digitalpages.model.ComicsCharacters;

@Repository	
@Transactional
public class ComicsCharactersDAOImpl implements ComicsCharactersDAO {

	
    private BasicDataSource dbcpDataSource;

	@Override
    public ComicsCharacters save(ComicsCharacters comicsCharacters){
    	buildDataSource();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
    	String SQL = "INSERT INTO COMICS_CHARACTERS VALUES (DEFAULT, :id_character, :id_comic)";
    	KeyHolder keyHolder = new GeneratedKeyHolder();
    	MapSqlParameterSource parameters = new MapSqlParameterSource();
    	parameters.addValue("id_character", comicsCharacters.getCharacter().getId()); 
    	parameters.addValue("id_comic", comicsCharacters.getComic().getId()); 


          try{
        	  namedParameterJdbcTemplate.update(SQL, parameters,keyHolder);
        	  comicsCharacters.setId((Integer) keyHolder.getKey());
        	  System.out.println("Created Record Name = " + comicsCharacters.toString());
          } catch(DuplicateKeyException ex){
        	  System.out.println("Registro Duplicado");
          }
          return comicsCharacters;
    }
	
	
	@Override
	public List<ComicsCharacters> findAll() {
		buildDataSource();

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		
		Map<String, Object> params = new HashMap<String, Object>();

		String sql = "SELECT * FROM CHARACTERS";

		List<ComicsCharacters> result = namedParameterJdbcTemplate.query(sql, params, new ComicsCharactersWrapper());

		return result;
	}
	
	@Override
	public ComicsCharacters findByID(int idCharacter) {
		
		buildDataSource();

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", idCharacter);
		ComicsCharacters result = new ComicsCharacters();
		String sql = "SELECT * FROM COMICS_CHARACTERS WHERE id=:id";

		try {
			result = namedParameterJdbcTemplate.queryForObject(

					sql, params, new ComicsCharactersWrapper());

		} catch (EmptyResultDataAccessException ex) {
			System.out.println("Registro nao encontrardo");
			return null;
		}
		return result;
	}
	
	private static final class ComicsCharactersWrapper implements RowMapper<ComicsCharacters> {

		public ComicsCharacters mapRow(ResultSet rs, int rowNum) throws SQLException {
			ComicsCharacters comicsCharacters = new ComicsCharacters();
			comicsCharacters.setId(rs.getInt("id"));
			comicsCharacters.setCharacter(new ComicCharacterDAOImpl().findByID(rs.getInt("id_character")));
			comicsCharacters.setCharacter(new ComicCharacterDAOImpl().findByID(rs.getInt("id_comic")));
			return comicsCharacters;
		}
	}
    
	private void buildDataSource() {
		dbcpDataSource = new BasicDataSource();
		dbcpDataSource.setDriverClassName("org.hsqldb.jdbcDriver");
		dbcpDataSource.setUrl("jdbc:hsqldb:mem:database");
		dbcpDataSource.setUsername("sa");
		dbcpDataSource.setPassword("");
	}
}