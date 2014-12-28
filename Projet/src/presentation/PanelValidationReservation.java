package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import metier.CreerClient;
import metier.CreerReservation;
import metier.RechercheClient;
import metier.RechercheReservation;
import donnee.Client;
import donnee.Reservation;
import exception.ObjetInconnuException;

/**
 * Panel permettant la saisie/selection du client pour valider une reservation
 */
public class PanelValidationReservation extends JPanel implements ActionListener {

	private JFrame frame;
	private Reservation reservation;
	private List<Client> listeClient;
	
	private JList<Client> jlClient;
	private DefaultListModel<Client> model;
	
	private JPanel panelCENTER;
	private JPanel panelCenterRight;
	
	private Container cNom;
	private Container cPrenom;
	private Container cNumTel;
	private Container cResume;
	private Container cSOUTH;
	
	private JButton bSendInfos;
	private JButton bEnregistrer;
	private JButton bRetour;
	
	private JTextField tfNom;
	private JTextField tfPrenom;
	private JTextField tfNumTel;
	
	public PanelValidationReservation(JFrame frame, Reservation reservation) {
		SimpleDateFormat formatterDeb = new SimpleDateFormat("'Le 'dd/MM/yyyy' de 'HH'h '");
		SimpleDateFormat formatterFin = new SimpleDateFormat("HH");
		Date dateReservation = null;
		int duree;
		
		JScrollPane scrollpane;
		JLabel lNom = new JLabel("Nom");
		JLabel lPrenom = new JLabel("Prenom");
		JLabel lNumTel = new JLabel("Tel");
		
		this.frame = frame;
		this.reservation = reservation;

		panelCENTER = new JPanel();
		panelCENTER.setLayout(new BoxLayout(panelCENTER, BoxLayout.X_AXIS));
		this.setLayout(new BorderLayout());
		
		panelCenterRight = new JPanel();
		panelCenterRight.setLayout(new BoxLayout(panelCenterRight, BoxLayout.Y_AXIS));
		panelCenterRight.setBackground(Color.LIGHT_GRAY);
		
		cNom = new Container();
		cPrenom = new Container();
		cNumTel = new Container();
		cResume = new Container();
		cSOUTH = new Container();
		
		cNom.setLayout(new FlowLayout());
		cPrenom.setLayout(new FlowLayout());
		cNumTel.setLayout(new FlowLayout());
		cResume.setLayout(new FlowLayout());
		cSOUTH.setLayout(new FlowLayout());
		
		bSendInfos = new JButton(" >> ");
		bEnregistrer = new JButton("Valider");
		bEnregistrer.setMinimumSize(new Dimension(150, 20));
		bEnregistrer.setMaximumSize(new Dimension(150, 20));
		bEnregistrer.setPreferredSize(new Dimension(150, 20));
		bRetour = new JButton("Retour");
		bRetour.setMinimumSize(new Dimension(150, 20));
		bRetour.setMaximumSize(new Dimension(150, 20));
		bRetour.setPreferredSize(new Dimension(150, 20));
		
		tfNom = new JTextField();
		tfNom.setMinimumSize(new Dimension(150, 20));
		tfNom.setMaximumSize(new Dimension(150, 20));
		tfNom.setPreferredSize(new Dimension(150, 20));
		tfNom.setBackground(Color.WHITE);
		tfPrenom = new JTextField();
		tfPrenom.setMinimumSize(new Dimension(150, 20));
		tfPrenom.setMaximumSize(new Dimension(150, 20));
		tfPrenom.setPreferredSize(new Dimension(150, 20));
		tfPrenom.setBackground(Color.WHITE);
		tfNumTel = new JTextField();
		tfNumTel.setMinimumSize(new Dimension(150, 20));
		tfNumTel.setMaximumSize(new Dimension(150, 20));
		tfNumTel.setPreferredSize(new Dimension(150, 20));
		tfNumTel.setBackground(Color.WHITE);

		lNom.setMinimumSize(new Dimension(150, 20));
		lNom.setMaximumSize(new Dimension(150, 20));
		lNom.setPreferredSize(new Dimension(150, 20));
		lPrenom.setMinimumSize(new Dimension(150, 20));
		lPrenom.setMaximumSize(new Dimension(150, 20));
		lPrenom.setPreferredSize(new Dimension(150, 20));
		lNumTel.setMinimumSize(new Dimension(150, 20));
		lNumTel.setMaximumSize(new Dimension(150, 20));
		lNumTel.setPreferredSize(new Dimension(150, 20));
		
		dateReservation = reservation.getDate();
		duree = reservation.getPlage();

		
		cResume.add(new JLabel("<html>Resume de la reservation <br/><br/>"
				+ "Type : " + (reservation.getSalle().getTypeSalle().getTypeSalle().equals("petite")?"Petite salle":(reservation.getSalle().getTypeSalle().getTypeSalle().equals("grande")?"Grande salle":"Salle equipee")) + "<br/>"
				+ (formatterDeb.format(dateReservation) 
						+ "a " + (Integer.parseInt(formatterFin.format(dateReservation))+duree) + "h ")
				+ "</html>"));
		cResume.add(new JLabel());
		cResume.add(new JLabel( ));
		
		
		cNom.add(lNom);
		cNom.add(tfNom);
		cPrenom.add(lPrenom);
		cPrenom.add(tfPrenom);
		cNumTel.add(lNumTel);
		cNumTel.add(tfNumTel);
		
		RechercheClient metierRechercheClient = new RechercheClient();
		listeClient = metierRechercheClient.listerClients();
		model = new DefaultListModel<Client>();
		for(Client clt : listeClient){
			model.addElement(clt);
		}
		
		jlClient = new JList<Client>(model);
		jlClient.setCellRenderer(new RendererJListClient());
		jlClient.setLayoutOrientation(JList.VERTICAL);
		jlClient.setMinimumSize(new Dimension(200, 300));
		jlClient.setMaximumSize(new Dimension(200, 300));
		jlClient.setPreferredSize(null);
		jlClient.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
		
		scrollpane = new JScrollPane(jlClient);
		scrollpane.setMaximumSize(new Dimension(15, 300));
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		panelCenterRight.setMaximumSize(new Dimension(200, 300));
		panelCenterRight.add(cResume);
		panelCenterRight.add(cNom);
		panelCenterRight.add(cPrenom);
		panelCenterRight.add(cNumTel);
		panelCenterRight.add(new JLabel(" "));
		panelCenterRight.add(bEnregistrer);
		
		panelCENTER.add(new JLabel("          "));
		panelCENTER.add(jlClient);
		panelCENTER.add(scrollpane);
		panelCENTER.add(new JLabel(" "));
		panelCENTER.add(bSendInfos);
		panelCENTER.add(new JLabel(" "));
		panelCENTER.add(panelCenterRight);
		
		cSOUTH.add(bRetour);
		
		this.add(panelCENTER, BorderLayout.CENTER);
		this.add(cSOUTH, BorderLayout.SOUTH);
		
		bSendInfos.addActionListener(this);
		bEnregistrer.addActionListener(this);
		bRetour.addActionListener(this);
	}

