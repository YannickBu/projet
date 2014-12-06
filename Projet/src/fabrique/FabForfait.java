package fabrique;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import donnee.Forfait;


public class FabForfait {

	private static FabForfait instance;
	
	public static FabForfait getInstance(){
		if(instance == null){
			instance = new FabForfait();
		}
		return instance;
	}
	
	public FabForfait() {
		
	}
	
	/**
	 * Récupère un forfait à partir du type de forfait (12h, 24h...)
	 * @param typeForfait
	 * @return
	 */
	public Forfait rechercherForfait(String typeForfait){
		Forfait forfait = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = FabConnexion.getConnexion().prepareStatement("select prixpetitesalle, prixgrandesalle, validite from forfait where typeforfait = ?");
			
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
			System.out.println("Erreur lors de la récupération du forfait "+ typeForfait + " dans FabForfait");
		}
		
		return forfait;
	}

}
