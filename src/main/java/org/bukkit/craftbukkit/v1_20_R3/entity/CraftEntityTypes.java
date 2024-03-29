package org.bukkit.craftbukkit.v1_20_R3.entity;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.kettingpowered.ketting.entity.CraftCustomHorse;
import org.kettingpowered.ketting.entity.CraftCustomMinecart;
import org.kettingpowered.ketting.entity.CraftCustomTamable;

public final class CraftEntityTypes {

    public record EntityTypeData<E extends Entity, M extends net.minecraft.world.entity.Entity>(EntityType entityType,
                                                                                                Class<E> entityClass,
                                                                                                BiFunction<CraftServer, M, CraftEntity> convertFunction,
                                                                                                Function<SpawnData, M> spawnFunction) {
    }

    public record SpawnData(WorldGenLevel world, Location location, boolean randomizeData, boolean normalWorld) {
        double x() {
            return location().getX();
        }

        double y() {
            return location().getY();
        }

        double z() {
            return location().getZ();
        }

        float yaw() {
            return location().getYaw();
        }

        float pitch() {
            return location().getPitch();
        }

        Level minecraftWorld() {
            return world().getMinecraftWorld();
        }
    }

    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> POS = (spawnData, entity) -> entity.setPos(spawnData.x(), spawnData.y(), spawnData.z());
    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> ABS_MOVE = (spawnData, entity) -> {
        entity.absMoveTo(spawnData.x(), spawnData.y(), spawnData.z(), spawnData.yaw(), spawnData.pitch());
        entity.setYHeadRot(spawnData.yaw()); // SPIGOT-3587
    };
    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> MOVE = (spawnData, entity) -> entity.moveTo(spawnData.x(), spawnData.y(), spawnData.z(), spawnData.yaw(), spawnData.pitch());
    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> MOVE_EMPTY_ROT = (spawnData, entity) -> entity.moveTo(spawnData.x(), spawnData.y(), spawnData.z(), 0, 0);
    private static final BiConsumer<SpawnData, AbstractHurtingProjectile> DIRECTION = (spawnData, entity) -> {
        Vector direction = spawnData.location().getDirection().multiply(10);
        entity.setDirection(direction.getX(), direction.getY(), direction.getZ());
    };
    private static final Map<Class<?>, EntityTypeData<?, ?>> CLASS_TYPE_DATA = new HashMap<>();
    private static final Map<EntityType, EntityTypeData<?, ?>> ENTITY_TYPE_DATA = new HashMap<>();