	/**
	 * Enregistre la reservation pour le client saisi
	 * Demande une validation avant lenregistrement de la reservation
	 * Cree prealablement le client sil nexiste pas encore
	 * Demande une validation avant lenregistrement du nouveau client
	 */
	public void enregistrerReservation(){
		CreerReservation metierCreerReservation = new CreerReservation();
		CreerClient metierCreerClient = new CreerClient();
		RechercheClient metierRechClt = new RechercheClient();
		int rep;
		
		if(tfNom.getText().equals("") || tfPrenom.equals("") || tfNumTel.equals("")){
			JOptionPane.showMessageDialog(frame, "Tous les champs ne sont pas remplis !");
			return;
		}
		
		try{
			//on recherche le client, on enregistre la reservation sil existe
			metierRechClt.rechercheClient(tfNom.getText(), tfPrenom.getText(), tfNumTel.getText());
			rep = JOptionPane.showConfirmDialog(
					frame,
					"Valider la reservation?",
				    "Validation",
				    JOptionPane.YES_NO_OPTION);
			if(!(rep==JOptionPane.OK_OPTION)){
				return;
			}
			metierCreerReservation.creerReservation(reservation, tfNom.getText(), tfPrenom.getText(), tfNumTel.getText());
		} catch(ObjetInconnuException e){
			//le client nexiste pas, on demande son enregistrement
			rep = JOptionPane.showConfirmDialog(
					frame,
					"Nouveau client : Creer?",
				    "Nouveau client",
				    JOptionPane.YES_NO_OPTION);
			if(!(rep==JOptionPane.OK_OPTION)){
				return;
			}
			metierCreerClient.creerClient(tfNom.getText(), tfPrenom.getText(), tfNumTel.getText());
			//On demande la validation de la reservation
			rep = JOptionPane.showConfirmDialog(
					frame,
					"Valider la reservation?",
				    "Validation",
				    JOptionPane.YES_NO_OPTION);
			if(!(rep==JOptionPane.OK_OPTION)){
				listeClient = metierRechClt.listerClients();
				model.removeAllElements();
				for(Client clt : listeClient){
					model.addElement(clt);
				}
				return;
			}
			metierCreerReservation.creerReservation(reservation, tfNom.getText(), tfPrenom.getText(), tfNumTel.getText());
		}
		
		JOptionPane.showMessageDialog(this, "Reservation enregistree");
		
		frame.getContentPane().removeAll();
		frame.getContentPane().add(new PanelReservationAuto(frame));
		frame.validate();
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o.equals(bSendInfos)){
			if(!jlClient.isSelectionEmpty()){
				tfNom.setText(jlClient.getSelectedValue().getNom());
				tfPrenom.setText(jlClient.getSelectedValue().getPrenom());
				tfNumTel.setText(jlClient.getSelectedValue().getNumTel());
			}
		} else if(o.equals(bEnregistrer)){
			enregistrerReservation();
		} else if(o.equals(bRetour)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelMenu(frame));
			frame.validate();
		}
	}
	
	private class RendererJListClient implements ListCellRenderer<Client> {
		private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
		
		public RendererJListClient() {
			
		}
		public Component getListCellRendererComponent(JList<? extends Client> jlist, Client client,
				int index, boolean isSelected, boolean cellHasFocus) {
			JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(jlist, client, index,
			        isSelected, cellHasFocus);
			String newText=client.getNom() + "   ("+client.getNumTel()+")";
			renderer.setText(newText);
		    renderer.setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
			return renderer;
		}
		
	}
}
