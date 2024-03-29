package wordfeud;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class QuestionMarkChooser extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField charField;
	private WFButton submitButton;
	private Stone veranderSteen;
	
	public QuestionMarkChooser(Stone steen){
		veranderSteen = steen;
		JPanel cp = (JPanel) this.getContentPane();
		cp.setBackground(new Color(23,26,30));
		cp.setLayout(new BoxLayout(cp, BoxLayout.X_AXIS));
		
		charField = new JTextField();
		charField.setColumns(1);
		charField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				submitButton.doClick();
			}
		});
		
		submitButton = new WFButton("Verander letter");
		submitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Character.toUpperCase(charField.getText().charAt(0)) >= 'A' && Character.toUpperCase(charField.getText().charAt(0)) <= 'Z')
				{
					veranderSteen.setBlancoLetter(Character.toUpperCase(charField.getText().charAt(0)));
					dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(
							null,
							"Input moet een letter zijn(A-Z)!",
							"Fout!",
							JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		add(charField);
		add(submitButton);
		
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}
