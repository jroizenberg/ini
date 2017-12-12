import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class testCodigoBarras {
//
//    public static void main(String[] args) throws IOException, BarcodeException {
//
//        JBarcodeBean barcode = new JBarcodeBean();
//        // nuestro tipo de codigo de barra
//        barcode.setCodeType(new Interleaved25());
//        // nuestro valor a codificar y algunas configuraciones mas
//        barcode.setCode("000000000000001");
//        barcode.setCheckDigit(true);
//        barcode.setShowText(false);
//        barcode.setEnabled(true);
//        BufferedImage bufferedImage = barcode.draw(new BufferedImage(180, 100, BufferedImage.TYPE_INT_RGB));
//    	Graphics2D g2 = bufferedImage.createGraphics();
//    	g2.setColor(Color.BLACK);
//    	
//    	g2.setFont(new Font("Times New Roman", Font.BOLD, 11)); 
//    	
//    	g2.drawString("ROIZENBERG JONATHAN",14, 20);
//    	g2.dispose();
//
//        // guardar en disco como png
//        File file = new File("C:/Users/Jonathan R/Desktop/codebar.png");
//        ImageIO.write(bufferedImage, "png", file);
//    }
//
////	Font currentFont = g2.getFont();
////	Font newFont = currentFont.deriveFont(currentFont.getSize() * 1.4F);
////	g2.setFont(newFont);
//	
////	g2.setFont(new Font("TimesRoman", Font.PLAIN, 16)); 
////	g2.drawString("Marcelo Jose",5, 30);
//    
//    /*
//     * 
////        barcode.setName("Jonathan Roizenberg");
//        BufferedImage bufferedImage = barcode.draw(new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB));
//    	Graphics2D g2 = bufferedImage.createGraphics();
////    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
////    	g2.setColor(Color.TRANSLUCENT);
////    	g2.fillRect(0, 0, 80, 80);
//    	g2.setColor(Color.black);
//    	g2.drawString("Roizenberg Jonathan",15, 15);
//    	g2.drawString("Marcelo Jose",15, 30);
//
//    	g2.dispose();
//     */
//    public BufferedImage ConvertirTxtPng(String txt) {
//    	BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//    	Graphics2D g2 = bufferedImage.createGraphics();
//    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
////    	g2.setColor(Color.TRANSLUCENT);
//    	g2.fillOval(0, 0,150, 50);
//    	g2.setColor(Color.black);
//    	g2.drawString(txt, 20, 40);
//    	g2.dispose();
//    	return bufferedImage;
//    	}

}
