package hamhamdash;

import jgame.*;

/**
 *
 * @author Cornel Alders
 */
public class Diamond extends GObject
{
	public Diamond(String name, boolean unique, int x, int y, int cid, String sprite)
	{
		super(name, unique, x, y, cid, sprite);
	}

	@Override
	public void move(){};

	@Override
	public void hit_bg(int tilecid){};

	@Override
	public void hit(JGObject obj){};
}
