package view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GuiListarFuncoes extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiListarFuncoes frame = new GuiListarFuncoes();
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
	public GuiListarFuncoes() {
		
		setTitle("Lista de Funções Cadastradas");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setBounds(100, 100, 600, 400);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 20, 544, 240);
		contentPane.add(scrollPane);
		
		DefaultTableModel modeloTabela = new DefaultTableModel(
			new Object[][] {},
			new String[] { "ID", "Nome da Função", "Status", "Qtd. Permissões" }
		);
		
		table = new JTable(modeloTabela);
		
		scrollPane.setViewportView(table);
		
		JButton btnAtualizar = new JButton("ATUALIZAR LISTA");
		btnAtualizar.setBounds(20, 290, 160, 35);
		contentPane.add(btnAtualizar);
		
		JButton btnEditar = new JButton("EDITAR");
		btnEditar.setBounds(210, 290, 160, 35);
		contentPane.add(btnEditar);
		
		JButton btnFechar = new JButton("FECHAR");
		btnFechar.setBounds(404, 290, 160, 35);
		contentPane.add(btnFechar);
		
		btnFechar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
}