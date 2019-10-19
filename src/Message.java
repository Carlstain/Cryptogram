import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
/***
 * La classe Message qui recuperera la matrice contenant le message code pour generer un cryptograme.
 * @author Mahadi Eric DIALLO/Farah Bensmail
 *
 */
public class Message extends Modulator{
	/**
	 * Le constructeur de la classe Message herite des methodes de sa classe mere Modulator.
	 * @param toCode le message a coder.
	 */
	public Message(String toCode) {
		super(toCode);
	}
/**
 * methode createCrypto genere le cryptogramme correspondant a la version 1 bit de l'encodage.	
 * @throws IOException
 */
	void createCrypto() throws IOException
	{
		insertMessageToMatrix();
		BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
        for (int i = 0; i < 200; i++)
        	{
        		for (int j =0 ; j< 200 ; j++)
		        	{
		            	if (matrix[i][j] == 0) 
		            		g2d.setColor(Color.BLACK); 
		            	else if (matrix[i][j]==1)
		            		g2d.setColor(Color.WHITE);
		            	else if (matrix[i][j]==2)		            
		            		g2d.setColor(Color.DARK_GRAY);		            
		            	else if (matrix[i][j]==8)
		            		g2d.setColor(Color.LIGHT_GRAY);
		            	else
		            		g2d.setColor(new Color(matrix[i][j]*10,matrix[i][j]*10,matrix[i][j]*10));
		            	g2d.fillRect(i, j, 1, 1);
		        	}
        	}
        g2d.dispose();
        /*Java faisons pivoter la matrice nous la remettons dans le bon sens*/
        BufferedImage dest = new BufferedImage(bufferedImage.getHeight(), bufferedImage.getWidth(), bufferedImage.getType());
        for (int y = 0; y < bufferedImage.getHeight(); y++) 
            for (int x = 0; x < bufferedImage.getWidth(); x++) 
                dest.setRGB(x,y,bufferedImage.getRGB(y,x));
        
        RenderedImage rendImage = dest;
        /*On genere le fichier png correspondant*/
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");  
        Date date = new Date();
        File file = new File("Cryptograms/crypted-"+formatter.format(date)+".png");
        ImageIO.write(rendImage, "png", file);
        Desktop desktop = Desktop.getDesktop();
        desktop.open(file);//ouverture auto du fichier
        System.out.println("Cryptogram Created !");
	}
	/**
	 * methode createCryptoColor genere le cryptogramme correspondant a la version 3 bits de l'encodage.
	 * @param background lorsque cette variable vaut 1 un cryptogramme avec un logo est crée.
	 * sinon il sera sans logo avec des palletes de couleurs differentes.
	 * @throws IOException
	 */
	void createCryptoColor(int background) throws IOException
	{
		insertMessageToMatrixColor(background);
		BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
        for (int i = 0; i < 200; i++)
        	for (int j =0 ; j< 200 ; j++)
		        {
	        		switch(background)
	        			{
			        		case 1:
					            if (matrixColor[i][j] == 0)
					                g2d.setColor(Color.BLACK);
					            else if (matrixColor[i][j]==1)
					                g2d.setColor(Color.WHITE);
					            /*Couleur en RGB pour certains champs*/
					            else if (matrixColor[i][j]==2)
					                g2d.setColor( new Color(255,250,250));//snow
					            else if (matrixColor[i][j]==3)
					                g2d.setColor(new Color(238,233,233));//snow 2
					            else if (matrixColor[i][j]==4)
					                g2d.setColor(new Color(205,201,201));//snow 3
					            else if (matrixColor[i][j]==5)
					                g2d.setColor(new Color(245,245,185));//white smoke
					            else if (matrixColor[i][j]==6)
					                g2d.setColor(new Color(139,137,137));//snow 4
					            else if (matrixColor[i][j]==7)
					            	g2d.setColor(Color.DARK_GRAY);
					            else if (matrixColor[i][j]==8)
					            	g2d.setColor(Color.LIGHT_GRAY);
					            else if (matrixColor[i][j]==9)
					            	g2d.setColor(new Color(220,220,140));//Gainsboro
					            else
					            	g2d.setColor(new Color(matrixColor[i][j],matrixColor[i][j],matrixColor[i][j]));
					            break;
				         default:
				        	 	if (matrixColor[i][j] == 0)
					                g2d.setColor(Color.BLACK);
					            else if (matrixColor[i][j]==1)
					                g2d.setColor(Color.WHITE);
					            else if (matrixColor[i][j]==2)
					                g2d.setColor(Color.RED);
					            else if (matrixColor[i][j]==3)
					                g2d.setColor(Color.ORANGE);
					            else if (matrixColor[i][j]==4)
					                g2d.setColor(Color.CYAN);
					            else if (matrixColor[i][j]==5)
					                g2d.setColor(Color.GREEN);
					            else if (matrixColor[i][j]==6)
					                g2d.setColor(Color.YELLOW);
					            else if (matrixColor[i][j]==7)
					            	g2d.setColor(Color.DARK_GRAY);
					            else if (matrixColor[i][j]==8)
					            	g2d.setColor(Color.LIGHT_GRAY);
					            else if (matrixColor[i][j]==9)
					            	g2d.setColor(Color.BLUE);
					            else
					            	g2d.setColor(new Color(matrixColor[i][j],matrixColor[i][j],matrixColor[i][j]));
				        	 break;
	        			}
        		 	g2d.fillRect(i, j, 1, 1);
		        }
        g2d.dispose();
        /*Java faisons pivoter la matrice nous la remettons dans le bon sens*/
        BufferedImage dest = new BufferedImage(bufferedImage.getHeight(), bufferedImage.getWidth(), bufferedImage.getType());
        for (int y = 0; y < bufferedImage.getHeight(); y++) 
            for (int x = 0; x < bufferedImage.getWidth(); x++) 
                dest.setRGB(x,y,bufferedImage.getRGB(y,x));
        /*On genere le fichier png correspondant*/
        RenderedImage rendImage = dest;
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");  
        Date date = new Date();
        File file = new File("Cryptograms/crypted-"+formatter.format(date)+".png");
        ImageIO.write(rendImage, "png", file);
        Desktop desktop = Desktop.getDesktop();
        desktop.open(file);//ouverture auto du fichier
        System.out.println("Cryptogram Created !");
	}

}
