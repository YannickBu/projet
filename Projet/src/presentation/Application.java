package presentation;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;

import fabrique.FabConnexion;


/**
 * Classe de lancement - Cree la JFrame
 */
public class Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		PanelMenu menu = new PanelMenu(frame);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					FabConnexion.closeConnexion();
				} catch (SQLException se) {
					System.out.println("Erreur de fermeture de la connexion - "+se.getMessage());
				}
				System.exit(0);
			}
		});
		
		frame.setSize(600, 400);
		frame.setLocationRelativeTo(null);
		
		frame.getContentPane().add(menu, BorderLayout.CENTER);
		
		frame.setVisible(true);
	}

}
