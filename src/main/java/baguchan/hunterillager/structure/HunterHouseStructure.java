package baguchan.hunterillager.structure;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

import java.util.Random;

public class HunterHouseStructure extends Structure<NoFeatureConfig> {
	public HunterHouseStructure(Codec<NoFeatureConfig> p_i51440_1_) {
		super(p_i51440_1_);
	}

	@Override
	public String getFeatureName() {
		return "hunterillager:hunterhouse";
	}

	protected boolean isFeatureChunk(ChunkGenerator p_230363_1_, BiomeProvider p_230363_2_, long p_230363_3_, SharedSeedRandom p_230363_5_, int p_230363_6_, int p_230363_7_, Biome p_230363_8_, ChunkPos p_230363_9_, VillageConfig p_230363_10_) {
		int i = p_230363_6_ >> 4;
		int j = p_230363_7_ >> 4;
		p_230363_5_.setSeed((long) (i ^ j << 4) ^ p_230363_3_);
		p_230363_5_.nextInt();
		if (p_230363_5_.nextInt(5) != 0) {
			return false;
		} else {
			return !this.isNearVillage(p_230363_1_, p_230363_3_, p_230363_5_, p_230363_6_, p_230363_7_);
		}
	}

	private boolean isNearVillage(ChunkGenerator p_242782_1_, long p_242782_2_, SharedSeedRandom p_242782_4_, int p_242782_5_, int p_242782_6_) {
		StructureSeparationSettings structureseparationsettings = p_242782_1_.getSettings().getConfig(Structure.VILLAGE);
		if (structureseparationsettings == null) {
			return false;
		} else {
			for (int i = p_242782_5_ - 10; i <= p_242782_5_ + 10; ++i) {
				for (int j = p_242782_6_ - 10; j <= p_242782_6_ + 10; ++j) {
					ChunkPos chunkpos = Structure.VILLAGE.getPotentialFeatureChunk(structureseparationsettings, p_242782_2_, p_242782_4_, i, j);
					if (i == chunkpos.x && j == chunkpos.z) {
						return true;
					}
				}
			}

			return false;
		}
	}

	public IStartFactory getStartFactory() {
		return Start::new;
	}

	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	public static class Start extends StructureStart<NoFeatureConfig> {
		public Start(Structure<NoFeatureConfig> p_i225806_1_, int p_i225806_2_, int p_i225806_3_, MutableBoundingBox p_i225806_4_, int p_i225806_5_, long p_i225806_6_) {
			super(p_i225806_1_, p_i225806_2_, p_i225806_3_, p_i225806_4_, p_i225806_5_, p_i225806_6_);
		}

		@Override
		public void generatePieces(DynamicRegistries p_230364_1_, ChunkGenerator p_230364_2_, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biome, NoFeatureConfig p_230364_7_) {
			BlockPos blockpos = new BlockPos(chunkX * 16, 90, chunkZ * 16);
			Rotation rotation = Rotation.values()[this.random.nextInt((Rotation.values()).length)];
			HunterHousePieces.addStructure(templateManagerIn, blockpos, rotation, this.pieces, this.random, biome);
			this.calculateBoundingBox();
		}

		public void placeInChunk(ISeedReader p_230366_1_, StructureManager p_230366_2_, ChunkGenerator p_230366_3_, Random p_230366_4_, MutableBoundingBox p_230366_5_, ChunkPos p_230366_6_) {
			super.placeInChunk(p_230366_1_, p_230366_2_, p_230366_3_, p_230366_4_, p_230366_5_, p_230366_6_);
			int i = this.boundingBox.y0;

			for (int j = p_230366_5_.x0; j <= p_230366_5_.x1; ++j) {
				for (int k = p_230366_5_.z0; k <= p_230366_5_.z1; ++k) {
					BlockPos blockpos = new BlockPos(j, i, k);
					if (!p_230366_1_.isEmptyBlock(blockpos) && this.boundingBox.isInside(blockpos)) {
						boolean flag = false;

						for (StructurePiece structurepiece : this.pieces) {
							if (structurepiece.getBoundingBox().isInside(blockpos)) {
								flag = true;
								break;
							}
						}

						if (flag) {
							for (int l = i - 1; l > 1; --l) {
								BlockPos blockpos1 = new BlockPos(j, l, k);
								if (!p_230366_1_.isEmptyBlock(blockpos1) && !p_230366_1_.getBlockState(blockpos1).getMaterial().isLiquid()) {
									break;
								}

								p_230366_1_.setBlock(blockpos1, Blocks.DIRT.defaultBlockState(), 2);
							}
						}
					}
				}
			}

		}
	}
}