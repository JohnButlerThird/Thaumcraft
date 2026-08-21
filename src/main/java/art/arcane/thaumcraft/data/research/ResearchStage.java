package art.arcane.thaumcraft.data.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Builder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Builder
public record ResearchStage(
        ResearchRequirements requirements,
        List<Identifier> recipeUnlocks,
        int warpPenalty) {

    public static final ResearchStage EMPTY =  new ResearchStage(ResearchRequirements.EMPTY, Collections.emptyList(), 0);

    public static Builder builder() {
        return new Builder(EMPTY);
    }

    public static final Codec<ResearchStage> CODEC = RecordCodecBuilder.create(i -> i.group(
            ResearchRequirements.CODEC.optionalFieldOf("requirements", EMPTY.requirements).forGetter(ResearchStage::requirements),
            Identifier.CODEC.listOf().optionalFieldOf("recipes", EMPTY.recipeUnlocks).forGetter(ResearchStage::recipeUnlocks),
            Codec.INT.optionalFieldOf("warp", EMPTY.warpPenalty).forGetter(ResearchStage::warpPenalty)
    ).apply(i, ResearchStage::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ResearchStage> STREAM_CODEC = StreamCodec.composite(
            ResearchRequirements.STREAM_CODEC, ResearchStage::requirements,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()), ResearchStage::recipeUnlocks,
            ByteBufCodecs.INT, ResearchStage::warpPenalty,
            ResearchStage::new);

    public static final class Builder {

        private ResearchRequirements requirements;
        private List<Identifier> recipeUnlocks;
        private int warpPenalty;

        private Builder(ResearchStage defaultValue) {
            this.requirements = defaultValue.requirements;
            this.recipeUnlocks = defaultValue.recipeUnlocks;
            this.warpPenalty = defaultValue.warpPenalty;
        }

        public Builder setRequirements(ResearchRequirements requirements) {
            this.requirements = requirements;
            return this;
        }

        public Builder setRecipeUnlocks(Identifier... recipes) {
            this.recipeUnlocks = Arrays.asList(recipes);
            return this;
        }

        public Builder setWarpPenalty(int warp) {
            this.warpPenalty = warp;
            return this;
        }

        public ResearchStage build() {
            return new ResearchStage(requirements, recipeUnlocks, warpPenalty);
        }
    }
}
