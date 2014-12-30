package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
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
	private Reservation reservation;
	private List<Client> listeClient;
	
	private JList<Client> jlClient;
	private DefaultListModel<Client> model;
	
	private JPanel panelCENTER;
	private JPanel panelCenterRight;
	private JPanel panelOptionsPaiement;
	
	private JPanel cNom;
	private JPanel cPrenom;
	private JPanel cNumTel;
	private JPanel cResume;
	private JPanel cSOUTH;
	
	private JButton bSendInfos;
	private JButton bEnregistrer;
	private JButton bRetour;
	private JButton bOptionsPaiement;
	
	private JTextField tfNom;
	private JTextField tfPrenom;
	private JTextField tfNumTel;
	
	private JComboBox cbForfait;
	private JCheckBox checkFidelite;

	private JLabel lResume;
	
	public PanelValidationReservation(JFrame frame, Reservation reservation) {
		SimpleDateFormat formatterDeb = new SimpleDateFormat("'Le 'dd/MM/yyyy' de 'HH'h '");
		SimpleDateFormat formatterFin = new SimpleDateFormat("HH");
		Double[] infosPaiement = null;
		Date dateReservation = null;
		int duree;
		
		JScrollPane scrollpane;
		JLabel lNom = new JLabel("Nom");
		JLabel lPrenom = new JLabel("Prenom");
		JLabel lNumTel = new JLabel("Tel");
		
		cbForfait = new JComboBox();
		cbForfait.setRenderer(new RendererCBForfait());
		cbForfait.setMinimumSize(new Dimension(120, 20));
		cbForfait.setMaximumSize(new Dimension(120, 20));
		cbForfait.setPreferredSize(new Dimension(120, 20));
		cbForfait.addItem(null);
		
		checkFidelite = new JCheckBox();
		checkFidelite.setBackground(Color.LIGHT_GRAY);
		
		this.frame = frame;
		this.reservation = reservation;

		panelCENTER = new JPanel();
		panelCENTER.setLayout(new BoxLayout(panelCENTER, BoxLayout.X_AXIS));
		this.setLayout(new BorderLayout());
		
		panelCenterRight = new JPanel();
		panelCenterRight.setLayout(new BoxLayout(panelCenterRight, BoxLayout.Y_AXIS));
		panelCenterRight.setBackground(Color.LIGHT_GRAY);
		
		panelOptionsPaiement = new JPanel();
		panelOptionsPaiement.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelOptionsPaiement.setBackground(Color.LIGHT_GRAY);	
		
		cNom = new JPanel();
		cPrenom = new JPanel();
		cNumTel = new JPanel();
		cResume = new JPanel();
		cSOUTH = new JPanel();
		
		cNom.setLayout(new BoxLayout(cNom, BoxLayout.Y_AXIS));
		cPrenom.setLayout(new BoxLayout(cPrenom, BoxLayout.Y_AXIS));
		cNumTel.setLayout(new BoxLayout(cNumTel, BoxLayout.Y_AXIS));
		cResume.setLayout(new FlowLayout());
		cSOUTH.setLayout(new FlowLayout());
		cNom.setBackground(Color.LIGHT_GRAY);
		cPrenom.setBackground(Color.LIGHT_GRAY);
		cNumTel.setBackground(Color.LIGHT_GRAY);
		
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

		infosPaiement = new ConfirmerReservation().getInfosApresPaiement(reservation, null, null, false);
		lResume = new JLabel("<html>Type : " + (reservation.getSalle().getTypeSalle().getTypeSalle().equals("petite")?"Petite salle":(reservation.getSalle().getTypeSalle().getTypeSalle().equals("grande")?"Grande salle":"Salle equipee")) + "<br/>"
				+ (formatterDeb.format(dateReservation) 
				+ "a " + (Integer.parseInt(formatterFin.format(dateReservation))+duree) + "h<br/>"
				+ "Prix : " + infosPaiement[0] + " euros")
				+ "</html>");
		lResume.setOpaque(true);
		lResume.setBackground(Color.LIGHT_GRAY);
		
		cResume.add(lResume);
		
		
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
		
		panelOptionsPaiement.add(bOptionsPaiement);
		
		panelCenterRight.setMaximumSize(new Dimension(200, 300));
		panelCenterRight.add(cResume);
		panelCenterRight.add(panelOptionsPaiement);
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
		bOptionsPaiement.addActionListener(this);
		cbForfait.addActionListener(this);
		checkFidelite.addActionListener(this);
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
			
			checkFidelite.setSelected(false);
			
			Client c = null;
			List<ForfaitClient> listeFC = null;

			c = new RechercheClient().rechercheClient(tfNom.getText(), tfPrenom.getText(), tfNumTel.getText());
			
			listeFC = FabForfaitClient.getInstance().rechercherForfaitClient(c.getId(), reservation.getSalle().getTypeSalle().getIdTypeSalle());
			
			cbForfait.removeAllItems();
			cbForfait.addItem(null);
			for(ForfaitClient fc : listeFC){
				cbForfait.addItem(fc);
			}
			
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
			
			tfNom.getDocument().addDocumentListener(this);
			tfPrenom.getDocument().addDocumentListener(this);
			tfNumTel.getDocument().addDocumentListener(this);
		} else if(o.equals(bEnregistrer)){
			enregistrerReservation();
		} else if(o.equals(bRetour)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelReservationAuto(frame));
			frame.validate();
		} else if(o.equals(cbForfait) || o.equals(checkFidelite)){
			SimpleDateFormat formatterDeb = new SimpleDateFormat("'Le 'dd/MM/yyyy' de 'HH'h '");
			SimpleDateFormat formatterFin = new SimpleDateFormat("HH");
			Double[] infosPaiement = null;
			Date dateReservation = reservation.getDate();
			int duree = reservation.getPlage();
			Client c = null;
			List<ForfaitClient> listeFC = null;
			try{
				c = new RechercheClient().rechercheClient(tfNom.getText(), tfPrenom.getText(), tfNumTel.getText());
			} catch(ObjetInconnuException oie){}
			infosPaiement = new ConfirmerReservation().getInfosApresPaiement(reservation, c!=null?c.getId():null,cbForfait.getSelectedItem()!=null?((ForfaitClient)cbForfait.getSelectedItem()).getIdForfaitClient():null, checkFidelite.isSelected()?true:false);
			lResume.setText("<html>Type : " + (reservation.getSalle().getTypeSalle().getTypeSalle().equals("petite")?"Petite salle":(reservation.getSalle().getTypeSalle().getTypeSalle().equals("grande")?"Grande salle":"Salle equipee")) + "<br/>"
					+ (formatterDeb.format(dateReservation) 
					+ "a " + (Integer.parseInt(formatterFin.format(dateReservation))+duree) + "h<br/>"
					+ "Prix : " + infosPaiement[0] + " euros")
					+ "</html>");
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
				listeFC = FabForfaitClient.getInstance().rechercherForfaitClient(c.getId(), reservation.getSalle().getTypeSalle().getIdTypeSalle());
				
				cbForfait.removeAllItems();
				cbForfait.addItem(null);
				for(ForfaitClient fc : listeFC){
					cbForfait.addItem(fc);
				}
				panelOptionsPaiement.removeAll();
				panelOptionsPaiement.setLayout(new GridBagLayout());
				GridBagConstraints GBC = new GridBagConstraints();
				GBC.fill = GridBagConstraints.BOTH; 
				GBC.anchor = GridBagConstraints.NORTH;
				GBC.gridy = 0;
				GBC.gridx = 0;
				GBC.gridheight = 1;
				GBC.gridwidth = 2;
				GBC.weightx = 0.1;
				GBC.weighty = 0.1;
				panelOptionsPaiement.add(cbForfait,GBC);
				GBC.gridy=1;
				GBC.gridwidth = 1;
				panelOptionsPaiement.add(new JLabel("Pts fidelite ("+c.getPointsFidelite()+")"),GBC);
				GBC.gridx=1;
				panelOptionsPaiement.add(checkFidelite,GBC);
				panelOptionsPaiement.validate();
				panelOptionsPaiement.repaint();
				
				tfNom.getDocument().addDocumentListener(this);
				tfPrenom.getDocument().addDocumentListener(this);
				tfNumTel.getDocument().addDocumentListener(this);
			} else {
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
}
