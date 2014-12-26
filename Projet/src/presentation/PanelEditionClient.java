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

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import metier.RechercheReservation;
import donnee.Client;
import donnee.ForfaitClient;
import donnee.Reservation;

public class PanelEditionClient extends JPanel implements ActionListener {
	
	private JFrame frame;
	
	private Client client;
	
	private JLabel lClientTitre;
	
	private JButton bActionRes;
	private JButton bActionFor;
	private JButton bRetour;
	
	private JList<List<Reservation>> jListeReservations;
	private JList<ForfaitClient> jListeForfaits;
	private DefaultListModel<List<Reservation>> modelRes;
	private DefaultListModel<ForfaitClient> modelFor;
	private List<List<Reservation>> listeReservations;
	
	public PanelEditionClient(JFrame frame, Client client) {
		RechercheReservation metierRechercheReservation = new RechercheReservation();
		
		JLabel lRes = new JLabel("Vos reservations", JLabel.CENTER);
		JLabel lFor = new JLabel("Vos forfaits", JLabel.CENTER);
		JLabel lFiltre = new JLabel("Filtre", JLabel.CENTER);
		
		bActionRes = new JButton("Valider");
		bActionFor = new JButton("Acheter un forfait");
		bRetour = new JButton("Retour");
		bActionRes.setBackground(Color.WHITE);
		bActionFor.setBackground(Color.WHITE);
		bRetour.setBackground(Color.WHITE);
		
		modelRes = new DefaultListModel<List<Reservation>>();
		modelFor = new DefaultListModel<ForfaitClient>();
		jListeReservations = new JList<List<Reservation>>(modelRes);
		jListeForfaits = new JList<ForfaitClient>(modelFor);
		jListeReservations.setCellRenderer(new RendererJListReservation());
		listeReservations = metierRechercheReservation.listerReservationsPourUnClient(2);//TODO client.getId());
		for(List<Reservation> res : listeReservations){
			modelRes.addElement(res);
		}
		
		this.frame = frame;
		this.client = client;
		
		GridBagLayout GBL = new GridBagLayout();
		GridBagConstraints GBC = null;
		this.setLayout(GBL);
		
		lClientTitre = new JLabel("Client (xx points)", JLabel.CENTER);
		
		GBC = new GridBagConstraints();
		GBC.fill = GridBagConstraints.BOTH; 
		GBC.anchor = GridBagConstraints.NORTH;
		GBC.gridy = 0;
		GBC.gridx = 4; //TODO plus grand?
		GBC.gridheight = 1;
		GBC.gridwidth = 4;//5 6 ?
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
		GBC.gridwidth = 7;
		GBC.insets = new Insets(0, 0, 0, 0);
		GBC.weightx = 0.5;
		GBC.weighty = 0.01;

		lFiltre.setOpaque(true);
		lFiltre.setBackground(Color.LIGHT_GRAY);
		this.add(lFiltre, GBC);
		
		GBC.gridy++;
		GBC.fill = GridBagConstraints.VERTICAL; 
		
		this.add(bActionRes, GBC);
		
		GBC.gridx = 7;
		GBC.gridwidth = 5;
		
		this.add(bActionFor, GBC);
		
		GBC.gridy++;
		GBC.gridx = 4; //TODO plus grand?
		GBC.gridheight = 1;
		GBC.gridwidth = 4;
		
		this.add(bRetour, GBC);
		
		bActionFor.addActionListener(this);
		bActionRes.addActionListener(this);
		bRetour.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o.equals(bRetour)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelMenu(frame));
			frame.validate();
		}
	}
	
	
	
	private class RendererJListReservation implements ListCellRenderer<List<Reservation>> {
		private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
		
		public RendererJListReservation() {
			
		}
		public Component getListCellRendererComponent(JList<? extends List<Reservation>> jlist, List<Reservation> reservations,
				int index, boolean isSelected, boolean cellHasFocus) {
			JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(jlist, reservations, index, isSelected, cellHasFocus);
			SimpleDateFormat formatterDeb = new SimpleDateFormat("' Le 'dd/MM/yyyy' de 'HH'h '");
			SimpleDateFormat formatterFin = new SimpleDateFormat("HH");
			Date dateReservationDebut = null;
			Date dateReservationFin = null;
			int duree;
			
			dateReservationDebut = reservations.get(0).getDate();
			dateReservationFin = reservations.get(reservations.size()-1).getDate();
			duree = reservations.get(reservations.size()-1).getPlage();
			
			String newText=(reservations.get(0).getSalle().getTypeSalle().equals("petite")?"Petite salle":(reservations.get(0).getSalle().getTypeSalle().equals("grande")?"Grande salle":"Salle equipee"))
					+ (formatterDeb.format(dateReservationDebut) 
							+ "a " + (Integer.parseInt(formatterFin.format(dateReservationFin))+duree) + "h ");
			
			renderer.setText(newText);
		    renderer.setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
			return renderer;
		}
		
	}

}
