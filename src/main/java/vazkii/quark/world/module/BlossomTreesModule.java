package vazkii.quark.world.module;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Functions;

import net.minecraft.block.ComposterBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import vazkii.quark.base.handler.VariantHandler;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.config.Config;
import vazkii.quark.base.world.WorldGenHandler;
import vazkii.quark.base.world.WorldGenWeights;
import vazkii.quark.world.block.BlossomLeavesBlock;
import vazkii.quark.world.block.BlossomSaplingBlock;
import vazkii.quark.world.block.BlossomSaplingBlock.BlossomTree;
import vazkii.quark.world.config.BlossomTreeConfig;
import vazkii.quark.world.gen.BlossomTreeGenerator;

@LoadModule(category = ModuleCategory.WORLD)
public class BlossomTreesModule extends QuarkModule {

	@Config BlossomTreeConfig blue = new BlossomTreeConfig(200, Biome.Category.ICY);
	@Config BlossomTreeConfig lavender = new BlossomTreeConfig(100, Biome.Category.SWAMP); 
	@Config BlossomTreeConfig orange = new BlossomTreeConfig(100, Biome.Category.SAVANNA); 
	@Config BlossomTreeConfig pink = new BlossomTreeConfig(100, Biome.Category.EXTREME_HILLS); 
	@Config BlossomTreeConfig yellow = new BlossomTreeConfig(200, Biome.Category.PLAINS); 
	
	public static Map<BlossomTree, BlossomTreeConfig> trees = new HashMap<>();
	
	@Override
	public void construct() {
		add("blue", MaterialColor.LIGHT_BLUE, blue);
		add("lavender", MaterialColor.PINK, lavender);
		add("orange", MaterialColor.ORANGE_TERRACOTTA, orange);
		add("pink", MaterialColor.PINK, pink);
		add("yellow", MaterialColor.YELLOW, yellow);
	}
	
	@Override
	public void setup() {
		trees.forEach((tree, config) -> {
			ComposterBlock.CHANCES.put(tree.leaf.getBlock().asItem(), 0.3F);
			ComposterBlock.CHANCES.put(tree.sapling.asItem(), 0.3F);
			
			WorldGenHandler.addGenerator(this, new BlossomTreeGenerator(config, tree), Decoration.TOP_LAYER_MODIFICATION, WorldGenWeights.BLOSSOM_TREES);
		});
	}
	
	private void add(String colorName, MaterialColor color, BlossomTreeConfig config) {
		BlossomLeavesBlock leaves = new BlossomLeavesBlock(colorName, this, color);
		BlossomTree tree = new BlossomTree(leaves);
		BlossomSaplingBlock sapling = new BlossomSaplingBlock(colorName, this, tree, leaves);
		VariantHandler.addFlowerPot(sapling, sapling.getRegistryName().getPath(), Functions.identity());
		
		trees.put(tree, config);
	}
	
}
