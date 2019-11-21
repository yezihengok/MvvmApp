package com.example.commlib.weight.banner;

import androidx.viewpager.widget.ViewPager.PageTransformer;

import com.example.commlib.weight.banner.transformer.AccordionTransformer;
import com.example.commlib.weight.banner.transformer.BackgroundToForegroundTransformer;
import com.example.commlib.weight.banner.transformer.CubeInTransformer;
import com.example.commlib.weight.banner.transformer.CubeOutTransformer;
import com.example.commlib.weight.banner.transformer.CubePageTransformer;
import com.example.commlib.weight.banner.transformer.DefaultTransformer;
import com.example.commlib.weight.banner.transformer.DepthPageTransformer;
import com.example.commlib.weight.banner.transformer.FlipHorizontalTransformer;
import com.example.commlib.weight.banner.transformer.FlipVerticalTransformer;
import com.example.commlib.weight.banner.transformer.ForegroundToBackgroundTransformer;
import com.example.commlib.weight.banner.transformer.RotateDownTransformer;
import com.example.commlib.weight.banner.transformer.RotateUpTransformer;
import com.example.commlib.weight.banner.transformer.ScaleInOutTransformer;
import com.example.commlib.weight.banner.transformer.StackTransformer;
import com.example.commlib.weight.banner.transformer.TabletTransformer;
import com.example.commlib.weight.banner.transformer.ZoomInTransformer;
import com.example.commlib.weight.banner.transformer.ZoomOutSlideTransformer;
import com.example.commlib.weight.banner.transformer.ZoomOutTranformer;

public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
    public static Class<? extends PageTransformer> CubePage = CubePageTransformer.class;
}
