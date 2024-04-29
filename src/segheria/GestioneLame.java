package segheria;

import java.awt.EventQueue;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import segheria.lame.Lama;
import segheria.lame.LameManager;

import javax.swing.border.BevelBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.SystemColor;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GestioneLame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	public static ArrayList<Lama> listLame = new ArrayList<Lama>();
	
	static JList<Lama> listLameList;
	
	static JButton btnModifica;
	static JButton btnElimina;
	static JButton btnNuovaLama;
	public static JLabel lblInfoMaxMetri;
	public static JLabel lblInfoMaxOre;
	public static JLabel lblInfoTotOre;
	public static JLabel lblInfoTotMetri;
	
	private int editMaxOre = 0;
	private int editMaxMetri = 0;
	
	Lama lamaCurrentlyInEdit; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GestioneLame frame = new GestioneLame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GestioneLame() {
		int spinnerMin = 0;
        int spinnerMax = 30000;
        int spinnerStepSize = 1;
        
		setResizable(false);
		setTitle("Gestione Lame");
		setBounds(100, 100, 438, 327);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {				
				closeManagement();
			}			
		});
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 36, 195, 218);
		contentPane.add(scrollPane);
		
		listLameList = new JList();		
		scrollPane.setViewportView(listLameList);
		listLameList.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listLameList.setBackground(new Color(227, 227, 227));
		
		JLabel lblNewLabel = new JLabel("Lista lame");
		lblNewLabel.setBounds(12, 12, 195, 16);
		contentPane.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		
		SpinnerNumberModel spinnerModeInputEditMaxMetri = new SpinnerNumberModel(editMaxMetri, spinnerMin, spinnerMax, spinnerStepSize);
		SpinnerNumberModel spinnerModeInputEditMaxOre = new SpinnerNumberModel(editMaxOre, spinnerMin, spinnerMax, spinnerStepSize);
		
		JLabel lblInfoLama = new JLabel("Info lama");
		lblInfoLama.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblInfoLama.setBounds(230, 12, 183, 16);
		contentPane.add(lblInfoLama);
		
		JPanel panel4_1_1 = new JPanel();
		panel4_1_1.setBounds(230, 38, 195, 131);
		contentPane.add(panel4_1_1);
		panel4_1_1.setLayout(null);
		panel4_1_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel4_1_1.setBackground(new Color(227, 227, 227));
		
		JLabel lblNome_1_1_1_1_1 = new JLabel("Ore totali:");
		lblNome_1_1_1_1_1.setBounds(6, 72, 98, 15);
		panel4_1_1.add(lblNome_1_1_1_1_1);
		lblNome_1_1_1_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNome_1_1_1_1_1.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		JLabel lblNome_1_1_1_1 = new JLabel("Metri totali:");
		lblNome_1_1_1_1.setBounds(6, 102, 98, 15);
		panel4_1_1.add(lblNome_1_1_1_1);
		lblNome_1_1_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNome_1_1_1_1.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		JLabel lblNome_1_1_1 = new JLabel("Ore massime:");
		lblNome_1_1_1.setBounds(6, 42, 98, 15);
		panel4_1_1.add(lblNome_1_1_1);
		lblNome_1_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNome_1_1_1.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		JLabel lblNome_1_1 = new JLabel("Metri massimi:");
		lblNome_1_1.setBounds(6, 12, 98, 15);
		panel4_1_1.add(lblNome_1_1);
		lblNome_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNome_1_1.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		lblInfoTotMetri = new JLabel("");
		lblInfoTotMetri.setHorizontalAlignment(SwingConstants.LEFT);
		lblInfoTotMetri.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblInfoTotMetri.setBounds(116, 102, 61, 15);
		panel4_1_1.add(lblInfoTotMetri);
		
		lblInfoTotOre = new JLabel("");
		lblInfoTotOre.setHorizontalAlignment(SwingConstants.LEFT);
		lblInfoTotOre.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblInfoTotOre.setBounds(116, 72, 61, 15);
		panel4_1_1.add(lblInfoTotOre);
		
		lblInfoMaxOre = new JLabel("");
		lblInfoMaxOre.setHorizontalAlignment(SwingConstants.LEFT);
		lblInfoMaxOre.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblInfoMaxOre.setBounds(116, 42, 61, 15);
		panel4_1_1.add(lblInfoMaxOre);
		
		lblInfoMaxMetri = new JLabel("");
		lblInfoMaxMetri.setHorizontalAlignment(SwingConstants.LEFT);
		lblInfoMaxMetri.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblInfoMaxMetri.setBounds(116, 12, 61, 15);
		panel4_1_1.add(lblInfoMaxMetri);
		
		btnElimina = new JButton("Elimina");		
		btnElimina.setEnabled(false);
		btnElimina.setBounds(6, 260, 100, 29);
		contentPane.add(btnElimina);
		
		btnModifica = new JButton("Modifica");
		btnModifica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ModificaLama modificaLamaWindow = new ModificaLama(lamaCurrentlyInEdit);
				modificaLamaWindow.setVisible(true);
				btnModifica.setEnabled(false);
				btnElimina.setEnabled(false);
				
				lblInfoMaxMetri.setText("");
            	lblInfoMaxOre.setText("");
            	lblInfoTotOre.setText("");
            	lblInfoTotMetri.setText("");
			}
		});
		
		btnModifica.setEnabled(false);
		btnModifica.setBounds(112, 260, 100, 29);
		contentPane.add(btnModifica);
		
		btnNuovaLama = new JButton("Nuova lama");
		btnNuovaLama.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NuovaLama nuovaLamaWindow = new NuovaLama();
				nuovaLamaWindow.setVisible(true);
				btnNuovaLama.setEnabled(false);
			}
		});
		btnNuovaLama.setBounds(313, 260, 112, 29);
		contentPane.add(btnNuovaLama);
		
		listLameList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JList list = (JList)e.getSource();
				
				if(e.getClickCount() == 1) {
		            int index = list.locationToIndex(e.getPoint());
		            
		            Lama selectedLama = Segheria.lameManager.getLama(((Lama)listLame.get(index)).id);
		            lamaCurrentlyInEdit = selectedLama;
		            
		            if(selectedLama.id > -1) {		            	
		            	lblInfoMaxMetri.setText(String.valueOf(selectedLama.maxMetri) + " m");
		            	lblInfoMaxOre.setText(String.valueOf(selectedLama.maxOre) + " h");
		            	lblInfoTotOre.setText(new DecimalFormat("#.##").format(selectedLama.oreUso) + " h");
		            	lblInfoTotMetri.setText(new DecimalFormat("#.##").format(selectedLama.metriUso) + " m");
		            	
		            	btnElimina.setEnabled(true);
		            	btnModifica.setEnabled(true);
		            }
		            
		        }
			}
		});
		
		btnElimina.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int result = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler elimare la lama: '" + lamaCurrentlyInEdit.name + "'?", "Conferma", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					
					if(result == JOptionPane.YES_OPTION) {
						Segheria.lameManager.deleteLama(lamaCurrentlyInEdit.id);
						setLameList();
						
						lblInfoMaxMetri.setText("");
		            	lblInfoMaxOre.setText("");
		            	lblInfoTotOre.setText("");
		            	lblInfoTotMetri.setText("");
		            	
		            	btnElimina.setEnabled(false);
		            	btnModifica.setEnabled(false);
					}
				} catch(Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		
		setLameList();
	}
	
	
	public static void setLameList() {
		listLame = Segheria.lameManager.getLame();
		
		if(listLame.size() == 0) {
			listLame.add(new Lama("Nessuna lama inserita..."));
		}
		
		listLameList.setModel(new DefaultComboBoxModel(listLame.toArray()));
		
		Segheria.checkWarnings();
	}
	
	public void closeManagement() {
		try {
			dispose();
			Segheria.selectLamaInUso.setEnabled(true);
			Segheria.btnGestioneLame.setEnabled(true);
			Segheria.refreshLameSelect();			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
