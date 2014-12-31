package fabrique;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import donnee.Client;
import donnee.Reservation;
import donnee.Salle;
import exception.ObjetExistantException;
import exception.ObjetInconnuException;

public class FabReservation {

	private static FabReservation singleton;

	public static FabReservation getInstance(){
		if(singleton==null) {
			singleton = new FabReservation();
		}
		return singleton;
	}
	
	/**
	 * Cree une nouvelle reservation
	 * @param idClient
	 * @param idSalle
	 * @param dateDebut
	 * @param plage
	 * @param dateCreation
	 * @param estPayee
	 * @throws ObjetExistantException
	 * @return reservation
	 */
	public Reservation creer(int idClient, int idSalle, Date dateDebut, int plage, Date dateCreation, boolean estPayee ) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection connection = FabConnexion.getConnexion();
		Reservation reserv = null;
		String query = "INSERT INTO Reservation (idsalle,idclient,datedebut,plage,datecreation,estpayee) "
				+ "VALUES(?,?,?,?,?,?)";
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.clearParameters();
			
			pst.setInt(1, idSalle);
			pst.setInt(2, idClient);
			pst.setTimestamp(3, new Timestamp(dateDebut.getTime()));
			pst.setInt(4,plage);
			pst.setTimestamp(5, new Timestamp(dateCreation.getTime()));
			pst.setBoolean(6, estPayee);
			pst.execute();
			
