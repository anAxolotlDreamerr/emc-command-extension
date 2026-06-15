package io.github.anaxolotldreamerr.client.util;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import io.github.anaxolotldreamerr.client.EmcCommandExtensionClient;
import net.fabricmc.fabric.api.client.rendering.v1.FabricRenderState;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.mixin.client.rendering.RenderStateMixin;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3fc;

import java.util.OptionalDouble;

public class Render {
    public static void drawLine(WorldRenderContext context, Vec3 start, Vec3 end, LocalPlayer player) {
        /*PoseStack matrices = context.matrices();

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cam = camera.position();

        matrices.pushPose();
        matrices.translate(-cam.x, -cam.y, -cam.z);

        RenderType renderType = RenderType.create(
                "line_through_wall",
                RenderSetup.builder(RenderPipelines.DEBUG_QUADS)
                        .setOutputTarget(OutputTarget.OUTLINE_TARGET)
                        .sortOnUpload()
                        .createRenderSetup()
        );

        MultiBufferSource.BufferSource bufferSource =
                Minecraft.getInstance().renderBuffers().bufferSource();

        VertexConsumer vc = bufferSource.getBuffer(renderType);

        PoseStack.Pose pose = matrices.last();

        // ===============================
        // ✔ ESP 粗线核心（屏幕空间稳定版）
        // ===============================

        Vec3 dir = end.subtract(start).normalize();

        Camera camObj = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 forward =new Vec3(camObj.forwardVector());

        // 屏幕右方向
        Vec3 right = forward.cross(new Vec3(0, 1, 0));

        if (right.lengthSqr() < 1e-6) {
            right = new Vec3(1, 0, 0);
        }

        right = right.normalize();

        double width = 1; // ← 线粗细（自己调）
        Vec3 side = right.scale(width);

        Vec3 p1 = start.add(side);
        Vec3 p2 = start.subtract(side);
        Vec3 p3 = end.subtract(side);
        Vec3 p4 = end.add(side);

        // ===============================
        // ✔ 画两个三角形（quad）
        // ===============================

        vc.addVertex(pose, (float) p1.x, (float) p1.y, (float) p1.z)
                .setColor(255, 0, 0, 255);

        vc.addVertex(pose, (float) p2.x, (float) p2.y, (float) p2.z)
                .setColor(255, 0, 0, 255);

        vc.addVertex(pose, (float) p3.x, (float) p3.y, (float) p3.z)
                .setColor(255, 0, 0, 255);


        vc.addVertex(pose, (float) p1.x, (float) p1.y, (float) p1.z)
                .setColor(255, 0, 0, 255);

        vc.addVertex(pose, (float) p3.x, (float) p3.y, (float) p3.z)
                .setColor(255, 0, 0, 255);

        vc.addVertex(pose, (float) p4.x, (float) p4.y, (float) p4.z)
                .setColor(255, 0, 0, 255);

        bufferSource.endBatch(renderType);

        matrices.popPose();*/

         PoseStack matrices = context.matrices();

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cam = camera.position();
        Vec3 camPos = camera.position();


        Vec3 normal = camPos.subtract(start).normalize();

        matrices.pushPose();
        matrices.translate(-cam.x, -cam.y, -cam.z);
        RenderType renderType = RenderType.create(
                "line_through_wall",
                RenderSetup.builder(RenderPipelines.LINES)
                        .setOutputTarget(OutputTarget.OUTLINE_TARGET)
                        .sortOnUpload()
                        .createRenderSetup()
        );
        MultiBufferSource.BufferSource bufferSource =
                Minecraft.getInstance().renderBuffers().bufferSource();

        VertexConsumer vc = bufferSource.getBuffer(renderType);

        PoseStack.Pose pose = matrices.last();

        vc.addVertex(pose, (float) start.x, (float) start.y, (float) start.z)
                .setColor(255, 0, 0, 255)
                .setNormal(
                        (float) normal.x,
                        (float) normal.y,
                        (float) normal.z
                )
                .setLineWidth(2.0f);

        vc.addVertex(pose, (float) end.x, (float) end.y, (float) end.z)
                .setColor(255, 0, 0, 255)
                .setNormal(
                        (float) normal.x,
                        (float) normal.y,
                        (float) normal.z
                )
                .setLineWidth(2.0f);

        bufferSource.endBatch(renderType);

        matrices.popPose();
    }
}
