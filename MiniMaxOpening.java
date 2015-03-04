import java.util.*;

public class MiniMaxOpening
{
	public static int COUNTER = 0;
	public static void run()
	{
		MorrisPositionList initBoard = new MorrisPositionList();
		System.out.println(MiniMax(6, true, initBoard));
	}


	public static outputObj MiniMax(int depth, boolean isWhite, MorrisPositionList board)
	{
		outputObj out = new outputObj();
		/* Means that we are at a terminal node */
		if (depth == 0)
		{
			//System.out.println(board);
			out.val = MorrisGame.statEstOpening(board);
			//System.out.println("Value: " + val);
			
			return out;
		}

		List<MorrisPositionList> nextMoves;
		outputObj in = new outputObj();
		if (isWhite)
		{
			nextMoves = MorrisGame.generateMovesOpening(board);
			out.val = Integer.MIN_VALUE;
			for (MorrisPositionList b : nextMoves)
			{
				in = MiniMax(depth - 1, false, b);
				out.numNodes += in.numNodes;
				out.numNodes++;
				if (in.val > out.val)
				{
					out.val = in.val;
					out.b = b;
				}
			}
		}
		else
		{
			nextMoves = MorrisGame.generateMovesOpeningBlack(board);
			out.val = Integer.MAX_VALUE;
			for (MorrisPositionList b : nextMoves)
			{
				in = MiniMax(depth - 1, true, b);
				out.numNodes += in.numNodes;
				out.numNodes++;
				if (in.val < out.val)
				{
					out.val = in.val;
					out.b = b;
				}
			}
		}
		return out;
	}

	public static void test(List<Integer> l)
	{
		l.add(4);
		System.out.println(l);
	}
	
	public static class outputObj
	{
		public int val, numNodes;
		public MorrisPositionList b;
		public String toString()
		{
			return 	"BoardPosition:\t\t" + b + "\n" +
					"Positions Evaluated:\t" + numNodes + "\n" + 
					"MINIMAX estimate:\t" + val;
		}
	}
}



