package no.runsafe.toybox.command;

import no.runsafe.framework.api.IWorld;
import no.runsafe.framework.api.command.argument.EntityType;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.RequiredArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;

public class SpawnMob extends PlayerCommand
{
	public SpawnMob()
	{
		super("spawnmob",
			"Spawns a mob",
			"runsafe.toybox.spawnmob",
			new EntityType(Args.name.value).require(),
			new RequiredArgument(Args.count.value)
		);
	}

	private enum Args
	{
		name("name"),
		count("count");

		private final String value;

		Args(String newArgumentName)
		{
			this.value = newArgumentName;
		}
	}

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		int n = Integer.parseInt(parameters.get(Args.count.value));
		String name = parameters.get(Args.name.value);

		if (name != null && name.equalsIgnoreCase("horse"))
			return "&cPlease use /spawnhorse for that.";

		for (int i = 0; i < n; ++i)
		{
			IWorld world = executor.getWorld();
			if (world != null)
				world.spawn(executor.getLocation(), no.runsafe.framework.minecraft.entity.EntityType.getTypeByName(name));
		}
		return null;
	}
}
