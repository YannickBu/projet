package presentation;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PanelMenu extends JPanel implements ActionListener {

	JFrame frame;
	
	JButton bVisu;
	JButton bReserv;
	JButton bEdit;
	
	/**
	 * Methode qui permet d'afficher le menu 
	 * @param frame
	 */
	public PanelMenu(JFrame frame) {
		
		this.frame = frame;
		
		Container c = new Container();
		c.setLayout(new GridLayout(3, 1));
		this.add(c);
		
		bVisu = new JButton("Visualier une réservation");
		bReserv = new JButton("Réserver");
		bEdit = new JButton("Editer infos client");
		
		bVisu.addActionListener(this);
		bReserv.addActionListener(this);
		bEdit.addActionListener(this);
		
		c.add(bVisu);
		c.add(bReserv);
		c.add(bEdit);
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o.equals(bVisu)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelVisualiser(frame));
			frame.validate();
		} else if (o.equals(bReserv)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelReservation(frame));
			frame.validate();
		}
	}

}
