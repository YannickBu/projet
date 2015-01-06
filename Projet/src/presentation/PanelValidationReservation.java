package presentation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import metier.ConfirmerReservation;
import metier.CreerClient;
import metier.CreerReservation;
import metier.RechercheClient;
import donnee.Client;
import donnee.ForfaitClient;
import donnee.Reservation;
import exception.ObjetInconnuException;
import fabrique.FabForfaitClient;

/**
 * Panel permettant la saisie/selection du client pour valider une reservation
 */
public class PanelValidationReservation extends JPanel implements ActionListener, DocumentListener {

	private JFrame frame;
	
	private List<Client> listeClient;
	
	private List<Reservation> listeReservations;
	private JList<Reservation> jListeReservations;
	private DefaultListModel<Reservation> modelReservation;
	
	private JList<Client> jlClient;
	private DefaultListModel<Client> model;
	
	private JPanel panelCenterRight;
	private JPanel panelOptionsPaiement;
	
	private JButton bSendInfos;
	private JButton bEnregistrer;
	private JButton bRetour;
	private JButton bOptionsPaiement;
	
	private JTextField tfNom;
	private JTextField tfPrenom;
	private JTextField tfNumTel;
	
	private JComboBox cbForfait;
	private JCheckBox checkFidelite;
	private JCheckBox checkPaiement;
	
	private ButtonGroup bgChoixPaiement;
	private JRadioButton rbImmediat;
	private JRadioButton rbDiffere;

	private JLabel lTypeSalle;
	private JLabel lDate;
	private JLabel lPrix;
	
	private Double[] infosPaiement;
	
