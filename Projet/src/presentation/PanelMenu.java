package presentation;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
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
		
		this.setBackground(Color.WHITE);
		this.setLayout(new GridBagLayout());
		
		Container c = new Container();
		this.add(c, new GridBagConstraints());
		

		GroupLayout layout = new GroupLayout(c);
		c.setLayout(layout);
		
		bVisu = new JButton("Visualiser une reservation");
		bVisu.setBackground(Color.LIGHT_GRAY);
		bReserv = new JButton("Reservation automatique");
		bReserv.setBackground(Color.LIGHT_GRAY);
		bEdit = new JButton("Editer infos client");
		bEdit.setBackground(Color.LIGHT_GRAY);
		
		bVisu.addActionListener(this);
		bReserv.addActionListener(this);
		bEdit.addActionListener(this);
		
		bVisu.setMinimumSize(new Dimension(250,23));
		bReserv.setMinimumSize(new Dimension(250,23));
		bEdit.setMinimumSize(new Dimension(250,23));
		
		layout.setHorizontalGroup(
                layout.createParallelGroup()
                    .addComponent(bVisu)
                    .addComponent(bReserv)
                    .addComponent(bEdit)
                );
		
		layout.setVerticalGroup(
			layout.createSequentialGroup()
			.addComponent(bVisu)
			.addComponent(bReserv)
			.addComponent(bEdit)
		);

		layout.setAutoCreateGaps(true);
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
