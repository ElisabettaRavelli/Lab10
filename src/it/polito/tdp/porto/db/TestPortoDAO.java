package it.polito.tdp.porto.db;

import it.polito.tdp.porto.model.Model;

public class TestPortoDAO {
	
	public static void main(String args[]) {
//		PortoDAO pd = new PortoDAO();
//		System.out.println(pd.getAutore(85));
//		System.out.println(pd.getArticolo(2293546));
//		System.out.println(pd.getArticolo(1941144));
		
		Model m = new Model();
		m.creaGrafo();
		System.out.format("Creati %d vertici e %d archi \n", m.getGrafo().vertexSet().size(),
				m.getGrafo().edgeSet().size());

	}

}
