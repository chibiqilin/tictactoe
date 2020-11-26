/**
 * Simple Tic Tac Toe game.
 * 
 * Can be played with 2 players or against an AI opponent. 
 * 
 * @author Andrew Vu
 * 
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Board implements ActionListener {

	static String title = "Tic Tac Toe";
	Random random = new Random();
	JFrame frame = new JFrame();
	JPanel statusPanel = new JPanel();
	JPanel tilePanel = new JPanel();
	JPanel controlPanel = new JPanel();
	JLabel status = new JLabel();
	JButton[] buttons = new JButton[9];
	JButton reset = new JButton();
	JButton quit = new JButton();
	JToggleButton ai_opponent = new JToggleButton();
	boolean x_turn;
	boolean game_over;
	boolean ai_on;

	Board() throws InterruptedException {
		initBoard();
		Computer computer = new Computer();
		while (true) {
			while (ai_on && !game_over) {
				if (!x_turn) {
					Thread.sleep(500);
					int move = computer.evaluate(buttons);
					makeMove(buttons[move]);
					if (!status())
						x_turn = !x_turn;

				} else
					Thread.sleep(100);
			}
			Thread.sleep(100);
		}
	}

	private void initBoard() {
		x_turn = true;
		game_over = false;
		ai_on = false;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 800);
		frame.getContentPane().setBackground(Color.white);
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);

		status.setBackground(Color.white);
		status.setForeground(Color.black);
		status.setHorizontalAlignment(JLabel.CENTER);
		status.setOpaque(true);
		status.setFont(new Font("Arial", Font.BOLD, 75));

		status.setText(title);

		statusPanel.setLayout(new BorderLayout());
		tilePanel.setLayout(new GridLayout(3, 3));

		for (int i = 0; i < 9; i++) {
			buttons[i] = new JButton();
			tilePanel.add(buttons[i]);
			buttons[i].setFocusable(false);
			buttons[i].addActionListener(this);
			buttons[i].setBackground(Color.white);
			buttons[i].setFont(new Font("Arial", Font.BOLD, 150));
			buttons[i].setText("");
		}
		
		ai_opponent.setText("Vs Computer");
		ItemListener itemListener = new ItemListener() { 
            public void itemStateChanged(ItemEvent itemEvent) 
            { 
                int state = itemEvent.getStateChange(); 
                if (state == ItemEvent.SELECTED) { 
                    ai_on = true;
                } 
                else {
                    ai_on = false;
                } 
            } 
        };
        ai_opponent.addItemListener(itemListener);
        
		reset.setText("Reset");
		reset.setFocusable(false);
		reset.addActionListener(this);
		
		quit.setText("Close");
		quit.addActionListener(this);
		
		controlPanel.add(ai_opponent);
		controlPanel.add(reset);
		controlPanel.add(quit);

		statusPanel.add(status);
		frame.add(statusPanel, BorderLayout.NORTH);
		frame.add(tilePanel, BorderLayout.CENTER);
		frame.add(controlPanel, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == reset) {
			resetBoard();
			return;
		}
		if (e.getSource() == quit)
			System.exit(0);
		if (game_over)
			return;
		if (ai_on && !x_turn)
			return;
		for (JButton i : buttons) {
			if (e.getSource() == i) {
				if (i.getText() != "")
					break;
				makeMove(i);
				if (!status())
					x_turn = !x_turn;
			}
		}
	}

	private void resetBoard() {
		game_over = false;
		x_turn = true;
		for (JButton i : buttons) {
			i.setText("");
			i.setBackground(Color.white);
		}
		status.setText(title);
	}

	private void makeMove(JButton i) {
		if (x_turn) {
			i.setForeground(Color.blue);
			i.setText("X");
		} else {
			i.setForeground(Color.red);
			i.setText("O");
		}
	}

	public boolean status() {
		boolean result = false;

		// check rows
		for (int i = 0; i < 9; i += 3) {
			if (buttons[i].getText() != "" && buttons[i].getText() == buttons[i + 1].getText()
					&& buttons[i + 1].getText() == buttons[i + 2].getText()) {

				int[] tiles = { i, i + 1, i + 2 };
				victory(buttons[i].getText() == "X", tiles);
				result = true;
			}
		}
		// check columns
		for (int i = 0; i < 3; i++) {
			if (buttons[i].getText() != "" && buttons[i].getText() == buttons[i + 3].getText()
					&& buttons[i + 3].getText() == buttons[i + 6].getText()) {

				int[] tiles = { i, i + 3, i + 6 };
				victory(buttons[i].getText() == "X", tiles);
				result = true;
			}
		}
		// check diagonals
		if (buttons[4].getText() != "") {
			if (buttons[0].getText() == buttons[4].getText() && buttons[4].getText() == buttons[8].getText()) {
				int[] tiles = { 0, 4, 8 };
				victory(buttons[4].getText() == "X", tiles);
			} else if (buttons[2].getText() == buttons[4].getText() && buttons[4].getText() == buttons[6].getText()) {
				int[] tiles = { 2, 4, 6 };
				victory(buttons[4].getText() == "X", tiles);
			}
		}
		// check draw
		for (int i = 0; i < 10; i++) {
			if (i < 9) {
				if (buttons[i].getText() == "")
					break;
			}
			else draw();
		}

		return result;
	}

	public void victory(boolean x_wins, int[] tiles) {
		game_over = true;
		for (int i : tiles)
			buttons[i].setBackground(Color.green);
		if (x_wins)
			status.setText("X Wins!");
		else
			status.setText("O Wins!");
	}
	public void draw() {
		game_over = true;
		x_turn = true;
		status.setText("Draw!");
	}
}
