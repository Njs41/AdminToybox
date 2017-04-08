package no.runsafe.toybox.command;

import no.runsafe.framework.api.command.argument.EntityType;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.RequiredArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.minecraft.RunsafeEntityType;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.toybox.handlers.MobDropHandler;

public class MobDrop extends PlayerCommand
{
	public MobDrop(MobDropHandler handler)
	{
		super("mobdrop",
			"Drops a block full of mobs",
			"runsafe.toybox.mobdrop",
			new EntityType(Args.mobType.value).require(),
			new RequiredArgument(Args.amount.value)
		);
		this.handler = handler;
	}

	private enum Args
	{
		mobType("mobType"),
		amount("amount");

		private final String value;

		Args(String newArgumentName)
		{
			this.value = newArgumentName;
		}
	}

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		handler.createMobDropper(
			executor.getLocation(),
			(RunsafeEntityType) parameters.getValue(Args.mobType.value),
			(Integer) parameters.getValue(Args.amount.value)
		);
		return null;
	}

	private final MobDropHandler handler;
}
