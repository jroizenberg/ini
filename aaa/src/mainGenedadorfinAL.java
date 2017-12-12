import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.sourceforge.jbarcodebean.JBarcodeBean;
import net.sourceforge.jbarcodebean.model.Code128;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;


public class mainGenedadorfinAL {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XWPFDocument doc = new XWPFDocument();
		
        XWPFParagraph title = doc.createParagraph();    
        XWPFRun run = title.createRun();
        title.setAlignment(ParagraphAlignment.CENTER);
        
		try{
	
			for(int j=0 ; j<1; j++){
				String val= "49";
//				String id=String.valueOf(j);
				int totalaAgregarCeros=6 - val.length();
				String nuevoVal=null;
				for(int i=0; i< totalaAgregarCeros; i++){
					if(nuevoVal== null)
						nuevoVal="0";
					else
						nuevoVal=nuevoVal+ "0";
//					i= i+1;
				}
				String nuevoVal2= nuevoVal + val;
				
				String nombreFinal="ROIZENBERG, JONATHAN "+j ;
//				if(nombreFinal.length() > 20){
//					nombreFinal.substring(0, 20);
//				} 
				mainGenedadorfinAL mm= new mainGenedadorfinAL();
				BufferedImage  buff= mm.obtenerCodigoBarras(nuevoVal2,nombreFinal.toUpperCase() );
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(buff, "png", os);
				InputStream is = new ByteArrayInputStream(os.toByteArray());
				run.addPicture(is, XWPFDocument.PICTURE_TYPE_PNG, null, Units.toEMU(120), Units.toEMU(50)); // --> hay que modificar este ultimo valor si es muy grande
	            is.close();
			}
		    FileOutputStream fos = new FileOutputStream("C:/Users/Jonathan R/Desktop/test1.doc");
            doc.write(fos);
            fos.close();            
		}catch(Exception e){
			
		}
	}

	public BufferedImage obtenerCodigoBarras(String id, String nomYApe){
	
	    JBarcodeBean barcode = new JBarcodeBean();
	    // nuestro tipo de codigo de barra
	    barcode.setCodeType(new Code128());
	    // nuestro valor a codificar y algunas configuraciones mas
	    barcode.setCode(id);
	    barcode.setCheckDigit(true);
	    barcode.setShowText(false);
	    barcode.setEnabled(true);
	    BufferedImage bufferedImage = barcode.draw(new BufferedImage(180, 100, BufferedImage.TYPE_INT_RGB));
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.setColor(Color.BLACK);
		Font currentFont = g2.getFont();
		Font newFont = currentFont.deriveFont(currentFont.getSize() * 1.1F);
		g2.setFont(newFont);
		if(nomYApe != null)
			g2.drawString(nomYApe,10, 10);
		g2.dispose();
	
		return bufferedImage;
	    // guardar en disco como png
	//    File file = new File("C:/Users/Jonathan R/Desktop/codebar.png");
	//    ImageIO.write(bufferedImage, "png", file);
	}

}
