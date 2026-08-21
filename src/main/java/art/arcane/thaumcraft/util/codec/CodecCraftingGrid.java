package art.arcane.thaumcraft.util.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record CodecCraftingGrid(int width, int height, List<String> pattern, Map<Character, Ingredient> keys) {

	public boolean verify(RecipeInput container, int offset) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				ItemStack stack = container.getItem(x + y * width + offset);
				Ingredient i = keys.get(pattern.get(y).charAt(x));
				if (i == null || (i.isEmpty() && !stack.isEmpty()) || !i.test(stack)) {
					return false;
				}
			}
		}
		return true;
	}

	public static Codec<CodecCraftingGrid> dimensionedCodec(int width, int height) {
		return RecordCodecBuilder.create(i -> i.group(
				Codec.STRING.listOf().fieldOf("pattern").forGetter(CodecCraftingGrid::pattern),
				Codec.unboundedMap(Codecs.CHAR, Ingredient.CODEC).fieldOf("keys").forGetter(CodecCraftingGrid::keys)
		).apply(i, (p, k) -> new CodecCraftingGrid(width, height, p, k)));
	}

	public static StreamCodec<RegistryFriendlyByteBuf, CodecCraftingGrid> dimensionedStreamCodec(int width, int height) {
		return StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), CodecCraftingGrid::pattern,
				ByteBufCodecs.map(HashMap::new, Codecs.CHAR_STREAM, Ingredient.CONTENTS_STREAM_CODEC), CodecCraftingGrid::keys,
				(pattern, keys) -> new CodecCraftingGrid(width, height, pattern, keys)
		);
	}
}