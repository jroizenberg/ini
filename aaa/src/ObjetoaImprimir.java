import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

class ObjetoaImprimir implements Printable
{
   public int print (Graphics g, PageFormat f, int pageIndex)
   {
      //Creamos un objeto 2D para dibujar en el
      Graphics2D g2 = (Graphics2D) g;
      //Este c�digo imprime 2 p�ginas una con un cuadrado o marco
      //y una segunda con un circulo en la esquina superior izquierda

      //Creamos el rect�ngulo
      //getImagebleX() coge la parte de la hoja donde podemos 
      //imprimir quitando los bordes. Si no hiciesemos 
      //esto as� y tuviesemos bordes definidos en la impresi�n 
      //lo que dibujasemos fuera de los bordes no lo 
      //imprimir�a aunque cupiese en la hoja f�sicamente.
      Rectangle2D rect = new Rectangle2D.Double(f.getImageableX(),
                                                f.getImageableY(),
                                                f.getImageableWidth(),
                                                f.getImageableHeight());

      //Creamos la circunferencia
      Ellipse2D circle = new Ellipse2D.Double(100,100,100,100);

      //pageIndex indica el n�mero de la p�gina que se imprime
      //cuando es 0 primera p�gina a imprimir, es un rect�ngulo
      //cuando es 1 segunda p�gina a imprimir, es una circunferencia
      //En otro caso se devulve que no hay m�s p�ginas a imprimir
      switch (pageIndex)
      {
         case 0 : //P�gina 1: Dibujamos sobre g y luego lo pasamos a g2
                  g.setColor(Color.black);
                  g.fillRect(110,120,30,5);
                  g.setColor(Color.pink);
                  g.drawLine(0,0,200,200);
                  g2 = (Graphics2D) g;
                  return PAGE_EXISTS; //La p�gina 1 existe y se imprimir�
         case 1 : //P�gina 2: Circunferencia y rect�ngulo
                  g2.setColor(Color.red);
                  g2.draw(circle);
                  g2.draw(rect);
                  return PAGE_EXISTS;  //La p�gina 2 existe y se imprimir�
         default: return NO_SUCH_PAGE;        //No se imprimir�n m�s p�ginas
      }
   }
}