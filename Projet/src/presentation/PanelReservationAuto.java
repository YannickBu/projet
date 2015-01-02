package presentation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import metier.RechercheReservation;

import com.toedter.calendar.JDateChooser;

import donnee.Reservation;

/**
 * Panel permettant de trouver une proposition de reservation selon le type de salle,
 * la tranche de la journee, la date et la duree de la reservation
 */
public class PanelReservationAuto extends JPanel implements ActionListener {

	private static final int DUREE_MAX_MATIN = 15;
	private static final int DUREE_MAX_APRES_MIDI = 11;
	private static final int DUREE_MAX_SOIR = 4;
	
	private JFrame frame;
	
	private JDateChooser jdDateChooser;
	
	private ButtonGroup bgChoixSalle;
	private JRadioButton rbPetiteSalle;
	private JRadioButton rbGrandeSalle;
	private JRadioButton rbSalleEquipee;
	
	private JTextField tfNbSemaines;
	
	private ButtonGroup choixTranche;
	private JRadioButton rbMatin;
	private JRadioButton rbApresMidi;
	private JRadioButton rbSoir;
	
	private JComboBox cbDuree;
	
	private JButton bRechercher;
	private JButton bRetourMenu;
	private JButton bAccepter;

	private Reservation creneauPropose;
	private List<Reservation> listeReservations;
	private JList<Reservation> jListeReservations;
	private DefaultListModel<Reservation> modelReservation;

	private JPanel panelRecherche;
	private JPanel panelCENTERInterneResultat;
	
