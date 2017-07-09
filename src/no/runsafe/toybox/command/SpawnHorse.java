package no.runsafe.toybox.command;

import no.runsafe.framework.api.command.argument.BooleanArgument;
import no.runsafe.framework.api.command.argument.Enumeration;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.WholeNumber;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.entity.animals.horses.INormalHorse;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.entity.LivingEntity;
import org.bukkit.entity.Horse;

public class SpawnHorse extends PlayerCommand
{
	public SpawnHorse()
	{
		super(
			"spawnhorse", "Spawns a horse", "runsafe.toybox.spawnmob",
			new WholeNumber(COUNT).withDefault(1),
			new BooleanArgument(TAME).withDefault(true),
			new Enumeration(TYPE, Horse.Variant.values()),
			new Enumeration(STYLE, Horse.Style.values()),
			new Enumeration(COLOUR, Horse.Color.values())
		);
	}

	private static final String COUNT = "count";
	private static final String TAME = "tame";
	private static final String TYPE = "type";
	private static final String STYLE = "style";
	private static final String COLOUR = "colour";

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		int count = parameters.getValue(COUNT);
		if (count > 255)
			return "&cMaximum amount: 255";
		Horse.Variant type = parameters.getValue(TYPE);
		Horse.Style style = parameters.getValue(STYLE);
		Horse.Color colour = parameters.getValue(COLOUR);

		INormalHorse horse = (INormalHorse) LivingEntity.Horse.spawn(executor.getLocation());
		horse.setTamed(parameters.getRequired(TAME));

		// Set the type of horse.
		if (type == null)
			horse.setRandomType();
		else
			horse.setType(type);

		if (horse.getType() != Horse.Variant.HORSE)
			return null;

		// Set the horse's style.
		if (style == null)
			horse.setRandomColour();
		else
			horse.setStyle(parameters.getValue(STYLE));

		// Set the horse's colour.
		if (colour == null)
			horse.setRandomColour();
		else
			horse.setColour(colour);

		return null;
	}
}
