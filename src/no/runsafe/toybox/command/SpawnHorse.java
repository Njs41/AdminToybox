package no.runsafe.toybox.command;

import no.runsafe.framework.api.command.argument.Enumeration;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.RequiredArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.toybox.horses.HorseSpawner;
import no.runsafe.toybox.horses.SpawnableHorseType;
import no.runsafe.toybox.horses.SpawnableHorseVariant;

import java.util.Random;

public class SpawnHorse extends PlayerCommand
{
	public SpawnHorse(HorseSpawner horseSpawner)
	{
		super("spawnhorse",
			"Spawns a horse",
			"runsafe.toybox.spawnmob",
			new RequiredArgument(COUNT),
			new RequiredArgument(TAME),
			new Enumeration(TYPE, SpawnableHorseType.values()),
			new Enumeration(VARIANT, SpawnableHorseVariant.values())
		);
		this.horseSpawner = horseSpawner;
	}

	private static final String COUNT = "count";
	private static final String TAME = "tame";
	private static final String TYPE = "type";
	private static final String VARIANT = "variant";

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		try
		{
			SpawnableHorseType type = parameters.getValue(TYPE);
			if (type == null)
				type = this.getRandomHorseType();
			SpawnableHorseVariant variant = parameters.getValue(VARIANT);
			if (variant == null)
				variant = this.getRandomHorseVariant();

			int count = Integer.valueOf(parameters.get(COUNT));

			for (int i = 0; i < count; ++i)
				this.horseSpawner.spawnHorse(executor.getLocation(), type, variant, parameters.get(TAME).equals("1"));
		}
		catch (IllegalArgumentException exception)
		{
			return "&cThat type of horse does not exist.";
		}

		return null;
	}

	public SpawnableHorseType getRandomHorseType()
	{
		SpawnableHorseType[] values = SpawnableHorseType.values();
		return values[this.random.nextInt(values.length)];
	}

	public SpawnableHorseVariant getRandomHorseVariant()
	{
		SpawnableHorseVariant[] values = SpawnableHorseVariant.values();
		return values[this.random.nextInt(values.length)];
	}

	private Random random = new Random();
	private HorseSpawner horseSpawner;
}
