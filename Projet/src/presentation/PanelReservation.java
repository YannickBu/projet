package presentation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.toedter.calendar.JDateChooser;

import metier.CreerReservation;
import metier.RechercheReservation;
import donnee.Reservation;
import donnee.Salle;

public class PanelReservation extends JPanel implements ActionListener {

	private static final String TRANCHE_SOIR = "20h - 00h";
	private static final String TRANCHE_APRES_MIDI = "13h - 20h";
	private static final String TRANCHE_MATIN = "9h - 13h";
	private static final int DUREE_MAX_MATIN = 15;
	private static final int DUREE_MAX_APRES_MIDI = 11;
	private static final int DUREE_MAX_SOIR = 4;
	
	private List<Reservation> creneauPropose;
	
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
	
	private JTextField tfNom = new JTextField();
	private JTextField tfTel = new JTextField();
	
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
	public PanelReservation(JFrame frame) {
		
		
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
	
	public void afficherUneProposition(){
		SimpleDateFormat formatterDateSaisie = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formatterDeb = new SimpleDateFormat("'Le 'dd/MM/yyyy' de 'HH'h '");
		SimpleDateFormat formatterFin = new SimpleDateFormat("HH");
		RechercheReservation metier = new RechercheReservation();
		Date dateReservationDebut = null;
		Date dateReservationFin = null;
		int duree;
		
		if(jdDateChooser.getDate()==null){
			JOptionPane.showMessageDialog(this, "Date non valide !");
			return;
		}
		
		creneauPropose = metier.rechercheCreneauLibre(
				formatterDateSaisie.format(jdDateChooser.getDate()), 
				Integer.parseInt(((String)cbDuree.getSelectedItem()).substring(0, 1)), 
				rbMatin.isSelected()?"matin":(rbApresMidi.isSelected()?"apres-midi":"soir"), 
				rbPetiteSalle.isSelected()?"petite":(rbGrandeSalle.isSelected()?"grande":"equipee"));
		panelCENTERInterneResultat.removeAll();
		
		if(creneauPropose!=null && creneauPropose.size()>0){
			dateReservationDebut = creneauPropose.get(0).getDate();
			dateReservationFin = creneauPropose.get(creneauPropose.size()-1).getDate();
			duree = creneauPropose.get(creneauPropose.size()-1).getPlage();
			panelCENTERInterneResultat.add(new JLabel("Proposition : " 
					+ formatterDeb.format(dateReservationDebut) 
					+ "a " + (Integer.parseInt(formatterFin.format(dateReservationFin))+duree) + "h " ));
			panelCENTERInterneResultat.add(bAccepter);
			bRechercher.setAlignmentX(Component.CENTER_ALIGNMENT);
		} else {
			panelCENTERInterneResultat.add(new JLabel("Aucun creneau libre a cette date"));
		}
		
		this.validate();
	}
	
	public void afficherSaisieClient(){
		frame.getContentPane().removeAll();
		frame.getContentPane().add(new PanelSaisieClient(frame, creneauPropose));
		frame.validate();
	/*	JPanel panel = new JPanel();
		Container containerSOUTH = new Container();
		Container containerCENTER = new Container();
		Container containerCENTERInterne = new Container();
		Container containerCENTERNom = new Container();
		Container containerCENTERTel = new Container();

		JLabel nom = new JLabel("Nom");
		JLabel tel = new JLabel("Tel");
		
		nom.setHorizontalAlignment(SwingConstants.CENTER);
		nom.setMinimumSize(new Dimension(23, 20));
		nom.setPreferredSize(new Dimension(23, 20));

		tel.setHorizontalAlignment(SwingConstants.CENTER);
		tel.setMinimumSize(new Dimension(23, 20));
		tel.setPreferredSize(new Dimension(23, 20));
		
		tfNom.setMinimumSize(new Dimension(23, 20));
		tfNom.setPreferredSize(new Dimension(23, 20));
		
		tfTel.setMinimumSize(new Dimension(23, 20));
		tfTel.setPreferredSize(new Dimension(23, 20));
		
		panel.setLayout(new BorderLayout());
		containerSOUTH.setLayout(new FlowLayout());
		containerCENTER.setLayout(new FlowLayout());
		containerCENTERInterne.setLayout(new BoxLayout(containerCENTERInterne, BoxLayout.Y_AXIS));
		containerCENTERNom.setLayout(new GridLayout(1,2));
		containerCENTERTel.setLayout(new GridLayout(1,2));
		
		frame.getContentPane().removeAll();
		
		containerSOUTH.add(bRetourRecherche);
		panel.add(containerSOUTH, BorderLayout.SOUTH);
		containerCENTERNom.add(nom);
		containerCENTERNom.add(tfNom);
		containerCENTERTel.add(tel);
		containerCENTERTel.add(tfTel);
		containerCENTERInterne.add(containerCENTERNom);
		containerCENTERInterne.add(containerCENTERTel);
		containerCENTERInterne.add(bEnregistrer);
		containerCENTER.add(containerCENTERInterne);
		panel.add(containerCENTER, BorderLayout.CENTER);
		
		frame.getContentPane().add(panel);
		frame.validate();*/
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
				cbDuree.addItem(i);
			}
		} else if(o.equals(rbApresMidi)){
			cbDuree.removeAllItems();
			for(int i=1; i<=DUREE_MAX_APRES_MIDI; i++){
				cbDuree.addItem(i);
			}
		} else if(o.equals(rbSoir)){
			cbDuree.removeAllItems();
			for(int i=1; i<=DUREE_MAX_SOIR; i++){
				cbDuree.addItem(i);
			}
		} else if(o.equals(bRechercher)){
			afficherUneProposition();
		} else if(o.equals(bAccepter)){
			afficherSaisieClient();
		} else if(o.equals(bRetourRecherche)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelReservation(frame));
			frame.validate();
		}
	}

}
