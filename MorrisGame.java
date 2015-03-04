import java.util.*;

public class MorrisGame
{
	public static int statEstOpening(MorrisPositionList board)
	{
		return (board.getNumPieces(posType.W) - board.getNumPieces(posType.B));
	}

	public static int statEstMidgameEndgame(MorrisPositionList board)
	{
		int bPieces = board.getNumPieces(posType.B);
		int wPieces = board.getNumPieces(posType.W);
		List<MorrisPositionList> l = generateMovesMidgameEndgame(board);
		int numBMoves = l.size();
		if (bPieces <= 2)
		{
			return 10000;
		}
		else if (wPieces <= 2)
		{
			return -10000;
		}
		else if (bPieces == 0)
		{
			return 10000;
		}
		else
		{
			return 1000*(wPieces - bPieces) - numBMoves;
		}
	}
	/*
		Input: a board position
		Output: a list L of board positions
	*/
	public static List<MorrisPositionList> generateMovesOpening(MorrisPositionList board)
	{
		/* Return the list produced by GenerateAdd applied to the board */
		return generateAdd(board);
	}

	/*
		Input: a board position
		Output: a list L of board positions
	*/
	public static List<MorrisPositionList> generateMovesOpeningBlack(MorrisPositionList board)
	{
		MorrisPositionList tempb = board.getFlipBoard();
		List<MorrisPositionList> moves = generateMovesOpening(tempb);
		for (int i = 0; i < moves.size(); i++)
		{
			MorrisPositionList b = moves.get(i);
			moves.set(i, b.getFlipBoard());
		}
		return moves;
	}

	/*
		Input: a board position
		Output: a list L of board positions
	*/
	public static List<MorrisPositionList> generateMovesMidgameEndgame(MorrisPositionList board)
	{
		/*
			If the board has 3 white pieces:
				Return the list produced by GnerateHopping
			Else
				Return the list produced by GenerateMove
		*/
		if (board.getNumPieces(posType.W) == 3)
		{
			return generateHopping(board);
		}
		else
		{
			return generateMove(board);
		}
	}

	
	/*
		Input: a board position
		Output: a list L of board positions
	*/
	public static List<MorrisPositionList> generateMovesMidgameEndgameBlack(MorrisPositionList board)
	{
		MorrisPositionList tempb = board.getFlipBoard();
		List<MorrisPositionList> moves = generateMovesMidgameEndgame(tempb);
		for (int i = 0; i < moves.size(); i++)
		{
			MorrisPositionList b = moves.get(i);
			moves.set(i, b.getFlipBoard());
		}
		return moves;
	}

	/*
		Input: a board position
		Output: a list L of board positions
	*/
	public static List<MorrisPositionList> generateAdd(MorrisPositionList board)
	{
		/*
			L = emptyList
			for each location in board:
				if board[location] == empty
					b = copyOfBoard
					b[location] = W
					if closeMill(location, b)
						generateRemove(b, L)
					else
						add b to L
			return L
		*/

		ArrayList<MorrisPositionList> l = new ArrayList<MorrisPositionList>();
		for (int i = 0; i < board.size(); i++)
		{
			if (board.get(i) == posType.X)
			{
				MorrisPositionList b = board.getCopy();
				b.set(i, posType.W);
				if (closeMill(i, b))
				{
					int s = l.size();
					l = generateRemove(b, l);
					//System.out.println("Before: " + s + "\t After " + l.size());
				}
				else
				{
					l.add(b);
				}
			}
		}
		return l;
	}

	/*
		Input: a board position
		Output: a list L of board positions
	*/
	public static List<MorrisPositionList> generateHopping(MorrisPositionList board)
	{
		/*
			L = emptyList
			for each location alpha in board:
				if board[alpha] == W
					for each location beta in board
						if board[beta] == empty
							b = copyOfBoard
							b[alpha] = empty
							b[beta] = W
							if closeMill(beta, b)
								generateRemove(b, L)
							else
								add b to L
			return L
		*/
		ArrayList<MorrisPositionList> l = new ArrayList<MorrisPositionList>();
		for (int i = 0; i < board.size(); i++)
		{
			if (board.get(i) == posType.W)
			{
				for (int j = 0; j < board.size(); j++)
				{
					if (board.get(j) == posType.X)
					{
						MorrisPositionList b = board.getCopy();
						b.set(i, posType.X);
						b.set(j, posType.W);
						if (closeMill(j, b))
						{
							generateRemove(b, l);
						}
						else
						{
							l.add(b);
						}
					}
				}
			}
		}
		return l;
	}


