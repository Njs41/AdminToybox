package no.runsafe.toybox.command;

import net.minecraft.server.v1_8_R3.*;
import no.runsafe.framework.api.command.argument.*;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.entity.IEntity;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.internal.wrapper.ObjectUnwrapper;
import no.runsafe.framework.minecraft.entity.PassiveEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

public class SpawnCustomMinecart extends PlayerCommand
{
	public SpawnCustomMinecart()
	{
		super("spawncustomminecart",
				"Spawn a custom minecart!",
				"runsafe.toybox.spawnminecart",
				new RequiredArgument(Args.blockName.value),
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
		//Get input
		String blockName = parameters.getValue(Args.blockName.value);
		blockName.toLowerCase();

		//Create minecart
		IEntity minecartEntity = PassiveEntity.Minecart.spawn(executor.getLocation());
		CraftEntity craftEntity = ObjectUnwrapper.convert(minecartEntity);
		EntityMinecartAbstract minecart = (EntityMinecartAbstract) craftEntity.getHandle();

		//Create block in minecart
		Block minecartBlock = Block.getByName(blockName);
		minecart.setDisplayBlock(minecartBlock.getBlockData());

		//Set block offset
		minecart.SetDisplayBlockOffset((Integer) parameters.getValue(Args.blockOffset.value));

		return null;
	}
}
