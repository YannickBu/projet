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
import java.util.Date;
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

import donnee.Salle;


/**
 * Panel permettant la visualisation de letat dune salle a une date donnee pour
 * les 15 creneaux de cette journee
 */
public class PanelVisualiser extends JPanel implements ActionListener {
	
	private JFrame frame;
	
	private JLabel[] tabEtat;
	/*private JLabel lEtat9h = new JLabel("Libre",JLabel.CENTER);
	private JLabel lEtat10h = new JLabel("Libre",JLabel.CENTER);
	private JLabel lEtat11h = new JLabel("Libre",JLabel.CENTER);
	private JLabel lEtat12h = new JLabel("Libre",JLabel.CENTER);
	private JLabel lEtat13h = new JLabel("Libre",JLabel.CENTER);
	private JLabel lEtat14h = new JLabel("Libre",JLabel.CENTER);
	private JLabel lEtat15h = new JLabel("Libre",JLabel.CENTER);
	private JLabel lEtat16h = new JLabel("Libre",JLabel.CENTER);
	private JLabel lEtat17h = new JLabel("Libre",JLabel.CENTER);
	private JLabel lEtat18h = new JLabel("Libre",JLabel.CENTER);
	private JLabel lEtat19h = new JLabel("Libre",JLabel.CENTER);
	private JLabel lEtat20h = new JLabel("Libre",JLabel.CENTER);
	private JLabel lEtat21h = new JLabel("Libre",JLabel.CENTER);
	private JLabel lEtat22h = new JLabel("Libre",JLabel.CENTER);
	private JLabel lEtat23h = new JLabel("Libre",JLabel.CENTER);*/
	
	private ButtonGroup bgChoixTypeSalle;
	private JRadioButton rbPetiteSalle;
	private JRadioButton rbGrandeSalle;
	private JRadioButton rbSalleEquipee;
	
	private JComboBox cbChoixNumSalle;
	
	private JDateChooser jdDateChooser;

	private Map<JButton, Date> mapBoutonSuppr;
	private JButton bRechercher;
	private JButton bRetour;

	private JPanel panelNORTH;
	private JPanel panelCENTER;
	private JPanel panelSOUTH;
	
	private List<Salle> listeSallesPourUnType;
	private int[] tabIdSalle;
	
	
	/**
	 * Methode qui permet de visualiser 
	 * @param frame
	 */
	public PanelVisualiser(JFrame frame) {
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
		/*tabEtat = new JLabel[]{lEtat9h,lEtat10h,lEtat11h,lEtat12h,lEtat13h,lEtat14h,lEtat15h,lEtat16h,lEtat17h,
				lEtat18h,lEtat19h,lEtat20h,lEtat21h,lEtat22h,lEtat23h};*/
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
		JPanel panelHorsDelais;
		Icon iCroix = new ImageIcon("src/img/redcross.png");
		JButton bCroix;
		JLabel horaire;
		int horaireCourant=9;
		int etatCourant=0;
		if(jdDateChooser.getDate()==null){
			JOptionPane.showMessageDialog(this, "Date non valide !");
			return;
		}
		//on recupere letat de la salle dur les 15 creneaux
		String[] etatsSalle = metierPlanning.etatsSalle(formatter.format(jdDateChooser.getDate()), tabIdSalle[cbChoixNumSalle.getSelectedIndex()]);
		
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
					panelCENTER.add(tabEtat[etatCourant]);
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
					bCroix = new JButton(iCroix);
					bCroix.setMaximumSize(new Dimension(18,18));
					bCroix.setMinimumSize(new Dimension(18,18));
					bCroix.setPreferredSize(new Dimension(18,18));
					bCroix.addActionListener(this);
					try {
						mapBoutonSuppr.put(bCroix, formatter2.parse(formatter.format(jdDateChooser.getDate())+(etatCourant+9<10?" 0":" ")+(etatCourant+9)+":00:00"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					panelHorsDelais = new JPanel();
					panelHorsDelais.setLayout(new FlowLayout());
					panelHorsDelais.add(tabEtat[etatCourant]);
					panelHorsDelais.add(bCroix);
					panelCENTER.add(panelHorsDelais);
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
			int rep = JOptionPane.showConfirmDialog(
					frame,
					"Supprimer cette reservation?",
				    "Suppression",
				    JOptionPane.YES_NO_OPTION);
			if(!(rep==JOptionPane.OK_OPTION)){
				return;
			}
			SupprimerReservation metierSupprRes = new SupprimerReservation();
			metierSupprRes.supprimerReservation(mapBoutonSuppr.get(o), tabIdSalle[cbChoixNumSalle.getSelectedIndex()]);
			alimenterContainerCENTER();
		}
	}

	
}
