package com.rdo.octo.motionlayouttest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class YoloImageFilterView extends AppCompatImageView {

    float[] m = new float[20];
    ColorMatrix mColorMatrix = new ColorMatrix();
    ColorMatrix mTmpColorMatrix = new ColorMatrix();
    private float mSaturation = 1.0F;
    private float mContrast = 1.0F;
    private float mWarmth = 1.0F;
    private float mCrossfade = 0.0F;
    private AttributeSet attributeSet;
    Drawable[] mLayers;
    LayerDrawable mLayer;


    public YoloImageFilterView(Context context) {
        super(context);
        this.init(context, (AttributeSet) null);
    }

    public YoloImageFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public YoloImageFilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.attributeSet = attrs;
    }

    public void setAltDrawable(Drawable drawable) {
        if (drawable != null) {
            this.mLayers = new Drawable[2];
            this.mLayers[0] = this.getDrawable();
            this.mLayers[1] = drawable;
            this.mLayer = new LayerDrawable(this.mLayers);
            this.mLayer.getDrawable(1).setAlpha((int) (255.0F * this.mCrossfade));
            super.setImageDrawable(this.mLayer);
        }
    }

    public void setSaturation(float saturation) {
        this.mSaturation = saturation;
        this.updateMatrix();
    }

    public float getSaturation() {
        return this.mSaturation;
    }

    public void setContrast(float contrast) {
        this.mContrast = contrast;
        this.updateMatrix();
    }

    public float getContrast() {
        return this.mContrast;
    }

    public void setWarmth(float warmth) {
        this.mWarmth = warmth;
        this.updateMatrix();
    }

    public float getWarmth() {
        return this.mWarmth;
    }

    public void setCrossfade(float crossfade) {
        this.mCrossfade = crossfade;
        if (this.mLayers != null) {
            this.mLayer.getDrawable(1).setAlpha((int) (255.0F * this.mCrossfade));
            super.setImageDrawable(this.mLayer);
        }

    }

    public float getCrossfade() {
        return this.mCrossfade;
    }

    private void saturation(float saturationStrength) {
        float Rf = 0.2999F;
        float Gf = 0.587F;
        float Bf = 0.114F;
        float MS = 1.0F - saturationStrength;
        float Rt = Rf * MS;
        float Gt = Gf * MS;
        float Bt = Bf * MS;
        this.m[0] = Rt + saturationStrength;
        this.m[1] = Gt;
        this.m[2] = Bt;
        this.m[3] = 0.0F;
        this.m[4] = 0.0F;
        this.m[5] = Rt;
        this.m[6] = Gt + saturationStrength;
        this.m[7] = Bt;
        this.m[8] = 0.0F;
        this.m[9] = 0.0F;
        this.m[10] = Rt;
        this.m[11] = Gt;
        this.m[12] = Bt + saturationStrength;
        this.m[13] = 0.0F;
        this.m[14] = 0.0F;
        this.m[15] = 0.0F;
        this.m[16] = 0.0F;
        this.m[17] = 0.0F;
        this.m[18] = 1.0F;
        this.m[19] = 0.0F;
    }

    private void warmth(float warmth) {
        float baseTemprature = 5000.0F;
        if (warmth <= 0.0F) {
            warmth = 0.01F;
        }

        float kelvin = baseTemprature / warmth;
        float color_r = kelvin / 100.0F;
        float color_g;
        float color_b;
        float colorR;
        if (color_r > 66.0F) {
            colorR = color_r - 60.0F;
            color_g = 329.69873F * (float) Math.pow((double) colorR, -0.13320475816726685D);
            color_b = 288.12216F * (float) Math.pow((double) colorR, 0.07551484555006027D);
        } else {
            color_b = 99.4708F * (float) Math.log((double) color_r) - 161.11957F;
            color_g = 255.0F;
        }

        float centiKelvin;
        if (color_r < 66.0F) {
            if (color_r > 19.0F) {
                centiKelvin = 138.51773F * (float) Math.log((double) (color_r - 10.0F)) - 305.0448F;
            } else {
                centiKelvin = 0.0F;
            }
        } else {
            centiKelvin = 255.0F;
        }

        float tmpColor_r = Math.min(255.0F, Math.max(color_g, 0.0F));
        float tmpColor_g = Math.min(255.0F, Math.max(color_b, 0.0F));
        float tmpColor_b = Math.min(255.0F, Math.max(centiKelvin, 0.0F));
        color_r = tmpColor_r;
        color_g = tmpColor_g;
        color_b = tmpColor_b;
        centiKelvin = baseTemprature / 100.0F;
        float colorG;
        if (centiKelvin > 66.0F) {
            float tmp = centiKelvin - 60.0F;
            colorR = 329.69873F * (float) Math.pow((double) tmp, -0.13320475816726685D);
            colorG = 288.12216F * (float) Math.pow((double) tmp, 0.07551484555006027D);
        } else {
            colorG = 99.4708F * (float) Math.log((double) centiKelvin) - 161.11957F;
            colorR = 255.0F;
        }

        float colorB;
        if (centiKelvin < 66.0F) {
            if (centiKelvin > 19.0F) {
                colorB = 138.51773F * (float) Math.log((double) (centiKelvin - 10.0F)) - 305.0448F;
            } else {
                colorB = 0.0F;
            }
        } else {
            colorB = 255.0F;
        }

        tmpColor_r = Math.min(255.0F, Math.max(colorR, 0.0F));
        tmpColor_g = Math.min(255.0F, Math.max(colorG, 0.0F));
        tmpColor_b = Math.min(255.0F, Math.max(colorB, 0.0F));
        color_r /= tmpColor_r;
        color_g /= tmpColor_g;
        color_b /= tmpColor_b;
        this.m[0] = color_r;
        this.m[1] = 0.0F;
        this.m[2] = 0.0F;
        this.m[3] = 0.0F;
        this.m[4] = 0.0F;
        this.m[5] = 0.0F;
        this.m[6] = color_g;
        this.m[7] = 0.0F;
        this.m[8] = 0.0F;
        this.m[9] = 0.0F;
        this.m[10] = 0.0F;
        this.m[11] = 0.0F;
        this.m[12] = color_b;
        this.m[13] = 0.0F;
        this.m[14] = 0.0F;
        this.m[15] = 0.0F;
        this.m[16] = 0.0F;
        this.m[17] = 0.0F;
        this.m[18] = 1.0F;
        this.m[19] = 0.0F;
    }

    private void updateMatrix() {
        this.mColorMatrix.reset();
        boolean filter = false;
        if (this.mSaturation != 1.0F) {
            this.saturation(this.mSaturation);
            this.mColorMatrix.set(this.m);
            filter = true;
        }

        if (this.mContrast != 1.0F) {
            this.mTmpColorMatrix.setScale(this.mContrast, this.mContrast, this.mContrast, 1.0F);
            this.mColorMatrix.postConcat(this.mTmpColorMatrix);
            filter = true;
        }

        if (this.mWarmth != 1.0F) {
            this.warmth(this.mWarmth);
            this.mTmpColorMatrix.set(this.m);
            this.mColorMatrix.postConcat(this.mTmpColorMatrix);
            filter = true;
        }

        if (filter) {
            this.setColorFilter(new ColorMatrixColorFilter(this.mColorMatrix));
        } else {
            this.clearColorFilter();
        }

    }
}