			reserv = new Reservation();
			rs = pst.getGeneratedKeys();
			if(rs.next()){
				reserv.setIdReserv(rs.getInt(1));
				reserv.setClient(FabClient.getInstance().rechercher(idClient));
				reserv.setSalle(FabSalle.getInstance().rechercherParId(idSalle));
				reserv.setDate(dateDebut);
				reserv.setDateCreation(dateCreation);
				reserv.setEstPaye(estPayee);
				reserv.setPlage(plage);
			}
		} catch (SQLException se) {
			System.out.println("Echec de la creation de la reservation - "+se.getMessage());
		}
		return reserv;
	}
	

	/**
	 * Recupere une reservation a partir de son idReservation
	 * @param id
	 * @throws ObjetInconnuException
	 * @return reservation
	 */
	public Reservation rechercher(int id) throws ObjetInconnuException{
		Reservation res = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection connection = FabConnexion.getConnexion();
		String query = "SELECT idsalle, idclient, datedebut, plage, datecreation, estpayee "
				+ "from reservation where idreservation = ?";
		try {
			pst = connection.prepareStatement(query);
			pst.clearParameters();
			pst.setInt(1, id);
			
			rs = pst.executeQuery();
			
			if(!rs.next()) {
				throw new ObjetInconnuException(Reservation.class.toString(), "Aucune Reservation a ete trouve pour l'identifiant "+id);
			}

			res = new Reservation();
			res.setIdReserv(id);
			res.setClient(FabClient.getInstance().rechercher(rs.getInt("idclient")));
			res.setSalle(FabSalle.getInstance().rechercherParId(rs.getInt("idsalle")));
			res.setDate(rs.getTimestamp("datedebut"));
			res.setDateCreation(rs.getTimestamp("datecreation"));
			res.setEstPaye(rs.getBoolean("estpayee"));
			res.setPlage(rs.getInt("plage"));

		} catch (SQLException e) {
			System.out.println("Echec de la recuperation de la reservation pour l'id "+id
					+" - "+e.getMessage());
		}
		return res;
	}

	
	/** Recherche de reservation par date et type de salle
	 * @param date
	 * @param typeSalle
	 * @return liste de reservation
	 */
	public List<Reservation> rechercherParDateEtTypeSalle(String date, int idSalle){
		List<Reservation> listeReservation = new ArrayList<Reservation>();
		PreparedStatement st = null;
		ResultSet rs = null;
		Reservation res = null;
		Salle salle = null;
		Client client = null;

		GregorianCalendar calendar = new java.util.GregorianCalendar(); 
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date dateDebut;
		java.util.Date dateFin;
		
		try {
			salle = FabSalle.getInstance().rechercherParId(idSalle);
			dateDebut = formatter.parse(date);
			calendar.setTime(dateDebut); // on passe dateFin à la journee qui suit dateDebut à 1h du matin
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 1);
			dateFin = calendar.getTime();
			st = FabConnexion.getConnexion()
					.prepareStatement("select idreservation, idsalle, idclient, datedebut, plage, datecreation, estpayee from reservation " +
							" where (datedebut between ? and ?) and idsalle = ? order by datedebut");
			st.setDate(1, new java.sql.Date(dateDebut.getTime()));
			st.setDate(2, new java.sql.Date(dateFin.getTime()));
			st.setInt(3, salle.getIdSalle());
			rs = st.executeQuery();
			while(rs.next()){
				client = FabClient.getInstance().rechercher(rs.getInt(3));
				res = new Reservation();
				res.setIdReserv(rs.getInt(1));
				res.setClient(client);
				res.setSalle(salle);
				res.setDate(rs.getTimestamp(4));
				res.setPlage(rs.getInt(5));
				res.setEstPaye(rs.getBoolean(7));
				res.setDateCreation(rs.getTimestamp(6));
				listeReservation.add(res);
			}
		} catch (SQLException e) {
			System.out.println("Erreur de la recuperation des reservations par date et type de salle"
					+e.getMessage());
		} catch (ParseException e) {
			System.out.println("Erreur de parsing : "+date+" avec le format 'dd-MM-yyyy' - "+e.getMessage());
		}
		
		return listeReservation;
	}
	
	/**
	 * Supprime une reservation a partir de son idReservation
	 * @param id
	 */
	public void supprimer(int id){
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		String query = "DELETE FROM reservation WHERE idreservation = ?";
		try {
			pst = connection.prepareStatement(query);
			pst.clearParameters();
			pst.setInt(1, id);
			pst.execute();
		} catch (SQLException e) {
			System.out.println("Echec de la suppression de la reservation pour l'id "+id
					+" - "+e.getMessage());
		}
	}
	
	/**
	 * Supprime une reservation a partir de lid de la salle et de la date
	 * @param idSalle
	 */
	public void supprimer(Date date, int idSalle){
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		String query = "DELETE FROM reservation WHERE idsalle = ? and datedebut = ?";
		try {
			pst = connection.prepareStatement(query);
			pst.clearParameters();
			pst.setInt(1, idSalle);
			pst.setDate(2, new java.sql.Date(date.getTime()));
			pst.execute();
		} catch (SQLException e) {
			System.out.println("Echec de la suppression de la reservation pour l'id "+idSalle
					+" - "+e.getMessage());
		}
	}
	
	/**
	 * Recupere l'ensemble des reservations
	 * @return liste de reservation
	 */
	public List<Reservation> lister() {
		Connection connection = FabConnexion.getConnexion();
		String query = "SELECT idreservation, idsalle, idclient, datedebut,"
				+ "plage, datecreation, estpayee FROM Reservation";
		Statement st = null;
		List<Reservation> listReservation = new ArrayList<Reservation>();
		try{
			st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);

			while(rs.next()){
				Reservation res = new Reservation();
				res.setIdReserv(rs.getInt("idreservation"));
				res.setClient(FabClient.getInstance().rechercher(rs.getInt("idclient")));
				res.setSalle(FabSalle.getInstance().rechercherParId(rs.getInt("idsalle")));
				res.setDate(rs.getDate("datedebut"));
				res.setPlage(rs.getInt("plage"));
				res.setDateCreation(rs.getDate("datecreation"));
				res.setEstPaye(rs.getBoolean("estpayee"));
				listReservation.add(res);
			}
		}catch(SQLException e){
			System.out.println("Erreur d'acces aux listes des reservations en base de donnees "
					+e.getMessage());
		}
		return listReservation;
	}
	
	/**
	 * Recupere toutes les reservations dun client par ordre chronologique 
	 * de debut de reservation et par ordre des idsalle
	 * @param id
	 * @return liste des reservation 
	 */
	public List<Reservation> listerParClient(int id){
		Connection connection = FabConnexion.getConnexion();
		String query = "SELECT idreservation, idsalle, datedebut, plage, datecreation, estpayee "
				+ "FROM Reservation where idclient = ? order by idsalle, datedebut";
		PreparedStatement st = null;
		List<Reservation> listReservation = new ArrayList<Reservation>();
		try{
			st = connection.prepareStatement(query);
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();

			while(rs.next()){
				Reservation res = new Reservation();
				res.setIdReserv(rs.getInt("idreservation"));
				res.setClient(FabClient.getInstance().rechercher(id));
				res.setSalle(FabSalle.getInstance().rechercherParId(rs.getInt("idsalle")));
				res.setDate(rs.getTimestamp("datedebut"));
				res.setPlage(rs.getInt("plage"));
				res.setDateCreation(rs.getTimestamp("datecreation"));
				res.setEstPaye(rs.getBoolean("estpayee"));
				listReservation.add(res);
			}
		}catch(SQLException e){
			System.out.println("Erreur dacces aux listes des reservations en base de donnees pour le client "+id
					+" - "+e.getMessage());
		}
		return listReservation;
	}
	
	/**
	 * Modifie la reservation ayant comme id celui du parametre res
	 * et la modifie avec les donness contenus dans res
	 * @param res
	 */
	public void modifierReservation(Reservation res){
		PreparedStatement pst = null;
		Connection connection = FabConnexion.getConnexion();
		
		String query = "update reservation set idsalle = ?, idclient = ?, "
				+ " datedebut = ?, plage = ?, datecreation = ?, estpayee = ? "
				+ " where idreservation = ?";
		try {
			pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.clearParameters();

			pst.setInt(1, res.getSalle().getIdSalle());
			pst.setInt(2, res.getClient().getId());
			pst.setTimestamp(3, new Timestamp(res.getDate().getTime()));
			pst.setInt(4,res.getPlage());
			pst.setTimestamp(5, new Timestamp(res.getDateCreation().getTime()));
			pst.setBoolean(6, res.getEstPaye());
			pst.setInt(7, res.getIdReserv());
			pst.execute();
		} catch (SQLException se) {
			System.out.println("Echec de la creation de la reservation - "+se.getMessage());
		}
	}
}
