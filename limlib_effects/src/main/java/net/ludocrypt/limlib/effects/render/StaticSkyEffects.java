package net.ludocrypt.limlib.effects.render;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.client.render.SkyProperties.SkyType;
import net.minecraft.util.math.Vec3d;

/**
 * A Sky effects controller
 * <p>
 * This is a simplification of the base {@link SkyEffects} class, where each
 * setting is a static, non-changing value
 */
public class StaticSkyEffects extends SkyEffects {

	public static final Codec<StaticSkyEffects> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Codec.FLOAT.optionalFieldOf("cloud_height").stable().forGetter((effects) -> {
			return effects.cloudHeight;
		}), Codec.BOOL.fieldOf("alternate_sky_color").stable().forGetter((effects) -> {
			return effects.alternateSkyColor;
		}), Codec.STRING.fieldOf("sky_type").stable().forGetter((effects) -> {
			return effects.skyType;
		}), Codec.BOOL.fieldOf("brighten_lighting").stable().forGetter((effects) -> {
			return effects.brightenLighting;
		}), Codec.BOOL.fieldOf("darkened").stable().forGetter((effects) -> {
			return effects.darkened;
		}), Codec.BOOL.fieldOf("thick_fog").stable().forGetter((effects) -> {
			return effects.thickFog;
		})).apply(instance, instance.stable(StaticSkyEffects::new));
	});

	private final Optional<Float> cloudHeight;
	private final boolean alternateSkyColor;
	private final String skyType;
	private final boolean brightenLighting;
	private final boolean darkened;
	private final boolean thickFog;

	public StaticSkyEffects(Optional<Float> cloudHeight, boolean alternateSkyColor, String skyType, boolean brightenLighting, boolean darkened, boolean thickFog) {
		this.cloudHeight = cloudHeight;
		this.alternateSkyColor = alternateSkyColor;
		this.skyType = skyType;
		this.brightenLighting = brightenLighting;
		this.darkened = darkened;
		this.thickFog = thickFog;
	}

	public Codec<? extends SkyEffects> getCodec() {
		return CODEC;
	}

	public float getCloudHeight() {
		return cloudHeight.orElse(Float.NaN);
	}

	public boolean hasAlternateSkyColor() {
		return alternateSkyColor;
	}

	public String getSkyType() {
		return skyType;
	}

	public boolean shouldBrightenLighting() {
		return brightenLighting;
	}

	public boolean isDarkened() {
		return darkened;
	}

	public boolean hasThickFog() {
		return thickFog;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public SkyProperties toClientSkyProperties() {
		return new SkyProperties(this.getCloudHeight(), this.alternateSkyColor, SkyType.valueOf(this.getSkyType()), this.shouldBrightenLighting(), this.isDarkened()) {

			@Override
			public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
				return null;
			}

			@Override
			public boolean useThickFog(int camX, int camY) {
				return StaticSkyEffects.this.hasThickFog();
			}

		};
	}

}
