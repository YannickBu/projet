package presentation;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class PanelReservation extends JPanel implements ActionListener {

	private JFrame frame;
	
	private JLabel lSalle;
	private JLabel lDate;
	private JLabel lTranche;
	private JLabel lDuree;
	
	private ButtonGroup choixSalle;
	private JRadioButton rbPetiteSalle;
	private JRadioButton rbGrandeSalle;
	private JRadioButton rbSalleEquipee;
	
	private ButtonGroup choixTranche;
	private JRadioButton rbMatin;
	private JRadioButton rbApresMidi;
	private JRadioButton rbSoir;
	
	private JTextField tfDateJour;
	private JTextField tfDateMois;
	private JTextField tfDateAnnee;
	
	private JComboBox cbDuree;
	
	private JButton bRechercher;
	private JButton bRetour;
	
	public PanelReservation(JFrame frame) {
		Container containerNORTH = new Container();
		Container containerCENTER = new Container();
		Container containerSOUTH = new Container();
		Container containerRadioSalle = new Container();
		Container containerRadioTranche = new Container();
		
		this.frame = frame;

		this.setLayout(new BorderLayout());
		containerNORTH.setLayout(new FlowLayout());
		containerCENTER.setLayout(new GridLayout(15, 2));
		containerSOUTH.setLayout(new FlowLayout());
		containerRadioSalle.setLayout(new BoxLayout(containerRadioSalle, 3));
		containerRadioTranche.setLayout(new BoxLayout(containerRadioTranche, 3));
		
		lSalle = new JLabel("Type de salle ");
		lDate = new JLabel("Date ");
		lTranche = new JLabel("Tranche ");
		lDuree = new JLabel("Durée ");
		
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
		
		//Radio Button salles
		choixSalle = new ButtonGroup();
		rbPetiteSalle = new JRadioButton("Petite salle");
		rbGrandeSalle = new JRadioButton("Grande salle");
		rbSalleEquipee = new JRadioButton("Salle équipée");
		rbPetiteSalle.setSelected(true);
		choixSalle.add(rbPetiteSalle);
		choixSalle.add(rbGrandeSalle);
		choixSalle.add(rbSalleEquipee);
		containerRadioSalle.add(rbPetiteSalle);
		containerRadioSalle.add(rbGrandeSalle);
		containerRadioSalle.add(rbSalleEquipee);
		
		//Radio Button tranches
		choixTranche = new ButtonGroup();
		rbMatin = new JRadioButton("9h - 13h");
		rbApresMidi = new JRadioButton("13h - 20h");
		rbSoir = new JRadioButton("20h - 00h");
		rbMatin.setSelected(true);
		choixTranche.add(rbMatin);
		choixTranche.add(rbApresMidi);
		choixTranche.add(rbSoir);
		containerRadioTranche.add(rbMatin);
		containerRadioTranche.add(rbApresMidi);
		containerRadioTranche.add(rbSoir);
		
		//Combo Box duree
		cbDuree = new JComboBox();
		cbDuree.removeAllItems();
		cbDuree.addItem(1);
		cbDuree.addItem(2);
		cbDuree.addItem(3);
		cbDuree.addItem(4);

		containerNORTH.add(lSalle);
		containerNORTH.add(containerRadioSalle);
		containerNORTH.add(lDate);
		containerNORTH.add(tfDateJour);
		containerNORTH.add(new JLabel("/"));
		containerNORTH.add(tfDateMois);
		containerNORTH.add(new JLabel("/"));
		containerNORTH.add(tfDateAnnee);
		containerNORTH.add(lTranche);
		containerNORTH.add(containerRadioTranche);
		containerNORTH.add(lDuree);
		containerNORTH.add(cbDuree);
		containerNORTH.add(bRechercher);
		alimenterContainerCENTER(containerCENTER);
		containerSOUTH.add(bRetour);
		
		this.add(containerNORTH, BorderLayout.NORTH);
		this.add(containerCENTER, BorderLayout.CENTER);
		this.add(containerSOUTH, BorderLayout.SOUTH);
		
		rbMatin.addActionListener(this);
		rbApresMidi.addActionListener(this);
		rbSoir.addActionListener(this);
		bRetour.addActionListener(this);
		
	}

	public void alimenterContainerCENTER(Container c){
		
	}
	
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o.equals(bRetour)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelMenu(frame));
			frame.validate();
		} else if(o.equals(rbMatin) || o.equals(rbSoir)){
			cbDuree.removeAllItems();
			cbDuree.addItem(1);
			cbDuree.addItem(2);
			cbDuree.addItem(3);
			cbDuree.addItem(4);
		} else if(o.equals(rbApresMidi)){
			cbDuree.removeAllItems();
			cbDuree.addItem(1);
			cbDuree.addItem(2);
			cbDuree.addItem(3);
			cbDuree.addItem(4);
			cbDuree.addItem(5);
			cbDuree.addItem(6);
			cbDuree.addItem(7);
		}
	}

}