    static {
        // Living
        register(new EntityTypeData<>(EntityType.ELDER_GUARDIAN, ElderGuardian.class, CraftElderGuardian::new, createLiving(net.minecraft.world.entity.EntityType.ELDER_GUARDIAN)));
        register(new EntityTypeData<>(EntityType.WITHER_SKELETON, WitherSkeleton.class, CraftWitherSkeleton::new, createLiving(net.minecraft.world.entity.EntityType.WITHER_SKELETON)));
        register(new EntityTypeData<>(EntityType.STRAY, Stray.class, CraftStray::new, createLiving(net.minecraft.world.entity.EntityType.STRAY)));
        register(new EntityTypeData<>(EntityType.HUSK, Husk.class, CraftHusk::new, createLiving(net.minecraft.world.entity.EntityType.HUSK)));
        register(new EntityTypeData<>(EntityType.ZOMBIE_VILLAGER, ZombieVillager.class, CraftVillagerZombie::new, createLiving(net.minecraft.world.entity.EntityType.ZOMBIE_VILLAGER)));
        register(new EntityTypeData<>(EntityType.SKELETON_HORSE, SkeletonHorse.class, CraftSkeletonHorse::new, createLiving(net.minecraft.world.entity.EntityType.SKELETON_HORSE)));
        register(new EntityTypeData<>(EntityType.ZOMBIE_HORSE, ZombieHorse.class, CraftZombieHorse::new, createLiving(net.minecraft.world.entity.EntityType.ZOMBIE_HORSE)));
        register(new EntityTypeData<>(EntityType.ARMOR_STAND, ArmorStand.class, CraftArmorStand::new, createLiving(net.minecraft.world.entity.EntityType.ARMOR_STAND)));
        register(new EntityTypeData<>(EntityType.DONKEY, Donkey.class, CraftDonkey::new, createLiving(net.minecraft.world.entity.EntityType.DONKEY)));
        register(new EntityTypeData<>(EntityType.MULE, Mule.class, CraftMule::new, createLiving(net.minecraft.world.entity.EntityType.MULE)));
        register(new EntityTypeData<>(EntityType.EVOKER, Evoker.class, CraftEvoker::new, createLiving(net.minecraft.world.entity.EntityType.EVOKER)));
        register(new EntityTypeData<>(EntityType.VEX, Vex.class, CraftVex::new, createLiving(net.minecraft.world.entity.EntityType.VEX)));
        register(new EntityTypeData<>(EntityType.VINDICATOR, Vindicator.class, CraftVindicator::new, createLiving(net.minecraft.world.entity.EntityType.VINDICATOR)));
        register(new EntityTypeData<>(EntityType.ILLUSIONER, Illusioner.class, CraftIllusioner::new, createLiving(net.minecraft.world.entity.EntityType.ILLUSIONER)));
        register(new EntityTypeData<>(EntityType.CREEPER, Creeper.class, CraftCreeper::new, createLiving(net.minecraft.world.entity.EntityType.CREEPER)));
        register(new EntityTypeData<>(EntityType.SKELETON, Skeleton.class, CraftSkeleton::new, createLiving(net.minecraft.world.entity.EntityType.SKELETON)));
        register(new EntityTypeData<>(EntityType.SPIDER, Spider.class, CraftSpider::new, createLiving(net.minecraft.world.entity.EntityType.SPIDER)));
        register(new EntityTypeData<>(EntityType.GIANT, Giant.class, CraftGiant::new, createLiving(net.minecraft.world.entity.EntityType.GIANT)));
        register(new EntityTypeData<>(EntityType.ZOMBIE, Zombie.class, CraftZombie::new, createLiving(net.minecraft.world.entity.EntityType.ZOMBIE)));
        register(new EntityTypeData<>(EntityType.SLIME, Slime.class, CraftSlime::new, createLiving(net.minecraft.world.entity.EntityType.SLIME)));
        register(new EntityTypeData<>(EntityType.GHAST, Ghast.class, CraftGhast::new, createLiving(net.minecraft.world.entity.EntityType.GHAST)));
        register(new EntityTypeData<>(EntityType.ZOMBIFIED_PIGLIN, PigZombie.class, CraftPigZombie::new, createLiving(net.minecraft.world.entity.EntityType.ZOMBIFIED_PIGLIN)));
        register(new EntityTypeData<>(EntityType.ENDERMAN, Enderman.class, CraftEnderman::new, createLiving(net.minecraft.world.entity.EntityType.ENDERMAN)));
        register(new EntityTypeData<>(EntityType.CAVE_SPIDER, CaveSpider.class, CraftCaveSpider::new, createLiving(net.minecraft.world.entity.EntityType.CAVE_SPIDER)));
        register(new EntityTypeData<>(EntityType.SILVERFISH, Silverfish.class, CraftSilverfish::new, createLiving(net.minecraft.world.entity.EntityType.SILVERFISH)));
        register(new EntityTypeData<>(EntityType.BLAZE, Blaze.class, CraftBlaze::new, createLiving(net.minecraft.world.entity.EntityType.BLAZE)));
        register(new EntityTypeData<>(EntityType.MAGMA_CUBE, MagmaCube.class, CraftMagmaCube::new, createLiving(net.minecraft.world.entity.EntityType.MAGMA_CUBE)));
        register(new EntityTypeData<>(EntityType.WITHER, Wither.class, CraftWither::new, createLiving(net.minecraft.world.entity.EntityType.WITHER)));
        register(new EntityTypeData<>(EntityType.BAT, Bat.class, CraftBat::new, createLiving(net.minecraft.world.entity.EntityType.BAT)));
        register(new EntityTypeData<>(EntityType.WITCH, Witch.class, CraftWitch::new, createLiving(net.minecraft.world.entity.EntityType.WITCH)));
        register(new EntityTypeData<>(EntityType.ENDERMITE, Endermite.class, CraftEndermite::new, createLiving(net.minecraft.world.entity.EntityType.ENDERMITE)));
        register(new EntityTypeData<>(EntityType.GUARDIAN, Guardian.class, CraftGuardian::new, createLiving(net.minecraft.world.entity.EntityType.GUARDIAN)));
        register(new EntityTypeData<>(EntityType.SHULKER, Shulker.class, CraftShulker::new, createLiving(net.minecraft.world.entity.EntityType.SHULKER)));
        register(new EntityTypeData<>(EntityType.PIG, Pig.class, CraftPig::new, createLiving(net.minecraft.world.entity.EntityType.PIG)));
        register(new EntityTypeData<>(EntityType.SHEEP, Sheep.class, CraftSheep::new, createLiving(net.minecraft.world.entity.EntityType.SHEEP)));
        register(new EntityTypeData<>(EntityType.COW, Cow.class, CraftCow::new, createLiving(net.minecraft.world.entity.EntityType.COW)));
        register(new EntityTypeData<>(EntityType.CHICKEN, Chicken.class, CraftChicken::new, createLiving(net.minecraft.world.entity.EntityType.CHICKEN)));
        register(new EntityTypeData<>(EntityType.SQUID, Squid.class, CraftSquid::new, createLiving(net.minecraft.world.entity.EntityType.SQUID)));
        register(new EntityTypeData<>(EntityType.WOLF, Wolf.class, CraftWolf::new, createLiving(net.minecraft.world.entity.EntityType.WOLF)));
        register(new EntityTypeData<>(EntityType.MUSHROOM_COW, MushroomCow.class, CraftMushroomCow::new, createLiving(net.minecraft.world.entity.EntityType.MOOSHROOM)));
        register(new EntityTypeData<>(EntityType.SNOWMAN, Snowman.class, CraftSnowman::new, createLiving(net.minecraft.world.entity.EntityType.SNOW_GOLEM)));
        register(new EntityTypeData<>(EntityType.OCELOT, Ocelot.class, CraftOcelot::new, createLiving(net.minecraft.world.entity.EntityType.OCELOT)));
        register(new EntityTypeData<>(EntityType.IRON_GOLEM, IronGolem.class, CraftIronGolem::new, createLiving(net.minecraft.world.entity.EntityType.IRON_GOLEM)));
        register(new EntityTypeData<>(EntityType.HORSE, Horse.class, CraftHorse::new, createLiving(net.minecraft.world.entity.EntityType.HORSE)));
        register(new EntityTypeData<>(EntityType.RABBIT, Rabbit.class, CraftRabbit::new, createLiving(net.minecraft.world.entity.EntityType.RABBIT)));
        register(new EntityTypeData<>(EntityType.POLAR_BEAR, PolarBear.class, CraftPolarBear::new, createLiving(net.minecraft.world.entity.EntityType.POLAR_BEAR)));
        register(new EntityTypeData<>(EntityType.LLAMA, Llama.class, CraftLlama::new, createLiving(net.minecraft.world.entity.EntityType.LLAMA)));
        register(new EntityTypeData<>(EntityType.PARROT, Parrot.class, CraftParrot::new, createLiving(net.minecraft.world.entity.EntityType.PARROT)));
        register(new EntityTypeData<>(EntityType.VILLAGER, Villager.class, CraftVillager::new, createLiving(net.minecraft.world.entity.EntityType.VILLAGER)));
        register(new EntityTypeData<>(EntityType.TURTLE, Turtle.class, CraftTurtle::new, createLiving(net.minecraft.world.entity.EntityType.TURTLE)));
        register(new EntityTypeData<>(EntityType.PHANTOM, Phantom.class, CraftPhantom::new, createLiving(net.minecraft.world.entity.EntityType.PHANTOM)));
        register(new EntityTypeData<>(EntityType.COD, Cod.class, CraftCod::new, createLiving(net.minecraft.world.entity.EntityType.COD)));
        register(new EntityTypeData<>(EntityType.SALMON, Salmon.class, CraftSalmon::new, createLiving(net.minecraft.world.entity.EntityType.SALMON)));
        register(new EntityTypeData<>(EntityType.PUFFERFISH, PufferFish.class, CraftPufferFish::new, createLiving(net.minecraft.world.entity.EntityType.PUFFERFISH)));
        register(new EntityTypeData<>(EntityType.TROPICAL_FISH, TropicalFish.class, CraftTropicalFish::new, createLiving(net.minecraft.world.entity.EntityType.TROPICAL_FISH)));
        register(new EntityTypeData<>(EntityType.DROWNED, Drowned.class, CraftDrowned::new, createLiving(net.minecraft.world.entity.EntityType.DROWNED)));
        register(new EntityTypeData<>(EntityType.DOLPHIN, Dolphin.class, CraftDolphin::new, createLiving(net.minecraft.world.entity.EntityType.DOLPHIN)));
        register(new EntityTypeData<>(EntityType.CAT, Cat.class, CraftCat::new, createLiving(net.minecraft.world.entity.EntityType.CAT)));
        register(new EntityTypeData<>(EntityType.PANDA, Panda.class, CraftPanda::new, createLiving(net.minecraft.world.entity.EntityType.PANDA)));
        register(new EntityTypeData<>(EntityType.PILLAGER, Pillager.class, CraftPillager::new, createLiving(net.minecraft.world.entity.EntityType.PILLAGER)));
        register(new EntityTypeData<>(EntityType.RAVAGER, Ravager.class, CraftRavager::new, createLiving(net.minecraft.world.entity.EntityType.RAVAGER)));
        register(new EntityTypeData<>(EntityType.TRADER_LLAMA, TraderLlama.class, CraftTraderLlama::new, createLiving(net.minecraft.world.entity.EntityType.TRADER_LLAMA)));
        register(new EntityTypeData<>(EntityType.WANDERING_TRADER, WanderingTrader.class, CraftWanderingTrader::new, createLiving(net.minecraft.world.entity.EntityType.WANDERING_TRADER)));
        register(new EntityTypeData<>(EntityType.FOX, Fox.class, CraftFox::new, createLiving(net.minecraft.world.entity.EntityType.FOX)));
        register(new EntityTypeData<>(EntityType.BEE, Bee.class, CraftBee::new, createLiving(net.minecraft.world.entity.EntityType.BEE)));
        register(new EntityTypeData<>(EntityType.HOGLIN, Hoglin.class, CraftHoglin::new, createLiving(net.minecraft.world.entity.EntityType.HOGLIN)));
        register(new EntityTypeData<>(EntityType.PIGLIN, Piglin.class, CraftPiglin::new, createLiving(net.minecraft.world.entity.EntityType.PIGLIN)));
        register(new EntityTypeData<>(EntityType.STRIDER, Strider.class, CraftStrider::new, createLiving(net.minecraft.world.entity.EntityType.STRIDER)));
        register(new EntityTypeData<>(EntityType.ZOGLIN, Zoglin.class, CraftZoglin::new, createLiving(net.minecraft.world.entity.EntityType.ZOGLIN)));
        register(new EntityTypeData<>(EntityType.PIGLIN_BRUTE, PiglinBrute.class, CraftPiglinBrute::new, createLiving(net.minecraft.world.entity.EntityType.PIGLIN_BRUTE)));
        register(new EntityTypeData<>(EntityType.AXOLOTL, Axolotl.class, CraftAxolotl::new, createLiving(net.minecraft.world.entity.EntityType.AXOLOTL)));
        register(new EntityTypeData<>(EntityType.GLOW_SQUID, GlowSquid.class, CraftGlowSquid::new, createLiving(net.minecraft.world.entity.EntityType.GLOW_SQUID)));
        register(new EntityTypeData<>(EntityType.GOAT, Goat.class, CraftGoat::new, createLiving(net.minecraft.world.entity.EntityType.GOAT)));
        register(new EntityTypeData<>(EntityType.ALLAY, Allay.class, CraftAllay::new, createLiving(net.minecraft.world.entity.EntityType.ALLAY)));
        register(new EntityTypeData<>(EntityType.FROG, Frog.class, CraftFrog::new, createLiving(net.minecraft.world.entity.EntityType.FROG)));
        register(new EntityTypeData<>(EntityType.TADPOLE, Tadpole.class, CraftTadpole::new, createLiving(net.minecraft.world.entity.EntityType.TADPOLE)));
        register(new EntityTypeData<>(EntityType.WARDEN, Warden.class, CraftWarden::new, createLiving(net.minecraft.world.entity.EntityType.WARDEN)));
        register(new EntityTypeData<>(EntityType.CAMEL, Camel.class, CraftCamel::new, createLiving(net.minecraft.world.entity.EntityType.CAMEL)));
        register(new EntityTypeData<>(EntityType.SNIFFER, Sniffer.class, CraftSniffer::new, createLiving(net.minecraft.world.entity.EntityType.SNIFFER)));
        register(new EntityTypeData<>(EntityType.BREEZE, Breeze.class, CraftBreeze::new, createLiving(net.minecraft.world.entity.EntityType.BREEZE)));

        Function<SpawnData, net.minecraft.world.entity.boss.enderdragon.EnderDragon> dragonFunction = createLiving(net.minecraft.world.entity.EntityType.ENDER_DRAGON);
        register(new EntityTypeData<>(EntityType.ENDER_DRAGON, EnderDragon.class, CraftEnderDragon::new, spawnData -> {
            Preconditions.checkArgument(spawnData.normalWorld(), "Cannot spawn entity %s during world generation", EnderDragon.class.getName());
            return dragonFunction.apply(spawnData);
        }));

        // Fireball
        register(new EntityTypeData<>(EntityType.FIREBALL, LargeFireball.class, CraftLargeFireball::new, createFireball(net.minecraft.world.entity.EntityType.FIREBALL)));
        register(new EntityTypeData<>(EntityType.SMALL_FIREBALL, SmallFireball.class, CraftSmallFireball::new, createFireball(net.minecraft.world.entity.EntityType.SMALL_FIREBALL)));
        register(new EntityTypeData<>(EntityType.WITHER_SKULL, WitherSkull.class, CraftWitherSkull::new, createFireball(net.minecraft.world.entity.EntityType.WITHER_SKULL)));
        register(new EntityTypeData<>(EntityType.DRAGON_FIREBALL, DragonFireball.class, CraftDragonFireball::new, createFireball(net.minecraft.world.entity.EntityType.DRAGON_FIREBALL)));
        register(new EntityTypeData<>(EntityType.WIND_CHARGE, WindCharge.class, CraftWindCharge::new, createFireball(net.minecraft.world.entity.EntityType.WIND_CHARGE)));

        // Hanging
        register(new EntityTypeData<>(EntityType.PAINTING, Painting.class, CraftPainting::new, createHanging(Painting.class, (spawnData, hangingData) -> {
                    if (spawnData.normalWorld && hangingData.randomize()) {
                        return net.minecraft.world.entity.decoration.Painting.create(spawnData.minecraftWorld(), hangingData.position(), hangingData.direction()).orElse(null);
                    } else {
                        net.minecraft.world.entity.decoration.Painting entity = new net.minecraft.world.entity.decoration.Painting(net.minecraft.world.entity.EntityType.PAINTING, spawnData.minecraftWorld());
                        entity.absMoveTo(spawnData.x(), spawnData.y(), spawnData.z(), spawnData.yaw(), spawnData.pitch());
                        entity.setDirection(hangingData.direction());
                        return entity;
                    }
                }
        )));
        register(new EntityTypeData<>(EntityType.ITEM_FRAME, ItemFrame.class, CraftItemFrame::new, createHanging(ItemFrame.class, (spawnData, hangingData) -> new net.minecraft.world.entity.decoration.ItemFrame(spawnData.minecraftWorld(), hangingData.position(), hangingData.direction()))));
        register(new EntityTypeData<>(EntityType.GLOW_ITEM_FRAME, GlowItemFrame.class, CraftGlowItemFrame::new, createHanging(GlowItemFrame.class, (spawnData, hangingData) -> new net.minecraft.world.entity.decoration.GlowItemFrame(spawnData.minecraftWorld(), hangingData.position(), hangingData.direction()))));

        // Move no rotation
        register(new EntityTypeData<>(EntityType.ARROW, Arrow.class, CraftArrow::new, createAndMoveEmptyRot(net.minecraft.world.entity.EntityType.ARROW)));
        register(new EntityTypeData<>(EntityType.ENDER_PEARL, EnderPearl.class, CraftEnderPearl::new, createAndMoveEmptyRot(net.minecraft.world.entity.EntityType.ENDER_PEARL)));
        register(new EntityTypeData<>(EntityType.THROWN_EXP_BOTTLE, ThrownExpBottle.class, CraftThrownExpBottle::new, createAndMoveEmptyRot(net.minecraft.world.entity.EntityType.EXPERIENCE_BOTTLE)));
        register(new EntityTypeData<>(EntityType.SPECTRAL_ARROW, SpectralArrow.class, CraftSpectralArrow::new, createAndMoveEmptyRot(net.minecraft.world.entity.EntityType.SPECTRAL_ARROW)));
        register(new EntityTypeData<>(EntityType.ENDER_CRYSTAL, EnderCrystal.class, CraftEnderCrystal::new, createAndMoveEmptyRot(net.minecraft.world.entity.EntityType.END_CRYSTAL)));
        register(new EntityTypeData<>(EntityType.TRIDENT, Trident.class, CraftTrident::new, createAndMoveEmptyRot(net.minecraft.world.entity.EntityType.TRIDENT)));
        register(new EntityTypeData<>(EntityType.LIGHTNING, LightningStrike.class, CraftLightningStrike::new, createAndMoveEmptyRot(net.minecraft.world.entity.EntityType.LIGHTNING_BOLT)));

        // Move
        register(new EntityTypeData<>(EntityType.SHULKER_BULLET, ShulkerBullet.class, CraftShulkerBullet::new, createAndMove(net.minecraft.world.entity.EntityType.SHULKER_BULLET)));
        register(new EntityTypeData<>(EntityType.BOAT, Boat.class, CraftBoat::new, createAndMove(net.minecraft.world.entity.EntityType.BOAT)));
        register(new EntityTypeData<>(EntityType.LLAMA_SPIT, LlamaSpit.class, CraftLlamaSpit::new, createAndMove(net.minecraft.world.entity.EntityType.LLAMA_SPIT)));
        register(new EntityTypeData<>(EntityType.CHEST_BOAT, ChestBoat.class, CraftChestBoat::new, createAndMove(net.minecraft.world.entity.EntityType.CHEST_BOAT)));

        // Set pos
        register(new EntityTypeData<>(EntityType.MARKER, Marker.class, CraftMarker::new, createAndSetPos(net.minecraft.world.entity.EntityType.MARKER)));
        register(new EntityTypeData<>(EntityType.BLOCK_DISPLAY, BlockDisplay.class, CraftBlockDisplay::new, createAndSetPos(net.minecraft.world.entity.EntityType.BLOCK_DISPLAY)));
        register(new EntityTypeData<>(EntityType.INTERACTION, Interaction.class, CraftInteraction::new, createAndSetPos(net.minecraft.world.entity.EntityType.INTERACTION)));
        register(new EntityTypeData<>(EntityType.ITEM_DISPLAY, ItemDisplay.class, CraftItemDisplay::new, createAndSetPos(net.minecraft.world.entity.EntityType.ITEM_DISPLAY)));
        register(new EntityTypeData<>(EntityType.TEXT_DISPLAY, TextDisplay.class, CraftTextDisplay::new, createAndSetPos(net.minecraft.world.entity.EntityType.TEXT_DISPLAY)));

        // MISC
        register(new EntityTypeData<>(EntityType.DROPPED_ITEM, Item.class, CraftItem::new, spawnData -> {
            // We use stone instead of empty, to give the plugin developer a visual clue, that the spawn method is working,
            // and that the item stack should probably be changed.
            net.minecraft.world.item.ItemStack itemStack = new net.minecraft.world.item.ItemStack(Items.STONE);
            ItemEntity item = new ItemEntity(spawnData.minecraftWorld(), spawnData.x(), spawnData.z(), spawnData.z(), itemStack);
            item.setPickUpDelay(10);

            return item;
        }));
        register(new EntityTypeData<>(EntityType.EXPERIENCE_ORB, ExperienceOrb.class, CraftExperienceOrb::new,
                spawnData -> new net.minecraft.world.entity.ExperienceOrb(spawnData.minecraftWorld(), spawnData.x(), spawnData.z(), spawnData.z(), 0)
        ));
        register(new EntityTypeData<>(EntityType.AREA_EFFECT_CLOUD, AreaEffectCloud.class, CraftAreaEffectCloud::new, spawnData -> new net.minecraft.world.entity.AreaEffectCloud(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.EGG, Egg.class, CraftEgg::new, spawnData -> new net.minecraft.world.entity.projectile.ThrownEgg(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.LEASH_HITCH, LeashHitch.class, CraftLeash::new, spawnData -> new net.minecraft.world.entity.decoration.LeashFenceKnotEntity(spawnData.minecraftWorld(), BlockPos.containing(spawnData.x(), spawnData.y(), spawnData.z())))); // SPIGOT-5732: LeashHitch has no direction and is always centered at a block
        register(new EntityTypeData<>(EntityType.SNOWBALL, Snowball.class, CraftSnowball::new, spawnData -> new net.minecraft.world.entity.projectile.Snowball(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.ENDER_SIGNAL, EnderSignal.class, CraftEnderSignal::new, spawnData -> new net.minecraft.world.entity.projectile.EyeOfEnder(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.SPLASH_POTION, ThrownPotion.class, CraftThrownPotion::new, spawnData -> {
            net.minecraft.world.entity.projectile.ThrownPotion entity = new net.minecraft.world.entity.projectile.ThrownPotion(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z());
            entity.setItem(CraftItemStack.asNMSCopy(new ItemStack(Material.SPLASH_POTION, 1)));
            return entity;
        }));
        register(new EntityTypeData<>(EntityType.PRIMED_TNT, TNTPrimed.class, CraftTNTPrimed::new, spawnData -> new net.minecraft.world.entity.item.PrimedTnt(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), null)));
        register(new EntityTypeData<>(EntityType.FALLING_BLOCK, FallingBlock.class, CraftFallingBlock::new, spawnData -> {
            BlockPos pos = BlockPos.containing(spawnData.x(), spawnData.y(), spawnData.z());
            return net.minecraft.world.entity.item.FallingBlockEntity.fall(spawnData.minecraftWorld(), pos, spawnData.world().getBlockState(pos));
        }));
        register(new EntityTypeData<>(EntityType.FIREWORK, Firework.class, CraftFirework::new, spawnData -> new net.minecraft.world.entity.projectile.FireworkRocketEntity(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), net.minecraft.world.item.ItemStack.EMPTY)));
        register(new EntityTypeData<>(EntityType.EVOKER_FANGS, EvokerFangs.class, CraftEvokerFangs::new, spawnData -> new net.minecraft.world.entity.projectile.EvokerFangs(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), (float) Math.toRadians(spawnData.yaw()), 0, null)));
        register(new EntityTypeData<>(EntityType.MINECART_COMMAND, CommandMinecart.class, CraftMinecartCommand::new, spawnData -> new net.minecraft.world.entity.vehicle.MinecartCommandBlock(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART, RideableMinecart.class, CraftMinecartRideable::new, spawnData -> new net.minecraft.world.entity.vehicle.Minecart(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART_CHEST, StorageMinecart.class, CraftMinecartChest::new, spawnData -> new net.minecraft.world.entity.vehicle.MinecartChest(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART_FURNACE, PoweredMinecart.class, CraftMinecartFurnace::new, spawnData -> new net.minecraft.world.entity.vehicle.MinecartFurnace(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART_TNT, ExplosiveMinecart.class, CraftMinecartTNT::new, spawnData -> new net.minecraft.world.entity.vehicle.MinecartTNT(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART_HOPPER, HopperMinecart.class, CraftMinecartHopper::new, spawnData -> new net.minecraft.world.entity.vehicle.MinecartHopper(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART_MOB_SPAWNER, SpawnerMinecart.class, CraftMinecartMobSpawner::new, spawnData -> new net.minecraft.world.entity.vehicle.MinecartSpawner(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));

        // None spawn able
        register(new EntityTypeData<>(EntityType.FISHING_HOOK, FishHook.class, CraftFishHook::new, null)); // Cannot spawn a fish hook
        register(new EntityTypeData<>(EntityType.PLAYER, Player.class, CraftPlayer::new, null)); // Cannot spawn a player
        //Ketting: Todo: Proper EntityType?
        //register(new EntityTypeData<>(EntityType.UNKNOWN, Minecart.class, CraftCustomMinecart::new, null));
        //register(new EntityTypeData<>(EntityType.UNKNOWN, Tameable.class, CraftCustomTamable::new, null));
        //register(new EntityTypeData<AbstractHorse, net.minecraft.world.entity.animal.horse.AbstractChestedHorse>(EntityType.UNKNOWN, AbstractHorse.class, CraftCustomHorse::new, null));
        //register(new EntityTypeData<AbstractHorse, net.minecraft.world.entity.animal.horse.AbstractHorse>(EntityType.UNKNOWN, AbstractHorse.class, CraftCustomHorse::new, null));
    }

    private static void register(EntityTypeData<?, ?> typeData) {
        EntityTypeData<?, ?> other = CLASS_TYPE_DATA.put(typeData.entityClass(), typeData);
        if (other != null) {
            Bukkit.getLogger().warning(String.format("Found multiple entity type data for class %s, replacing '%s' with new value '%s'", typeData.entityClass().getName(), other, typeData));
        }

        other = ENTITY_TYPE_DATA.put(typeData.entityType(), typeData);
        if (other != null) {
            Bukkit.getLogger().warning(String.format("Found multiple entity type data for entity type %s, replacing '%s' with new value '%s'", typeData.entityType().getKey(), other, typeData));
        }
    }

    private static <R extends net.minecraft.world.entity.Entity> Function<SpawnData, R> fromEntityType(net.minecraft.world.entity.EntityType<R> entityTypes) {
        return spawnData -> entityTypes.create(spawnData.minecraftWorld());
    }

    private static <R extends net.minecraft.world.entity.LivingEntity> Function<SpawnData, R> createLiving(net.minecraft.world.entity.EntityType<R> entityTypes) {
        return combine(fromEntityType(entityTypes), ABS_MOVE);
    }

    private static <R extends AbstractHurtingProjectile> Function<SpawnData, R> createFireball(net.minecraft.world.entity.EntityType<R> entityTypes) {
        return combine(createAndMove(entityTypes), DIRECTION);
    }

    private static <R extends net.minecraft.world.entity.Entity> Function<SpawnData, R> createAndMove(net.minecraft.world.entity.EntityType<R> entityTypes) {
        return combine(fromEntityType(entityTypes), MOVE);
    }

    private static <R extends net.minecraft.world.entity.Entity> Function<SpawnData, R> createAndMoveEmptyRot(net.minecraft.world.entity.EntityType<R> entityTypes) {
        return combine(fromEntityType(entityTypes), MOVE_EMPTY_ROT);
    }

    private static <R extends net.minecraft.world.entity.Entity> Function<SpawnData, R> createAndSetPos(net.minecraft.world.entity.EntityType<R> entityTypes) {
        return combine(fromEntityType(entityTypes), POS);
    }

    private record HangingData(boolean randomize, BlockPos position, Direction direction) {
    }

    private static <E extends Hanging, R extends HangingEntity> Function<SpawnData, R> createHanging(Class<E> clazz, BiFunction<SpawnData, HangingData, R> spawnFunction) {
        return spawnData -> {
            boolean randomizeData = spawnData.randomizeData();
            BlockFace face = BlockFace.SELF;
            BlockFace[] faces = new BlockFace[]{BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH};

            int width = 16; // 1 full block, also painting smallest size.
            int height = 16; // 1 full block, also painting smallest size.

            if (ItemFrame.class.isAssignableFrom(clazz)) {
                width = 12;
                height = 12;
                faces = new BlockFace[]{BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN};
            }

            final BlockPos pos = BlockPos.containing(spawnData.x(), spawnData.y(), spawnData.z());
            for (BlockFace dir : faces) {
                BlockState nmsBlock = spawnData.world().getBlockState(pos.relative(CraftBlock.blockFaceToNotch(dir)));
                if (nmsBlock.isSolid() || DiodeBlock.isDiode(nmsBlock)) {
                    boolean taken = false;
                    AABB bb = (ItemFrame.class.isAssignableFrom(clazz))
                            ? net.minecraft.world.entity.decoration.ItemFrame.calculateBoundingBox(null, pos, CraftBlock.blockFaceToNotch(dir).getOpposite(), width, height)
                            : HangingEntity.calculateBoundingBox(null, pos, CraftBlock.blockFaceToNotch(dir).getOpposite(), width, height);
                    List<net.minecraft.world.entity.Entity> list = spawnData.world().getEntities(null, bb);
                    for (Iterator<net.minecraft.world.entity.Entity> it = list.iterator(); !taken && it.hasNext(); ) {
                        net.minecraft.world.entity.Entity e = it.next();
                        if (e instanceof HangingEntity) {
                            taken = true; // Hanging entities do not like hanging entities which intersect them.
                        }
                    }

                    if (!taken) {
                        face = dir;
                        break;
                    }
                }
            }

            // No valid face found
            if (face == BlockFace.SELF) {
                // SPIGOT-6387: Allow hanging entities to be placed in midair
                face = BlockFace.SOUTH;
                randomizeData = false; // Don't randomize if no valid face is found, prevents null painting
            }

            Direction dir = CraftBlock.blockFaceToNotch(face).getOpposite();
            return spawnFunction.apply(spawnData, new HangingData(randomizeData, pos, dir));
        };
    }

    private static <T, R> Function<T, R> combine(Function<T, R> before, BiConsumer<T, ? super R> after) {
        return (t) -> {
            R r = before.apply(t);
            after.accept(t, r);
            return r;
        };
    }

    public static <E extends Entity, M extends net.minecraft.world.entity.Entity> EntityTypeData<E, M> getEntityTypeData(EntityType entityType) {
        return (EntityTypeData<E, M>) ENTITY_TYPE_DATA.get(entityType);
    }

    public static <E extends Entity, M extends net.minecraft.world.entity.Entity> EntityTypeData<E, M> getEntityTypeData(Class<E> entityClass) {
        return (EntityTypeData<E, M>) CLASS_TYPE_DATA.get(entityClass);
    }

    private CraftEntityTypes() {
    }
}
