package no.runsafe.toybox.command;

import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.command.argument.RequiredArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.entity.IEntity;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.RunsafeLocation;
import no.runsafe.framework.minecraft.entity.EntityType;

import java.util.Map;

public class Bazooka extends PlayerCommand
{
	public Bazooka(IScheduler scheduler)
	{
		super(
			"bazooka", "Fire an entity and make it explode", "runsafe.toybox.bazooka",
			new RequiredArgument("entityName"), new RequiredArgument("delay"), new RequiredArgument("strength")
		);
		this.scheduler = scheduler;
	}

	@Override
	public String OnExecute(IPlayer executor, Map<String, String> parameters)
	{
		final int delay = Integer.parseInt(parameters.get("delay"));
		final float strength = Float.parseFloat(parameters.get("strength"));
		final IEntity projectile = executor.Launch(EntityType.Get(parameters.get("entityName")));
		if (projectile != null)
		{
			scheduler.startSyncTask(
				new Runnable()
				{
					@Override
					public void run()
					{
						RunsafeLocation location = projectile.getLocation();
						projectile.getWorld().createExplosion(location, strength, false);
						projectile.remove();
					}
				},
				delay
			);
		}
		return null;
	}

	private final IScheduler scheduler;
}
