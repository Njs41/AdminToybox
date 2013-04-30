package no.runsafe.toybox.command;

import no.runsafe.framework.command.player.PlayerCommand;
import no.runsafe.framework.server.RunsafeLocation;
import no.runsafe.framework.server.RunsafeWorld;
import no.runsafe.framework.server.player.RunsafePlayer;
import org.bukkit.entity.TNTPrimed;

import java.util.HashMap;

public class Nuke extends PlayerCommand
{
	public Nuke()
	{
		super("nuke", "Nukes in a certain radius", "runsafe.toybox.nuke", "radius", "power");
	}

	@Override
	public String OnExecute(RunsafePlayer executor, HashMap<String, String> parameters)
	{
		RunsafeWorld world = executor.getWorld();
		int radius = Integer.valueOf(parameters.get("radius"));
		int power = Integer.valueOf(parameters.get("power"));
		RunsafeLocation location = executor.getLocation();

		double maxX = location.getX() + radius;
		double maxZ = location.getZ() + radius;

		location.decrementX(radius);
		location.decrementZ(radius);

		double currentX = location.getX();

		while (currentX < maxX)
		{
			double currentZ = location.getZ();
			while (currentZ < maxZ)
			{
				TNTPrimed tnt = world.spawn(location, TNTPrimed.class);
				tnt.setIsIncendiary(true);
				tnt.setYield(power);
				location.incrementZ(radius);
				currentZ = location.getZ();
			}
			location.incrementX(radius);
			currentX = location.getX();
		}
		return null;
	}
}
