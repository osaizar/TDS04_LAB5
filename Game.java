public class Game {

	private static int WIDTH = 10;
	private static int HEIGHT = 10;
	int[][] gameBoard = new int[WIDTH][HEIGHT];

	public Game(){

  }

  public String makeMove(int player, int row, int col){
    if (col < 0 || col > WIDTH || row < 0 || row > HEIGHT){
      return "Col and row values are not correct";
    }
    else if (gameBoard[row][col] != 0){
      return "The selected square is not empty";
    }
    else{
      gameBoard[row][col] = player;
      return "correct";
    }
  }

  public String getGameBoard(){
    String rt = "";
    for (int i = 0; i < WIDTH; i++){
      for (int j = 0; j < HEIGHT; j++){

        if(gameBoard[i][j] == 1){
          rt += " x ";
        }else if(gameBoard[i][j]==2){
          rt += " o ";
        }else{
          rt += " - ";
        }
      }
      rt += "\n\n";
    }

    return rt;
  }

	public int getWinner(){
		if (gameCheck(1)){
			return 1;
		}else if (gameCheck(2)){
			return 2;
		}else{
			return 0;
		}
	}

	private boolean gameCheck(int player){
		for (int i = 0; i < WIDTH; i++){
			for (int j = 0; j < HEIGHT; j++){
				if (gameBoard[i][j] == player){
					if(checkVertical(player) || checkHorizontal(player)){
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean checkVertical(int player){
		int inRow = 0;
		for(int w = 0; w < WIDTH; w++){
			for (int h = 0; h < HEIGHT; h++){
				if(gameBoard[w][h] == player){
					inRow ++;
					if (inRow == 5){
						return true;
					}
				}else{
					inRow = 0;
				}
			}
			inRow = 0;
		}
		return false;
	}

	private boolean checkHorizontal(int player){
		int inRow = 0;
		for(int h = 0; h < HEIGHT; h++){
			for (int w = 0; w < WIDTH; w++){
				if(gameBoard[w][h] == player){
					inRow ++;
					if (inRow == 5){
						return true;
					}
				}else{
					inRow = 0;
				}
			}
			inRow = 0;
		}
		return false;
	}
}
