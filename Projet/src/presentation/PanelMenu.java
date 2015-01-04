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
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel affichant le menu principal
 */
public class PanelMenu extends JPanel implements ActionListener {

	JFrame frame;
	
	JButton bVisu;
	JButton bReservAuto;
	JButton bReserbManu;
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
		
		JLabel lTitre = new JLabel("Menu principal",JLabel.CENTER);
		JLabel lab = new JLabel(" ");

		GroupLayout layout = new GroupLayout(c);
		c.setLayout(layout);
		
		bVisu = new JButton("Visualiser une reservation");
		bVisu.setBackground(Color.LIGHT_GRAY);
		bReservAuto = new JButton("Reservation automatique");
		bReservAuto.setBackground(Color.LIGHT_GRAY);
		bReserbManu = new JButton("Reservation manuelle");
		bReserbManu.setBackground(Color.LIGHT_GRAY);
		bEdit = new JButton("Editer infos client");
		bEdit.setBackground(Color.LIGHT_GRAY);
		
		bVisu.addActionListener(this);
		bReservAuto.addActionListener(this);
		bReserbManu.addActionListener(this);
		bEdit.addActionListener(this);
		
		bVisu.setMinimumSize(new Dimension(250,23));
		bReservAuto.setMinimumSize(new Dimension(250,23));
		bReserbManu.setMinimumSize(new Dimension(250,23));
		bEdit.setMinimumSize(new Dimension(250,23));
		
		layout.setHorizontalGroup(
                layout.createParallelGroup()
                	.addComponent(lTitre, GroupLayout.Alignment.CENTER)
                    .addComponent(lab, GroupLayout.Alignment.CENTER)
        			.addComponent(bVisu, GroupLayout.Alignment.CENTER)
                    .addComponent(bReservAuto, GroupLayout.Alignment.CENTER)
                    .addComponent(bReserbManu, GroupLayout.Alignment.CENTER)
                    .addComponent(bEdit, GroupLayout.Alignment.CENTER)
                );
		
		layout.setVerticalGroup(
			layout.createSequentialGroup()
			.addComponent(lTitre)
			.addComponent(lab)
			.addComponent(bVisu)
			.addComponent(bReservAuto)
			.addComponent(bReserbManu)
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
		} else if (o.equals(bReservAuto)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelReservationAuto(frame));
			frame.validate();
		} else if (o.equals(bReserbManu)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelReservationManu(frame));
			frame.validate();
		} else if (o.equals(bEdit)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelSaisieClient(frame));
			frame.validate();
		}
	}

}
