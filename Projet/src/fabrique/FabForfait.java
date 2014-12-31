package fabrique;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

import donnee.Forfait;



public class FabForfait {

	private static FabForfait instance;
	
	public static FabForfait getInstance(){
		if(instance == null){
			instance = new FabForfait();
		}
		return instance;
	}
	
	
	/**
	 * Recupere un forfait a partir du type de forfait (12h, 24h...)
	 * @param typeForfait
	 * @return Forfait
	 */
	public Forfait rechercherForfait(String typeForfait){
		Forfait forfait = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = FabConnexion.getConnexion()
					.prepareStatement("select prixpetitesalle, prixgrandesalle, validite "
							+ "from forfait where typeforfait = ?");
			
			st.setString(1, typeForfait);
			
			rs = st.executeQuery();
			
			if(rs.next()){
				forfait = new Forfait();
				forfait.setTypeForfait(typeForfait);
				forfait.setPrixPetitesSalles(rs.getInt("prixpetitesalle"));
				forfait.setPrixGrandesSalles(rs.getInt("prixgrandesalle"));
				forfait.setValidite(rs.getInt("validite"));
			}
		} catch (SQLException e) {
			System.out.println("Erreur lors de la récupération du forfait "+ typeForfait 
					+ " - " + e.getMessage());
		}
		
		return forfait;
	}

	/**
	 * Liste tous les forfaits
	 * @return liste des forfaits
	 */
	public List<Forfait> lister(){
		List<Forfait> listeForfait = new ArrayList<Forfait>();
		Forfait fc = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = FabConnexion.getConnexion().prepareStatement("SELECT typeforfait, prixpetitesalle, prixgrandesalle, validite FROM forfait");
			rs=st.executeQuery();
			
			while(rs.next()){
				fc = new Forfait();
				fc.setTypeForfait(rs.getString("typeforfait"));
				fc.setPrixPetitesSalles(rs.getInt("prixpetitesalle"));
				fc.setPrixGrandesSalles(rs.getInt("prixgrandesalle"));
				fc.setValidite(rs.getInt("validite"));
				listeForfait.add(fc);
			}
		} catch (SQLException e) {
			System.out.println("Erreur lors de la recuperation des forfaits" + e.getMessage());
		}
		
		return listeForfait;
	}
}
