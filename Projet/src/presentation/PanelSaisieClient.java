package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import metier.RechercheClient;
import donnee.Client;
import exception.ObjetInconnuException;

/**
 * Panel de choix du client
 *
 */
public class PanelSaisieClient extends JPanel implements ActionListener{

	private JFrame frame;
	private List<Client> listeClient;
	
	private JList<Client> jlClient;
	private DefaultListModel<Client> model;
	
	private JPanel panelCENTER;
	private JPanel panelCenterRight;
	
	private Container cNom;
	private Container cPrenom;
	private Container cNumTel;
	private Container cSOUTH;
	
	private JButton bSendInfos;
	private JButton bValider;
	private JButton bRetour;
	
	private JTextField tfNom;
	private JTextField tfPrenom;
	private JTextField tfNumTel;
	
	public PanelSaisieClient(JFrame frame) {
		JScrollPane scrollpane;
		JLabel lNom = new JLabel("Nom");
		JLabel lPrenom = new JLabel("Prenom");
		JLabel lNumTel = new JLabel("Tel");
		
		this.frame = frame;

		panelCENTER = new JPanel();
		panelCENTER.setLayout(new BoxLayout(panelCENTER, BoxLayout.X_AXIS));
		this.setLayout(new BorderLayout());
		
		panelCenterRight = new JPanel();
		panelCenterRight.setLayout(new BoxLayout(panelCenterRight, BoxLayout.Y_AXIS));
		panelCenterRight.setBackground(Color.LIGHT_GRAY);
		
		cNom = new Container();
		cPrenom = new Container();
		cNumTel = new Container();
		cSOUTH = new Container();
		
		cNom.setLayout(new FlowLayout());
		cPrenom.setLayout(new FlowLayout());
		cNumTel.setLayout(new FlowLayout());
		cSOUTH.setLayout(new FlowLayout());
		
		bSendInfos = new JButton(" >> ");
		bValider = new JButton("Valider");
		bValider.setMinimumSize(new Dimension(150, 20));
		bValider.setMaximumSize(new Dimension(150, 20));
		bValider.setPreferredSize(new Dimension(150, 20));
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
		panelCenterRight.add(cNom);
		panelCenterRight.add(cPrenom);
		panelCenterRight.add(cNumTel);
		panelCenterRight.add(new JLabel(" "));
		panelCenterRight.add(bValider);
		
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
		bValider.addActionListener(this);
		bRetour.addActionListener(this);
	}

	/**
	 * On recherche le client saisi et on envoie sur le panel suivant
	 */
	public void selectionClient(){
		
		if(tfNom.getText().equals("") || tfPrenom.equals("") || tfNumTel.equals("")){
			JOptionPane.showMessageDialog(frame, "Tous les champs ne sont pas remplis !");
			return;
		}
		
		try{
			//on recherche le client
			Client clt = new RechercheClient().rechercheClient(tfNom.getText(), tfPrenom.getText(), tfNumTel.getText());

			frame.getContentPane().removeAll();
			frame.getContentPane().add(new PanelEditionClient(frame, clt));
			frame.validate();
			
		} catch(ObjetInconnuException e){
			//le client nexiste pas (cas impossible normalement)
			JOptionPane.showMessageDialog(
					frame,
					"Client inconnu !");
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o.equals(bSendInfos)){
			if(!jlClient.isSelectionEmpty()){
				tfNom.setText(jlClient.getSelectedValue().getNom());
				tfPrenom.setText(jlClient.getSelectedValue().getPrenom());
				tfNumTel.setText(jlClient.getSelectedValue().getNumTel());
			}
		} else if(o.equals(bValider)){
			selectionClient();
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