	/*
		Input: a board position
		Output: a list L of board positions
	*/
	public static List<MorrisPositionList> generateMove(MorrisPositionList board)
	{
		/*
			L = emptyList
			for each location in board:
				if board[location] == W
					n = listOfNeighbors(n)
					for each j in n
						if board[j] == empty
							b = copyOfBoard()
							b[location] = empty;
							b[j] = W;
							if closeMill(j, b)
								GenerateRemove(b, L)
							else
								add b to L
			return L
		*/
		ArrayList<MorrisPositionList> l = new ArrayList<MorrisPositionList>();
		for (int i = 0; i < board.size(); i++)
		{
			if (board.get(i) == posType.W)
			{
				List<Integer> n = neighbors(i);
				for (int j : n)
				{
					if (board.get(j) == posType.X)
					{
						MorrisPositionList b = board.getCopy();
						b.set(i, posType.X);
						b.set(j, posType.W);
						if (closeMill(j, b))
						{
							l = generateRemove(b, l);
						}
						else
						{
							l.add(b);
						}
					}
				}
			}
		}
		return l;
	}

	/*
		Input: a board position and a list L
		Output: positions are added to L by removing black pieces
	*/
	public static ArrayList<MorrisPositionList> generateRemove(MorrisPositionList board, ArrayList<MorrisPositionList> l)
	{
		/*
			for each location in board:
				if board[location] == B
					if not closeMill(location, board)
						b = copyOfBoard()
						b[location] = empty
						add b to L
			If no positions were added(all back pieces are in mills) add b to L
			return L
		*/
		for (int i = 0; i < board.size(); i++)
		{
			if (board.get(i) == posType.B)
			{
				if (!closeMill(i, board))
				{
					MorrisPositionList b = board.getCopy();
					b.set(i, posType.X);
					l.add(b);
				}
			}
		}
		/*
		if (allInMill(posType.B))
		{
			l.add(b);
		}
		*/
		return l;
	}

	/*
		Input: a location j in the array representing the board
		Output: a list of locations in the array corresponding to j's neighbors
	*/
	public static List<Integer> neighbors(int j)
	{
		/*
			switch(j)
				j == 0:
					return [1, 3, 8](these are d0, b1, a3)
				etc.
		*/
		switch(j)
		{
			case 0:
				return Arrays.asList(1, 3, 8);
			case 1:
				return Arrays.asList(0, 2, 4);
			case 2:
				return Arrays.asList(1, 5, 13);
			case 3:
				return Arrays.asList(0, 4, 6, 9);
			case 4:
				return Arrays.asList(1, 3, 5);
			case 5:
				return Arrays.asList(2, 4, 7, 12);
			case 6:
				return Arrays.asList(3, 7, 10);
			case 7:
				return Arrays.asList(5, 6, 11);
			case 8:
				return Arrays.asList(0, 9, 20);
			case 9:
				return Arrays.asList(3, 8, 10, 17);
			case 10:
				return Arrays.asList(6, 9, 14);
			case 11:
				return Arrays.asList(7, 12, 16);
			case 12:
				return Arrays.asList(5, 11, 13, 19);
			case 13:
				return Arrays.asList(2, 12, 22);
			case 14:
				return Arrays.asList(10, 15, 17);
			case 15:
				return Arrays.asList(14, 16, 18);
			case 16:
				return Arrays.asList(11, 15, 19);
			case 17:
				return Arrays.asList(9, 14, 18, 20);
			case 18:
				return Arrays.asList(15, 17, 19, 21);
			case 19:
				return Arrays.asList(12, 16, 18, 22);
			case 20:
				return Arrays.asList(8, 17, 21);
			case 21:
				return Arrays.asList(18, 20, 22);
			case 22:
				return Arrays.asList(13, 19, 21);
			default:
				return (new ArrayList<Integer>());
		}
	}

