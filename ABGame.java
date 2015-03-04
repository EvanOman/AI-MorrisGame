import java.util.*;
import java.io.*;

public class ABGame
{
	public static void main(String[] args)
	{
		String inputFileName = args[0];
		String outputFileName = args[1];
		int depth = Integer.parseInt(args[2]);
		MorrisPositionList initBoard = new MorrisPositionList(getBoardConfig(inputFileName));
		outputObj algOut = ABMiniMax(depth, true, initBoard, Integer.MIN_VALUE, Integer.MAX_VALUE);
		writeOutput(algOut, outputFileName);
	}
	
	
	public static outputObj ABMiniMax(int depth, boolean isWhite, MorrisPositionList board, int alpha, int beta)
	{
		outputObj out = new outputObj();
		/* Means that we are at a terminal node */
		if (depth == 0)
		{
			out.val = MorrisGame.statEstMidgameEndgame(board);
			return out;
		}

		List<MorrisPositionList> nextMoves;
		outputObj in = new outputObj();
		nextMoves = (isWhite) ? MorrisGame.generateMovesMidgameEndgame(board) : MorrisGame.generateMovesMidgameEndgameBlack(board);
		for (MorrisPositionList b : nextMoves)
		{
			if (isWhite)
			{
				in = ABMiniMax(depth - 1, false, b, alpha, beta);
				out.numNodes += in.numNodes;
				out.numNodes++;
				if (in.val > alpha)
				{
					alpha = in.val;
					out.b = b;
				}
			}
			else
			{
				in = ABMiniMax(depth - 1, true, b, alpha, beta);
				out.numNodes += in.numNodes;
				out.numNodes++;
				if (in.val < beta)
				{
					beta = in.val;
					out.b = b;
				}
			}
			if (alpha >= beta)
			{
				break;
			}
		}
		
		out.val = (isWhite) ? alpha : beta;
		return out;
	}

	public static List<Character> getBoardConfig(String fName)
	{
		String line = null;
		
		try
		{
			FileReader fileR = new FileReader(fName);
			BufferedReader buffR = new BufferedReader(fileR);
			line = buffR.readLine();
			ArrayList<Character> out = new ArrayList<Character>();
			for (char a : line.toCharArray())
			{
				out.add(a);
			}
			buffR.close();
			return out;
		}
		catch(FileNotFoundException ex)
		{
			System.out.println( "Unable to open file '" + fName + "'");
		}
		catch(IOException ex)
		{
			System.out.println("Error reading file '" + fName + "'");
		}
		return null;
	}
	
	public static void writeOutput(outputObj out, String fName)
	{
		try {
			// Assume default encoding.
			FileWriter fileWriter = new FileWriter(fName);

			// Always wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// Note that write() does not automatically
			// append a newline character.
			bufferedWriter.write(out.toString());

			// Always close files.
			bufferedWriter.close();
		}
		catch(IOException ex) {
			System.out.println("Error writing to file '" + fName + "'");
		}
	}
	
	public static class outputObj
	{
		public int val, numNodes;
		public MorrisPositionList b;
		public String toString()
		{
			return 	"BoardPosition:\t\t\t" + b + "\n" +
					"Positions Evaluated:\t" + numNodes + "\n" + 
					"MINIMAX estimate:\t\t" + val;
		}
	}
}



