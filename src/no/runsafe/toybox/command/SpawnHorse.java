package no.runsafe.toybox.command;

import no.runsafe.framework.api.command.argument.EnumArgument;
import no.runsafe.framework.api.command.argument.RequiredArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.toybox.horses.HorseSpawner;
import no.runsafe.toybox.horses.SpawnableHorseType;
import no.runsafe.toybox.horses.SpawnableHorseVariant;

import java.util.Map;
import java.util.Random;

public class SpawnHorse extends PlayerCommand
{
	public SpawnHorse(HorseSpawner horseSpawner)
	{
		super(
			"spawnhorse", "Spawns a horse", "runsafe.toybox.spawnmob",
			new RequiredArgument("count"), new RequiredArgument("tame"),
			new EnumArgument("type", SpawnableHorseType.values(), false),
			new EnumArgument("variant", SpawnableHorseVariant.values(), false)
		);
		this.horseSpawner = horseSpawner;
	}

	@Override
	public String OnExecute(IPlayer executor, Map<String, String> parameters)
	{
		try
		{
			SpawnableHorseType type = parameters.containsKey("type")
				? SpawnableHorseType.valueOf(parameters.get("type").toUpperCase())
				: this.getRandomHorseType();
			SpawnableHorseVariant variant = parameters.containsKey("variant")
				? SpawnableHorseVariant.valueOf(parameters.get("variant").toUpperCase())
				: this.getRandomHorseVariant();

			if (type == null)
				return "&cInvalid horse type.";

			if (variant == null)
				return "&cInvalid horse variant.";

			int count = Integer.valueOf(parameters.get("count"));

			for (int i = 0; i < count; ++i)
				this.horseSpawner.spawnHorse(executor.getLocation(), type, variant, parameters.get("tame").equals("1"));
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
