package org.wso2.developerstudio.rcp.plugin.handler;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.splash.EclipseSplashHandler;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class SplashHandler extends EclipseSplashHandler {
	 
	
	    private static final String PLUGIN_ID = "org.wso2.developerstudio.rcp.plugin";
		private static final String BETA_PNG = "icons/version/420_4.png";
	    private static final int BORDER =165;
	    private Image image;
	    

	    public SplashHandler() {
	        super();
	    }

	    @Override
	    public void init(Shell splash) {
	        super.init(splash);
	        ImageDescriptor descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, BETA_PNG);
	        if (descriptor != null)
	            image = descriptor.createImage();
	        if (image !=null) {
	            final int xposition = splash.getSize().x - image.getImageData().width - 60;
	            final int yposition = BORDER;
	            getContent().addPaintListener (new PaintListener () {
	                public void paintControl (PaintEvent e) {
	                    e.gc.drawImage (image, xposition, yposition);
	                }
	            });
	        }
	    }

	    @Override
	    public void dispose() {
	        super.dispose();
	        if (image != null)
	            image.dispose();
	    }

}
