import java.util.*;
public class MorrisPositionList
{
	public List<posType> posList;
	public MorrisPositionList()
	{
		posList = Arrays.asList(posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X, posType.X);
	}

	public MorrisPositionList(List<Character> inputBoard)
	{
		posList = new ArrayList<posType>();
		for (char c : inputBoard)
		{
			posType pos = (c == 'W') ? posType.W : ((c == 'B') ? posType.B : posType.X);
			posList.add(pos);
		}
	}

	public List<Character> getCharList()
	{
		ArrayList<Character> out = new ArrayList<Character>();
		for (posType pos : posList)
		{
			out.add(pos.val);
		}
		return out;
	}

	public char[] getCharArr()
	{
		char[] out = new char[posList.size()];
		for (int i = 0; i < posList.size(); i++)
		{
			out[i] = posList.get(i).val;
		}
		return out;
	}

	public MorrisPositionList getCopy()
	{
		List<Character> boardState = getCharList();
		return (new MorrisPositionList(boardState));
	}

	public MorrisPositionList getFlipBoard()
	{
		MorrisPositionList out = new MorrisPositionList();
		for (int i = 0; i < posList.size(); i++)
		{
			posType val = posList.get(i);
			if (val == posType.B)
			{
				out.set(i, posType.W);
			}
			else if (val == posType. W)
			{
				out.set(i, posType.B);
			}
			else
			{
				out.set(i, posType.X);
			}
		}
		return out;
	}

	public posType get(int i)
	{
		return posList.get(i);
	}

	public int size()
	{
		return posList.size();
	}

	public void add(posType val)
	{
		posList.add(val);
	}

	public void set(int i, posType val)
	{
		posList.set(i, val);
	}

	public int getNumPieces(posType piecePos)
	{
		int counter = 0;
		for (posType pos : posList)
		{
			if (pos == piecePos)
			{
				counter++;
			}
		}
		return counter;
	}

	public String toString()
	{
		return (new String(getCharArr()));
	}
}