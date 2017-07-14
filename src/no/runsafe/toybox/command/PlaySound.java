package no.runsafe.toybox.command;

import no.runsafe.framework.api.command.argument.DecimalNumber;
import no.runsafe.framework.api.command.argument.Enumeration;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.Sound;

public class PlaySound extends PlayerCommand
{
	public PlaySound()
	{
		super(
			"playsound", "Plays a sound", "runsafe.toybox.playsound",
			new Enumeration(SOUND, org.bukkit.Sound.values()).require(),
			new DecimalNumber(VOLUME).withDefault(1.0F),
			new DecimalNumber(PITCH).withDefault(1.0F)
		);
	}

	private static final String SOUND = "sound";
	private static final String VOLUME = "volume";
	private static final String PITCH = "pitch";

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		float volume = parameters.getValue(VOLUME);
		float pitch = parameters.getValue(PITCH);

		Sound sound = Sound.Get(parameters.getRequired(SOUND));
		if (sound == null)
			return "&cThat sound does not exist.";

		executor.getWorld().playSound(executor.getLocation(), sound, volume, pitch);
		return "&2Sound played.";
	}
}
