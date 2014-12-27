package presentation;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import metier.RechercheReservation;
import donnee.Client;
import donnee.ForfaitClient;
import donnee.Reservation;

/**
 * Panel permettant la visualisation des forfaits et reservations dun client
 * Permet la confirmation dune reservation non confirmee
 * ainsi que la suppression dune reservation hors delais
 */
public class PanelEditionClient extends JPanel implements ActionListener {
	
	private JFrame frame;
	
	private Client client;
	
	private JLabel lClientTitre;
	
	private JButton bActionRes;
	private JButton bActionFor;
	private JButton bRetour;
	private JButton bFiltreTous;
	private JButton bFiltreNonConfirme;
	private JButton bFiltreConfirme;
	private JButton bFiltreHorsDelais;
	
	private JList<Reservation> jListeReservations;
	private JList<ForfaitClient> jListeForfaits;
	private DefaultListModel<Reservation> modelRes;
	private DefaultListModel<ForfaitClient> modelFor;
	private List<Reservation> listeReservations;
	
	public PanelEditionClient(JFrame frame, Client client) {
		RechercheReservation metierRechercheReservation = new RechercheReservation();
		
		JLabel lRes = new JLabel("Vos reservations", JLabel.CENTER);
		JLabel lFor = new JLabel("Vos forfaits", JLabel.CENTER);
		JLabel lFiltre = new JLabel("Filtre", JLabel.CENTER);
		
		bActionRes = new JButton("Valider");
		bActionFor = new JButton("Acheter un forfait");
		bRetour = new JButton("Retour");
		bFiltreTous = new JButton("Tous");
		bFiltreConfirme = new JButton("C");
		bFiltreNonConfirme = new JButton("NC");
		bFiltreHorsDelais = new JButton("HD");
		bActionRes.setBackground(Color.WHITE);
		bActionFor.setBackground(Color.WHITE);
		bRetour.setBackground(Color.WHITE);
		bFiltreTous.setBackground(Color.WHITE);
		bFiltreConfirme.setBackground(Color.CYAN);
		bFiltreNonConfirme.setBackground(Color.ORANGE);
		bFiltreHorsDelais.setBackground(Color.RED);
		bFiltreTous.setBorder(BorderFactory.createLoweredBevelBorder());
		
		modelRes = new DefaultListModel<Reservation>();
		modelFor = new DefaultListModel<ForfaitClient>();
		jListeReservations = new JList<Reservation>(modelRes);
		jListeForfaits = new JList<ForfaitClient>(modelFor);
		jListeReservations.setCellRenderer(new RendererJListReservation());
		listeReservations = metierRechercheReservation.listerReservationsPourUnClient(client.getId(), null);
		for(Reservation res : listeReservations){
			modelRes.addElement(res);
		}
		
		this.frame = frame;
		this.client = client;
		
		GridBagLayout GBL = new GridBagLayout();
		GridBagConstraints GBC = null;
		this.setLayout(GBL);
		
		lClientTitre = new JLabel("Client (xx points)", JLabel.CENTER);//TODO pt fid
		
		GBC = new GridBagConstraints();
		GBC.fill = GridBagConstraints.BOTH; 
		GBC.anchor = GridBagConstraints.NORTH;
		GBC.gridy = 0;
		GBC.gridx = 0;
		GBC.gridheight = 1;
		GBC.gridwidth = 12;
		GBC.weightx = 0.5;
		GBC.weighty = 0.1;
		
		this.add(lClientTitre, GBC);

		GBC.gridy = 1;
		GBC.gridx = 0;
		GBC.gridheight = 1;
		GBC.gridwidth = 7;
		GBC.insets = new Insets(2, 2, 2, 2);

		lRes.setOpaque(true);
		lRes.setBackground(Color.LIGHT_GRAY);
		this.add(lRes, GBC);
		
		GBC.gridx = 7;
		GBC.gridwidth = 5;

		lFor.setOpaque(true);
		lFor.setBackground(Color.LIGHT_GRAY);
		this.add(lFor, GBC);
		
		GBC.gridy++;
		GBC.gridx = 0;
		GBC.gridheight = 3;
		GBC.gridwidth = 7;
		GBC.weightx = 1;
		GBC.weighty = 1;
		
		this.add(jListeReservations, GBC);
		
		GBC.gridx = 7;
		GBC.gridheight = 4;
		GBC.gridwidth = 5;
		
		this.add(jListeForfaits, GBC);

		GBC.gridx = 0;
		GBC.gridy = 5;
		GBC.gridheight = 1;
		GBC.gridwidth = 2;
		GBC.insets = new Insets(0, 0, 0, 0);
		GBC.weightx = 0.1;
		GBC.weighty = 0.01;

		lFiltre.setOpaque(true);
		lFiltre.setBackground(Color.LIGHT_GRAY);
		this.add(lFiltre, GBC);

		GBC.gridx = 3;
		GBC.gridwidth = 1;

		this.add(bFiltreTous, GBC);
		
		GBC.gridx = 4;

		this.add(bFiltreConfirme, GBC);
		
		GBC.gridx = 5;

		this.add(bFiltreNonConfirme, GBC);
		
		GBC.gridx = 6;

		this.add(bFiltreHorsDelais, GBC);

		GBC.insets = new Insets(10, 10, 10, 10);
		GBC.gridx = 3;
		GBC.gridy++;
		GBC.fill = GridBagConstraints.VERTICAL; 
		
		this.add(bActionRes, GBC);
		
		GBC.gridx = 7;
		GBC.gridwidth = 5;
		
		this.add(bActionFor, GBC);
		
		GBC.gridy++;
		GBC.gridx = 0;
		GBC.gridheight = 1;
		GBC.gridwidth = 12;
		
		this.add(bRetour, GBC);
		
		bActionFor.addActionListener(this);
		bActionRes.addActionListener(this);
		bRetour.addActionListener(this);
		bFiltreTous.addActionListener(this);
		bFiltreConfirme.addActionListener(this);
		bFiltreNonConfirme.addActionListener(this);
		bFiltreHorsDelais.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		RechercheReservation metierRechercheReservation = new RechercheReservation();
		if(o.equals(bRetour)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelSaisieClient(frame));
			frame.validate();
		} else if(o.equals(bFiltreTous)){
			listeReservations = metierRechercheReservation.listerReservationsPourUnClient(client.getId(), null);
			modelRes.removeAllElements();
			for(Reservation res : listeReservations){
				modelRes.addElement(res);
			}
			bFiltreTous.setBorder(BorderFactory.createLoweredBevelBorder());
			bFiltreConfirme.setBorder(null);
			bFiltreNonConfirme.setBorder(null);
			bFiltreHorsDelais.setBorder(null);
		} else if(o.equals(bFiltreConfirme)){
			listeReservations = metierRechercheReservation.listerReservationsPourUnClient(client.getId(), Reservation.ETAT_CONFIRME);
			modelRes.removeAllElements();
			for(Reservation res : listeReservations){
				modelRes.addElement(res);
			}
			bFiltreTous.setBorder(null);
			bFiltreConfirme.setBorder(BorderFactory.createLoweredBevelBorder());
			bFiltreNonConfirme.setBorder(null);
			bFiltreHorsDelais.setBorder(null);
		} else if(o.equals(bFiltreNonConfirme)){
			listeReservations = metierRechercheReservation.listerReservationsPourUnClient(client.getId(), Reservation.ETAT_NON_CONFIRME);
			modelRes.removeAllElements();
			for(Reservation res : listeReservations){
				modelRes.addElement(res);
			}
			bFiltreTous.setBorder(null);
			bFiltreConfirme.setBorder(null);
			bFiltreNonConfirme.setBorder(BorderFactory.createLoweredBevelBorder());
			bFiltreHorsDelais.setBorder(null);
		} else if(o.equals(bFiltreHorsDelais)){
			listeReservations = metierRechercheReservation.listerReservationsPourUnClient(client.getId(), Reservation.ETAT_HORS_DELAIS);
			modelRes.removeAllElements();
			for(Reservation res : listeReservations){
				modelRes.addElement(res);
			}
			bFiltreTous.setBorder(null);
			bFiltreConfirme.setBorder(null);
			bFiltreNonConfirme.setBorder(null);
			bFiltreHorsDelais.setBorder(BorderFactory.createLoweredBevelBorder());
		}
	}
	
	
	
	private class RendererJListReservation implements ListCellRenderer<Reservation> {
		private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
		
		public RendererJListReservation() {
			
		}
		public Component getListCellRendererComponent(JList<? extends Reservation> jlist, Reservation reservations,
				int index, boolean isSelected, boolean cellHasFocus) {
			JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(jlist, reservations, index, isSelected, cellHasFocus);
			SimpleDateFormat formatterDeb = new SimpleDateFormat("' Le 'dd/MM/yyyy' de 'HH'h '");
			SimpleDateFormat formatterFin = new SimpleDateFormat("HH");
			Date dateReservation = null;
			int duree;
			
			dateReservation = reservations.getDate();
			duree = reservations.getPlage();
			
			String newText=(reservations.getSalle().getTypeSalle().equals("petite")?"Petite salle":(reservations.getSalle().getTypeSalle().equals("grande")?"Grande salle":"Salle equipee"))
					+ (formatterDeb.format(dateReservation) 
							+ "a " + (Integer.parseInt(formatterFin.format(dateReservation))+duree) + "h ");
			
			renderer.setText(newText);
			if(reservations.getEstPaye()){
				renderer.setBackground(isSelected ?  new Color(0, 200, 200) : Color.CYAN);
			} else if(reservations.getDate().getTime() - reservations.getDateCreation().getTime() >= 7*24*60*60*1000){
				renderer.setBackground(isSelected ?  new Color(200, 0, 0) : Color.RED);
			}else{
				renderer.setBackground(isSelected ?  Color.YELLOW : Color.ORANGE);
			}
			return renderer;
		}
		
	}

}