	public PanelValidationReservation(JFrame frame, List<Reservation> listeReservations) {
		SimpleDateFormat formatterDeb = new SimpleDateFormat("'Le 'dd/MM/yyyy' de 'HH'h '");
		SimpleDateFormat formatterFin = new SimpleDateFormat("HH");
		infosPaiement = null;
		Date dateReservation = null;
		int duree;

		dateReservation = listeReservations.get(0).getDate();
		duree = listeReservations.get(0).getPlage();
		
		JScrollPane scrollpane;
		JLabel lNom = new JLabel("Nom");
		JLabel lPrenom = new JLabel("Prenom");
		JLabel lNumTel = new JLabel("Tel");
		lTypeSalle = new JLabel();
		lDate = new JLabel();
		lPrix = new JLabel();
		lTypeSalle.setOpaque(true);
		lTypeSalle.setBackground(Color.LIGHT_GRAY);
		lDate.setOpaque(true);
		lDate.setBackground(Color.LIGHT_GRAY);
		lPrix.setOpaque(true);
		lPrix.setBackground(Color.LIGHT_GRAY);
		lTypeSalle.setText("Type : " + (listeReservations.get(0).getSalle().getTypeSalle().getTypeSalle().equals("petite")?"Petite salle":(listeReservations.get(0).getSalle().getTypeSalle().getTypeSalle().equals("grande")?"Grande salle":"Salle equipee")));
		lDate.setText(formatterDeb.format(dateReservation) 
				+ "a " + (Integer.parseInt(formatterFin.format(dateReservation))+duree) + "h");

		
		cbForfait = new JComboBox();
		cbForfait.setRenderer(new RendererCBForfait());
		cbForfait.setMinimumSize(new Dimension(120, 20));
		cbForfait.setMaximumSize(new Dimension(120, 20));
		cbForfait.setPreferredSize(new Dimension(120, 20));
		cbForfait.addItem(null);
		
		checkFidelite = new JCheckBox();
		checkFidelite.setBackground(Color.LIGHT_GRAY);
		
		checkPaiement = new JCheckBox();
		checkPaiement.setBackground(Color.LIGHT_GRAY);
		
		bgChoixPaiement = new ButtonGroup();
		rbDiffere = new JRadioButton();
		rbImmediat = new JRadioButton();
		rbDiffere.setSelected(true);
		rbDiffere.setBackground(Color.LIGHT_GRAY);
		rbImmediat.setBackground(Color.LIGHT_GRAY);
		bgChoixPaiement.add(rbDiffere);
		bgChoixPaiement.add(rbImmediat);
		
		this.frame = frame;
		this.listeReservations = listeReservations;

		this.setLayout(new GridBagLayout());
		

		modelReservation = new DefaultListModel<Reservation>();
		jListeReservations = new JList<Reservation>(modelReservation);
		jListeReservations.setCellRenderer(new RendererJListReservation());
		
		for(Reservation r : listeReservations){
			modelReservation.addElement(r);
		}
		
		GridBagConstraints GBC = new GridBagConstraints();
		panelCenterRight = new JPanel();
		panelCenterRight.setLayout(new GridBagLayout());
		panelCenterRight.setBackground(Color.LIGHT_GRAY);
		//panelCenterRight.setPreferredSize(new Dimension(200, 300));
		
		panelOptionsPaiement = new JPanel();
		panelOptionsPaiement.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelOptionsPaiement.setBackground(Color.LIGHT_GRAY);	
		panelOptionsPaiement.setVisible(false);
		
		bSendInfos = new JButton(" >> ");
		bSendInfos.setBackground(Color.WHITE);
		bEnregistrer = new JButton("Valider");
		bEnregistrer.setBackground(Color.WHITE);
		bEnregistrer.setMinimumSize(new Dimension(150, 20));
		bEnregistrer.setMaximumSize(new Dimension(150, 20));
		bEnregistrer.setPreferredSize(new Dimension(150, 20));
		bRetour = new JButton("Retour");
		bRetour.setBackground(Color.WHITE);
		bRetour.setMinimumSize(new Dimension(150, 20));
		bRetour.setMaximumSize(new Dimension(150, 20));
		bRetour.setPreferredSize(new Dimension(150, 20));
		bOptionsPaiement = new JButton("Forfaits et pts fidelite");
		bOptionsPaiement.setBackground(Color.WHITE);
		
		tfNom = new JTextField();
		tfNom.setMinimumSize(new Dimension(100, 20));
		tfNom.setMaximumSize(new Dimension(100, 20));
		tfNom.setPreferredSize(new Dimension(100, 20));
		tfNom.setBackground(Color.WHITE);
		tfPrenom = new JTextField();
		tfPrenom.setMinimumSize(new Dimension(100, 20));
		tfPrenom.setMaximumSize(new Dimension(100, 20));
		tfPrenom.setPreferredSize(new Dimension(100, 20));
		tfPrenom.setBackground(Color.WHITE);
		tfNumTel = new JTextField();
		tfNumTel.setMinimumSize(new Dimension(100, 20));
		tfNumTel.setMaximumSize(new Dimension(100, 20));
		tfNumTel.setPreferredSize(new Dimension(100, 20));
		tfNumTel.setBackground(Color.WHITE);

		lNom.setMinimumSize(new Dimension(50, 20));
		lNom.setMaximumSize(new Dimension(50, 20));
		lNom.setPreferredSize(new Dimension(50, 20));
		lPrenom.setMinimumSize(new Dimension(50, 20));
		lPrenom.setMaximumSize(new Dimension(50, 20));
		lPrenom.setPreferredSize(new Dimension(50, 20));
		lNumTel.setMinimumSize(new Dimension(50, 20));
		lNumTel.setMaximumSize(new Dimension(50, 20));
		lNumTel.setPreferredSize(new Dimension(50, 20));
		
		RechercheClient metierRechercheClient = new RechercheClient();
		listeClient = metierRechercheClient.listerClients();
		model = new DefaultListModel<Client>();
		for(Client clt : listeClient){
			model.addElement(clt);
		}
		
		jlClient = new JList<Client>(model);
		jlClient.setCellRenderer(new RendererJListClient());
		jlClient.setLayoutOrientation(JList.VERTICAL);
		jlClient.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
		
		scrollpane = new JScrollPane(jlClient);
		scrollpane.setPreferredSize(new Dimension(200, 300));
		scrollpane.setMinimumSize(new Dimension(200, 300));
		scrollpane.setMaximumSize(new Dimension(200, 300));
		
		panelOptionsPaiement.add(bOptionsPaiement);
		
		if(listeReservations.size()==1){
	
			infosPaiement = new ConfirmerReservation().getInfosApresPaiement(listeReservations.get(0), null, null, false);
			
			GBC.fill = GridBagConstraints.HORIZONTAL; 
			GBC.anchor = GridBagConstraints.NORTH;
			GBC.gridy = 0;
			GBC.gridx = 0;
			GBC.gridheight = 1;
			GBC.gridwidth = 2;
			GBC.weightx = 1;
			GBC.weighty = 1;
			panelCenterRight.add(lTypeSalle,GBC);
			
			GBC.gridy++;
			panelCenterRight.add(lDate,GBC);
			
			GBC.gridy++;
			lPrix.setText("Prix : " + infosPaiement[0] + " euro"+(infosPaiement[0]>1?"s":""));
			panelCenterRight.add(lPrix,GBC);
		} else {
			infosPaiement = new ConfirmerReservation().getInfosApresPaiement(listeReservations, null, null, false);
			
			GBC.fill = GridBagConstraints.HORIZONTAL; 
			GBC.anchor = GridBagConstraints.NORTH;
			GBC.gridy = 0;
			GBC.gridx = 0;
			GBC.gridheight = 1;
			GBC.gridwidth = 2;
			GBC.weightx = 1;
			GBC.weighty = 1;
			panelCenterRight.add(lTypeSalle,GBC);
			
			GBC.gridy++;
			JScrollPane scrollPaneReservations = new JScrollPane(jListeReservations);
			scrollPaneReservations.setPreferredSize(new Dimension(220, 100));
			scrollPaneReservations.setMinimumSize(new Dimension(220, 100));
			scrollPaneReservations.setMaximumSize(new Dimension(220, 100));
			panelCenterRight.add(scrollPaneReservations,GBC);
			
			GBC.gridy++;
			lPrix.setText("Prix : " + infosPaiement[0] + " euro"+(infosPaiement[0]>1?"s":""));
			panelCenterRight.add(lPrix,GBC);
		}
		
		GBC.gridy++;
		GBC.gridwidth = 1;
		panelCenterRight.add(new JLabel("Paiement :"),GBC);
		GBC.gridy++;
		panelCenterRight.add(new JLabel("Differe",JLabel.RIGHT),GBC);
		GBC.gridx++;
		panelCenterRight.add(rbDiffere,GBC);
		GBC.gridx=0;
		GBC.gridy++;
		panelCenterRight.add(new JLabel("Immediat",JLabel.RIGHT),GBC);
		GBC.gridx++;
		panelCenterRight.add(rbImmediat,GBC);
		GBC.gridx = 0;
		GBC.gridy++;
		GBC.gridwidth = 2;
		GBC.fill = GridBagConstraints.VERTICAL; 
		panelCenterRight.add(panelOptionsPaiement,GBC);
		GBC.gridy++;
		GBC.gridwidth = 1;
		GBC.fill = GridBagConstraints.HORIZONTAL; 
		panelCenterRight.add(lNom,GBC);
		GBC.gridx++;
		panelCenterRight.add(tfNom,GBC);
		GBC.gridx = 0;
		GBC.gridy++;
		panelCenterRight.add(lPrenom,GBC);
		GBC.gridx++;
		panelCenterRight.add(tfPrenom,GBC);
		GBC.gridx = 0;
		GBC.gridy++;
		panelCenterRight.add(lNumTel,GBC);
		GBC.gridx++;
		panelCenterRight.add(tfNumTel,GBC);
		GBC.gridx = 0;
		GBC.gridy++;
		GBC.gridwidth = 2;
		GBC.insets = new Insets(15, 5, 5, 5);
		panelCenterRight.add(bEnregistrer,GBC);
		GBC.gridy++;
		
		
		GBC.fill = GridBagConstraints.BOTH; 
		GBC.anchor = GridBagConstraints.NORTH;
		GBC.gridy = 0;
		GBC.gridx = 0;
		GBC.gridheight = 1;
		GBC.gridwidth = 3;
		GBC.weightx = 1;
		GBC.weighty = 1;
		GBC.insets = new Insets(0, 0, 0, 0);
		JLabel lTitreHaut = new JLabel("Validation de la reservation",JLabel.CENTER);
		lTitreHaut.setOpaque(true);
		lTitreHaut.setBackground(Color.GRAY);
		this.add(lTitreHaut,GBC);
		GBC.anchor = GridBagConstraints.EAST;
		GBC.fill = GridBagConstraints.BOTH; 
		GBC.gridy++;
		GBC.gridwidth=1;
		GBC.insets = new Insets(3, 10, 0, 0);
		this.add(scrollpane,GBC);
		GBC.fill = GridBagConstraints.NONE;
		GBC.anchor = GridBagConstraints.CENTER;
		GBC.gridx++;
		GBC.weightx = 0.1;
		GBC.weighty = 0.1;
		GBC.insets = new Insets(3, 5, 0, 5);
		this.add(bSendInfos,GBC);
		GBC.fill = GridBagConstraints.BOTH;
		GBC.anchor = GridBagConstraints.EAST;
		GBC.gridx++;
		GBC.weightx = 1;
		GBC.weighty = 1;
		GBC.insets = new Insets(3, 0, 0, 10);
		this.add(panelCenterRight,GBC);
		GBC.gridy++;
		GBC.gridx = 0;
		GBC.gridwidth = 3;
		GBC.fill = GridBagConstraints.NONE;
		GBC.anchor = GridBagConstraints.CENTER;
		GBC.insets = new Insets(5, 0, 3, 0);
		this.add(bRetour,GBC);
		
		
		bSendInfos.addActionListener(this);
		bEnregistrer.addActionListener(this);
		bRetour.addActionListener(this);
		bOptionsPaiement.addActionListener(this);
		cbForfait.addActionListener(this);
		checkFidelite.addActionListener(this);
		rbDiffere.addActionListener(this);
		rbImmediat.addActionListener(this);
		tfNom.getDocument().addDocumentListener(this);
		tfPrenom.getDocument().addDocumentListener(this);
		tfNumTel.getDocument().addDocumentListener(this);
	}

