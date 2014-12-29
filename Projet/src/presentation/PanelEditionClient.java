package presentation;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import metier.ModifierReservation;
import metier.RechercheReservation;
import metier.RechercherForfait;
import metier.RechercherForfaitClient;
import metier.SupprimerReservation;
import metier.VendreForfait;
import donnee.Client;
import donnee.Forfait;
import donnee.ForfaitClient;
import donnee.Reservation;

/**
 * Panel permettant la visualisation des forfaits et reservations dun client
 * Permet la confirmation dune reservation non confirmee
 * ainsi que la suppression dune reservation hors delais
 */
public class PanelEditionClient extends JPanel implements ActionListener, ListSelectionListener {
	
	private JFrame frame;
	
	private Client client;
	
	private JPanel panelForfait;
	
	private JLabel lClientTitre;
	JLabel lRes;
	JLabel lFor;
	
	private JButton bActionRes;
	private JButton bActionFor;
	private JButton bRetour;
	private JButton bFiltreTous;
	private JButton bFiltreNonConfirme;
	private JButton bFiltreConfirme;
	private JButton bFiltreHorsDelais;
	private JButton bAcheterForfait;
	
	private JComboBox<Forfait> cbForfait;
	private JComboBox<String> cbTypeSalle;
	
	private JList<Reservation> jListeReservations;
	private JList<ForfaitClient> jListeForfaitClient;
	private JList<Forfait> jListeForfait;
	private DefaultListModel<Reservation> modelReservation;
	private DefaultListModel<ForfaitClient> modelForfaitClient;
	private DefaultListModel<Forfait> modelForfait;
	private List<Reservation> listeReservations;
	private List<ForfaitClient> listeForfaitClient;
	private List<Forfait> listeForfait;
	
	public PanelEditionClient(JFrame frame, Client client) {
		
		panelForfait = new JPanel();
		panelForfait.setLayout(new GridLayout(1,1));
		
		lRes = new JLabel("Vos reservations", JLabel.CENTER);
		lFor = new JLabel("Vos forfaits", JLabel.CENTER);
		JLabel lFiltre = new JLabel("Filtre", JLabel.CENTER);
		
		cbForfait = new JComboBox<Forfait>();
		cbForfait.setRenderer(new RendererCBForfait());
		
		cbTypeSalle = new JComboBox<String>();
		cbTypeSalle.addItem("Petite salle");
		cbTypeSalle.addItem("Grande salle");
		
		bActionRes = new JButton("Valider");
		bActionFor = new JButton("Acheter un forfait");
		bRetour = new JButton("Retour");
		bFiltreTous = new JButton("Tous");
		bFiltreConfirme = new JButton("C");
		bFiltreNonConfirme = new JButton("NC");
		bFiltreHorsDelais = new JButton("HD");
		bAcheterForfait = new JButton("Acheter");
		bActionRes.setBackground(Color.WHITE);
		bActionFor.setBackground(Color.WHITE);
		bRetour.setBackground(Color.WHITE);
		bFiltreTous.setBackground(Color.WHITE);
		bFiltreConfirme.setBackground(Color.CYAN);
		bFiltreNonConfirme.setBackground(Color.ORANGE);
		bFiltreHorsDelais.setBackground(Color.RED);
		bFiltreTous.setBorder(BorderFactory.createLoweredBevelBorder());
		bAcheterForfait.setBackground(Color.WHITE);
		
		modelReservation = new DefaultListModel<Reservation>();
		modelForfaitClient = new DefaultListModel<ForfaitClient>();
		modelForfait = new DefaultListModel<Forfait>();
		jListeReservations = new JList<Reservation>(modelReservation);
		jListeForfaitClient = new JList<ForfaitClient>(modelForfaitClient);
		jListeForfait = new JList<Forfait>(modelForfait);
		jListeReservations.setCellRenderer(new RendererJListReservation());
		jListeForfaitClient.setCellRenderer(new RendererJListForfaitClient());
		jListeForfait.setCellRenderer(new RendererJListForfait());
		listeReservations = new RechercheReservation().listerReservationsPourUnClient(client.getId(), null);
		listeForfaitClient = new RechercherForfaitClient().listerPourUnClient(client.getId());
		listeForfait = new RechercherForfait().lister();
		
		for(Reservation res : listeReservations){
			modelReservation.addElement(res);
		}
		for(ForfaitClient forfaitClient : listeForfaitClient){
			modelForfaitClient.addElement(forfaitClient);
		}
		for(Forfait forfait : listeForfait){
			modelForfait.addElement(forfait);
			cbForfait.addItem(forfait);
		}
			
		jListeReservations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jListeForfaitClient.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		this.frame = frame;
		this.client = client;
		
		GridBagLayout GBL = new GridBagLayout();
		GridBagConstraints GBC = null;
		this.setLayout(GBL);
		
		lClientTitre = new JLabel(client.getNom()+" "+client.getPrenom()+" ("+client.getPointsFidelite()+" point"+(client.getPointsFidelite()>1?"s":"")+")", JLabel.CENTER);//TODO pt fid
		
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
		lRes.setBackground(Color.GRAY);
		this.add(lRes, GBC);
		
		GBC.gridx = 7;
		GBC.gridwidth = 5;

		lFor.setOpaque(true);
		lFor.setBackground(Color.GRAY);
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
		
		panelForfait.add(jListeForfaitClient);
		this.add(panelForfait, GBC);

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
		GBC.gridx = 0;
		GBC.gridy++;
		GBC.gridwidth = 7;
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
		
		bActionRes.setEnabled(false);
		bActionRes.setText("Selectionner une reservation");
		
		bActionFor.addActionListener(this);
		bActionRes.addActionListener(this);
		bRetour.addActionListener(this);
		bFiltreTous.addActionListener(this);
		bFiltreConfirme.addActionListener(this);
		bFiltreNonConfirme.addActionListener(this);
		bFiltreHorsDelais.addActionListener(this);
		bAcheterForfait.addActionListener(this);
		jListeReservations.addListSelectionListener(this);
		cbForfait.addActionListener(this);
	}

