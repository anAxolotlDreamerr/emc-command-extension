package io.github.anaxolotldreamerr.client.util;

public class ColorUtil {
    public static int[] hexToArgb(long color) {
        int alpha = (int) ((color >> 24) & 0xFF);
        int red   = (int) ((color >> 16) & 0xFF);
        int green = (int) ((color >> 8) & 0xFF);
        int blue  = (int) (color & 0xFF);

        return new int[]{
                alpha,
                red,
                green,
                blue
        };
    }
}
