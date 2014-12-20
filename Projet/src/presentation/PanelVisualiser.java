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

import metier.RechercheReservation;

import com.toedter.calendar.JDateChooser;

import donnee.Salle;


public class PanelVisualiser extends JPanel implements ActionListener {
	
	private JFrame frame;
	
	private JLabel[] tabEtat;
	private JLabel lEtat9h = new JLabel("Libre",JLabel.CENTER);
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
	private JLabel lEtat23h = new JLabel("Libre",JLabel.CENTER);
	
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
		containerCENTER.setLayout(new GridLayout(6, 5));
		containerSOUTH.setLayout(new FlowLayout());
		containerRadio.setLayout(new BoxLayout(containerRadio, 3));
		
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
		containerRadio.add(rbPetiteSalle);
		containerRadio.add(rbGrandeSalle);
		containerRadio.add(rbSalleEquipee);

		//ComboBox choixSalle
		cbChoixNumSalle = new JComboBox();
		alimenterChoixSalle();
		
		jdDateChooser = new JDateChooser();
		jdDateChooser.setPreferredSize(new Dimension(120, 25));
		
		containerNORTH.add(containerRadio);
		containerNORTH.add(cbChoixNumSalle);
		containerNORTH.add(jdDateChooser);
		containerNORTH.add(bRechercher);
		initialiserContainerCENTER();
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
	public void initialiserContainerCENTER(){
		JLabel horaire;
		int horaireCourant=9;
		int etatCourant=0;
		tabEtat = new JLabel[]{lEtat9h,lEtat10h,lEtat11h,lEtat12h,lEtat13h,lEtat14h,lEtat15h,lEtat16h,lEtat17h,
				lEtat18h,lEtat19h,lEtat20h,lEtat21h,lEtat22h,lEtat23h};

		for(int i=0; i<3; i++){
			for(int j=0; j<5; j++){
				horaire = new JLabel(horaireCourant+"h - "+(horaireCourant+1)+"h  ", JLabel.CENTER);
				containerCENTER.add(horaire);
				horaireCourant++;
			}
			for(int j=0; j<5; j++){
				containerCENTER.add(tabEtat[etatCourant]);
				etatCourant++;
			}
		}
		containerCENTER.setVisible(false);
	}
	
	/**
	 * Met a jour l'etat des reservations pour une date et un type de salle donnes
	 * @param c
	 */
	public void alimenterContainerCENTER(){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		RechercheReservation metierPlanning = new RechercheReservation();
		if(jdDateChooser.getDate()==null){
			JOptionPane.showMessageDialog(this, "Date non valide !");
			return;
		}
		String[] etatsSalle = metierPlanning.etatsSalle(formatter.format(jdDateChooser.getDate()), tabIdSalle[cbChoixNumSalle.getSelectedIndex()]);
		
		JLabel lHoraire = null;
		JLabel lEtat = null;
		
		//Affichage des differents creneaux et de l'etat de leur reservation (libre, confirmee, non confirmee ou hors delais)
		for(int i=0; i<15; i++){
			
			lEtat = new JLabel(etatsSalle[i]);
			if(etatsSalle[i].equals("Libre")){
				tabEtat[i].setForeground(Color.GREEN);
				tabEtat[i].setText("Libre");
			} else if(etatsSalle[i].equals("Confirmee")){
				tabEtat[i].setForeground(Color.CYAN);
				tabEtat[i].setText("Confirmee");
			} else if(etatsSalle[i].equals("Non confirmee")){
				tabEtat[i].setForeground(Color.ORANGE);
				tabEtat[i].setText("Non confirmee");
			} else {
				tabEtat[i].setForeground(Color.RED);
				tabEtat[i].setText("Hors delais");
			}
			
		}
		containerCENTER.setVisible(true);
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
