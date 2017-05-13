package no.runsafe.toybox.handlers;

import no.runsafe.framework.api.ILocation;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.IWorld;
import no.runsafe.framework.api.block.IBlock;
import no.runsafe.framework.api.event.block.IBlockBreak;
import no.runsafe.framework.api.event.block.IBlockRedstone;
import no.runsafe.framework.api.event.plugin.IPluginDisabled;
import no.runsafe.framework.api.event.plugin.IPluginEnabled;
import no.runsafe.framework.api.event.world.IWorldLoad;
import no.runsafe.framework.api.event.world.IWorldUnload;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.Item;
import no.runsafe.framework.minecraft.event.block.RunsafeBlockRedstoneEvent;
import no.runsafe.toybox.repositories.LockedObjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LockedObjectHandler implements IPluginEnabled, IPluginDisabled, IBlockBreak, IBlockRedstone, IWorldLoad, IWorldUnload
{
	public LockedObjectHandler(LockedObjectRepository repository, IConsole output, IServer server)
	{
		this.repository = repository;
		this.output = output;
		this.server = server;
	}

	public boolean isLockedBlock(IBlock block)
	{
		ILocation blockLocation = block.getLocation();
		String worldName = blockLocation.getWorld().getName();

		if (this.lockedObjects.containsKey(worldName))
		{
			List<ILocation> locations = this.lockedObjects.get(worldName);
			for (ILocation location : locations)
				if (location.distance(blockLocation) < 1)
					return true;
		}
		return false;
	}

	public boolean canLockBlock(IBlock block)
	{
		for (Item item : LockedObjectHandler.lockableItems)
			if (block.is(item))
				return true;

		return false;
	}

	public void lockBlock(IBlock block)
	{
		if (!this.isLockedBlock(block))
		{
			ILocation location = block.getLocation();
			String worldName = location.getWorld().getName();

			this.repository.storeLockedObject(location);
			if (!this.lockedObjects.containsKey(worldName))
				this.lockedObjects.put(worldName, new ArrayList<ILocation>());

			this.lockedObjects.get(worldName).add(location);
		}
	}

	public void unlockBlock(IBlock block)
	{
		if (isLockedBlock(block))
		{
			ILocation location = block.getLocation();
			String worldName = location.getWorld().getName();

			if (lockedObjects.containsKey(worldName))
			{
				List<ILocation> locations = new ArrayList<ILocation>(lockedObjects.get(worldName).size());
				for (ILocation checkLocation : lockedObjects.get(worldName))
					if (checkLocation.distance(location) >= 1)
						locations.add(checkLocation);
				lockedObjects.put(worldName, locations);
			}
			repository.removeLockedObject(location);
		}
	}

	@Override
	public boolean OnBlockBreak(IPlayer player, IBlock block)
	{
		if (block != null)
		{
			if (this.isLockedBlock(block))
			{
				if (player != null)
					player.sendColouredMessage("&cThe block is impenetrable to your attempts.");

				return false;
			}
		}
		return true;
	}

	@Override
	public void OnPluginEnabled()
	{
		for (IWorld world : server.getWorlds())
			OnWorldLoad(world);
	}

	@Override
	public void OnBlockRedstoneEvent(RunsafeBlockRedstoneEvent event)
	{
		IBlock block = event.getBlock();
		if (block != null && isLockedBlock(block))
			event.setNewCurrent(5);
	}

	@Override
	public void OnPluginDisabled()
	{
		this.lockedObjects.clear(); // Dump any objects we have in memory.
	}

	@Override
	public void OnWorldLoad(IWorld world)
	{
		String worldName = world.getName();
		if (!lockedObjects.containsKey(worldName))
			lockedObjects.put(worldName, new ArrayList<ILocation>());
		List<ILocation> locations = repository.getLockedObjects(worldName);
		if (locations.isEmpty())
			return;
		output.logInformation("Locking %d objects in world %s.", locations.size(), worldName);
		for (ILocation location : locations)
		{
			if (location == null)
				continue;

			IBlock block = location.getBlock();
			if (block == null || !this.canLockBlock(block))
			{
				repository.removeLockedObject(location);
				output.logError("Invalid locked object, removing: %s", location.toString());
			}
			else
				lockedObjects.get(worldName).add(location);
		}
	}

	@Override
	public void OnWorldUnload(IWorld world)
	{
		if (lockedObjects.containsKey(world.getName()))
			lockedObjects.remove(world.getName());
	}

	private Map<String, List<ILocation>> lockedObjects = new ConcurrentHashMap<String, List<ILocation>>();
	private LockedObjectRepository repository;
	private IConsole output;
	private final IServer server;
	private static List<Item> lockableItems = new ArrayList<Item>();

	static
	{
		lockableItems.add(Item.Brewing.BrewingStand);
		lockableItems.add(Item.Redstone.Lever);
		lockableItems.add(Item.Redstone.Diode);
		lockableItems.add(Item.Redstone.Comparator);
		lockableItems.add(Item.Redstone.Button.Stone);
		lockableItems.add(Item.Redstone.Button.Wood);
		lockableItems.add(Item.Redstone.Device.Dispenser);
		lockableItems.add(Item.Redstone.Device.NoteBlock);
		lockableItems.add(Item.Redstone.Device.Hopper);
		lockableItems.add(Item.Redstone.Device.Dropper);
		lockableItems.add(Item.Decoration.Chest);
		lockableItems.add(Item.Decoration.EnchantmentTable);
		lockableItems.add(Item.Decoration.EnderChest);
		lockableItems.add(Item.Decoration.Anvil.Any);
		lockableItems.add(Item.Redstone.Lamp.Off);
		lockableItems.add(Item.Redstone.Lamp.On);
	}
}
