package segheria;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import segheria.lame.Lama;

import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ModificaLama extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField inputName;
	private int maxMetri = 1;
	private int maxOre = 1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModificaLama frame = new ModificaLama(null);
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
	public ModificaLama(Lama lama) {
		setResizable(false);
		setTitle("Modifica Lama");
		setBounds(100, 100, 286, 183);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelEditLama = new JPanel();
		panelEditLama.setLayout(null);
		panelEditLama.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelEditLama.setBackground(new Color(227, 227, 227));
		panelEditLama.setBounds(12, 12, 263, 131);
		contentPane.add(panelEditLama);
		
		JLabel lblEditMetriMassimi = new JLabel("Metri massimi:");
		lblEditMetriMassimi.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEditMetriMassimi.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblEditMetriMassimi.setBounds(6, 38, 98, 15);
		panelEditLama.add(lblEditMetriMassimi);
		
		SpinnerNumberModel spinnerModeInputEditMaxMetri = new SpinnerNumberModel(maxMetri, 1, 9000, 1);
		JSpinner inputMetri = new JSpinner(spinnerModeInputEditMaxMetri);
		inputMetri.setBounds(108, 32, 90, 26);
		panelEditLama.add(inputMetri);
		
		JLabel lblEditOreMassime = new JLabel("Ore massime:");
		lblEditOreMassime.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEditOreMassime.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblEditOreMassime.setBounds(6, 64, 98, 15);
		panelEditLama.add(lblEditOreMassime);
		
		SpinnerNumberModel spinnerModeInputEditMaxOre = new SpinnerNumberModel(maxOre, 1, 9000, 1);
		JSpinner inputOre = new JSpinner(spinnerModeInputEditMaxOre);
		inputOre.setBounds(108, 59, 90, 26);
		panelEditLama.add(inputOre);
		
		JLabel lblEditNome = new JLabel("Nome:");
		lblEditNome.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEditNome.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblEditNome.setBounds(6, 12, 98, 15);
		panelEditLama.add(lblEditNome);
		
		inputName = new JTextField();
		inputName.setText("");
		inputName.setColumns(10);
		inputName.setBounds(108, 6, 145, 26);
		panelEditLama.add(inputName);
		
		JButton btnCancel = new JButton("Annula");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				GestioneLame.btnModifica.setEnabled(true);
				GestioneLame.btnElimina.setEnabled(true);
			}
		});
		btnCancel.setBounds(6, 97, 120, 29);
		panelEditLama.add(btnCancel);
		
		JButton btnSalva = new JButton("Salva");
		btnSalva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(inputName.getText().equals("")) {
						JOptionPane.showConfirmDialog(null, "Devi compilare correttamente i campi!", "Attenzione", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE);
					} else {
						Segheria.lameManager.editLama(lama.id, inputName.getText(), (int)inputMetri.getValue(), (int)inputOre.getValue());
						dispose();
						GestioneLame.btnModifica.setEnabled(true);
						GestioneLame.btnElimina.setEnabled(true);
						GestioneLame.setLameList();
					}
				} catch(Exception ex) {
					System.out.println(ex.getMessage());	
				}
			}
		});
		
		btnSalva.setBounds(138, 97, 120, 29);
		panelEditLama.add(btnSalva);
		
		inputName.setText(lama.name);
		inputMetri.setValue(lama.maxMetri);
		inputOre.setValue(lama.maxOre);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {					
					if(!inputName.getText().equals(lama.name) || (int)inputOre.getValue() != lama.maxOre || (int)inputMetri.getValue() != lama.maxMetri) {
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
			
			GestioneLame.lblInfoMaxMetri.setText("");
			GestioneLame.lblInfoMaxOre.setText("");
        	GestioneLame.lblInfoTotOre.setText("");
        	GestioneLame.lblInfoTotMetri.setText("");
        	
			GestioneLame.setLameList();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
