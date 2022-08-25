package net.ludocrypt.limlib.registry.mixin;

import org.quiltmc.loader.api.QuiltLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.ludocrypt.limlib.registry.registration.PreRegistration;
import net.minecraft.util.registry.BuiltinRegistries;

@Mixin(BuiltinRegistries.class)
public class BuiltinRegistriesMixin {

	@Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Maps;newLinkedHashMap()Ljava/util/LinkedHashMap;", shift = Shift.AFTER))
	private static void limlib$clinit(CallbackInfo ci) {
		QuiltLoader.getEntrypoints(PreRegistration.ENTRYPOINT_KEY, PreRegistration.class).forEach(PreRegistration::register);
	}

}
