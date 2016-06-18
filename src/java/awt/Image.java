/*
 * Copyright (c) 1999, 2007, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package java.awt;

import hc.android.ActivityManager;
import hc.android.AndroidClassUtil;
import hc.android.ImageUtil;
import hc.android.ScreenAdapter;

import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * The abstract class <code>Image</code> is the superclass of all
 * classes that represent graphical images. The image must be
 * obtained in a platform-specific manner.
 *
 * @author      Sami Shaio
 * @author      Arthur van Hoff
 * @since       JDK1.0
 */
public abstract class Image {
	protected Bitmap bitmap;
	public float initZoom = 1.0F;
	protected Drawable bitmapDrawableAdapter;
	
	public Bitmap getBitmapAdAPI(){
		return bitmap;
	}

	public final Drawable getAdapterBitmapDrawableAdAPI(Component component){
		if(bitmapDrawableAdapter == null){
			bitmapDrawableAdapter = new BitmapDrawable(ActivityManager.getActivity().getResources(), getAdapterBitmap(component)); 
		}
		return bitmapDrawableAdapter;
	}
	
	private final Bitmap getAdapterBitmap(Component component){
		final ScreenAdapter screenAdapterAdAPI = component.getScreenAdapterAdAPI();
		Bitmap src = getBitmapAdAPI();

		if(screenAdapterAdAPI.type == ScreenAdapter.TYPE_MLET){
			return src;
		}
		
		final int oldW = src.getWidth();
		final int oldH = src.getHeight();

		int scaleWSize = screenAdapterAdAPI.imageSizeToScreenFloat(oldW, initZoom);
		int scaleHSize = screenAdapterAdAPI.imageSizeToScreenFloat(oldH, initZoom);
		
//		//最小限定。因为树节点的图标太小
//		if(scaleWSize < 20){
//			scaleHSize = scaleHSize * 20 / scaleWSize;
//			scaleWSize = 20;
//		}
		
		Bitmap out;
		if(oldW == scaleWSize && oldH == scaleHSize){
			out = src;
		}else{
			out = ImageUtil.zoomBitmap(src, scaleWSize, scaleHSize);
		}
		return out;
	}
	
	protected float accelerationPriority = .5f;

	public abstract int getWidth(ImageObserver observer);

	public abstract int getHeight(ImageObserver observer);

	public abstract ImageProducer getSource();

	public abstract Graphics getGraphics();

	public abstract Object getProperty(String name, ImageObserver observer);

	public static final Object UndefinedProperty = new Object();

	public Image getScaledInstance(int width, int height, int hints) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public static final int SCALE_DEFAULT = 1;
	public static final int SCALE_FAST = 2;
	public static final int SCALE_SMOOTH = 4;
	public static final int SCALE_REPLICATE = 8;
	public static final int SCALE_AREA_AVERAGING = 16;

	public void flush() {
	}

	public ImageCapabilities getCapabilities(GraphicsConfiguration gc) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public void setAccelerationPriority(float priority) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public float getAccelerationPriority() {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

}
