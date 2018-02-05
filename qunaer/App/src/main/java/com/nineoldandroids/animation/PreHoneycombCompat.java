package com.nineoldandroids.animation;

import android.view.View;
import com.nineoldandroids.util.FloatProperty;
import com.nineoldandroids.util.IntProperty;
import com.nineoldandroids.util.Property;
import com.nineoldandroids.view.animation.AnimatorProxy;

final class PreHoneycombCompat {
    static Property<View, Float> ALPHA = new AnonymousClass1("alpha");
    static Property<View, Float> PIVOT_X = new AnonymousClass2("pivotX");
    static Property<View, Float> PIVOT_Y = new AnonymousClass3("pivotY");
    static Property<View, Float> ROTATION = new AnonymousClass6("rotation");
    static Property<View, Float> ROTATION_X = new AnonymousClass7("rotationX");
    static Property<View, Float> ROTATION_Y = new AnonymousClass8("rotationY");
    static Property<View, Float> SCALE_X = new AnonymousClass9("scaleX");
    static Property<View, Float> SCALE_Y = new AnonymousClass10("scaleY");
    static Property<View, Integer> SCROLL_X = new AnonymousClass11("scrollX");
    static Property<View, Integer> SCROLL_Y = new AnonymousClass12("scrollY");
    static Property<View, Float> TRANSLATION_X = new AnonymousClass4("translationX");
    static Property<View, Float> TRANSLATION_Y = new AnonymousClass5("translationY");
    static Property<View, Float> X = new AnonymousClass13(MapViewConstants.ATTR_X);
    static Property<View, Float> Y = new AnonymousClass14(MapViewConstants.ATTR_Y);

    final class AnonymousClass10 extends FloatProperty<View> {
        AnonymousClass10(String str) {
            super(str);
        }

        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setScaleY(f);
        }

        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getScaleY());
        }
    }

    final class AnonymousClass11 extends IntProperty<View> {
        AnonymousClass11(String str) {
            super(str);
        }

        public void setValue(View view, int i) {
            AnimatorProxy.wrap(view).setScrollX(i);
        }

        public Integer get(View view) {
            return Integer.valueOf(AnimatorProxy.wrap(view).getScrollX());
        }
    }

    final class AnonymousClass12 extends IntProperty<View> {
        AnonymousClass12(String str) {
            super(str);
        }

        public void setValue(View view, int i) {
            AnimatorProxy.wrap(view).setScrollY(i);
        }

        public Integer get(View view) {
            return Integer.valueOf(AnimatorProxy.wrap(view).getScrollY());
        }
    }

    final class AnonymousClass13 extends FloatProperty<View> {
        AnonymousClass13(String str) {
            super(str);
        }

        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setX(f);
        }

        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getX());
        }
    }

    final class AnonymousClass14 extends FloatProperty<View> {
        AnonymousClass14(String str) {
            super(str);
        }

        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setY(f);
        }

        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getY());
        }
    }

    final class AnonymousClass1 extends FloatProperty<View> {
        AnonymousClass1(String str) {
            super(str);
        }

        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setAlpha(f);
        }

        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getAlpha());
        }
    }

    final class AnonymousClass2 extends FloatProperty<View> {
        AnonymousClass2(String str) {
            super(str);
        }

        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setPivotX(f);
        }

        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getPivotX());
        }
    }

    final class AnonymousClass3 extends FloatProperty<View> {
        AnonymousClass3(String str) {
            super(str);
        }

        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setPivotY(f);
        }

        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getPivotY());
        }
    }

    final class AnonymousClass4 extends FloatProperty<View> {
        AnonymousClass4(String str) {
            super(str);
        }

        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setTranslationX(f);
        }

        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getTranslationX());
        }
    }

    final class AnonymousClass5 extends FloatProperty<View> {
        AnonymousClass5(String str) {
            super(str);
        }

        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setTranslationY(f);
        }

        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getTranslationY());
        }
    }

    final class AnonymousClass6 extends FloatProperty<View> {
        AnonymousClass6(String str) {
            super(str);
        }

        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setRotation(f);
        }

        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getRotation());
        }
    }

    final class AnonymousClass7 extends FloatProperty<View> {
        AnonymousClass7(String str) {
            super(str);
        }

        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setRotationX(f);
        }

        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getRotationX());
        }
    }

    final class AnonymousClass8 extends FloatProperty<View> {
        AnonymousClass8(String str) {
            super(str);
        }

        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setRotationY(f);
        }

        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getRotationY());
        }
    }

    final class AnonymousClass9 extends FloatProperty<View> {
        AnonymousClass9(String str) {
            super(str);
        }

        public void setValue(View view, float f) {
            AnimatorProxy.wrap(view).setScaleX(f);
        }

        public Float get(View view) {
            return Float.valueOf(AnimatorProxy.wrap(view).getScaleX());
        }
    }

    private PreHoneycombCompat() {
    }
}
