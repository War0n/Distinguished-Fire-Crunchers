package wordfeud;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SpelPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Spel spel;
	private BordPanel speelVeld;
	private JPanel leftBottomContainer;
	private JPanel scores;
	private JLabel score;
	private FunctionPanel functiepanel;
	private LetterbakPanel letterbak;
	private ChatGUI cg;
	private Chat chat;
	private ButtonPanel buttons;
	private WFButton swapButton;
	private WFButton skipButton;
	private WFButton placeButton;
	private WFButton chatButton;
	private WFButton backButton;
	private WFButton clearButton;
	private WFButton shuffleButton;

	public SpelPanel(int spelID) {

		setMaximumSize(new Dimension(630, 700));
		setPreferredSize(getMaximumSize());
		swapButton = new WFButton("Swap");
		skipButton = new WFButton("Pass");
		placeButton = new WFButton("Play");
		chatButton = new WFButton("Chat");
		clearButton = new WFButton("Clear");
		shuffleButton = new WFButton("Shuffle");
		backButton = new WFButton("< Terug");

		spel = new Spel(this, spelID);
		chat = new Chat(spel.getSpelId());

		score = new JLabel("Scoreveld hier");
		score.setForeground(Color.white);

		cg = new ChatGUI(spel);
		cg.setVisible(false);
		chat.addObserver(cg);

		letterbak = new LetterbakPanel(spel.getLetterBak());
		buttons = new ButtonPanel();

		buttons.add(placeButton);
		buttons.add(skipButton);
		buttons.add(swapButton);
		buttons.add(clearButton);
		buttons.add(chatButton);
		buttons.add(shuffleButton);

		initButtons();
		setBackground(new Color(23, 26, 30));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		speelVeld = new BordPanel(spel.getBord());
		functiepanel = new FunctionPanel();
		functiepanel.setLayout(new BoxLayout(functiepanel, BoxLayout.X_AXIS));
		leftBottomContainer = new JPanel();
		scores = new JPanel();
		scores.setLayout(new BorderLayout());
		scores.setBackground(letterbak.getBackground());
		scores.setMaximumSize(new Dimension(400, 20));
		scores.setPreferredSize(scores.getMaximumSize());

		leftBottomContainer.setLayout(new BoxLayout(leftBottomContainer,
				BoxLayout.Y_AXIS));
		this.add(speelVeld);
		scores.add(backButton, BorderLayout.WEST);
		scores.add(score, BorderLayout.EAST);
		functiepanel.setBackground(letterbak.getBackground());
		functiepanel.setMaximumSize(new Dimension(630, 70));
		functiepanel.setPreferredSize(functiepanel.getMaximumSize());
		letterbak.setPreferredSize(new Dimension(300, 40));
		buttons.setMinimumSize(new Dimension(100, 70));
		buttons.setPreferredSize(getMinimumSize());
		leftBottomContainer.setBackground(letterbak.getBackground());
		leftBottomContainer.setMaximumSize(new Dimension(500, 70));
		leftBottomContainer.setPreferredSize(leftBottomContainer
				.getMaximumSize());
		leftBottomContainer.add(letterbak);
		leftBottomContainer.add(scores);
		functiepanel.add(leftBottomContainer);
		functiepanel.add(buttons);
		this.add(functiepanel);
		// System.out.println("Spel Compiler Done!");
	}

	public void setParentContentPane(JPanel contentPane) {
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}

	public void initButtons() {
		chatButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cg.setVisible(true);
				cg.setState(JFrame.NORMAL);
			}
		});

		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setParentContentPane(new ActieveSpellenMenu(false));
			}
		});

		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				letterbak.addLetters(speelVeld.clearField());//

			}
		});

		shuffleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				letterbak.shuffle();
			}
		});

		skipButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				spel.getVerloop().doTurn("Pass", false);
			}
		});

		swapButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new SwapGUI(spel);
			}
		});
		placeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				spel.getVerloop().play();
			}
		});
	}

	public WFButton getPlayButton() {
		return placeButton;
	}

	public WFButton getSwapButton() {
		return swapButton;
	}

	public WFButton getSkipButton() {
		return skipButton;
	}

	public WFButton getClearButton() {
		return clearButton;
	}

	public WFButton getShuffleButton() {
		return shuffleButton;
	}

	public void setScore(String text) {
		score.setText(text);
		repaint();
	}

	public LetterbakPanel getLetterbakPanel() {
		return letterbak;
	}
}