	/**
	 * Genere le panel dachat des forfaits
	 */
	private void genererPanelAchatForfaits() {
		GridBagConstraints GBC = null;
		JLabel label = null;
		panelForfait.removeAll();
		panelForfait.setLayout(new GridBagLayout());
		
		GBC = new GridBagConstraints();
		GBC.fill = GridBagConstraints.BOTH; 
		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridy = 0;
		GBC.gridx = 0;
		GBC.gridheight = 1;
		GBC.gridwidth = 1;
		GBC.weightx = 1;
		GBC.weighty = 1;
		GBC.insets = new Insets(0, 2, 0, 0);
		label = new JLabel("Forfait", JLabel.CENTER);
		label.setOpaque(true);
		label.setBackground(Color.LIGHT_GRAY);
		panelForfait.add(label, GBC);
		GBC.insets = new Insets(0, 0, 0, 0);
		GBC.gridx++;
		label = new JLabel("Validite", JLabel.CENTER);
		label.setOpaque(true);
		label.setBackground(Color.LIGHT_GRAY);
		panelForfait.add(label, GBC);
		GBC.gridx++;
		label = new JLabel("Petites salles", JLabel.CENTER);
		label.setOpaque(true);
		label.setBackground(Color.LIGHT_GRAY);
		panelForfait.add(label, GBC);
		GBC.gridx++;
		GBC.insets = new Insets(0, 0, 0, 2);
		label = new JLabel("Grandes salles", JLabel.CENTER);
		label.setOpaque(true);
		label.setBackground(Color.LIGHT_GRAY);
		panelForfait.add(label, GBC);
		
		GBC.insets = new Insets(2, 2, 2, 2);
		
		for(Forfait forfait : listeForfait){
			GBC.gridy++;
			GBC.gridx = 0;
			label = new JLabel(forfait.getTypeForfait(), JLabel.CENTER);
			label.setOpaque(true);
			label.setBackground(Color.WHITE);
			panelForfait.add(label, GBC);
			GBC.gridx++;
			label = new JLabel(Integer.toString(forfait.getValidite())+" mois", JLabel.CENTER);
			label.setOpaque(true);
			label.setBackground(Color.WHITE);
			panelForfait.add(label, GBC);
			GBC.gridx++;
			label = new JLabel(Integer.toString(forfait.getPrixPetitesSalles())+" euros", JLabel.CENTER);
			label.setOpaque(true);
			label.setBackground(Color.WHITE);
			panelForfait.add(label, GBC);
			GBC.gridx++;
			label = new JLabel(Integer.toString(forfait.getPrixGrandesSalles())+" euros", JLabel.CENTER);
			label.setOpaque(true);
			label.setBackground(Color.WHITE);
			panelForfait.add(label, GBC);
		}
		
		GBC.gridy++;
		GBC.gridx=0;
		GBC.gridwidth=2;
		GBC.weightx = 0.1;
		GBC.weighty = 0.1;
		GBC.fill=GridBagConstraints.VERTICAL;
		GBC.anchor = GridBagConstraints.EAST;
		
		panelForfait.add(cbForfait, GBC);
		
		GBC.gridx=2;
		GBC.gridwidth=1;
		
		panelForfait.add(cbTypeSalle, GBC);

		GBC.gridx=3;
		
		panelForfait.add(bAcheterForfait, GBC);
		
		lFor.setText("Forfaits disponibles");
		bActionFor.setText("Lister les forfaits");
		panelForfait.validate();
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
			modelReservation.removeAllElements();
			for(Reservation res : listeReservations){
				modelReservation.addElement(res);
			}
			bFiltreTous.setBorder(BorderFactory.createLoweredBevelBorder());
			bFiltreConfirme.setBorder(null);
			bFiltreNonConfirme.setBorder(null);
			bFiltreHorsDelais.setBorder(null);
		} else if(o.equals(bFiltreConfirme)){
			listeReservations = metierRechercheReservation.listerReservationsPourUnClient(client.getId(), Reservation.ETAT_CONFIRME);
			modelReservation.removeAllElements();
			for(Reservation res : listeReservations){
				modelReservation.addElement(res);
			}
			bFiltreTous.setBorder(null);
			bFiltreConfirme.setBorder(BorderFactory.createLoweredBevelBorder());
			bFiltreNonConfirme.setBorder(null);
			bFiltreHorsDelais.setBorder(null);
		} else if(o.equals(bFiltreNonConfirme)){
			listeReservations = metierRechercheReservation.listerReservationsPourUnClient(client.getId(), Reservation.ETAT_NON_CONFIRME);
			modelReservation.removeAllElements();
			for(Reservation res : listeReservations){
				modelReservation.addElement(res);
			}
			bFiltreTous.setBorder(null);
			bFiltreConfirme.setBorder(null);
			bFiltreNonConfirme.setBorder(BorderFactory.createLoweredBevelBorder());
			bFiltreHorsDelais.setBorder(null);
		} else if(o.equals(bFiltreHorsDelais)){
			listeReservations = metierRechercheReservation.listerReservationsPourUnClient(client.getId(), Reservation.ETAT_HORS_DELAIS);
			modelReservation.removeAllElements();
			for(Reservation res : listeReservations){
				modelReservation.addElement(res);
			}
			bFiltreTous.setBorder(null);
			bFiltreConfirme.setBorder(null);
			bFiltreNonConfirme.setBorder(null);
			bFiltreHorsDelais.setBorder(BorderFactory.createLoweredBevelBorder());
		} else if(o.equals(bActionRes)){
			if(bActionRes.getText().equals("Supprimer")){
				SupprimerReservation metierSupprRes = new SupprimerReservation();
				metierSupprRes.supprimerReservation(jListeReservations.getSelectedValue().getIdReserv());
				modelReservation.remove(jListeReservations.getSelectedIndex());
				JOptionPane.showMessageDialog(this, "Reservation hors delais supprimee");
			} else {
				ModifierReservation metierModifRes = new ModifierReservation();
				Reservation resNewDonnees = new Reservation();
				resNewDonnees.setClient(jListeReservations.getSelectedValue().getClient());
				resNewDonnees.setDate(jListeReservations.getSelectedValue().getDate());
				resNewDonnees.setDateCreation(jListeReservations.getSelectedValue().getDateCreation());
				resNewDonnees.setEstPaye(true);
				resNewDonnees.setIdReserv(jListeReservations.getSelectedValue().getIdReserv());
				resNewDonnees.setPlage(jListeReservations.getSelectedValue().getPlage());
				resNewDonnees.setSalle(jListeReservations.getSelectedValue().getSalle());
				metierModifRes.payer(resNewDonnees);
				modelReservation.remove(jListeReservations.getSelectedIndex());
			}
		} else if(o.equals(bActionFor)){
			if(bActionFor.getText().equals("Acheter un forfait")){
				genererPanelAchatForfaits();
			} else {
				panelForfait.removeAll();
				panelForfait.setLayout(new GridLayout(1,1));
				panelForfait.add(jListeForfaitClient);
				lFor.setText("Vos forfaits");
				bActionFor.setText("Acheter un forfait");
			}
		} else if(o.equals(bAcheterForfait)){
			int rep = JOptionPane.showConfirmDialog(
					this,
					"Achat forfait "+((Forfait)cbForfait.getSelectedItem()).getTypeForfait()
						+" - "+cbTypeSalle.getSelectedItem()+" : "
						+("Petite salle".equals(cbTypeSalle.getSelectedItem())
								?((Forfait)cbForfait.getSelectedItem()).getPrixPetitesSalles():((Forfait)cbForfait.getSelectedItem()).getPrixGrandesSalles())+" euros",
				    "Valider achat",
				    JOptionPane.YES_NO_OPTION);
			if(!(rep==JOptionPane.OK_OPTION)){
				return;
			}
			new VendreForfait().vendreForfait(client.getId(), ("Petite salle".equals(cbTypeSalle.getSelectedItem())?0:1), ((Forfait)cbForfait.getSelectedItem()).getTypeForfait());
			JOptionPane.showMessageDialog(this, "Achat valide");
			listeForfaitClient = new RechercherForfaitClient().listerPourUnClient(client.getId());
			for(ForfaitClient forfaitClient : listeForfaitClient){
				modelForfaitClient.addElement(forfaitClient);
			}
		}
	}

	
	public void valueChanged(ListSelectionEvent e) {
		Object o = e.getSource();
		if(o.equals(jListeReservations)){
			if(jListeReservations.getSelectedValue()!=null 
					&& !jListeReservations.getSelectedValue().getEstPaye()){
					bActionRes.setEnabled(true);
					if(new Date().getTime() - jListeReservations.getSelectedValue().getDateCreation().getTime() >= 7*24*60*60*1000){
						bActionRes.setText("Supprimer");
					} else {
						bActionRes.setText("Confirmer");
					}
			}else{
				bActionRes.setEnabled(false);
				bActionRes.setText("Selectionner une reservation");
			}
		} else if(o.equals(jListeForfaitClient)){
			
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
			Date dateReservation = reservations.getDate();
			int duree = reservations.getPlage();
			
			String newText=(reservations.getSalle().getTypeSalle().getTypeSalle().equals("petite")?"Petite salle":(reservations.getSalle().getTypeSalle().getTypeSalle().equals("grande")?"Grande salle":"Salle equipee"))
					+ (formatterDeb.format(dateReservation) 
							+ "a " + (Integer.parseInt(formatterFin.format(dateReservation))+duree) + "h ");
			
			renderer.setText(newText);
			renderer.setHorizontalAlignment(JLabel.CENTER);
			if(reservations.getEstPaye()){
				renderer.setBackground(isSelected ?  new Color(0, 200, 200) : Color.CYAN);
			}else if(new Date().getTime() - reservations.getDateCreation().getTime() >= 7*24*60*60*1000){
				renderer.setBackground(isSelected ?  new Color(200, 0, 0) : Color.RED);
			}else{
				renderer.setBackground(isSelected ?  Color.YELLOW : Color.ORANGE);
			}
			return renderer;
		}
		
	}
	
	private class RendererJListForfaitClient implements ListCellRenderer<ForfaitClient> {
		private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
		
		public RendererJListForfaitClient() {
			
		}
		public Component getListCellRendererComponent(JList<? extends ForfaitClient> jlist, ForfaitClient forfaitClient,
				int index, boolean isSelected, boolean cellHasFocus) {
			JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(jlist, forfaitClient, index, isSelected, cellHasFocus);
			String newText="Forfait "+forfaitClient.getTypeSalle().getTypeSalle()+" salle - "+forfaitClient.getTempsRestant()+"h restante"+(forfaitClient.getTempsRestant()>1?"s":"");
			renderer.setText(newText);
			renderer.setHorizontalAlignment(JLabel.CENTER);
			renderer.setBackground(Color.WHITE);
			renderer.setBorder(null);
			return renderer;
		}
		
	}
	
	private class RendererJListForfait implements ListCellRenderer<Forfait> {
		private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
		
		public RendererJListForfait() {
			
		}
		public Component getListCellRendererComponent(JList<? extends Forfait> jlist, Forfait forfait,
				int index, boolean isSelected, boolean cellHasFocus) {
			JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(jlist, forfait, index, isSelected, cellHasFocus);
			String newText="Forfait "+forfait.getTypeForfait()+" Petite salle "+forfait.getPrixPetitesSalles()+" Gde salle "+forfait.getPrixGrandesSalles() + " Valide "+forfait.getValidite()+" mois";
			renderer.setText(newText);
			renderer.setHorizontalAlignment(JLabel.CENTER);

			return renderer;
		}
		
	}
	
	private class RendererCBForfait extends BasicComboBoxRenderer {
		
		public RendererCBForfait() {
			
		}
		@Override public Component getListCellRendererComponent(JList list, Object value,    int index,    boolean isSelected,    boolean cellHasFocus){
		        JLabel ret=(JLabel)super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
		        ret.setText("Forfait "+((Forfait)value).getTypeForfait());
		        return ret;
		}
	}
}
