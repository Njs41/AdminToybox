package no.runsafe.toybox.command;

import no.runsafe.framework.api.IWorld;
import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.AutoWorldArgument;
import no.runsafe.framework.api.command.argument.Enumeration;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.log.IConsole;

public class Time extends ExecutableCommand
{
	public Time(IConsole console)
	{
		super(
			"time", "Change the time in a world", "runsafe.toybox.time",
			new Enumeration(TIME, WellKnownTimes.values()).require(),
			new AutoWorldArgument()
		);
		this.console = console;
	}

	private static final String TIME = "time";
	private static final String WORLD = "world";

	@Override
	public String OnExecute(ICommandExecutor executor, IArgumentList parameters)
	{
		IWorld target = parameters.getValue(WORLD);

		if (target == null)
			return "You must specify a world from the console!";

		WellKnownTimes time = parameters.getValue(TIME);
		if (time == null)
			return null;
		target.setTime(time.getTime());
		console.logInformation("Time in world %s set to %s by %s", target.getName(), time, executor.getName());
		return String.format("Time set for %s", target.getName());
	}

	public enum WellKnownTimes
	{
		Dawn(22000),
		Morning(0),
		Noon(6000),
		Dusk(13000),
		Midnight(18000);

		public int getTime()
		{
			return time;
		}

		private WellKnownTimes(int time)
		{
			this.time = time;
		}

		private final int time;
	}

	private final IConsole console;
}
