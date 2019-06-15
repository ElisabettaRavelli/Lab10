package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.CoAuthor;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public Map<Integer, Author> getAllAutore() {

		final String sql = "SELECT * FROM author" ;
		Map<Integer, Author> result = new HashMap<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				result.put(autore.getId(), autore);
			
			}

			return result;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public List<CoAuthor> getCoAuthor() {
		final String sql = "select c1.authorid as a1, c2.authorid as a2 " + 
				"from creator as c1, creator as c2 " + 
				"where c1.eprintid=c2.eprintid " + 
				"and c1.authorid>c2.authorid " ;
		List<CoAuthor> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while(rs.next()) {
				
				CoAuthor coAutore = new CoAuthor(rs.getInt("a1"), rs.getInt("a2"));
				result.add(coAutore);
				
			}
			return result;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public Paper getArticoloComune(Author aPrimo, Author aSecondo) {
		
		final String sql = "select p.eprintid, title, issn, publication, type, types " + 
				"from creator as c1, creator as c2, paper as p " + 
				"where p.eprintid = c1.eprintid " + 
				"and p.eprintid = c2.eprintid " + 
				"and c1.authorid = ? " + 
				"and c2.authorid = ? " + 
				"limit 1 ";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, aPrimo.getId());
			st.setInt(2, aSecondo.getId());
			ResultSet res = st.executeQuery();
			
			Paper p = null;
			if(res.next()) {
				
				p = new Paper(res.getInt("eprintid"), res.getString("title" ), res.getString("issn"),
						res.getString("publication"), res.getString("type"), res.getString("types")) ;
			}
			return p;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
}