package com.skocken.efficientadapter.lib.util;

import com.skocken.efficientadapter.lib.viewholder.AbsViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AdapterUtil {

    private AdapterUtil() {
    }

    public static View generateView(ViewGroup parent, int layoutResId) {
        if (parent == null) {
            return null;
        }
        Context context = parent.getContext();
        if(context == null) {
            return null;
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(layoutResId, parent, false);
    }

    @NonNull
    public static <T extends AbsViewHolder> AbsViewHolder generateViewHolder(View v,
            Class<T> viewHolderClass) {
        Constructor<?> constructorWithView = getConstructorWithView(viewHolderClass);
        try {
            Object viewHolder = constructorWithView.newInstance(v);
            return (AbsViewHolder) viewHolder;
        } catch (InvocationTargetException e) {
            throw new RuntimeException(
                    "Impossible to instantiate "
                            + viewHolderClass.getSimpleName(), e);
        } catch (InstantiationException e) {
            throw new RuntimeException(
                    "Impossible to instantiate "
                            + viewHolderClass.getSimpleName(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    "Impossible to instantiate "
                            + viewHolderClass.getSimpleName(), e);
        }
    }

    /**
     * Get the constructor with a view for this class
     */
    private static Constructor<?> getConstructorWithView(
            Class<? extends AbsViewHolder> viewHolderClass) {
        Constructor<?>[] constructors = viewHolderClass.getDeclaredConstructors();
        if (constructors != null) {
            for (Constructor<?> constructor : constructors) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                if (parameterTypes != null
                        && parameterTypes.length == 1
                        && parameterTypes[0].isAssignableFrom(View.class)) {
                    return constructor;
                }
            }
        }
        throw new RuntimeException(
                "Impossible to found a constructor with a view for "
                        + viewHolderClass.getSimpleName());
    }
}
