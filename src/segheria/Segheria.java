package segheria;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.awt.Font;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.border.LineBorder;

import segheria.lame.Lama;
import segheria.lame.LameManager;
import server.Server;
import tagli.TagliManager;

import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpinnerModel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class Segheria extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	static JComboBox selectLamaInUso;
	static JButton btnGestioneLame;
	
	private int sessionLunghezza = 1;
	private int sessionLarghezza = 1;
	private int sessionSpessore = 1;
	
	public static JSpinner inputSessionLunghezza;
	public static JSpinner inputSessionLarghezza;
	public static JSpinner inputSessionSpessore;
	public static JLabel lblSessionTemperatura;
	private static JLabel lblSessionTagli;
	private static JLabel lblSessionOre;
	private static JLabel lblSessionMetri;
	private static JLabel lblSessionVolume;
	
	private static JPanel bannerMetri;
	private static JPanel bannerOre;
    
    public static ArrayList<Lama> listLame = new ArrayList<Lama>();
    
    public static DatabaseManager db = new DatabaseManager();
    public static LameManager lameManager = new LameManager();
    public static TagliManager tagliManager = new TagliManager();
    
    private static LocalDateTime startDataObj = LocalDateTime.now();
    private static DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static String sessionStart = startDataObj.format(myFormatObj);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server server = new Server();
					Thread serverThread = new Thread(server);
				    serverThread.start();
				    
					Segheria frame = new Segheria();				    
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
	public Segheria() {
		setTitle("Segheria");
		setAlwaysOnTop(true);
		setResizable(false);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				db.close();
			}			
		});
		
		int spinnerMin = 1;
        int spinnerMax = 30000;
        int spinnerStepSize = 1;
        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 397, 276);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel1 = new JPanel();
		panel1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel1.setBackground(new Color(227, 227, 227));
		panel1.setBounds(12, 12, 372, 45);
		contentPane.add(panel1);
		panel1.setLayout(null);
		
		JLabel lblDeco1 = new JLabel("Lama in uso:");
		lblDeco1.setBounds(10, 14, 73, 15);
		panel1.add(lblDeco1);
		lblDeco1.setFont(new Font("Lucida Grande", Font.PLAIN, 12));

		selectLamaInUso = new JComboBox();
		selectLamaInUso.setBounds(90, 10, 150, 27);
		panel1.add(selectLamaInUso);
		
		refreshLameSelect();
		
		btnGestioneLame = new JButton("Gestione lame");
		btnGestioneLame.setBounds(238, 8, 128, 29);
		panel1.add(btnGestioneLame);
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(null);
		panel2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel2.setBackground(new Color(227, 227, 227));
		panel2.setBounds(12, 69, 120, 45);
		contentPane.add(panel2);
		
		JLabel lblDeco2 = new JLabel("Temperatura:");
		lblDeco2.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblDeco2.setBounds(6, 6, 108, 15);
		panel2.add(lblDeco2);
		
		lblSessionTemperatura = new JLabel("0 °C");
		lblSessionTemperatura.setBounds(8, 23, 106, 16);
		panel2.add(lblSessionTemperatura);
		
		JPanel panel3 = new JPanel();
		panel3.setLayout(null);
		panel3.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel3.setBackground(new Color(227, 227, 227));
		panel3.setBounds(144, 69, 72, 45);
		contentPane.add(panel3);
		
		JLabel lblDeco3 = new JLabel("Tagli:");
		lblDeco3.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblDeco3.setBounds(6, 6, 60, 15);
		panel3.add(lblDeco3);
		
		lblSessionTagli = new JLabel("0");
		lblSessionTagli.setBounds(8, 23, 58, 16);
		panel3.add(lblSessionTagli);
		
		JPanel panel4 = new JPanel();
		panel4.setLayout(null);
		panel4.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel4.setBackground(new Color(227, 227, 227));
		panel4.setBounds(228, 69, 72, 45);
		contentPane.add(panel4);
		
		JLabel lblDeco4 = new JLabel("Ore:");
		lblDeco4.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblDeco4.setBounds(6, 6, 60, 15);
		panel4.add(lblDeco4);
		
		lblSessionOre = new JLabel("0 h");
		lblSessionOre.setBounds(8, 23, 58, 16);
		panel4.add(lblSessionOre);
		
		JPanel panel5 = new JPanel();
		panel5.setLayout(null);
		panel5.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel5.setBackground(new Color(227, 227, 227));
		panel5.setBounds(312, 69, 72, 45);
		contentPane.add(panel5);
		
		JLabel lblDeco5 = new JLabel("Metri:");
		lblDeco5.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblDeco5.setBounds(6, 6, 60, 15);
		panel5.add(lblDeco5);
		
		lblSessionMetri = new JLabel("0 m");
		lblSessionMetri.setBounds(8, 23, 58, 16);
		panel5.add(lblSessionMetri);
		
		JPanel panel6 = new JPanel();
		panel6.setLayout(null);
		panel6.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel6.setBackground(new Color(227, 227, 227));
		panel6.setBounds(12, 126, 120, 50);
		contentPane.add(panel6);
		
		JLabel lblDeco6 = new JLabel("Lunghezza:");
		lblDeco6.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblDeco6.setBounds(6, 6, 88, 15);
		panel6.add(lblDeco6);
        
		SpinnerNumberModel spinnerModelLunghezza = new SpinnerNumberModel(sessionLunghezza, spinnerMin, spinnerMax, spinnerStepSize);
        inputSessionLunghezza = new JSpinner(spinnerModelLunghezza);
		inputSessionLunghezza.setBounds(4, 20, 80, 26);
		panel6.add(inputSessionLunghezza);
		
		JLabel lblDeco9 = new JLabel("cm");
		lblDeco9.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblDeco9.setBounds(91, 25, 19, 15);
		panel6.add(lblDeco9);
		
		JPanel panel7 = new JPanel();
		panel7.setLayout(null);
		panel7.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel7.setBackground(new Color(227, 227, 227));
		panel7.setBounds(144, 126, 116, 50);
		contentPane.add(panel7);
		
		JLabel lblDeco7 = new JLabel("Larghezza:");
		lblDeco7.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblDeco7.setBounds(6, 6, 88, 15);
		panel7.add(lblDeco7);
		
		SpinnerNumberModel spinnerModelLarghezza = new SpinnerNumberModel(sessionLarghezza, spinnerMin, spinnerMax, spinnerStepSize);
		inputSessionLarghezza = new JSpinner(spinnerModelLarghezza);
		inputSessionLarghezza.setBounds(4, 20, 80, 26);
		panel7.add(inputSessionLarghezza);
		
		JLabel lblDeco10 = new JLabel("cm");
		lblDeco10.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblDeco10.setBounds(91, 25, 19, 15);
		panel7.add(lblDeco10);
		
		JPanel panel8 = new JPanel();
		panel8.setLayout(null);
		panel8.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel8.setBackground(new Color(227, 227, 227));
		panel8.setBounds(268, 126, 116, 50);
		contentPane.add(panel8);
		
		JLabel lblDeco8 = new JLabel("Spessore:");
		lblDeco8.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblDeco8.setBounds(6, 6, 88, 15);
		panel8.add(lblDeco8);
		
		SpinnerNumberModel spinnerModelSpessore = new SpinnerNumberModel(sessionSpessore, spinnerMin, spinnerMax, spinnerStepSize);
		inputSessionSpessore = new JSpinner(spinnerModelSpessore);
		inputSessionSpessore.setBounds(4, 20, 80, 26);
		panel8.add(inputSessionSpessore);
		
		JLabel lblDeco11 = new JLabel("cm");
		lblDeco11.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblDeco11.setBounds(91, 25, 19, 15);
		panel8.add(lblDeco11);
		
		JPanel panel9 = new JPanel();
		panel9.setLayout(null);
		panel9.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel9.setBackground(new Color(227, 227, 227));
		panel9.setBounds(12, 188, 120, 45);
		contentPane.add(panel9);
		
		JLabel lblVolume = new JLabel("Volume:");
		lblVolume.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVolume.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblVolume.setBounds(6, 6, 104, 15);
		panel9.add(lblVolume);
		
		lblSessionVolume = new JLabel("0");
		lblSessionVolume.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSessionVolume.setBounds(16, 23, 72, 16);
		panel9.add(lblSessionVolume);
		
		JLabel lblDeco12 = new JLabel("m3");
		lblDeco12.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblDeco12.setBounds(92, 25, 19, 15);
		panel9.add(lblDeco12);
		
		bannerMetri = new JPanel();
		bannerMetri.setVisible(false);
		bannerMetri.setLayout(null);
		bannerMetri.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		bannerMetri.setBackground(new Color(255, 55, 40));
		bannerMetri.setBounds(144, 188, 240, 23);
		contentPane.add(bannerMetri);
		
		JLabel lblBannerMetri = new JLabel("Necessario affilare lama");
		lblBannerMetri.setForeground(new Color(248, 255, 244));
		lblBannerMetri.setHorizontalAlignment(SwingConstants.LEFT);
		lblBannerMetri.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblBannerMetri.setBounds(5, 0, 149, 23);
		bannerMetri.add(lblBannerMetri);
		
		JButton btnAffila = new JButton("Ok");
		btnAffila.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Lama selectedLama = (Lama) selectLamaInUso.getSelectedItem();
				lameManager.affilaLama(selectedLama.id);
				checkWarnings();
				resetSession();
			}
		});
		btnAffila.setBounds(172, 2, 68, 20);
		bannerMetri.add(btnAffila);
		
		bannerOre = new JPanel();
		bannerOre.setVisible(false);
		bannerOre.setLayout(null);
		bannerOre.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		bannerOre.setBackground(new Color(255, 55, 40));
		bannerOre.setBounds(144, 210, 240, 23);
		contentPane.add(bannerOre);
		
		JLabel lblBannerOre = new JLabel("Necessario cambio lama");
		lblBannerOre.setHorizontalAlignment(SwingConstants.LEFT);
		lblBannerOre.setForeground(new Color(248, 255, 244));
		lblBannerOre.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblBannerOre.setBounds(5, 0, 149, 23);
		bannerOre.add(lblBannerOre);
		
		JButton btnCambia = new JButton("Ok");
		btnCambia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Lama selectedLama = (Lama) selectLamaInUso.getSelectedItem();
				lameManager.cambiaLama(selectedLama.id);
				checkWarnings();
				resetSession();
			}
		});
		btnCambia.setBounds(172, 2, 68, 20);
		bannerOre.add(btnCambia);
		
		btnGestioneLame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GestioneLame lameWindow = new GestioneLame();
				lameWindow.setVisible(true);
				selectLamaInUso.setEnabled(false);
				btnGestioneLame.setEnabled(false);
			}
		});
		
		selectLamaInUso.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	try {
		    		Lama selectedLama = (Lama) selectLamaInUso.getSelectedItem();
		    		
		    		try {
		    			Statement stmt = db.conn.createStatement();			
		    			stmt.executeUpdate("UPDATE options SET value='" + selectedLama.id + "' WHERE option='selected-lama';");
		    			stmt.close();
		    			
		    			resetSession();
		    			
		    			checkWarnings();
		    			
		    		} catch (SQLException ex) {
		    			ex.printStackTrace();
		    		}
		    	} catch(Exception ex) {
					System.out.println(ex.getMessage());
				}
		    }
		});
		
		checkWarnings();
	}
	
	static public void refreshLameSelect() {
		int sellama = lameManager.getSelectedLama();
		
		listLame = lameManager.getLame();
		selectLamaInUso.setModel(new DefaultComboBoxModel(listLame.toArray()));
	
		if(sellama > -1) {
			for(int i=0; i<listLame.size(); i++) {
				Lama tmp = listLame.get(i);				
				if(tmp.id == sellama) {
					selectLamaInUso.setSelectedItem(tmp);
				}
			}
		}
	}
	
	public static void refreshDock() {
		try {
			int[] data = tagliManager.getSessionData();
			double metri = (double)data[1] / 100;
			double ore = ((double)data[2] / 60) / 60;
			
			int lung = (int)inputSessionLunghezza.getValue();
			double lungDec = (double)lung  / 100;
			
			int larg = (int)inputSessionLarghezza.getValue();
			double largDec = (double)larg  / 100;
			
			int spes = (int)inputSessionSpessore.getValue();
			double spesDec = (double)spes  / 100;
			
			double prevVolume = Double.valueOf(lblSessionVolume.getText());
			double volume = prevVolume + (lungDec * largDec * spesDec);
					
			lblSessionTagli.setText(String.valueOf(data[0]));
			lblSessionMetri.setText(new DecimalFormat("#.##").format(metri) + " m");
			lblSessionOre.setText(new DecimalFormat("#.##").format(ore) + " h");
			lblSessionVolume.setText(new DecimalFormat("#.##").format(volume));

		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void resetSession() {
		LocalDateTime dataObj = LocalDateTime.now();
	    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String now = dataObj.format(myFormatObj);
	    sessionStart = now;
	    
	    lblSessionTagli.setText("0");
		lblSessionMetri.setText("0 m");
		lblSessionOre.setText("0 h");
	}
	
	public static void resetDock() {
		inputSessionLunghezza.setValue(1);
		inputSessionLarghezza.setValue(1);
		inputSessionSpessore.setValue(1);
	}
	
	public static void checkWarnings() {
		Lama selectedLama = (Lama) selectLamaInUso.getSelectedItem();
		boolean[] warnings = lameManager.checkLimits(selectedLama.id);
		
		bannerMetri.setVisible(warnings[0]);
		bannerOre.setVisible(warnings[1]);
	}
}
