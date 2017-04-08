package no.runsafe.toybox.command;

import no.runsafe.framework.api.IWorld;
import no.runsafe.framework.api.command.argument.EntityType;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.RequiredArgument;
import no.runsafe.framework.api.command.argument.WholeNumber;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.entity.IEntity;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.Buff;
import no.runsafe.framework.minecraft.Enchant;
import no.runsafe.framework.minecraft.Item;
import no.runsafe.framework.minecraft.entity.RunsafeLivingEntity;
import no.runsafe.framework.minecraft.item.meta.RunsafeMeta;

public class SpawnGodMob extends PlayerCommand
{
	public SpawnGodMob()
	{
		super("spawngodmob",
			"Spawns a god-like mob",
			"runsafe.toybox.spawngodmob",
			new EntityType(Args.mobName.value).require(),
			new WholeNumber(Args.amount.value).require()
		);
	}

	private enum Args
	{
		mobName("mobName"),
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
		Integer n = parameters.getValue(Args.amount.value);
		IWorld world = executor.getWorld();
		if (world == null || n == null)
			return null;

		for (int i = 0; i < n; ++i)
		{
			String mobName = parameters.get(Args.mobName.value);
			IEntity entity = world.spawnCreature(executor.getLocation(), mobName);
			if (entity instanceof RunsafeLivingEntity)
			{
				RunsafeLivingEntity livingEntity = (RunsafeLivingEntity) entity;

				RunsafeMeta weapon;
				if (mobName != null && mobName.equalsIgnoreCase("skeleton"))
					weapon = Item.Combat.Bow.enchant(Enchant.All).getItem();
				else
					weapon = Item.Combat.Sword.Diamond.enchant(Enchant.All).getItem();

				livingEntity.getEquipment()
					.setBoots(Item.Combat.Boots.Diamond.enchant(Enchant.All).getItem())
					.setChestplate(Item.Combat.Chestplate.Diamond.enchant(Enchant.All).getItem())
					.setHelmet(Item.Combat.Helmet.Diamond.enchant(Enchant.All).getItem())
					.setLeggings(Item.Combat.Leggings.Diamond.enchant(Enchant.All).getItem())
					.setItemInHand(weapon);

				Buff.Combat.Damage.Increase.duration(1200).amplification(5).applyTo(livingEntity);
				Buff.Resistance.Fire.duration(1200).amplification(5).applyTo(livingEntity);
				Buff.Resistance.Damage.duration(1200).amplification(5).applyTo(livingEntity);
			}
		}

		return null;
	}
}
