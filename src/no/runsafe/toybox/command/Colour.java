package no.runsafe.toybox.command;

import no.runsafe.framework.api.command.argument.RequiredArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.Item;
import no.runsafe.framework.minecraft.item.RunsafeItemStack;
import no.runsafe.framework.minecraft.item.meta.RunsafeLeatherArmor;

import java.util.Map;

public class Colour extends PlayerCommand
{
	public Colour()
	{
		super(
			"colour", "Colours an item you are holding.", "runsafe.toybox.colour",
			new RequiredArgument("hex")
		);
	}

	@Override
	public String OnExecute(IPlayer executor, Map<String, String> parameters)
	{
		RunsafeItemStack item = executor.getItemInHand();

		if (item.is(Item.Combat.Leggings.Leather) || item.is(Item.Combat.Boots.Leather) || item.is(Item.Combat.Chestplate.Leather) || item.is(Item.Combat.Helmet.Leather))
		{
			RunsafeLeatherArmor armor = (RunsafeLeatherArmor) item;
			if (parameters.get("hex").equals("random"))
				((RunsafeLeatherArmor) item).RandomColour();
			else
				armor.setColor(Integer.valueOf(parameters.get("hex"), 16));
			return "&2Coloured!";
		}
		return "&cYou cannot colour that item.";
	}
}
