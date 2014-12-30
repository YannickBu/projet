package presentation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import metier.ConfirmerReservation;
import metier.RechercheReservation;

import com.toedter.calendar.JDateChooser;

import donnee.Reservation;

/**
 * Panel permettant de trouver une proposition de reservation selon le type de salle,
 * la tranche de la journee, la date et la duree de la reservation
 */
public class PanelReservationAuto extends JPanel implements ActionListener {

	private static final String TRANCHE_SOIR = "20h - 00h";
	private static final String TRANCHE_APRES_MIDI = "13h - 20h";
	private static final String TRANCHE_MATIN = "9h - 13h";
	private static final int DUREE_MAX_MATIN = 15;
	private static final int DUREE_MAX_APRES_MIDI = 11;
	private static final int DUREE_MAX_SOIR = 4;
	
	private Reservation creneauPropose;
	
	private JFrame frame;
	
	private JDateChooser jdDateChooser;
	
	private JLabel lTranche;
	private JLabel lDuree;
	
	private ButtonGroup bgChoixSalle;
	private JRadioButton rbPetiteSalle;
	private JRadioButton rbGrandeSalle;
	private JRadioButton rbSalleEquipee;
	
	private ButtonGroup choixTranche;
	private JRadioButton rbMatin;
	private JRadioButton rbApresMidi;
	private JRadioButton rbSoir;
	
	private JComboBox cbDuree;
	
	private JButton bRechercher;
	private JButton bRetourMenu;
	private JButton bRetourRecherche;
	private JButton bAccepter;
	
	private JPanel panelNORTH = new JPanel();
	private JPanel panelCENTER = new JPanel();
	private JPanel panelSOUTH = new JPanel();
	private JPanel panelCENTERInterne = new JPanel();
	private JPanel panelCENTERInterneResultat = new JPanel();
	private JPanel panelRadioSalle = new JPanel();
	private JPanel panelRadioTranche = new JPanel();
	
	/**
	 * Methode qui permet d'afficher les reservations 
	 * @param frame
	 */
	public PanelReservationAuto(JFrame frame) {
		
		
		this.frame = frame;

		this.setLayout(new BorderLayout());
		panelNORTH.setLayout(new FlowLayout());
		panelCENTER.setLayout(new FlowLayout());
		panelSOUTH.setLayout(new FlowLayout());
		panelCENTERInterne.setLayout(new BoxLayout(panelCENTERInterne, BoxLayout.Y_AXIS));
		panelCENTERInterneResultat.setLayout(new FlowLayout());
		panelRadioSalle.setLayout(new BoxLayout(panelRadioSalle, 3));
		panelRadioTranche.setLayout(new BoxLayout(panelRadioTranche, 3));
		
		lTranche = new JLabel("Tranche ");
		lDuree = new JLabel("Duree ");
		
		bRechercher = new JButton("Rechercher");
		bRetourMenu = new JButton("Retour");
		bRetourRecherche = new JButton("Retour");
		bAccepter = new JButton("Accepter");
		
		jdDateChooser = new JDateChooser();
		jdDateChooser.setPreferredSize(new Dimension(120, 25));
		
		//Radio Button salles
		bgChoixSalle = new ButtonGroup();
		rbPetiteSalle = new JRadioButton("Petite salle");
		rbGrandeSalle = new JRadioButton("Grande salle");
		rbSalleEquipee = new JRadioButton("Salle equipee");
		rbPetiteSalle.setSelected(true);
		bgChoixSalle.add(rbPetiteSalle);
		bgChoixSalle.add(rbGrandeSalle);
		bgChoixSalle.add(rbSalleEquipee);
		panelRadioSalle.add(rbPetiteSalle);
		panelRadioSalle.add(rbGrandeSalle);
		panelRadioSalle.add(rbSalleEquipee);
		
		
		//Radio Button tranches
		choixTranche = new ButtonGroup();
		rbMatin = new JRadioButton(TRANCHE_MATIN);
		rbApresMidi = new JRadioButton(TRANCHE_APRES_MIDI);
		rbSoir = new JRadioButton(TRANCHE_SOIR);
		rbMatin.setSelected(true);
		choixTranche.add(rbMatin);
		choixTranche.add(rbApresMidi);
		choixTranche.add(rbSoir);
		panelRadioTranche.add(rbMatin);
		panelRadioTranche.add(rbApresMidi);
		panelRadioTranche.add(rbSoir);
		
		//Combo Box duree
		cbDuree = new JComboBox();
		cbDuree.removeAllItems();
		for(int i=1; i<=DUREE_MAX_MATIN; i++){
			cbDuree.addItem(i+"h");
		}

		panelNORTH.add(panelRadioSalle);
		panelNORTH.add(jdDateChooser);
		panelNORTH.add(lTranche);
		panelNORTH.add(panelRadioTranche);
		panelNORTH.add(lDuree);
		panelNORTH.add(cbDuree);
		panelCENTER.add(panelCENTERInterne);
		panelCENTERInterne.add(bRechercher);
		panelCENTERInterne.add(new JLabel(" "));
		panelCENTERInterne.add(panelCENTERInterneResultat);
		panelSOUTH.add(bRetourMenu);
		
		this.add(panelNORTH, BorderLayout.NORTH);
		this.add(panelCENTER, BorderLayout.CENTER);
		this.add(panelSOUTH, BorderLayout.SOUTH);
		
		rbMatin.addActionListener(this);
		rbApresMidi.addActionListener(this);
		rbSoir.addActionListener(this);
		bRetourMenu.addActionListener(this);
		bRechercher.addActionListener(this);
		bAccepter.addActionListener(this);
		bRetourRecherche.addActionListener(this);
		
	}
	
	/**
	 * Recherche et affichage dune proposition pour les criteres renseignes
	 */
	public void afficherUneProposition(){
		SimpleDateFormat formatterDateSaisie = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formatterDeb = new SimpleDateFormat("'Le 'dd/MM/yyyy' de 'HH'h '");
		SimpleDateFormat formatterFin = new SimpleDateFormat("HH");
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
			panelCENTERInterneResultat.add(new JLabel("Proposition : " 
					+ formatterDeb.format(dateReservation) 
					+ "a " + (Integer.parseInt(formatterFin.format(dateReservation))+duree) + "h"));
			panelCENTERInterneResultat.add(bAccepter);
			bRechercher.setAlignmentX(Component.CENTER_ALIGNMENT);
		} else {
			panelCENTERInterneResultat.add(new JLabel("Aucun creneau libre a cette date"));
		}
		
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
			afficherUneProposition();
		} else if(o.equals(bAccepter)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelValidationReservation(frame, creneauPropose));
			frame.validate();
		} else if(o.equals(bRetourRecherche)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelReservationAuto(frame));
			frame.validate();
		}
	}

}
