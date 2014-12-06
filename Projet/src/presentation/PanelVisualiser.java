package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import metier.RechercheReservation;
import donnee.Reservation;

public class PanelVisualiser extends JPanel implements ActionListener {

	private int DELAIS_DE_PAIEMENT_EN_JOURS = 7;
	
	private JFrame frame;
	
	private JLabel lSalle;
	private JLabel lDate;
	
	private ButtonGroup choixSalle;
	private JRadioButton rb1;
	private JRadioButton rb2;
	private JRadioButton rb3;
	
	private JTextField tfDateJour;
	private JTextField tfDateMois;
	private JTextField tfDateAnnee;
	
	private JButton bRechercher;
	private JButton bRetour;

	private Container containerNORTH;
	private Container containerCENTER;
	private Container containerSOUTH;
	
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
		tfDateJour = new JTextField();
		tfDateMois = new JTextField();
		tfDateAnnee = new JTextField();
		
		tfDateJour.setMinimumSize(new Dimension(23, 20));
		tfDateJour.setPreferredSize(new Dimension(23, 20));
		
		tfDateMois.setMinimumSize(new Dimension(23, 20));
		tfDateMois.setPreferredSize(new Dimension(23, 20));
		
		tfDateAnnee.setMinimumSize(new Dimension(45, 20));
		tfDateAnnee.setPreferredSize(new Dimension(45, 20));
		
		choixSalle = new ButtonGroup();
		rb1 = new JRadioButton("Petite salle");
		rb2 = new JRadioButton("Grande salle");
		rb3 = new JRadioButton("Salle équipée");
		rb1.setSelected(true);
		choixSalle.add(rb1);
		choixSalle.add(rb2);
		choixSalle.add(rb3);
		containerRadio.add(rb1);
		containerRadio.add(rb2);
		containerRadio.add(rb3);
		
		containerNORTH.add(lSalle);
		containerNORTH.add(containerRadio);
		containerNORTH.add(lDate);
		containerNORTH.add(tfDateJour);
		containerNORTH.add(new JLabel("/"));
		containerNORTH.add(tfDateMois);
		containerNORTH.add(new JLabel("/"));
		containerNORTH.add(tfDateAnnee);
		containerNORTH.add(bRechercher);
		alimenterContainerCENTER(containerCENTER);
		containerSOUTH.add(bRetour);

		bRetour.addActionListener(this);
		bRechercher.addActionListener(this);
		
		this.add(containerNORTH, BorderLayout.NORTH);
		this.add(containerCENTER, BorderLayout.CENTER);
		this.add(containerSOUTH, BorderLayout.SOUTH);
	}

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
	 * Met à jour l'état des réservations pour une date et un type de salle donnés
	 * @param c
	 */
	public void alimenterContainerCENTER(){
		GregorianCalendar calendarDebut = new java.util.GregorianCalendar();
		GregorianCalendar calendarCreation = new java.util.GregorianCalendar(); 
		
		RechercheReservation rechResMetier = new RechercheReservation();
		List<Reservation> listeReservations = rechResMetier.rechercheReservationParDateEtTypeSalle(tfDateJour.getText()+"-"+tfDateMois.getText()+"-"+tfDateAnnee.getText(), rb1.isSelected()?"petite":(rb2.isSelected()?"grande":"equipee"));
		
		Reservation reserv = null;
		
		JLabel lHoraire = null;
		JLabel lEtat = null;
		
		int plage=0;
		
		containerCENTER.removeAll();
		containerCENTER.setLayout(new GridLayout(15, 2));
		
		//Affichage des différents créneaux et de l'état de leur réservation (libre, confirmée, non confirmée ou hors délais)
		for(int i=9; i<24; i++){
			
			if(listeReservations.size()>0 && reserv==null){
				reserv = listeReservations.remove(0);
				plage = reserv.getPlage();
				calendarDebut.setTime(reserv.getDate());
				calendarCreation.setTime(reserv.getDateCreation());
			}
			
			lHoraire = new JLabel(i+"h - "+(i+1)+"h  ");
			lHoraire.setHorizontalAlignment(SwingConstants.TRAILING);
			containerCENTER.add(lHoraire);
			
			if(reserv!=null && calendarDebut.get(Calendar.HOUR_OF_DAY) <= i && calendarDebut.get(Calendar.HOUR_OF_DAY)+plage > i){
				
				if(reserv.getEstPaye()){
					lEtat = new JLabel("Confirmée");
					lEtat.setForeground(Color.CYAN);
				} else {
					if(calendarDebut.getTimeInMillis() - calendarCreation.getTimeInMillis() > (DELAIS_DE_PAIEMENT_EN_JOURS*24*60*60*1000)){
						lEtat = new JLabel("Hors délais");
						lEtat.setForeground(Color.RED);
					} else {
						lEtat = new JLabel("Non confirmée");
						lEtat.setForeground(Color.ORANGE);
					}
				}
				
				containerCENTER.add(lEtat);
				if(calendarDebut.get(Calendar.HOUR_OF_DAY)+plage-1==i)
					reserv=null;
				
			} else {
				
				lEtat = new JLabel("Libre");
				lEtat.setForeground(Color.GREEN);
				containerCENTER.add(lEtat);
				
			}
		}
		containerCENTER.revalidate();
	}
	
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o.equals(bRetour)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelMenu(frame));
			frame.validate();
		} else if(o.equals(bRechercher)) {
			alimenterContainerCENTER();
		}
	}

	
}
