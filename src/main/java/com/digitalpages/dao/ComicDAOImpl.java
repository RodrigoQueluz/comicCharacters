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

import com.digitalpages.model.Comic;

@Repository	
@Transactional
public class ComicDAOImpl implements ComicDAO {

	
    private BasicDataSource dbcpDataSource;

	@Override
    public Comic save(Comic comic){
    	buildDataSource();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
    	String SQL = "INSERT INTO COMICS VALUES (DEFAULT, :title, :issue, :thumb, :description)";
    	KeyHolder keyHolder = new GeneratedKeyHolder();
    	MapSqlParameterSource parameters = new MapSqlParameterSource();
    	parameters.addValue("title", comic.getTitle()); 
    	parameters.addValue("issue", comic.getIssue()); 
    	parameters.addValue("thumb", comic.getThumb()); 
    	parameters.addValue("description", comic.getDescription()); 

          try{
        	  namedParameterJdbcTemplate.update(SQL, parameters,keyHolder);
        	  comic.setId((Integer) keyHolder.getKey());
        	  System.out.println("Created Record Name = " + comic.toString());
          } catch(DuplicateKeyException ex){
        	  System.out.println("Registro Duplicado");
          }
          return comic;
    }
	
	
	@Override
	public List<Comic> findAll() {
		buildDataSource();

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		
		Map<String, Object> params = new HashMap<String, Object>();

		String sql = "SELECT * FROM COMICS";

		List<Comic> result = namedParameterJdbcTemplate.query(sql, params, new ComicWrapper());

		return result;
	}
	
	@Override
	public Comic findByID(int idComic) {
		
		buildDataSource();

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", idComic);
		Comic result = new Comic();
		String sql = "SELECT * FROM COMICS WHERE id=:id";

		try {
			result = namedParameterJdbcTemplate.queryForObject(

					sql, params, new ComicWrapper());

		} catch (EmptyResultDataAccessException ex) {
			System.out.println("Registro nao encontrardo");
			return null;
		}
		return result;
	}
	
	private static final class ComicWrapper implements RowMapper<Comic> {

		public Comic mapRow(ResultSet rs, int rowNum) throws SQLException {
			Comic comic = new Comic();
			comic.setTitle(rs.getString("title"));
			comic.setId(rs.getInt("id"));
			comic.setIssue(rs.getString("issue"));
			comic.setDescription(rs.getString("description"));
			comic.setThumb(rs.getString("thumb"));

			return comic;
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