	/**
	 * Methode qui permet d'afficher les reservations 
	 * @param frame
	 */
	public PanelReservationAuto(JFrame frame) {
		GridBagConstraints GBC = new GridBagConstraints();
		
		this.frame = frame;

		this.setLayout(new GridBagLayout());
		panelRecherche = new JPanel();
		panelRecherche.setLayout(new GridBagLayout());
		panelRecherche.setBackground(Color.LIGHT_GRAY);
		//panelRecherche.setPreferredSize(new Dimension(400, 150));
		panelCENTERInterneResultat = new JPanel();
		panelCENTERInterneResultat.setLayout(new GridBagLayout());
		panelCENTERInterneResultat.setPreferredSize(new Dimension(300, 150));
		
		bRechercher = new JButton("Rechercher");
		bRetourMenu = new JButton("Retour");
		bAccepter = new JButton("Accepter");
		bRechercher.setBackground(Color.WHITE);
		bRetourMenu.setBackground(Color.WHITE);
		bAccepter.setBackground(Color.WHITE);
		
		jdDateChooser = new JDateChooser();
		jdDateChooser.setPreferredSize(new Dimension(120, 25));
		
		//Radio Button salles
		bgChoixSalle = new ButtonGroup();
		rbPetiteSalle = new JRadioButton("Petite salle");
		rbGrandeSalle = new JRadioButton("Grande salle");
		rbSalleEquipee = new JRadioButton("Salle equipee");
		rbPetiteSalle.setBackground(Color.LIGHT_GRAY);
		rbGrandeSalle.setBackground(Color.LIGHT_GRAY);
		rbSalleEquipee.setBackground(Color.LIGHT_GRAY);
		rbPetiteSalle.setSelected(true);
		bgChoixSalle.add(rbPetiteSalle);
		bgChoixSalle.add(rbGrandeSalle);
		bgChoixSalle.add(rbSalleEquipee);
		
		tfNbSemaines = new JTextField("1");
		tfNbSemaines.setPreferredSize(new Dimension(35, 23));
		
		//Radio Button tranches
		choixTranche = new ButtonGroup();
		rbMatin = new JRadioButton("9h - 13h");
		rbApresMidi = new JRadioButton("13h - 20h");
		rbSoir = new JRadioButton("20h - 00h");
		rbMatin.setBackground(Color.LIGHT_GRAY);
		rbApresMidi.setBackground(Color.LIGHT_GRAY);
		rbSoir.setBackground(Color.LIGHT_GRAY);
		rbMatin.setSelected(true);
		choixTranche.add(rbMatin);
		choixTranche.add(rbApresMidi);
		choixTranche.add(rbSoir);
		
		//Combo Box duree
		cbDuree = new JComboBox();
		cbDuree.removeAllItems();
		for(int i=1; i<=DUREE_MAX_MATIN; i++){
			cbDuree.addItem(i+"h");
		}
		
		modelReservation = new DefaultListModel<Reservation>();
		jListeReservations = new JList<Reservation>(modelReservation);
		jListeReservations.setCellRenderer(new RendererJListReservation());
		
		rbMatin.addActionListener(this);
		rbApresMidi.addActionListener(this);
		rbSoir.addActionListener(this);
		bRetourMenu.addActionListener(this);
		bRechercher.addActionListener(this);
		bAccepter.addActionListener(this);
		
		GBC.fill = GridBagConstraints.BOTH; 
		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridy = 0;
		GBC.gridx = 0;
		GBC.gridheight = 1;
		GBC.gridwidth = 1;
		GBC.weightx = 1;
		GBC.weighty = 1;
		JLabel lTitreHaut = new JLabel("Reservation automatique",JLabel.CENTER);
		lTitreHaut.setOpaque(true);
		lTitreHaut.setBackground(Color.GRAY);
		this.add(lTitreHaut,GBC);

		GBC.fill = GridBagConstraints.NONE; 
		GBC.anchor = GridBagConstraints.WEST;
		GBC.insets = new Insets(10, 10, 0, 0);
		panelRecherche.add(rbPetiteSalle,GBC);
		GBC.gridy++;
		GBC.insets = new Insets(0, 10, 0, 0);
		panelRecherche.add(rbGrandeSalle,GBC);
		GBC.gridy++;
		GBC.insets = new Insets(0, 10, 0, 0);
		panelRecherche.add(rbSalleEquipee,GBC);

		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridy = 0;
		GBC.gridx++;
		GBC.gridwidth = 3;
		GBC.insets = new Insets(10, 0, 0, 0);
		panelRecherche.add(jdDateChooser,GBC);

		GBC.gridy++;
		GBC.gridwidth = 2;
		GBC.gridheight = 2;
		GBC.insets = new Insets(0, 0, 0, 0);
		panelRecherche.add(new JLabel("Nb semaines ",JLabel.RIGHT),GBC);

		GBC.anchor = GridBagConstraints.WEST;
		GBC.gridx+=2;
		GBC.gridwidth = 1;
		panelRecherche.add(tfNbSemaines,GBC);
		
		GBC.gridy = 0;
		GBC.gridx++;
		GBC.gridheight = 1;
		GBC.insets = new Insets(10, 0, 0, 10);
		panelRecherche.add(rbMatin,GBC);
		GBC.gridy++;
		GBC.insets = new Insets(0, 0, 0, 10);
		panelRecherche.add(rbApresMidi,GBC);
		GBC.gridy++;
		GBC.insets = new Insets(0, 0, 0, 10);
		panelRecherche.add(rbSoir,GBC);

		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridy = 1;
		GBC.gridx++;
		GBC.insets = new Insets(0, 0, 10, 0);
		panelRecherche.add(cbDuree,GBC);
		
		GBC.gridy = 4;
		GBC.gridx = 0;
		GBC.gridwidth = 6;
		GBC.insets = new Insets(0, 0, 10, 0);
		panelRecherche.add(bRechercher,GBC);
		
		GBC.fill = GridBagConstraints.HORIZONTAL; 
		GBC.gridy = 1;
		GBC.gridwidth = 1;
		this.add(panelRecherche,GBC);

		GBC.fill = GridBagConstraints.VERTICAL; 
		GBC.gridy++;
		this.add(panelCENTERInterneResultat,GBC);

		GBC.fill = GridBagConstraints.NONE; 
		GBC.gridy++;
		this.add(bRetourMenu,GBC);
	}
	
