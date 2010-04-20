package hamhamdash;

import jgame.*;

/**
 *
 * @author Cornel Alders
 */
public class Diamond extends GObject
{

	public int diamondPoint = 3;// Points for diamond
	public int diamondsLeft = 5;// Diamonds left for next level


	public Diamond(String name, boolean unique, int x, int y, String sprite)

	{
		super(name, unique, x, y, 3, "diamond");
	}


	@Override
	public void move()
	{
		super.move();
	};


	@Override
	public void hit_bg(int tilecid)
	{

		if ( tilecid == 1 )
		{
			game.getCurrentLevel().digTile(getCenterTile().x, getCenterTile().y);
		}
	}

	@Override
	public void hit(JGObject obj)
	{
		if(obj.colid == 1)
		{
			remove();
				
		}

	}





	

}
