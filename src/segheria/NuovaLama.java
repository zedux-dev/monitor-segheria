package segheria;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import java.awt.Window.Type;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class NuovaLama extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private int maxMetri = 1;
	private int maxOre = 1;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NuovaLama frame = new NuovaLama();
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
	public NuovaLama() {
		setTitle("Nuova Lama");
		setResizable(false);
		setBounds(100, 100, 286, 183);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel4 = new JPanel();
		panel4.setLayout(null);
		panel4.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel4.setBackground(new Color(227, 227, 227));
		panel4.setBounds(12, 12, 263, 131);
		contentPane.add(panel4);
		
		JLabel lblMetriMassimi = new JLabel("Metri massimi:");
		lblMetriMassimi.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMetriMassimi.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblMetriMassimi.setBounds(6, 38, 98, 15);
		panel4.add(lblMetriMassimi);
		
		SpinnerNumberModel spinnerModeInputEditMaxMetri = new SpinnerNumberModel(maxMetri, 1, 9000, 1);
		JSpinner inputMetri = new JSpinner(spinnerModeInputEditMaxMetri);
		inputMetri.setBounds(108, 32, 90, 26);
		panel4.add(inputMetri);
		
		JLabel lblOreMassime = new JLabel("Ore massime:");
		lblOreMassime.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOreMassime.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblOreMassime.setBounds(6, 64, 98, 15);
		panel4.add(lblOreMassime);
		
		SpinnerNumberModel spinnerModeInputEditMaxOre = new SpinnerNumberModel(maxOre, 1, 9000, 1);
		JSpinner inputOre = new JSpinner(spinnerModeInputEditMaxOre);
		inputOre.setBounds(108, 59, 90, 26);
		panel4.add(inputOre);
		
		JLabel lblNome = new JLabel("Nome:");
		lblNome.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNome.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblNome.setBounds(6, 12, 98, 15);
		panel4.add(lblNome);
		
		JTextField inputName = new JTextField();
		inputName.setColumns(10);
		inputName.setBounds(108, 6, 145, 26);
		panel4.add(inputName);
		
		JButton btnCrea = new JButton("Crea");		
		btnCrea.setBounds(133, 96, 120, 29);
		panel4.add(btnCrea);
		
		JButton btnAnnulla = new JButton("Annulla");
		btnAnnulla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				GestioneLame.btnNuovaLama.setEnabled(true);
			}
		});
		
		btnAnnulla.setBounds(6, 96, 120, 29);
		panel4.add(btnAnnulla);
		
		btnCrea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(inputName.getText().equals("")) {
						JOptionPane.showConfirmDialog(null, "Devi compilare correttamente i campi!", "Attenzione", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE);
					} else {
						Segheria.lameManager.newLama(inputName.getText(), (int)inputMetri.getValue(), (int)inputOre.getValue());
						dispose();
						GestioneLame.btnNuovaLama.setEnabled(true);
						GestioneLame.setLameList();
					}
				} catch(Exception ex) {
					System.out.println(ex.getMessage());	
				}
			}
		});
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {					
					if(!inputName.getText().equals("")) {
						int result = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler annullare?\n" + "Le modifiche andranno perse!", "Conferma", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						
						if(result == JOptionPane.YES_OPTION) {
							closeManagement();
						}
					} else {
						closeManagement();
					}
				} catch(Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
	}
	
	public void closeManagement() {
		try {
			dispose();
			GestioneLame.btnNuovaLama.setEnabled(true);
			GestioneLame.setLameList();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
