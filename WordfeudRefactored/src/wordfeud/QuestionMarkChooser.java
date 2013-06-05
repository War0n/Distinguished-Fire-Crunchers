package wordfeud;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class QuestionMarkChooser extends JFrame{
	private JPanel cp;
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
				veranderSteen.setBlancoLetter(charField.getText().charAt(0));
				dispose();
			}
		});
		
		add(charField);
		add(submitButton);
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}
