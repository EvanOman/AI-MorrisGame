import java.util.*;
import java.io.*;

public class MiniMaxOpeningBlack
{
	public static void main(String[] args)
	{
		String inputFileName = args[0];
		String outputFileName = args[1];
		int depth = Integer.parseInt(args[2]);
		MorrisPositionList initBoard = new MorrisPositionList(getBoardConfig(inputFileName));
		System.out.println(initBoard);
		outputObj algOut = MiniMax(depth, true, initBoard);
		writeOutput(algOut, outputFileName);
	}

	public static outputObj MiniMax(int depth, boolean isBlack, MorrisPositionList board)
	{
		outputObj out = new outputObj();
		/* Means that we are at a terminal node */
		if (depth == 0)
		{
			out.val = MorrisGame.statEstOpeningBlack(board);
			return out;
		}

		outputObj in = new outputObj();
		List<MorrisPositionList> nextMoves = (isBlack) ? MorrisGame.generateMovesOpeningBlack(board) : MorrisGame.generateMovesOpening(board);
		out.val = (isBlack) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		for (MorrisPositionList b : nextMoves)
		{
			if (isBlack)
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
			else
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
		try
		{
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



