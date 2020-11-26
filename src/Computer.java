import javax.swing.JButton;

public class Computer {
	
	Computer(){
	}

	public int evaluate(JButton[] buttons) {
		String[][] board = new String[3][3];
		int result = 0;
		int score = Integer.MIN_VALUE;
		for (int i=0; i<buttons.length; i++) {
			board[i/3][i%3]=buttons[i].getText();
		}
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				if (board[i][j] == "") {
					board[i][j] = "O";
					int newScore = minimax(board, true, 0);
					board[i][j] = "";
					if (newScore > score) {
						score = newScore;
						result = (i*3) + j;
					}
				}
			}
		}
		return result;
	}

	private int minimax(String[][] board, boolean x_turn, int depth) {
		int result;
		switch(status(board)){
			case "X":
				return -10;
			case "O":
				return 10;
			case "":
				return 0;
			default:
				break;
		}
		
		if (x_turn) {
			result = Integer.MAX_VALUE;
			for (int i=0; i<3; i++) {
				for (int j=0; j<3; j++) {
					if (board[i][j] == "") {
						board[i][j] = "X";
						int newScore = minimax(board, false, depth+1);
						board[i][j] = "";
						result = Integer.min(result, newScore);
					}
				}
			}
		} else {
			result = Integer.MIN_VALUE;
			for (int i=0; i<3; i++) {
				for (int j=0; j<3; j++) {
					if (board[i][j] == "") {
						board[i][j] = "O";
						int newScore = minimax(board, true, depth+1);
						board[i][j] = "";
						result = Integer.max(result, newScore);
					}
				}
			}
		}
		
		return result;
	}
	
	public String status(String[][] board) {
		String result = "";

		// check rows
		for (int i=0; i<3; i++) {
			if (	board[i][0] != "" && 
					board[i][0] == board[i][1] && 
					board[i][1] == board[i][2]) {
				return board[i][0];
			}
		}
		// check column
		for (int i=0; i<3; i++) {
			if (	board[0][i] != "" && 
					board[0][i] == board[1][i] && 
					board[1][i] == board[2][i]) {
				return board[0][i];
			}
		}
		// check diagonal
		if (board[1][1] != "") {
			if (board[1][1] == board[0][0] && board[1][1] == board[2][2])
				return board[1][1];
			if (board[1][1] == board[2][0] && board[1][1] == board[0][2])
				return board[1][1];
		}
		// check for empty tiles
		for (int i=0; i<9; i++) {
			if (board[i/3][i%3] == "") result = "I";
		}
		
		return result;
	}
}
