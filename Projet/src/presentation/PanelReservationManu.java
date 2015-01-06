package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import metier.RechercheReservation;
import metier.SupprimerReservation;

import com.toedter.calendar.JDateChooser;

import donnee.Reservation;
import donnee.Salle;


/**
 * Panel permettant la reservation manuelle dune reservation
 */
public class PanelReservationManu extends JPanel implements ActionListener {
	
	private JFrame frame;
	
	private JLabel[] tabEtat;
	
	private ButtonGroup bgChoixTypeSalle;
	private JRadioButton rbPetiteSalle;
	private JRadioButton rbGrandeSalle;
	private JRadioButton rbSalleEquipee;
	
	private JComboBox cbChoixNumSalle;
	
	private JDateChooser jdDateChooser;

	private Map<JButton, Date> mapBoutonSuppr;
	private Map<JButton, Date> mapBoutonAdd;
	private JButton bRechercher;
	private JButton bRetour;

	private JPanel panelNORTH;
	private JPanel panelCENTER;
	private JPanel panelSOUTH;
	
	private List<Salle> listeSallesPourUnType;
	private int[] tabIdSalle;
	private Reservation[] tabReservations;
	
	
	/**
	 * Methode qui permet de visualiser 
	 * @param frame
	 */
	public PanelReservationManu(JFrame frame) {
		panelNORTH = new JPanel();
		panelCENTER = new JPanel();
		panelSOUTH = new JPanel();
		JPanel panelRadio = new JPanel();
		
		this.frame = frame;

		this.setLayout(new BorderLayout());
		panelNORTH.setLayout(new FlowLayout());
		panelCENTER.setLayout(new GridLayout(6, 5));
		panelSOUTH.setLayout(new FlowLayout());
		panelRadio.setLayout(new BoxLayout(panelRadio, BoxLayout.Y_AXIS));
		panelCENTER.setBorder(BorderFactory.createLoweredBevelBorder());
		
		mapBoutonSuppr = new HashMap<JButton, Date>();
		mapBoutonAdd = new HashMap<JButton, Date>();
		bRechercher = new JButton("Rechercher");
		bRechercher.setBackground(Color.WHITE);
		bRetour = new JButton("Retour");
		bRetour.setBackground(Color.WHITE);
		
		//tfDateMois.setMinimumSize(new Dimension(23, 20));
		//tfDateMois.setPreferredSize(new Dimension(23, 20));
		
		//tfDateAnnee.setMinimumSize(new Dimension(45, 20));
		//tfDateAnnee.setPreferredSize(new Dimension(45, 20));
		
		bgChoixTypeSalle = new ButtonGroup();
		rbPetiteSalle = new JRadioButton("Petite salle");
		rbGrandeSalle = new JRadioButton("Grande salle");
		rbSalleEquipee = new JRadioButton("Salle equipee");
		rbPetiteSalle.setSelected(true);
		bgChoixTypeSalle.add(rbPetiteSalle);
		bgChoixTypeSalle.add(rbGrandeSalle);
		bgChoixTypeSalle.add(rbSalleEquipee);
		panelRadio.add(rbPetiteSalle);
		panelRadio.add(rbGrandeSalle);
		panelRadio.add(rbSalleEquipee);

		//ComboBox choixSalle
		cbChoixNumSalle = new JComboBox();
		alimenterChoixSalle();
		
		jdDateChooser = new JDateChooser();
		jdDateChooser.setPreferredSize(new Dimension(120, 25));
		
		panelNORTH.add(panelRadio);
		panelNORTH.add(cbChoixNumSalle);
		panelNORTH.add(jdDateChooser);
		panelNORTH.add(bRechercher);
		initialiserContainerCENTER();
		panelSOUTH.add(bRetour);

		bRetour.addActionListener(this);
		bRechercher.addActionListener(this);
		rbPetiteSalle.addActionListener(this);
		rbGrandeSalle.addActionListener(this);
		rbSalleEquipee.addActionListener(this);
		
		this.add(panelNORTH, BorderLayout.NORTH);
		this.add(panelCENTER, BorderLayout.CENTER);
		this.add(panelSOUTH, BorderLayout.SOUTH);
	}
	
	public void alimenterChoixSalle(){
		RechercheReservation metier = new RechercheReservation();
		listeSallesPourUnType = metier.rechercherSallesParType(rbPetiteSalle.isSelected()?"petite":(rbGrandeSalle.isSelected()?"grande":"equipee"));

		cbChoixNumSalle.removeAllItems();
		
		tabIdSalle = new int[listeSallesPourUnType.size()];
		for(int i=1; i<=listeSallesPourUnType.size();i++){
			cbChoixNumSalle.addItem("Salle "+ i);
			tabIdSalle[i-1] = listeSallesPourUnType.get(i-1).getIdSalle();
		}
	}

