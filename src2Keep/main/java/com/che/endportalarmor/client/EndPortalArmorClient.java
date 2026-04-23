package com.che.endportalarmor.client;

import com.che.endportalarmor.EndPortalArmorMod;
import com.che.endportalarmor.client.model.OpenFrameHelmetModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndPortalArmorMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EndPortalArmorClient {
    public static final ModelLayerLocation OPEN_FRAME_HELMET_LAYER = new ModelLayerLocation(
            new ResourceLocation(EndPortalArmorMod.MOD_ID, "open_frame_helmet"), "main");

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(OPEN_FRAME_HELMET_LAYER, OpenFrameHelmetModel::createBodyLayer);
    }
}
