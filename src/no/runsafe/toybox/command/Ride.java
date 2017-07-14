package no.runsafe.toybox.command;

import no.runsafe.framework.api.IWorld;
import no.runsafe.framework.api.command.argument.EntityType;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.entity.IEntity;
import no.runsafe.framework.api.player.IPlayer;

public class Ride extends PlayerCommand
{
	public Ride()
	{
		super(
			"ride", "Spawns an entity and mounts you to it", "runsafe.toybox.ride",
			new EntityType(ENTITY_TYPE).require()
		);
	}

	private static final String ENTITY_TYPE = "entityType";

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		IWorld world = executor.getWorld();
		if (world == null)
			return null;
		IEntity entity = executor.getWorld().spawn(executor.getLocation(), parameters.getValue(ENTITY_TYPE));
		entity.setPassenger(executor);
		return null;
	}
}