	/**
	 * Enregistre la reservation pour le client saisi
	 * Demande une validation avant lenregistrement de la reservation
	 * Cree prealablement le client sil nexiste pas encore
	 * Demande une validation avant lenregistrement du nouveau client
	 */
	private void enregistrerReservation(){
		SimpleDateFormat formatterDeb = new SimpleDateFormat("'Le 'dd/MM/yyyy' de 'HH'h '");
		SimpleDateFormat formatterDeb2 = new SimpleDateFormat("'A partir du 'dd/MM/yyyy");
		SimpleDateFormat formatterFin = new SimpleDateFormat("HH");
		Date dateReservation = listeReservations.get(0).getDate();
		int duree = listeReservations.get(0).getPlage();
		int reponse;
		
		if(tfNom.getText().equals("") || tfPrenom.equals("") || tfNumTel.equals("")){
			JOptionPane.showMessageDialog(frame, "Tous les champs ne sont pas remplis !");
			return;
		}
		
		try{
			//on recherche le client, on enregistre la reservation sil existe
			new RechercheClient().rechercheClient(tfNom.getText(), tfPrenom.getText(), tfNumTel.getText());
			if(listeReservations.size()==1){
				reponse = JOptionPane.showConfirmDialog(
					frame,
					"<html>"
							+ "Type : " + (listeReservations.get(0).getSalle().getTypeSalle().getTypeSalle().equals("petite")?"Petite salle":(listeReservations.get(0).getSalle().getTypeSalle().getTypeSalle().equals("grande")?"Grande salle":"Salle equipee")) + "<br/>"
							+ (formatterDeb.format(dateReservation) 
									+ "a " + (Integer.parseInt(formatterFin.format(dateReservation))+duree) + "h<br/>"
									+ "Prix : " + infosPaiement[0] + " euro"+(infosPaiement[0]>1?"s":""))+"<br/>"
							+ (cbForfait.getSelectedIndex()>0?"Temps restant sur votre forfait "+((ForfaitClient)cbForfait.getSelectedItem()).getForfait().getTypeForfait()+" : "+infosPaiement[2].intValue()+"h<br/>":"")
							+ (checkFidelite.isSelected()?"Points fidelite restant : "+infosPaiement[1].intValue():"")
							+ "<br/><br/>Valider la reservation?"
					+ "</html>",
				    "Validation",
				    JOptionPane.YES_NO_OPTION);
			} else {
				reponse = JOptionPane.showConfirmDialog(
						frame,
						"<html>"
								+ "Type : " + (listeReservations.get(0).getSalle().getTypeSalle().getTypeSalle().equals("petite")?"Petite salle":(listeReservations.get(0).getSalle().getTypeSalle().getTypeSalle().equals("grande")?"Grande salle":"Salle equipee")) + "<br/>"
								+ (formatterDeb2.format(dateReservation) 
										+ " pendant "+listeReservations.size()+" semaines<br/>"
										+ "Prix : " + infosPaiement[0] + " euro"+(infosPaiement[0]>1?"s":""))+"<br/>"
								+ (cbForfait.getSelectedIndex()>0?"Temps restant sur votre forfait "+((ForfaitClient)cbForfait.getSelectedItem()).getForfait().getTypeForfait()+" : "+infosPaiement[2].intValue()+"h<br/>":"")
								+ (checkFidelite.isSelected()?"Points fidelite restant : "+infosPaiement[1].intValue():"")
								+ "<br/><br/>Valider la reservation?"
						+ "</html>",
					    "Validation",
					    JOptionPane.YES_NO_OPTION);
			}
			if(!(reponse==JOptionPane.OK_OPTION)){
				return;
			}

			if(rbImmediat.isSelected() && cbForfait.getSelectedIndex()>0){
				((ForfaitClient)cbForfait.getSelectedItem()).setTempsRestant(infosPaiement[2].intValue());
			}
			new CreerReservation().creerReservation(listeReservations, tfNom.getText(), tfPrenom.getText(), tfNumTel.getText(), 
					rbImmediat.isSelected() && cbForfait.getSelectedIndex()>0?((ForfaitClient)cbForfait.getSelectedItem()):null, 
					rbImmediat.isSelected() && checkFidelite.isSelected()?infosPaiement[1].intValue():null);
		} catch(ObjetInconnuException e){
			//le client nexiste pas, on demande son enregistrement
			reponse = JOptionPane.showConfirmDialog(
					frame,
					"Nouveau client : Creer?",
				    "Nouveau client",
				    JOptionPane.YES_NO_OPTION);
			if(!(reponse==JOptionPane.OK_OPTION)){
				return;
			}
			Client c = new CreerClient().creerClient(tfNom.getText(), tfPrenom.getText(), tfNumTel.getText());
			genererPanelOptionsPaiement(c);
			//On demande la validation de la reservation
			if(listeReservations.size()==1){
				reponse = JOptionPane.showConfirmDialog(
					frame,
					"<html>"
							+ "Type : " + (listeReservations.get(0).getSalle().getTypeSalle().getTypeSalle().equals("petite")?"Petite salle":(listeReservations.get(0).getSalle().getTypeSalle().getTypeSalle().equals("grande")?"Grande salle":"Salle equipee")) + "<br/>"
							+ (formatterDeb.format(dateReservation) 
									+ "a " + (Integer.parseInt(formatterFin.format(dateReservation))+duree) + "h<br/>"
									+ "Prix : " + infosPaiement[0] + " euro"+(infosPaiement[0]>1?"s":""))+"<br/>"
							+ (cbForfait.getSelectedIndex()>0?"Temps restant sur votre forfait "+((ForfaitClient)cbForfait.getSelectedItem()).getForfait().getTypeForfait()+" : "+infosPaiement[2].intValue()+"h<br/>":"")
							+ (checkFidelite.isSelected()?"Points fidelite restant : "+infosPaiement[1].intValue():"")
							+ "<br/><br/>Valider la reservation?"
					+ "</html>",
				    "Validation",
				    JOptionPane.YES_NO_OPTION);
			} else {
				reponse = JOptionPane.showConfirmDialog(
						frame,
						"<html>"
								+ "Type : " + (listeReservations.get(0).getSalle().getTypeSalle().getTypeSalle().equals("petite")?"Petite salle":(listeReservations.get(0).getSalle().getTypeSalle().getTypeSalle().equals("grande")?"Grande salle":"Salle equipee")) + "<br/>"
								+ (formatterDeb2.format(dateReservation) 
										+ " pendant "+listeReservations.size()+" semaines<br/>"
										+ "Prix : " + infosPaiement[0] + " euro"+(infosPaiement[0]>1?"s":""))+"<br/>"
								+ (cbForfait.getSelectedIndex()>0?"Temps restant sur votre forfait "+((ForfaitClient)cbForfait.getSelectedItem()).getForfait().getTypeForfait()+" : "+infosPaiement[2].intValue()+"h<br/>":"")
								+ (checkFidelite.isSelected()?"Points fidelite restant : "+infosPaiement[1].intValue():"")
								+ "<br/><br/>Valider la reservation?"
						+ "</html>",
					    "Validation",
					    JOptionPane.YES_NO_OPTION);
			}
			if(!(reponse==JOptionPane.OK_OPTION)){
				listeClient = new RechercheClient().listerClients();
				model.removeAllElements();
				for(Client clt : listeClient){
					model.addElement(clt);
				}
				return;
			}
			if(rbImmediat.isSelected() && cbForfait.getSelectedIndex()>0){
				((ForfaitClient)cbForfait.getSelectedItem()).setTempsRestant(infosPaiement[2].intValue());
			}
			new CreerReservation().creerReservation(listeReservations, tfNom.getText(), tfPrenom.getText(), tfNumTel.getText(), 
					rbImmediat.isSelected() && cbForfait.getSelectedIndex()>0?((ForfaitClient)cbForfait.getSelectedItem()):null, 
					rbImmediat.isSelected() && checkFidelite.isSelected()?infosPaiement[1].intValue():null);
		}
		
		JOptionPane.showMessageDialog(this, "Reservation enregistree");
		
		frame.getContentPane().removeAll();
		frame.getContentPane().add(new PanelReservationAuto(frame));
		frame.validate();
	}
	