	/**
	 * Recherche et affichage dune proposition pour les criteres renseignes
	 */
	public void afficherUneProposition(){
		SimpleDateFormat formatterDateSaisie = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formatterDeb = new SimpleDateFormat("'Le 'dd/MM/yyyy' de 'HH'h '");
		SimpleDateFormat formatterFin = new SimpleDateFormat("HH");
		GridBagConstraints GBC = new GridBagConstraints();
		RechercheReservation metier = new RechercheReservation();
		Date dateReservation = null;
		int duree;
		
		if(jdDateChooser.getDate()==null){
			JOptionPane.showMessageDialog(this, "Date non valide !");
			return;
		}
		
		//Recherche dune proposition pour la date, duree, tranche et type de salle saisis
		creneauPropose = metier.rechercheCreneauLibre(
				formatterDateSaisie.format(jdDateChooser.getDate()), 
				Integer.parseInt(((String)cbDuree.getSelectedItem()).substring(0, 1)), 
				rbMatin.isSelected()?"matin":(rbApresMidi.isSelected()?"apres-midi":"soir"), 
				rbPetiteSalle.isSelected()?"petite":(rbGrandeSalle.isSelected()?"grande":"equipee"));
		panelCENTERInterneResultat.removeAll();
		
		//sil existe une proposition repondant aux criteres
		if(creneauPropose!=null){
			dateReservation = creneauPropose.getDate();
			duree = creneauPropose.getPlage();
			GBC.fill = GridBagConstraints.NONE; 
			GBC.anchor = GridBagConstraints.SOUTH;
			GBC.gridy = 0;
			GBC.gridx = 0;
			GBC.gridheight = 1;
			GBC.gridwidth = 1;
			GBC.weightx = 1;
			GBC.weighty = 1;
			panelCENTERInterneResultat.add(new JLabel("Proposition : " 
					+ formatterDeb.format(dateReservation) 
					+ "a " + (Integer.parseInt(formatterFin.format(dateReservation))+duree) + "h"),GBC);
			GBC.anchor = GridBagConstraints.NORTH;
			GBC.gridy++;
			GBC.insets = new Insets(10, 0, 0, 0);
			panelCENTERInterneResultat.add(bAccepter,GBC);
			bRechercher.setAlignmentX(Component.CENTER_ALIGNMENT);
		} else {
			GBC.fill = GridBagConstraints.NONE; 
			GBC.anchor = GridBagConstraints.CENTER;
			GBC.gridy = 0;
			GBC.gridx = 0;
			GBC.gridheight = 1;
			GBC.gridwidth = 1;
			GBC.weightx = 1;
			GBC.weighty = 1;
			panelCENTERInterneResultat.add(new JLabel("Aucun creneau libre a cette date"),GBC);
		}
		this.repaint();
		this.validate();
	}
	
