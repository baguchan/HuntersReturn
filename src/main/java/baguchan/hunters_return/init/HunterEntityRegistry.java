package baguchan.hunters_return.init;

import baguchan.hunters_return.HuntersReturn;
import baguchan.hunters_return.entity.Hunter;
import baguchan.hunters_return.entity.projectile.BoomerangEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = baguchan.hunters_return.HuntersReturn.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HunterEntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES_REGISTRY = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, HuntersReturn.MODID);


    public static final Supplier<EntityType<Hunter>> HUNTERILLAGER = ENTITIES_REGISTRY.register("hunter", () -> EntityType.Builder.of(Hunter::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(prefix("hunter")));
    public static final Supplier<EntityType<BoomerangEntity>> BOOMERANG = ENTITIES_REGISTRY.register("boomerang", () -> EntityType.Builder.<BoomerangEntity>of(BoomerangEntity::new, MobCategory.MISC).sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(20).build(prefix("boomerang")));


	@SubscribeEvent
	public static void registerEntityAttribute(EntityAttributeCreationEvent event) {
		event.put(HUNTERILLAGER.get(), Hunter.createAttributes().build());
	}

	private static String prefix(String path) {
		return "hunters_return." + path;
	}
}
