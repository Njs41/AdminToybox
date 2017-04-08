package no.runsafe.toybox.command;

import no.runsafe.framework.api.ILocation;
import no.runsafe.framework.api.command.argument.*;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.entity.PassiveEntity;
import no.runsafe.framework.minecraft.entity.RunsafeMinecart;
import org.bukkit.Material;

public class SpawnCustomMinecart extends PlayerCommand
{
	public SpawnCustomMinecart()
	{
		super("spawncustomminecart",
				"Spawn a custom minecart!",
				"runsafe.toybox.spawnminecart",
				new Enumeration(Args.blockName.value, Material.values()).require(),
				new WholeNumber(Args.blockOffset.value).withDefault(8)
		);
	}

	private enum Args
	{
		blockName("blockName"),
		blockOffset("blockOffset");

		private final String value;

		Args(String newArgumentName)
		{
			this.value = newArgumentName;
		}
	}

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		ILocation location = executor.getLocation();
		if (location == null)
			return "Invalid location.";

		//Create minecart
		RunsafeMinecart minecart = (RunsafeMinecart) PassiveEntity.Minecart.spawn(location);

		//Create block in minecart
		minecart.setDisplayBlock((Material) parameters.getValue(Args.blockName.value));

		//Set block offset
		minecart.setDisplayBlockOffset((Integer) parameters.getValue(Args.blockOffset.value));

		return null;
	}
}
