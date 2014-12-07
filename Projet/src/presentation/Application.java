package presentation;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JFrame;

import metier.CreerReservation;
import fabrique.FabReservation;

public class Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		PanelMenu menu = new PanelMenu(frame);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		frame.setBounds(450, 250, 600, 400);
		
		frame.getContentPane().add(menu, BorderLayout.CENTER);
		
		//frame.pack();
		frame.setVisible(true);
		
		
		
		/*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		
		Date dateDeb;
		Date dateFin;
		try {
			dateDeb = formatter.parse("2013-11-06 15-00-00");
			dateFin = formatter.parse("2013-11-01 12-10-10");
			FabReservation.getInstance().creer(0, 1, dateDeb, 1, dateFin, true);
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		
		/*
		CreerReservation metier = new CreerReservation();
		metier.rechercheCreneauLibre("03-12-2014", 1, "matin", "petite");
		System.out.println("m");
		*/
	}

}
