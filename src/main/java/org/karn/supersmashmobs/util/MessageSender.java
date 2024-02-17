package org.karn.supersmashmobs.util;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.ClearTitleS2CPacket;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.karn.supersmashmobs.game.MainGame;

public class MessageSender {
    public static void sendMsgAll(MinecraftServer server, Text msg){
        server.getPlayerManager().getPlayerList().forEach(p->{
            p.sendMessage(msg);
        });
    }

    public static void sendTitleAll(MinecraftServer server, Text title, @Nullable Text subtitle){
        server.getPlayerManager().getPlayerList().forEach(p->{
            sendTitle(p,title,subtitle);
        });
    }
    public static void sendKillLog(PlayerEntity player, DamageSource source){
        String sourceName = "null";
        if(source.isOf(DamageTypes.ON_FIRE) || source.isOf(DamageTypes.IN_FIRE) || source.isOf(DamageTypes.LAVA) || source.isOf(DamageTypes.HOT_FLOOR)){
            sourceName = "화염";
        } else {
            sourceName = source.getAttacker().getEntityName();
        }
        Text killlog = Text.empty().append(player.getName()).append(" << "+sourceName+" | 남은 목숨: "+(MainGame.joinedPlayer.get(player)-1));
        sendMsgAll(player.getServer(),killlog);
    }

    public static void sendTitle(ServerPlayerEntity player, Text title, @Nullable Text subtitle){
        player.networkHandler.sendPacket(new TitleS2CPacket(title));
        if(subtitle == null)
            player.networkHandler.sendPacket(new SubtitleS2CPacket(Text.empty()));
        else
            player.networkHandler.sendPacket(new SubtitleS2CPacket(subtitle));
    }

    public static void clearTitle(ServerPlayerEntity player){
        player.networkHandler.sendPacket(new ClearTitleS2CPacket(true));
    }
}