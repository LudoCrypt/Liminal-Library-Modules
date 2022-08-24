package net.ludocrypt.limlib.world.chunk;

import java.util.HashMap;
import java.util.Optional;

import net.ludocrypt.limlib.world.mixin.BlockEntityAccessor;
import net.ludocrypt.limlib.world.nbt.NbtPlacerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.HolderSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.structure.StructureSet;

public abstract class AbstractNbtChunkGenerator extends LiminalChunkGenerator {

	public final HashMap<String, NbtPlacerUtil> loadedStructures = new HashMap<String, NbtPlacerUtil>(30);
	public final Identifier nbtId;

	public AbstractNbtChunkGenerator(Registry<StructureSet> registry, Optional<HolderSet<StructureSet>> optional, BiomeSource biomeSource, Identifier nbtId) {
		super(registry, optional, biomeSource);
		this.nbtId = nbtId;
	}

	public abstract void storeStructures(ServerWorld world);

	protected void store(String id, ServerWorld world) {
		loadedStructures.put(id, NbtPlacerUtil.load(world.getServer().getResourceManager(), new Identifier(this.nbtId.getNamespace(), "nbt/" + this.nbtId.getPath() + "/" + id + ".nbt")).get());
	}

	protected void store(String id, ServerWorld world, int from, int to) {
		for (int i = from; i <= to; i++) {
			store(id + "_" + i, world);
		}
	}

	public void generateNbt(ChunkRegion region, BlockPos at, String id) {
		generateNbt(region, at, id, BlockRotation.NONE);
	}

	public void generateNbt(ChunkRegion region, BlockPos at, String id, BlockRotation rotation) {
		loadedStructures.get(id).rotate(rotation).generateNbt(region, at, (pos, state, nbt) -> this.modifyStructure(region, pos, state, nbt)).spawnEntities(region, at, rotation);
	}

	protected void modifyStructure(ChunkRegion region, BlockPos pos, BlockState state, NbtCompound nbt) {
		if (!state.isAir()) {
			if (state.isOf(Blocks.BARREL)) {
				region.setBlockState(pos, state, Block.NOTIFY_ALL, 1);
				if (region.getBlockEntity(pos)instanceof LootableContainerBlockEntity lootTable) {
					lootTable.setLootTable(this.getBarrelLootTable(), region.getSeed() + MathHelper.hashCode(pos));
				}
			} else if (state.isOf(Blocks.BARRIER)) {
				region.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL, 1);
			} else {
				region.setBlockState(pos, state, Block.NOTIFY_ALL, 1);
			}
			BlockEntity blockEntity = region.getBlockEntity(pos);
			if (blockEntity != null) {
				if (state.isOf(blockEntity.getCachedState().getBlock())) {
					((BlockEntityAccessor) blockEntity).callWriteNbt(nbt);
				}
			}
		}
	}

	protected Identifier getBarrelLootTable() {
		return LootTables.SIMPLE_DUNGEON_CHEST;
	}

}
