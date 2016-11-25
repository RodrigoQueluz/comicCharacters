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

import com.digitalpages.model.ComicCharacter;

@Repository	
@Transactional
public class ComicCharacterDAOImpl implements ComicCharacterDAO {

	
    private BasicDataSource dbcpDataSource;

	@Override
    public ComicCharacter save(ComicCharacter character){
    	buildDataSource();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
    	String SQL = "INSERT INTO CHARACTERS VALUES (DEFAULT, :name, :description, :last_update, :thumb)";
    	KeyHolder keyHolder = new GeneratedKeyHolder();
    	MapSqlParameterSource parameters = new MapSqlParameterSource();
    	parameters.addValue("name", character.getName()); 
    	parameters.addValue("description", character.getDescription()); 
    	parameters.addValue("last_update", character.getLastUpdate());   
    	parameters.addValue("thumb", character.getThumb());


          try{
        	  namedParameterJdbcTemplate.update(SQL, parameters,keyHolder);
        	  character.setId((Integer) keyHolder.getKey());
        	  System.out.println("Created Record Name = " + character.toString());
          } catch(DuplicateKeyException ex){
        	  System.out.println("Registro Duplicado");
          }
          return character;
    }
	
	
	@Override
	public List<ComicCharacter> findAll() {
		buildDataSource();

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		
		Map<String, Object> params = new HashMap<String, Object>();

		String sql = "SELECT * FROM CHARACTERS";

		List<ComicCharacter> result = namedParameterJdbcTemplate.query(sql, params, new ComicCharacterWrapper());

		return result;
	}
	
	@Override
	public ComicCharacter findByID(int idCharacter) {
		
		buildDataSource();

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", idCharacter);
		ComicCharacter result = new ComicCharacter();
		String sql = "SELECT * FROM CHARACTERS WHERE id=:id";

		try {
			result = namedParameterJdbcTemplate.queryForObject(

					sql, params, new ComicCharacterWrapper());

		} catch (EmptyResultDataAccessException ex) {
			System.out.println("Registro nao encontrardo");
			return null;
		}
		return result;
	}
	
	private static final class ComicCharacterWrapper implements RowMapper<ComicCharacter> {

		public ComicCharacter mapRow(ResultSet rs, int rowNum) throws SQLException {
			ComicCharacter character = new ComicCharacter();
			character.setName(rs.getString("name"));
			character.setDescription(rs.getString("description"));
			character.setId(rs.getInt("id"));
			character.setLastUpdate(rs.getDate("last_update"));
			character.setThumb(rs.getString("thumb"));
			return character;
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