	/*
		Input: a location j in the array representing the board and the board b
		Output: true if the move to j closes a mill
	*/
	public static boolean closeMill(int j, MorrisPositionList b)
	{
		/*
			switch(j)
				j == 0:
					if ((b[1] == C && b[2] == C) || (b[3] == C && b[6] == C) || (b[8] == C && b[20] == C))
						return true
					else
						return false
				etc.

			The following logic is based on the following list of mills:
			0	1	2
			0	3	6
			0	8 	20
			11	12	13
			14	15	16
			15	18	21
			16	19	22
			17	18	19
			2 	5	7
			20	17	14
			20	21	22
			22	13	2
			3	4	5
			3	9	17
			5	12	19
			6	10 	14
			7	11	16
			8	9	10
		*/

		posType C = b.get(j);
		if (C == posType.X)
		{
			return false;
		}

		switch(j)
		{
			case 0:
				return (checkMill(b, C, 1, 2) || checkMill(b, C, 8, 20) || checkMill(b, C, 3, 6));
			case 1:
				return (checkMill(b, C, 0, 2));
			case 2:
				return (checkMill(b, C, 0, 1) || checkMill(b, C, 5, 7) || checkMill(b, C, 13, 22));
			case 3:
				return (checkMill(b, C, 0, 6) || checkMill(b, C, 4, 5) || checkMill(b, C, 9, 17));
			case 4:
				return (checkMill(b, C, 3, 5));
			case 5:
				return (checkMill(b, C, 3, 4) || checkMill(b, C, 2, 7) || checkMill(b, C, 12, 19));
			case 6:
				return (checkMill(b, C, 0, 3) || checkMill(b, C, 10, 14));
			case 7:
				return (checkMill(b, C, 2, 5) || checkMill(b, C, 11, 16));
			case 8:
				return (checkMill(b, C, 0, 20) || checkMill(b, C, 9, 10));
			case 9:
				return (checkMill(b, C, 8, 10) || checkMill(b, C, 3, 17));
			case 10:
				return (checkMill(b, C, 8, 9) || checkMill(b, C, 6, 14));
			case 11:
				return (checkMill(b, C, 7, 16) || checkMill(b, C, 12, 13));
			case 12:
				return (checkMill(b, C, 11, 13) || checkMill(b, C, 5, 19));
			case 13:
				return (checkMill(b, C, 11, 12) || checkMill(b, C, 2, 22));
			case 14:
				return (checkMill(b, C, 17, 20) || checkMill(b, C, 15, 16) || checkMill(b, C, 6, 14));
			case 15:
				return (checkMill(b, C, 14, 16) || checkMill(b, C, 18, 21));
			case 16:
				return (checkMill(b, C, 14, 15) || checkMill(b, C, 19, 22) || checkMill(b, C, 7, 11));
			case 17:
				return (checkMill(b, C, 3, 9) || checkMill(b, C, 14, 20) || checkMill(b, C, 18, 19));
			case 18:
				return (checkMill(b, C, 17, 19) || checkMill(b, C, 15, 21));
			case 19:
				return (checkMill(b, C, 17, 18) || checkMill(b, C, 16, 22) || checkMill(b, C, 5, 12));
			case 20:
				return (checkMill(b, C, 0, 8) || checkMill(b, C, 14, 17) || checkMill(b, C, 21, 22));
			case 21:
				return (checkMill(b, C, 20, 22) || checkMill(b, C, 15, 18));
			case 22:
				return (checkMill(b, C, 2, 13) || checkMill(b, C, 16, 19) || checkMill(b, C, 20, 21));
			default:
				return false;
		}
	}

	private static boolean checkMill(MorrisPositionList b, posType C, int v1, int v2)
	{
		return (b.get(v1) == C && b.get(v2) == C);
	}
}