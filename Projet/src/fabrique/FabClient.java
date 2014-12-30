package fabrique;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import donnee.Client;
import donnee.Reservation;
import exception.ObjetExistantException;
import exception.ObjetInconnuException;


public class FabClient {

	private static FabClient singleton;

	public static FabClient getInstance(){
		if(singleton==null) {
			singleton = new FabClient();
		}
		return singleton;
	}
	
	/**
	 * Cree un client
	 * @param nom
	 * @param prenom
	 * @param tel
	 * @return client
	 * @throws ObjetExistantException
	 */
	public Client creer(String nom, String prenom , String tel) throws ObjetExistantException {
		Client client = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		String query = "INSERT INTO Client (nom,prenom,tel,fidelite) VALUES(?,?,?,?)";
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.clearParameters();
			
			pst.setString(1, nom);
			pst.setString(2, prenom);
			pst.setString(3,tel);
			pst.setInt(4, 0);
			
			pst.execute();
			
			client = new Client();
			rs = pst.getGeneratedKeys();
			if(rs.next()){
				client.setId(rs.getInt(1));
				client.setNom(nom);
				client.setPrenom(prenom);
				client.setNumTel(tel);
				client.setPointsFidelite(0);
			}
			
		} catch (SQLException se) {
			System.out.println("Echec de la creation du client "+nom+" - "+se.getMessage());
		}

		return client;
	}

	/**
	 * Recherche un client a partir de son id
	 * @param id
	 * @return client
	 * @throws ObjetInconnuException
	 */
	public Client rechercher(int id) throws ObjetInconnuException{
		Client client = null;
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		String query = "SELECT nom, prenom, tel, fidelite FROM Client WHERE idclient = ?";
		try {
			pst = connection.prepareStatement(query);
			pst.clearParameters();
			
			pst.setInt(1, id);
			
			ResultSet rs = pst.executeQuery();
			
			if(!rs.next()) {
				throw new ObjetInconnuException(Client.class.toString(), "Aucun client a ete trouve pour l'identifiant "+id);
			}

			client = new Client();
			client.setId(id);
			client.setNom(rs.getString("nom"));
			client.setPrenom(rs.getString("prenom"));
			client.setNumTel(rs.getString("tel"));
			client.setPointsFidelite(rs.getInt("fidelite"));

		} catch (SQLException e) {
			System.out.println("Echec de la recuperation du client pour l'id "+id
					+" - "+e.getMessage());
		}
		return client;
	}
	
	/**
	 * Recherche un client a partir de son nom, prenom et telephone
	 * @param nom le nom du client a rechercher
	 * @param prenom le prenom du client a rechercher
	 * @param num le numero de telephone du client a rechercher
	 * @return client
	 * @throws ObjetInconnuException
	 */
	public Client rechercher(String nom, String prenom, String num) throws ObjetInconnuException{
		Client client = null;
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		String query = "SELECT idclient, nom, prenom, tel, fidelite FROM Client WHERE nom = ? and prenom = ? and tel = ?";
		try {
			pst = connection.prepareStatement(query);
			pst.clearParameters();
			
			pst.setString(1, nom);
			pst.setString(2, prenom);
			pst.setString(3, num);
			
			ResultSet rs = pst.executeQuery();
			
			if(!rs.next()) {
				throw new ObjetInconnuException(Client.class.toString(), "Aucun client a ete trouve pour le nom "+nom+" et le tel: "+num);
			}

			client = new Client();
			client.setId(rs.getInt("idclient"));
			client.setNom(nom);
			client.setPrenom(prenom);
			client.setNumTel(num);
			client.setPointsFidelite(rs.getInt("fidelite"));

		} catch (SQLException e) {
			System.out.println("Echec de la recuperation du client du nom "+nom
					+" - "+e.getMessage());
		}
		return client;
	}

	/**
	 * Modifie le client ayant comme id celui du client clt
	 * et la modifie avec les donness contenus dans clt
	 * @param clt
	 */
	public void modifierClient(Client clt){
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		
		String query = "update client set nom = ?, prenom = ?, tel = ?, fidelite = ? "
				+ " where idclient = ?";
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.clearParameters();

			pst.setString(1, clt.getNom());
			pst.setString(2, clt.getPrenom());
			pst.setString(3, clt.getNumTel());
			pst.setInt(4, clt.getPointsFidelite());
			pst.setInt(5, clt.getId());
			pst.execute();
		} catch (SQLException se) {
			System.out.println("Echec de la modification du client "+clt.getId()+" - "+se.getMessage());
		}
	}
	
	/**
	 * Supprime un client par son id
	 * @param id
	 */
	public void supprimer(int id){
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		String query = "DELETE FROM Client WHERE idclient = ?";
		try {
			pst = connection.prepareStatement(query);
			pst.clearParameters();
			
			pst.setInt(1, id);
			
			pst.execute();
			
		} catch (SQLException e) {
			System.out.println("Echec de la suppression du client pour l'id "+id
					+" - "+e.getMessage());
		}
	}

	/**
	 * Recupere l'ensemble des clients
	 * @return la liste des clients
	 */
	public List<Client> lister() {
		Connection connection = FabConnexion.getConnexion();
		String query = "SELECT nom,prenom,tel,fidelite FROM Client order by nom";
		Statement st = null;
		ResultSet rs = null;
		List<Client> listClient = new ArrayList<Client>();
		Client client = null;
		try{
			st = connection.createStatement();
			rs = st.executeQuery(query);

			while(rs.next()){
				client = new Client();
				client.setNom(rs.getString("nom"));
				client.setPrenom(rs.getString("prenom"));
				client.setNumTel(rs.getString("tel"));
				client.setPointsFidelite(rs.getInt("fidelite"));
				listClient.add(client);
			}
		}catch(SQLException e){
			System.out.println("Erreur d'acces aux listes des clients en base de donnees - "
					+e.getMessage());
		}
		return listClient;
	}

}