	/**
	 * Initialise le tableau central des horaires et etat des salles (avec letat Libre)
	 */
	public void initialiserContainerCENTER(){
		JLabel horaire;
		int horaireCourant=9;
		int etatCourant=0;
		tabEtat=new JLabel[15];
		//on initialise le tableau detat a 'Libre'
		for(int i=0; i<15; i++){
			tabEtat[i]=new JLabel("Libre",JLabel.CENTER);
		}
		//On remplit le tableau central avec les horaires et les etats a 'Libre'
		for(int i=0; i<3; i++){
			for(int j=0; j<5; j++){
				horaire = new JLabel(horaireCourant+"h - "+(horaireCourant+1)+"h  ", JLabel.CENTER);
				panelCENTER.add(horaire);
				horaireCourant++;
			}
			for(int j=0; j<5; j++){
				panelCENTER.add(tabEtat[etatCourant]);
				etatCourant++;
			}
		}
		panelCENTER.setVisible(false);
	}
	
	/**
	 * Met a jour l'etat des reservations pour une date et un type de salle donnes
	 */
	public void alimenterContainerCENTER(){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		RechercheReservation metierPlanning = new RechercheReservation();
		JPanel panelAddSuppr;
		Icon iredcross = new ImageIcon("src/img/redcross.png");
		Icon igreencross = new ImageIcon("src/img/greencross.png");
		JButton bCroixSuppr;
		JButton bCroixAdd;
		JLabel horaire;
		int horaireCourant=9;
		int etatCourant=0;
		if(jdDateChooser.getDate()==null){
			JOptionPane.showMessageDialog(this, "Date non valide !");
			return;
		}
		//on recupere letat de la salle dur les 15 creneaux
		String[] etatsSalle = metierPlanning.etatsSalle(formatter.format(jdDateChooser.getDate()), tabIdSalle[cbChoixNumSalle.getSelectedIndex()]);
		tabReservations = new RechercheReservation().reservationParHeure(formatter.format(jdDateChooser.getDate()), tabIdSalle[cbChoixNumSalle.getSelectedIndex()]);
		
		panelCENTER.removeAll();
		panelCENTER.setLayout(new GridLayout(6, 5));
		//on alimente le tableau central des horaires et etats salle
		for(int i=0; i<3; i++){
			//horaire
			for(int j=0; j<5; j++){
				horaire = new JLabel(horaireCourant+"h - "+(horaireCourant+1)+"h  ", JLabel.CENTER);
				panelCENTER.add(horaire);
				horaireCourant++;
			}
			//etats
			for(int j=0; j<5; j++){
				if(etatsSalle[etatCourant].equals("Libre")){
					bCroixAdd = new JButton(igreencross);
					bCroixAdd.setMaximumSize(new Dimension(18,18));
					bCroixAdd.setMinimumSize(new Dimension(18,18));
					bCroixAdd.setPreferredSize(new Dimension(18,18));
					bCroixAdd.addActionListener(this);
					try {
						mapBoutonAdd.put(bCroixAdd, formatter2.parse(formatter.format(jdDateChooser.getDate())+(etatCourant+9<10?" 0":" ")+(etatCourant+9)+":00:00"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					panelAddSuppr = new JPanel();
					panelAddSuppr.setLayout(new FlowLayout());
					panelAddSuppr.add(tabEtat[etatCourant]);
					panelAddSuppr.add(bCroixAdd);
					panelCENTER.add(panelAddSuppr);
					tabEtat[etatCourant].setForeground(Color.GREEN);
					tabEtat[etatCourant].setText("Libre");
				} else if(etatsSalle[etatCourant].equals("Confirmee")){
					panelCENTER.add(tabEtat[etatCourant]);
					tabEtat[etatCourant].setForeground(Color.CYAN);
					tabEtat[etatCourant].setText("Confirmee");
				} else if(etatsSalle[etatCourant].equals("Non confirmee")){
					panelCENTER.add(tabEtat[etatCourant]);
					tabEtat[etatCourant].setForeground(Color.ORANGE);
					tabEtat[etatCourant].setText("Non confirmee");
				} else {
					bCroixSuppr = new JButton(iredcross);
					bCroixSuppr.setMaximumSize(new Dimension(18,18));
					bCroixSuppr.setMinimumSize(new Dimension(18,18));
					bCroixSuppr.setPreferredSize(new Dimension(18,18));
					bCroixSuppr.addActionListener(this);
					try {
						mapBoutonSuppr.put(bCroixSuppr, formatter2.parse(formatter.format(jdDateChooser.getDate())+(etatCourant+9<10?" 0":" ")+(etatCourant+9)+":00:00"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					panelAddSuppr = new JPanel();
					panelAddSuppr.setLayout(new FlowLayout());
					panelAddSuppr.add(tabEtat[etatCourant]);
					panelAddSuppr.add(bCroixSuppr);
					panelCENTER.add(panelAddSuppr);
					tabEtat[etatCourant].setForeground(Color.RED);
					tabEtat[etatCourant].setText("Hors delais");
				}
				etatCourant++;
			}
		}
		panelCENTER.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o.equals(bRetour)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelMenu(frame));
			frame.validate();
		} else if(o.equals(bRechercher)) {
			alimenterContainerCENTER();
		} else if(o.equals(rbPetiteSalle) || o.equals(rbGrandeSalle) || o.equals(rbSalleEquipee)){
			alimenterChoixSalle();
		} else if(mapBoutonSuppr.containsKey(o)){
			Calendar calendar = new GregorianCalendar();
			int rep = JOptionPane.showConfirmDialog(
					frame,
					"Supprimer cette reservation?",
				    "Suppression",
				    JOptionPane.YES_NO_OPTION);
			if(!(rep==JOptionPane.OK_OPTION)){
				return;
			}
			calendar.setTime(mapBoutonSuppr.get(o));
			new SupprimerReservation().supprimerReservationParDateCreation(tabReservations[calendar.get(Calendar.HOUR_OF_DAY)-9].getDateCreation(), tabReservations[calendar.get(Calendar.HOUR_OF_DAY)-9].getClient().getId());
			alimenterContainerCENTER();
		} else if(mapBoutonAdd.containsKey(o)){
			Calendar calendar = new GregorianCalendar();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			calendar.setTime(mapBoutonAdd.get(o));
			List<Reservation> listeReservations;
			Reservation laReservation;
			Integer[] tabChoixPlage;
			int heure = calendar.get(Calendar.HOUR_OF_DAY)-9;
			int nbHeuresPossibles = 0;
			Integer choixSemaines;
			
			while(heure<tabReservations.length && tabReservations[heure]==null){
				nbHeuresPossibles++;heure++;
			}
			
			tabChoixPlage = new Integer[nbHeuresPossibles];
			for(int i=0; i<nbHeuresPossibles; i++){
				tabChoixPlage[i]=i+1;
			}
			
			Integer choixPlage = (Integer) JOptionPane.showInputDialog(frame, 
			        "Duree de la reservation ?",
			        "Duree",
			        JOptionPane.QUESTION_MESSAGE, 
			        null, 
			        tabChoixPlage, 
			        tabChoixPlage[0]);
		
			if(choixPlage==null){
				return;
			}
			
			try{
				choixSemaines = Integer.parseInt(JOptionPane.showInputDialog("Combien de semaines ?", 1));
				if(choixSemaines==null || choixSemaines<1){
					throw new NumberFormatException();
				}
			} catch(NumberFormatException nfe){
				JOptionPane.showMessageDialog(this, "Nombre de semaines non valide !");
				return;
			}
			
			listeReservations = new ArrayList<Reservation>();
			laReservation = new Reservation();
			
			
			laReservation = new RechercheReservation().genererReservation(null, tabIdSalle[cbChoixNumSalle.getSelectedIndex()], mapBoutonAdd.get(o), choixPlage, false);
			listeReservations.add(laReservation);
			if(choixSemaines>1){
				calendar.setTime(mapBoutonAdd.get(o));
				for(int i=1; i<choixSemaines; i++){
					calendar.add(Calendar.DAY_OF_MONTH, 7);
					laReservation = new RechercheReservation().genererReservation(null, tabIdSalle[cbChoixNumSalle.getSelectedIndex()], calendar.getTime(), choixPlage, false);
					if(!(new RechercheReservation().estLibre(laReservation))){
						JOptionPane.showMessageDialog(this, "Creneaux deja reserve le " + formatter.format(calendar.getTime()));
						return;
					}
					listeReservations.add(laReservation);
				}
			}
			
			
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelValidationReservation(frame, listeReservations));
			frame.validate();
		}
	}

	
}
