package ru.ushell.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

public class BlurUtils {
    public static Bitmap blur(Context context, Bitmap image, float blurRadius) {
        int width = Math.round(image.getWidth() * blurRadius);
        int height = Math.round(image.getHeight() * blurRadius);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        script.setRadius(blurRadius);
        script.setInput(tmpIn);
        script.forEach(tmpOut);

        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    public static Bitmap getViewScreenshot(View view) {
        Bitmap screenshot = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(screenshot);
        view.draw(canvas);
        return screenshot;
    }
}