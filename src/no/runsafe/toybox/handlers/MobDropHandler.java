package no.runsafe.toybox.handlers;

import no.runsafe.framework.api.ILocation;
import no.runsafe.framework.api.entity.IEntity;
import no.runsafe.framework.api.minecraft.RunsafeEntityType;
import no.runsafe.framework.minecraft.Item;
import no.runsafe.toybox.MobDropData;

import java.util.HashMap;

public class MobDropHandler
{
	public MobDropHandler()
	{
		this.fallingMobDrops = new HashMap<Integer, MobDropData>();
	}

	public void createMobDropper(ILocation location, RunsafeEntityType entityName, Integer amount)
	{
		IEntity block = location.getWorld().spawnFallingBlock(location, Item.BuildingBlock.Obsidian);
		MobDropData data = new MobDropData(entityName, amount);
		this.fallingMobDrops.put(block.getEntityId(), data);
	}

	public boolean entityIsMobDropper(Integer entityID)
	{
		return this.fallingMobDrops.containsKey(entityID);
	}

	public void processMobDropper(Integer entityID, ILocation location)
	{
		MobDropData data = this.fallingMobDrops.get(entityID);
		int current = 0;

		while (current < data.getMobAmount())
		{
			location.getWorld().spawnCreature(location, data.getEntityType().getName());
			current++;
		}

		this.fallingMobDrops.remove(entityID);
	}

	private final HashMap<Integer, MobDropData> fallingMobDrops;
}