	/**
	 * @param c Client - Permet de recuperer et dafficher ses points de fidelite restant
	 */
	private void genererPanelOptionsPaiement(Client c) {
		panelOptionsPaiement.removeAll();
		panelOptionsPaiement.setLayout(new GridBagLayout());
		GridBagConstraints GBC = new GridBagConstraints();
		GBC.fill = GridBagConstraints.BOTH; 
		GBC.anchor = GridBagConstraints.NORTH;
		GBC.gridy = 0;
		GBC.gridx = 0;
		GBC.gridheight = 1;
		GBC.gridwidth = 2;
		GBC.weightx = 1;
		GBC.weighty = 1;
		panelOptionsPaiement.add(cbForfait,GBC);
		GBC.gridy=1;
		GBC.gridwidth = 1;
		panelOptionsPaiement.add(new JLabel("Pts fidelite ("+c.getPointsFidelite()+")"),GBC);
		GBC.gridx=1;
		panelOptionsPaiement.add(checkFidelite,GBC);
		panelOptionsPaiement.validate();
		panelOptionsPaiement.repaint();
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o.equals(bSendInfos)){
			if(!jlClient.isSelectionEmpty()){
				tfNom.setText(jlClient.getSelectedValue().getNom());
				tfPrenom.setText(jlClient.getSelectedValue().getPrenom());
				tfNumTel.setText(jlClient.getSelectedValue().getNumTel());
			}
			
			checkFidelite.setSelected(false);
			
			Client c = null;
			List<ForfaitClient> listeFC = null;

			c = new RechercheClient().rechercheClient(tfNom.getText(), tfPrenom.getText(), tfNumTel.getText());
			
			listeFC = FabForfaitClient.getInstance().rechercherForfaitClient(c.getId(), listeReservations.get(0).getSalle().getTypeSalle().getIdTypeSalle());
			
			cbForfait.removeAllItems();
			cbForfait.addItem(null);
			for(ForfaitClient fc : listeFC){
				cbForfait.addItem(fc);
			}
			
			genererPanelOptionsPaiement(c);
			
			tfNom.getDocument().addDocumentListener(this);
			tfPrenom.getDocument().addDocumentListener(this);
			tfNumTel.getDocument().addDocumentListener(this);
		} else if(o.equals(bEnregistrer)){
			enregistrerReservation();
		} else if(o.equals(bRetour)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelReservationAuto(frame));
			frame.validate();
		} else if(o.equals(rbDiffere)){
			for(Reservation r : listeReservations){
				r.setEstPaye(false);
			}
			panelOptionsPaiement.setVisible(false);
		} else if(o.equals(rbImmediat)){
			for(Reservation r : listeReservations){
				r.setEstPaye(true);
			}
			panelOptionsPaiement.setVisible(true);
		} else if(o.equals(cbForfait) || o.equals(checkFidelite)){
			infosPaiement = null;
			Client c = null;
			try{
				c = new RechercheClient().rechercheClient(tfNom.getText(), tfPrenom.getText(), tfNumTel.getText());
			} catch(ObjetInconnuException oie){}
			if(listeReservations.size()==1){
				infosPaiement = new ConfirmerReservation().getInfosApresPaiement(listeReservations.get(0), c!=null?c.getId():null,cbForfait.getSelectedItem()!=null?((ForfaitClient)cbForfait.getSelectedItem()).getIdForfaitClient():null, checkFidelite.isSelected()?true:false);
			}else{
				infosPaiement = new ConfirmerReservation().getInfosApresPaiement(listeReservations, c!=null?c.getId():null,cbForfait.getSelectedItem()!=null?((ForfaitClient)cbForfait.getSelectedItem()).getIdForfaitClient():null, checkFidelite.isSelected()?true:false);
			}
			lPrix.setText("Prix : " + infosPaiement[0] + " euro"+(infosPaiement[0]>1?"s":""));
		} else if(o.equals(bOptionsPaiement)){
			if(!"".equals(tfNom.getText()) && !"".equals(tfPrenom.getText()) && !"".equals(tfNumTel.getText())){
				Client c = null;
				List<ForfaitClient> listeFC = null;
				try{
					c = new RechercheClient().rechercheClient(tfNom.getText(), tfPrenom.getText(), tfNumTel.getText());
				} catch(ObjetInconnuException oie){
					cbForfait.removeAllItems();
					cbForfait.addItem(null);
					panelOptionsPaiement.removeAll();
					panelOptionsPaiement.setLayout(new FlowLayout());
					panelOptionsPaiement.add(bOptionsPaiement);
					panelOptionsPaiement.validate();
					panelOptionsPaiement.repaint();
					JOptionPane.showMessageDialog(this, "Client inexistant");
					return;
				}
				listeFC = FabForfaitClient.getInstance().rechercherForfaitClient(c.getId(), listeReservations.get(0).getSalle().getTypeSalle().getIdTypeSalle());
				
				cbForfait.removeAllItems();
				cbForfait.addItem(null);
				for(ForfaitClient fc : listeFC){
					cbForfait.addItem(fc);
				}
				
				genererPanelOptionsPaiement(c);
				
				tfNom.getDocument().addDocumentListener(this);
				tfPrenom.getDocument().addDocumentListener(this);
				tfNumTel.getDocument().addDocumentListener(this);
			}else{
				JOptionPane.showMessageDialog(this, "Les 3 champs doivent etre remplis !");
			}
		}
	}


	public void insertUpdate(DocumentEvent e) {
		cbForfait.removeAllItems();
		cbForfait.addItem(null);
		panelOptionsPaiement.removeAll();
		panelOptionsPaiement.setLayout(new FlowLayout());
		panelOptionsPaiement.add(bOptionsPaiement);
		panelOptionsPaiement.validate();
		panelOptionsPaiement.repaint();
		tfNom.getDocument().removeDocumentListener(this);
		tfPrenom.getDocument().removeDocumentListener(this);
		tfNumTel.getDocument().removeDocumentListener(this);
	}

	public void removeUpdate(DocumentEvent e) {
		cbForfait.removeAllItems();
		cbForfait.addItem(null);
		panelOptionsPaiement.removeAll();
		panelOptionsPaiement.setLayout(new FlowLayout());
		panelOptionsPaiement.add(bOptionsPaiement);
		panelOptionsPaiement.validate();
		panelOptionsPaiement.repaint();
		tfNom.getDocument().removeDocumentListener(this);
		tfPrenom.getDocument().removeDocumentListener(this);
		tfNumTel.getDocument().removeDocumentListener(this);
	}

	public void changedUpdate(DocumentEvent e) {}
	
	
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
	
	private class RendererCBForfait extends BasicComboBoxRenderer {
		
		public RendererCBForfait() {
			
		}
		@Override public Component getListCellRendererComponent(JList list, Object fc, int index, boolean isSelected, boolean cellHasFocus){
		        JLabel ret=(JLabel)super.getListCellRendererComponent(list,fc,index,isSelected,cellHasFocus);
		        if(fc != null){
		        	ret.setText("Forfait "+((ForfaitClient)fc).getForfait().getTypeForfait()+" ("+((ForfaitClient)fc).getTempsRestant()+"h)");
		        }else{
		        	ret.setText("Aucun forfait");
		        }
		        return ret;
		}
	}
	
	
	private class RendererJListReservation implements ListCellRenderer<Reservation> {
		private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
		
		public RendererJListReservation() {
			
		}
		public Component getListCellRendererComponent(JList<? extends Reservation> jlist, Reservation reservations,
				int index, boolean isSelected, boolean cellHasFocus) {
			JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(jlist, reservations, index, isSelected, cellHasFocus);
			SimpleDateFormat formatterDeb = new SimpleDateFormat("' Le 'dd/MM/yyyy' de 'HH'h '");
			SimpleDateFormat formatterFin = new SimpleDateFormat("HH");
			Date dateReservation = reservations.getDate();
			int duree = reservations.getPlage();
			
			String newText=(formatterDeb.format(dateReservation) 
							+ "a " + (Integer.parseInt(formatterFin.format(dateReservation))+duree) + "h ");
			
			renderer.setText(newText);
			renderer.setHorizontalAlignment(JLabel.CENTER);
			renderer.setBorder(null);
			renderer.setBackground(Color.WHITE);
			return renderer;
		}
		
	}
}
