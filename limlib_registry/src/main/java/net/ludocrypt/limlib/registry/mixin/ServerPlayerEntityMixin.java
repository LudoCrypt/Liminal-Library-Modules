package net.ludocrypt.limlib.registry.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import net.ludocrypt.limlib.registry.util.LimlibUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

	@Shadow
	public ServerPlayNetworkHandler networkHandler;

	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, PlayerPublicKey publicKey) {
		super(world, pos, yaw, gameProfile, publicKey);
	}

	@Inject(method = "moveToWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V", ordinal = 5, shift = Shift.AFTER))
	public void limlib$moveToWorld(ServerWorld to, CallbackInfoReturnable<Entity> ci) {
		if (LimlibUtil.travelingSound != null) {
			this.playSound(LimlibUtil.travelingSound, SoundCategory.AMBIENT, LimlibUtil.travelingVolume, LimlibUtil.travelingPitch);
		}
	}

	@ModifyArg(method = "moveToWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/WorldEventS2CPacket;<init>(ILnet/minecraft/util/math/BlockPos;IZ)V", ordinal = 0), index = 0)
	private int limlib$moveToWorld(int in) {
		return 29848748;
	}

}
