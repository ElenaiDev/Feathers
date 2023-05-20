package com.elenai.feathers.attributes;

import com.elenai.feathers.Feathers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = Feathers.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FeathersAttributes {

    public static final HashMap<RegistryObject<Attribute>, UUID> UUIDS = new HashMap<>();
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Feathers.MODID);

    public static final RegistryObject<Attribute> MAX_FEATHERS = registerAttribute("feathers.max_feathers", (id) -> new RangedAttribute(id, 20.0D, 0.0D, 1024.0D).setSyncable(true), "1ce4960d-c50e-44bf-ad23-7bcd77f4c1dc");
    public static final RegistryObject<Attribute> FEATHER_REGEN = registerAttribute("feathers.feather_regen", (id) -> new RangedAttribute(id, 1.0D, 0.0D, 1024.0D).setSyncable(true), "d74ded8f-c5b6-4222-80e2-dbea7ccf8d02");

    public static RegistryObject<Attribute> registerAttribute(String name, Function<String, Attribute> attribute, String uuid) {
        return registerAttribute(name, attribute, UUID.fromString(uuid));
    }

    public static RegistryObject<Attribute> registerAttribute(String name, Function<String, Attribute> attribute, UUID uuid) {
        RegistryObject<Attribute> registryObject = ATTRIBUTES.register(name, () -> attribute.apply(name));
        UUIDS.put(registryObject, uuid);
        return registryObject;
    }

    public static void register(IEventBus modEventBus) {
        ATTRIBUTES.register(modEventBus);
    }

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        for (EntityType<? extends LivingEntity> e : event.getTypes())
            if (e == EntityType.PLAYER) for (RegistryObject<Attribute> v : ATTRIBUTES.getEntries())
                event.add(e, v.get());
    }

}
