package no.runsafe.toybox.command;

import no.runsafe.framework.api.IWorld;
import no.runsafe.framework.api.command.IBranchingExecution;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.WholeNumber;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;

public class LightningCoords extends PlayerCommand implements IBranchingExecution
{
	public LightningCoords()
	{
		super("lightning",
			"Fires lightning at a coordinate",
			"runsafe.toybox.lightning",
			new WholeNumber(Args.x.value).require(),
			new WholeNumber(Args.y.value).require(),
			new WholeNumber(Args.z.value).require()
		);
	}

	private enum Args
	{
		x("x"),
		y("y"),
		z("z");

		private final String value;

		Args(String newArgumentName)
		{
			this.value = newArgumentName;
		}
	}

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		Integer x = parameters.getValue(Args.x.value);
		Integer y = parameters.getValue(Args.y.value);
		Integer z = parameters.getValue(Args.z.value);
		assert (x != null && y != null && z != null);
		IWorld world = executor.getWorld();
		if (world != null)
			world.strikeLightning(world.getLocation(x + 0.5, y + 0.5, z + 0.5));
		return null;
	}
}