	/**
	 * Recherche et affichage dune proposition sur plusieurs semaines
	 * @param nbSemaines
	 */
	public void afficherUneProposition(int nbSemaines){
		SimpleDateFormat formatterDateSaisie = new SimpleDateFormat("dd-MM-yyyy");
		GridBagConstraints GBC = new GridBagConstraints();
		RechercheReservation metier = new RechercheReservation();
		creneauPropose=null;
		
		//Recherche dune proposition pour la date, duree, tranche et type de salle saisis
		listeReservations = metier.rechercheCreneauLibre(
				formatterDateSaisie.format(jdDateChooser.getDate()), 
				Integer.parseInt(((String)cbDuree.getSelectedItem()).substring(0, 1)), 
				rbMatin.isSelected()?"matin":(rbApresMidi.isSelected()?"apres-midi":"soir"), 
				rbPetiteSalle.isSelected()?"petite":(rbGrandeSalle.isSelected()?"grande":"equipee"),
						nbSemaines);
		panelCENTERInterneResultat.removeAll();
		
		//sil existe une proposition repondant aux criteres
		if(listeReservations!=null && listeReservations.size()>1){
			modelReservation.removeAllElements();
			for(Reservation res : listeReservations){
				modelReservation.addElement(res);
			}
			
			GBC.fill = GridBagConstraints.BOTH; 
			GBC.anchor = GridBagConstraints.SOUTH;
			GBC.gridy = 0;
			GBC.gridx = 0;
			GBC.gridheight = 1;
			GBC.gridwidth = 1;
			GBC.weightx = 1;
			GBC.weighty = 1;
			JScrollPane scrollPane = new JScrollPane(jListeReservations);
			panelCENTERInterneResultat.add(scrollPane,GBC);
			GBC.anchor = GridBagConstraints.NORTH;
			GBC.gridy++;
			GBC.gridx = 0;
			GBC.gridwidth = 2;
			GBC.weightx=0.1;
			GBC.weighty=0.1;
			GBC.insets = new Insets(10, 0, 0, 0);
			GBC.fill = GridBagConstraints.NONE; 
			panelCENTERInterneResultat.add(bAccepter,GBC);
			bRechercher.setAlignmentX(Component.CENTER_ALIGNMENT);
		} else {
			GBC.fill = GridBagConstraints.NONE; 
			GBC.anchor = GridBagConstraints.CENTER;
			GBC.gridy = 0;
			GBC.gridx = 0;
			GBC.gridheight = 1;
			GBC.gridwidth = 1;
			GBC.weightx = 1;
			GBC.weighty = 1;
			panelCENTERInterneResultat.add(new JLabel("Aucun creneau libre a cette date"),GBC);
		}
		this.repaint();
		this.validate();
	}
	
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o.equals(bRetourMenu)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelMenu(frame));
			frame.validate();
		} else if(o.equals(rbMatin)){
			cbDuree.removeAllItems();
			for(int i=1; i<=DUREE_MAX_MATIN; i++){
				cbDuree.addItem(i+"h");
			}
		} else if(o.equals(rbApresMidi)){
			cbDuree.removeAllItems();
			for(int i=1; i<=DUREE_MAX_APRES_MIDI; i++){
				cbDuree.addItem(i+"h");
			}
		} else if(o.equals(rbSoir)){
			cbDuree.removeAllItems();
			for(int i=1; i<=DUREE_MAX_SOIR; i++){
				cbDuree.addItem(i+"h");
			}
		} else if(o.equals(bRechercher)){
			if(jdDateChooser.getDate()==null){
				JOptionPane.showMessageDialog(this, "Date non valide !");
				return;
			}
			try{
				int nbSemaines = Integer.parseInt(tfNbSemaines.getText());
				if(nbSemaines < 1){
					throw new NumberFormatException();
				}
				if(nbSemaines==1){
					afficherUneProposition();
				} else {
					afficherUneProposition(nbSemaines);
				}
			} catch(NumberFormatException nfe){
				JOptionPane.showMessageDialog(this, "Nombre de semaines non valide !");
				return;
			}
		} else if(o.equals(bAccepter)){
			frame.getContentPane().removeAll();
			if(listeReservations==null || listeReservations.size()==0){
				listeReservations = new ArrayList<Reservation>();
				listeReservations.add(creneauPropose);
			}
			frame.getContentPane().add(new PanelValidationReservation(frame, listeReservations));
			frame.validate();
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
			
			String newText=(formatterDeb.format(dateReservation) 
							+ "a " + (Integer.parseInt(formatterFin.format(dateReservation))+duree) + "h ");
			
			renderer.setText(newText);
			renderer.setHorizontalAlignment(JLabel.CENTER);
			renderer.setBorder(null);
			renderer.setBackground(Color.WHITE);
			return renderer;
		}
		
	}
	
}
