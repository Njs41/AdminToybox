package no.runsafe.toybox.events;

import no.runsafe.framework.api.block.IBlock;
import no.runsafe.framework.api.block.ISign;
import no.runsafe.framework.api.event.block.IBlockPlace;
import no.runsafe.framework.api.player.IPlayer;

public class BlockPlace implements IBlockPlace
{
	@Override
	public boolean OnBlockPlace(IPlayer player, IBlock block)
	{
		if (block instanceof ISign)
		{
			ISign sign = (ISign) block;
			if (sign.getLine(0).equals("[Infinite]"))
				if (!player.hasPermission("runsafe.toybox.infinitedispensers"))
					return denyAction(player);
		}
		return true;
	}

	/**
	 * Displays the message "I'm sorry playername, I'm afraid I can't let you do that" and returns false.
	 * @param player player to deny action.
	 * @return false.
	 */
	private boolean denyAction(IPlayer player)
	{
		player.sendColouredMessage(String.format("&cI'm sorry %s, I'm afraid I can't let you do that", player.getName()));
		return false;
	}
}
