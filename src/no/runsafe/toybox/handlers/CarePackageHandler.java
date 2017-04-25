package no.runsafe.toybox.handlers;

import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.IWorld;
import no.runsafe.framework.api.block.IBlock;
import no.runsafe.framework.api.block.IChest;
import no.runsafe.framework.api.entity.IEntity;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.Item;
import no.runsafe.framework.minecraft.entity.RunsafeFallingBlock;
import no.runsafe.framework.minecraft.inventory.RunsafeInventory;
import no.runsafe.framework.minecraft.item.meta.RunsafeMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CarePackageHandler
{
	public CarePackageHandler(IServer server)
	{
		this.server = server;
		this.awaitingDrops = new ConcurrentHashMap<UUID, RunsafeInventory>();
		this.fallingDrops = new HashMap<Integer, RunsafeInventory>();
	}

	public void CreateCarePackage(IPlayer player)
	{
		RunsafeInventory newInventory = server.createInventory(null, 27, "Care Package"); // Create
		player.openInventory(newInventory); // Show player the inventory.
		this.awaitingDrops.put(player.getUniqueId(), newInventory); // Store the inventory pointer.
	}

	public boolean PlayerHasOpenCarePackage(IPlayer player)
	{
		return this.awaitingDrops.containsKey(player.getUniqueId()); // Does the player have an awaiting drop?
	}

	private RunsafeInventory GetAwaitingInventory(IPlayer player)
	{
		return this.awaitingDrops.get(player.getUniqueId()); // Get an awaiting inventory linked to a player.
	}

	private void RemoveAwaitingInventory(IPlayer player)
	{
		this.awaitingDrops.remove(player.getUniqueId()); // Remove awaiting drop linked to player.
	}

	private RunsafeInventory GetFallingInventory(Integer entityID)
	{
		return this.fallingDrops.get(entityID);
	}

	private void RemoveFallingInventory(Integer entityID)
	{
		this.fallingDrops.remove(entityID);
	}

	public void DropPackage(IPlayer player)
	{
		IWorld world = player.getWorld();
		if (world == null)
			return;
		IEntity block = world.spawnFallingBlock(player.getLocation(), Item.Decoration.Chest);
		((RunsafeFallingBlock) block).setDropItem(false);
		this.fallingDrops.put(block.getEntityId(), this.GetAwaitingInventory(player));
		this.RemoveAwaitingInventory(player);
	}

	public boolean CheckDrop(Integer entityID)
	{
		return this.fallingDrops.containsKey(entityID);
	}

	public void ProcessDrop(Integer entityID, IBlock block)
	{
		List<RunsafeMeta> items = this.GetFallingInventory(entityID).getContents();
		this.RemoveFallingInventory(entityID);

		if (block instanceof IChest)
		{
			IChest chest = (IChest) block;
			RunsafeInventory chestInventory = chest.getInventory();

			for (RunsafeMeta item : items)
				chestInventory.addItems(item);
		}
	}

	private final ConcurrentHashMap<UUID, RunsafeInventory> awaitingDrops;
	private final HashMap<Integer, RunsafeInventory> fallingDrops;
	private final IServer server;
}
