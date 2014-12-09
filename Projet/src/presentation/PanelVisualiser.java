package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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


public class PanelVisualiser extends JPanel implements ActionListener {
	
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
	 * Met ˆ jour l'état des réservations pour une date et un type de salle donnés
	 * @param c
	 */
	public void alimenterContainerCENTER(){
		RechercheReservation metierPlanning = new RechercheReservation();
		String[] etatsSalle = metierPlanning.etatsSalle(tfDateJour.getText()+"-"+tfDateMois.getText()+"-"+tfDateAnnee.getText(), rb1.isSelected()?"petite":(rb2.isSelected()?"grande":"equipee"));
		
		JLabel lHoraire = null;
		JLabel lEtat = null;
		
		containerCENTER.removeAll();
		containerCENTER.setLayout(new GridLayout(15, 2));
		
		//Affichage des différents créneaux et de l'état de leur réservation (libre, confirmée, non confirmée ou hors délais)
		for(int i=9; i<24; i++){
			
			lHoraire = new JLabel(i+"h - "+(i+1)+"h  ");
			lHoraire.setHorizontalAlignment(SwingConstants.TRAILING);
			containerCENTER.add(lHoraire);
			
			lEtat = new JLabel(etatsSalle[i-9]);
			if(etatsSalle[i-9].equals("Libre")){
				lEtat.setForeground(Color.GREEN);
			} else if(etatsSalle[i-9].equals("Confirmée")){
				lEtat.setForeground(Color.CYAN);
			} else if(etatsSalle[i-9].equals("Non confirmée")){
				lEtat.setForeground(Color.ORANGE);
			} else {
				lEtat.setForeground(Color.RED);
			}
			containerCENTER.add(lEtat);
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
