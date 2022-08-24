package net.ludocrypt.limlib.effects;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import net.ludocrypt.limlib.effects.render.EmptySkyEffects;
import net.ludocrypt.limlib.effects.render.SkyEffects;
import net.ludocrypt.limlib.effects.render.StaticSkyEffects;
import net.ludocrypt.limlib.effects.sound.distortion.DistortionEffect;
import net.ludocrypt.limlib.effects.sound.distortion.StaticDistortionEffect;
import net.ludocrypt.limlib.effects.sound.reverb.ReverbEffect;
import net.ludocrypt.limlib.effects.sound.reverb.StaticReverbEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class LimlibEffects implements ModInitializer {

	@Override
	public void onInitialize(ModContainer mod) {
		Registry.register(ReverbEffect.REVERB_EFFECT_CODEC, new Identifier("limlib", "static"), StaticReverbEffect.CODEC);
		Registry.register(DistortionEffect.DISTORTION_EFFECT_CODEC, new Identifier("limlib", "static"), StaticDistortionEffect.CODEC);

		Registry.register(SkyEffects.SKY_EFFECTS_CODEC, new Identifier("limlib", "static"), StaticSkyEffects.CODEC);
		Registry.register(SkyEffects.SKY_EFFECTS_CODEC, new Identifier("limlib", "empty"), EmptySkyEffects.CODEC);
	}

}
