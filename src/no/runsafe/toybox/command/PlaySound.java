package no.runsafe.toybox.command;

import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.RequiredArgument;
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
			new RequiredArgument(Args.volume.value),
			new RequiredArgument(Args.pitch.value)
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
		float volume = Float.valueOf(parameters.get(Args.volume.value));
		float pitch = Float.valueOf(parameters.get(Args.pitch.value));

		Sound sound = Sound.Get(parameters.get(Args.sound.value));
		if (sound == null)
			return "&cThat sound does not exist.";

		executor.getWorld().playSound(executor.getLocation(), sound, volume, pitch);
		return "&2Sound played.";
	}
}
