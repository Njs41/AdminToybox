package no.runsafe.toybox.command;

import no.runsafe.framework.api.ILocation;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.entity.ILivingEntity;
import no.runsafe.framework.api.event.entity.IEntityDamageByEntityEvent;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.Item;
import no.runsafe.framework.minecraft.WorldBlockEffect;
import no.runsafe.framework.minecraft.WorldBlockEffectType;
import no.runsafe.framework.minecraft.entity.RunsafeEntity;
import no.runsafe.framework.minecraft.entity.RunsafeProjectile;
import no.runsafe.framework.minecraft.event.entity.RunsafeEntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

public class Thorns extends PlayerCommand implements IEntityDamageByEntityEvent
{
	public Thorns()
	{
		super("thorns",
			"Protect the player with insta-gib thorns",
			"runsafe.toybox.thorns"
		);
	}

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		String playerName = executor.getName();
		if (protectedPlayers.contains(playerName))
		{
			protectedPlayers.remove(playerName);
			return "&aThorns disabled.";
		}
		else
		{
			protectedPlayers.add(playerName);
			return "&aThorns enabled.";
		}
	}

	@Override
	public void OnEntityDamageByEntity(RunsafeEntityDamageByEntityEvent event)
	{
		RunsafeEntity entity = event.getEntity();
		RunsafeEntity attackingEntity = event.getDamageActor();

		if (entity != null && attackingEntity != null && entity instanceof IPlayer)
		{
			ILivingEntity killTarget = null;

			if (attackingEntity instanceof ILivingEntity)
				killTarget = (ILivingEntity) attackingEntity;
			else if (attackingEntity instanceof RunsafeProjectile)
				killTarget = ((RunsafeProjectile) attackingEntity).getShootingEntity();

			// Check we have an entity to kill and that we're protecting a player.
			if (killTarget != null && protectedPlayers.contains(((IPlayer) entity).getName()))
			{
				// Play an effect at the targets location.
				ILocation killTargetLocation = killTarget.getLocation();
				if (killTargetLocation != null) killTargetLocation.playEffect(effect, 0.2F, 100, 50D);

				// Play an effect at the killers location.
				ILocation entityLocation = entity.getLocation();
				if (entityLocation != null) entityLocation.playEffect(effect, 0.2F, 100, 50D);

				killTarget.damage(50D); // This is more than enough to kill the entity.
				event.cancel(); // Cancel the event so we don't take damage.
			}
		}
	}

	private final WorldBlockEffect effect = new WorldBlockEffect(WorldBlockEffectType.BLOCK_DUST, Item.BuildingBlock.Wood.Jungle);
	private final List<String> protectedPlayers = new ArrayList<String>();
}
