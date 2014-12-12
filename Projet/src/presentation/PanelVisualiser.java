package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import metier.RechercheReservation;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import donnee.Salle;


public class PanelVisualiser extends JPanel implements ActionListener {
	
	private JFrame frame;
	
	private JLabel lSalle;
	private JLabel lDate;
	
	private ButtonGroup bgChoixTypeSalle;
	private JRadioButton rbPetiteSalle;
	private JRadioButton rbGrandeSalle;
	private JRadioButton rbSalleEquipee;
	
	private JComboBox cbChoixNumSalle;
	
	private JDateChooser jdDateChooser;
	
	private JButton bRechercher;
	private JButton bRetour;

	private Container containerNORTH;
	private Container containerCENTER;
	private Container containerSOUTH;
	

	private List<Salle> listeSallesPourUnType;
	private int[] tabIdSalle;
	
	
	/**
	 * Methode qui permet de visualiser 
	 * @param frame
	 */
	public PanelVisualiser(JFrame frame) {
		containerNORTH = new Container();
		containerCENTER = new Container();
		containerSOUTH = new Container();
		Container containerRadio = new Container();
		
		this.frame = frame;

		this.setLayout(new BorderLayout());
		containerNORTH.setLayout(new FlowLayout());
		containerCENTER.setLayout(new GridLayout(15, 2));
		containerSOUTH.setLayout(new FlowLayout());
		containerRadio.setLayout(new BoxLayout(containerRadio, 3));
		
		lSalle = new JLabel("Type de salle ");
		lDate = new JLabel("Date ");
		bRechercher = new JButton("Rechercher");
		bRetour = new JButton("Retour");
		
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
		containerRadio.add(rbPetiteSalle);
		containerRadio.add(rbGrandeSalle);
		containerRadio.add(rbSalleEquipee);

		//ComboBox choixSalle
		cbChoixNumSalle = new JComboBox();
		alimenterChoixSalle();
		
		jdDateChooser = new JDateChooser();
		
		containerNORTH.add(lSalle);
		containerNORTH.add(containerRadio);
		containerNORTH.add(cbChoixNumSalle);
		containerNORTH.add(lDate);
		containerNORTH.add(jdDateChooser);
		containerNORTH.add(bRechercher);
		alimenterContainerCENTER(containerCENTER);
		containerSOUTH.add(bRetour);

		bRetour.addActionListener(this);
		bRechercher.addActionListener(this);
		rbPetiteSalle.addActionListener(this);
		rbGrandeSalle.addActionListener(this);
		rbSalleEquipee.addActionListener(this);
		
		this.add(containerNORTH, BorderLayout.NORTH);
		this.add(containerCENTER, BorderLayout.CENTER);
		this.add(containerSOUTH, BorderLayout.SOUTH);
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
	 * @param c
	 */
	public void alimenterContainerCENTER(Container c){
		JLabel horaire;
		for(int i=9; i<24; i++){
			horaire = new JLabel(i+"h - "+(i+1)+"h  ");
			horaire.setHorizontalAlignment(SwingConstants.TRAILING);
			c.add(horaire);
			c.add(new JLabel(" "));
		}
	}
	
	/**
	 * Met a jour l'etat des reservations pour une date et un type de salle donnes
	 * @param c
	 */
	public void alimenterContainerCENTER(){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		RechercheReservation metierPlanning = new RechercheReservation();
		String[] etatsSalle = metierPlanning.etatsSalle(formatter.format(jdDateChooser.getDate()), tabIdSalle[cbChoixNumSalle.getSelectedIndex()]);
		
		JLabel lHoraire = null;
		JLabel lEtat = null;
		
		containerCENTER.removeAll();
		containerCENTER.setLayout(new GridLayout(15, 2));
		
		//Affichage des differents creneaux et de l'etat de leur reservation (libre, confirmee, non confirmee ou hors delais)
		for(int i=9; i<24; i++){
			
			lHoraire = new JLabel(i+"h - "+(i+1)+"h  ");
			lHoraire.setHorizontalAlignment(SwingConstants.TRAILING);
			containerCENTER.add(lHoraire);
			
			lEtat = new JLabel(etatsSalle[i-9]);
			if(etatsSalle[i-9].equals("Libre")){
				lEtat.setForeground(Color.GREEN);
			} else if(etatsSalle[i-9].equals("Confirmee")){
				lEtat.setForeground(Color.CYAN);
			} else if(etatsSalle[i-9].equals("Non confirmee")){
				lEtat.setForeground(Color.ORANGE);
			} else {
				lEtat.setForeground(Color.RED);
			}
			containerCENTER.add(lEtat);
		}
		containerCENTER.validate();
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
		}
	}

	
}
