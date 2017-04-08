package no.runsafe.toybox.command;

import no.runsafe.framework.api.command.argument.*;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.Sound;

public class PlaySound extends PlayerCommand
{
	public PlaySound()
	{
		super("playsound",
			"Plays a sound",
			"runsafe.toybox.playsound",
			new RequiredArgument(Args.sound.value),
			new WholeNumber(Args.volume.value).withDefault(100),
			new DecimalNumber(Args.pitch.value).withDefault(1.0F)
		);
	}

	private enum Args
	{
		sound("sound"),
		volume("volume"),
		pitch("pitch");

		private final String value;

		Args(String newArgumentName)
		{
			this.value = newArgumentName;
		}
	}

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		float volume = Float.valueOf((Integer) parameters.getValue(Args.volume.value));
		float pitch = Float.valueOf((Float) parameters.getValue(Args.pitch.value));

		Sound sound = Sound.Get((String) parameters.getValue(Args.sound.value));
		if (sound == null)
			return "&cThat sound does not exist.";

		executor.getWorld().playSound(executor.getLocation(), sound, volume, pitch);
		return "&2Sound played.";
	}
}
