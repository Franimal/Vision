import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;


public class VisionProcessing {

	/**
	 * Focuses on a point (x,y) of the currently loaded image.  (x,y) should be percentages 0.0 -> 1.0 of the total width/height.
	 * 
	 * @param image The BufferedImage to focus on.
	 * @param xPercent The percentage across the image (left -> right)
	 * @param yPercent The percentage down the image (top -> bottom)
	 * @return A new, processed BufferedImage.  The returned image is a deep copy of the original - that is, there is no reference to the original.
	 */
	public static BufferedImage focusOnPoint(BufferedImage image, float xPercent, float yPercent){
		
		image = deepCopy(image);
		
		FastRGB rgb = new FastRGB(image);
		
		for(int x = 0; x < image.getWidth(); x++){
			for(int y = 0; y < image.getHeight(); y++){
				
				int xDist = (int)Math.abs((xPercent*image.getWidth() - x));
				int yDist = (int)Math.abs((yPercent*image.getHeight() - y));
				
				double dist = Math.sqrt(xDist*xDist+yDist*yDist);
				int size = (int)(Math.sqrt(dist));
				
				if(size < 4){
					size = 4;
				}
				
				int rectWidth = size;
				int rectHeight = size;
				
				int rectX = (int)(x-rectWidth/2f);
				int rectY = (int)(y-rectHeight/2f);
				
				//Now blur the rectangle we've found together as one color..
				
				float totalR = 0;
				float totalG = 0;
				float totalB = 0;
				float meanR = 0;
				float meanG = 0;
				float meanB = 0;
				float count = 0;
				
				for(int rx = rectX; rx < rectX+rectWidth; rx++){
					
					if(rx < 0 || rx >= image.getWidth()){
						continue;
					}
					
					for(int ry = rectY; ry < rectY+rectHeight; ry++){
						
						if(ry < 0 || ry >= image.getHeight()){
							continue;
						}
						
						Color col = new Color(rgb.getRGB(rx,  ry), true);
						totalR += col.getRed();
						totalG += col.getGreen();
						totalB += col.getGreen();
						
						count++;
					}
				}
				
				
				meanR = totalR/count;
				meanG = totalG/count;
				meanB = totalB/count;
			
				if(meanR > 0 && meanR < 256 && meanG > 0 && meanG < 256 && meanB > 0 && meanB < 256){
					//System.out.println(meanR + " " + meanG + " " + meanB);
					image.setRGB(x, y, new Color((int)meanR, (int)meanG, (int)meanB).getRGB());
				}
				
			}
		}
		
		return image;
	}
	
	/**
	 * Creates a deep copy of the given BufferedImage, containing no references to the original.
	 * @param bi
	 * @return
	 */
	public static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		}
	
	
}
