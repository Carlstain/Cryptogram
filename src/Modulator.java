import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
/***
 * 
 * @author Mahadi Eric DIALLO / Paul-Samuel NIAMKE
 *
 */

public class Modulator {

		byte[][] matrix = new byte[200][200];//Contiendra notre message encodé sur 1 bit.
		int[][] matrixColor = new int[200][200];//Contiendra notre message encodé sur 3 bits.
		ArrayList<Byte> messageToBinary = new ArrayList<>();//Contient le message a coder en byte.
		/**
		 * Constructeur de la classe Modulator 
		 * Il definit les differentes zone des matrice qui seront utilisées pour l'encodage 1 et 3 bits.
		 * Ainsi qu'une conversion du message a crypter en une sequence de Byte.
		 * @param toCode le message a coder.
		 */
		public Modulator (String toCode)
		{
			byte x=1;
			int y=30;
			int color =2;
			/*Definition des contours de la zone du message*/
			for(int i = 20 ; i<180 ; i+=2)
				for(int j = 20 ; j<180 ; j+=2)
				{
					matrix[19][j]=x;
					matrix[i][19]=x;
					matrix[179][j]=x;
					matrix[i][179]=x;
					matrixColor[19][j]=x;
					matrixColor[i][19]=x;
					matrixColor[179][j]=x;
					matrixColor[i][179]=x;
				}
			
			/*Definition du coin haut-droit*/
			for(int i = 0 ; i<20 ; i+=2)
				for(int j = 0 ; j<20 ; j+=2)
				{
					matrix[0][j]=x;
					matrix[i][0]=x;
					matrix[19][j]=x;
					matrix[i][19]=x;
					matrixColor[0][j]=x;
					matrixColor[i][0]=x;
					matrixColor[19][j]=x;
					matrixColor[i][19]=x;
				}
			/*ajout des echantillon dans les rectangle haut et bas*/
			while(y<160)
			{
				for(int i = 0 ; i<19 ; i++)
					for(int j = y ; j<y+20 ; j++)
						matrixColor[i][j]=color;
				for(int i = 181 ; i<200 ; i+=1)
					for(int j = y ; j<y+20 ; j++)
						matrixColor[i][j]=color+1;
				y+=40;
				color+=2;
			}
			/*Definition du coin haut-gauche*/
			for(int i = 0 ; i<20 ; i+=2)
				for(int j = 180 ; j<200 ; j+=2)
				{
					//matrix[i][j]=x;
					matrix[0][j]=x;
					matrix[i][180]=x;
					matrix[19][j]=x;
					matrix[i][199]=x;
					matrixColor[0][j]=x;
					matrixColor[i][180]=x;
					matrixColor[19][j]=x;
					matrixColor[i][199]=x;
				}
			/*Definition du coin bas-gauche*/
			for(int i = 180 ; i<200 ; i+=2)
				for(int j = 0 ; j<20 ; j+=2)
				{
					//matrix[i][j]=x;
					matrix[180][j]=x;
					matrix[i][19]=x;
					matrix[199][j]=x;
					matrix[i][0]=x;
					matrixColor[180][j]=x;
					matrixColor[i][19]=x;
					matrixColor[199][j]=x;
					matrixColor[i][0]=x;
				}
			/*Definition du coin bas-droit*/
			for(int i = 180 ; i<200 ; i++)
				for(int j = 180 ; j<200 ; j++){matrix[i][j]=x;matrixColor[i][j]=x;}
			for (int c = 0 ; c < toCode.length() ; c++)
				{
					String string;
					if(toCode.charAt(c)<Math.pow(2,6))
						string = "0"+Integer.toBinaryString(toCode.charAt(c));
					else
						string = Integer.toBinaryString(toCode.charAt(c));
					for(int d = string.length()-1 ; d>=0 ; d--)
					{
						if(string.charAt(d)==49)
							messageToBinary.add((byte) 1);
						else
							messageToBinary.add((byte) 0);
					}
				}
			//System.out.println(messageToBinary);
		}
	
		public byte[][] getMatrix(){return matrix;}
		/**
		 * methode hamming code pour l'insertion des bit de parite dans le message selon une parite paire.
		 * @return le message avec les bits de control inserés.
		 */
		private byte[] hammingCode ()
		{
			byte[] coded = new byte[messageToBinary.size()+(int)Math.ceil(Math.log(messageToBinary.size())/Math.log(2))+1];
			ArrayList<Integer> bitsToOne = new ArrayList<>();
			ArrayList<String> bitsToOneBinary = new ArrayList<>();
			ArrayList<Integer> controlBitsPosition = new ArrayList<>(Arrays.asList(0,1));
			int j=0,xor=0;
			for(int i = 3 ; i <= coded.length ; i++)
			{
				if((Math.log(i)%Math.log(2))!=0.0 & i!=8 & i!=32 & i!=64 & i!=128 & j<messageToBinary.size())
				{
					coded[i-1]=messageToBinary.get(j);
					j++;
					if(coded[i-1]==1)
						{	
							bitsToOne.add(i-1);
							xor=xor^i;
						}
				}
				else
					controlBitsPosition.add(i-1);
					
			}
			
			int position = 0;
			
			for(Integer i : bitsToOne)
				bitsToOneBinary.add(Integer.toBinaryString(i));
			
			for(int c =Integer.toBinaryString(xor).length()-1 ; c>=0 ; c--)
			{
				if(Integer.toBinaryString(xor).charAt(c)==49)
					coded[controlBitsPosition.get(position)]=1;
				else
					coded[controlBitsPosition.get(position)]=0;
				
				position++;
			}
			return coded;
		}
		/**
		 * Insert le message code dans la matrice pour la version 1 bit. il s'agit donc d'un sequence de 1 et de 0.
		 * Calcul aussi le decalage entre les bits representant le message.
		 * @throws IOException en cas d'echec d'ouverture du fichier contenant le logo
		 */
		protected void insertMessageToMatrix() throws IOException
		{
			byte[] coded = hammingCode();
			int[][] logo = logoToBytes();
			
			int x = 0,padding = 0;
			byte modulo=0;/*entier a rajouter pour obtenir le bon decalage*/
			byte color;
			int f = (int) Math.sqrt(25600/coded.length);//Coefficient de decalage
			
			if(f>=1 && f<=6)
				{
					padding=f*3;
					color=2;
				}
			else if(f>18)
				{
					padding=f/3;
					modulo=(byte) (f%3);
					color=3;
				}
			else
				{
					padding=f;
					color=1;
				}
			
			for(int i= (20-padding)/2 ; i<((20-padding)/2)+padding ; i++)
				{
					for(int j= (20-padding)/2 ; j<((20-padding)/2)+padding ; j++)
						{
							if(modulo!=0)
								matrix[i][j]=modulo;
							matrix[(20-padding)/2][j]=color;
							matrix[i][(20-padding)/2]=color;
							matrix[((20-padding)/2)+padding-1][j]=color;
							matrix[i][((20-padding)/2)+padding-1]=color;
						}
				}
			
			for(int i= (20-padding)/2 ; i<((20-padding)/2)+padding ; i++)
				{
					for(int j= 180+((20-padding)/2) ; j<(180+((20-padding)/2))+padding ; j++)
						{
							if(modulo!=0)
								matrix[i][j]=modulo;
							matrix[(20-padding)/2][j]=color;
							matrix[i][180+((20-padding)/2)]=color;
							matrix[((20-padding)/2)+padding-1][j]=color;
							matrix[i][180+((20-padding)/2)+padding-1]=color;
						}
				}
			
			for(int i= 180+((20-padding)/2) ; i<(180+((20-padding)/2))+padding ; i++)
				{
					for(int j= (20-padding)/2 ; j<((20-padding)/2)+padding ; j++)
						{
							if(modulo!=0)
								matrix[i][j]=modulo;
							matrix[180+((20-padding)/2)][j]=color;
							matrix[i][(20-padding)/2]=color;
							matrix[180+((20-padding)/2)+padding-1][j]=color;
							matrix[i][((20-padding)/2)+padding-1]=color;
						}
				}
			
			for(int i = 20 ; i<180 ; i+=2*f)
				{
					int detector = 0;
					for(int j = 20 ; j<180 ; j+=2*f)
						{
							if(x<=coded.length-1)/*ajout du code detecteur*/
								{
									matrix[i][j]=coded[x];x++;
									detector = detector+matrix[i][j];
								}
							else
								break;
							
							for(int k = i ; k<i+(f) ; k++)
								for(int l = j ; l<j+(f) ; l++)
									if(k<179 && l<179)
										matrix[k][l]=matrix[i][j];
						}
					
					for(int m = 0 ; m<19 ; m++)
						matrix[i][m]=(byte)detector;
				}
			for(int i = 20 ; i<180 ; i++)
					for(int j = 20 ; j<180 ; j++)
							if(matrix[i][j]==0 && logo[i-20][j-20]==8)
								matrix[i][j]=8;
			
		}
		/**
		 * Insert le message dans matrice pour la version 3 bits, il s'agit ici de sequence de 3 bit composés de
		 * 0 ou de 1, chaque combinaison de 1 et de 0 est representee par un entier allant de 0 a 7.
		 * @param background ici ce paramettre determine la coloration des sequences de bits.
		 * @throws IOException en cas d'echec d'ouverture du fichier contenant le logo
		 */
		protected void insertMessageToMatrixColor(int background) throws IOException
		{
			byte[] coded = hammingCode();
			int[][] logo = logoToBytes();
			int padding,color,modulo=0;//entier a rajouter pour obtenir le bon decalage
			ArrayList<Byte> recoded = new ArrayList<>();
			
			for(byte i : coded)
				recoded.add(i);
			while(recoded.size()%3!=0)
				recoded.add((byte) 0);
			
			int y=0,detector=0;
			int f = (int) Math.sqrt(25600/(recoded.size()/3));//Coefficient de decalage
			
			if(f>=1 && f<=6)
				{
					padding=f*3;
					color=9;
				}
			else if(f>18)
				{
					padding=f/5;
					modulo=f%5;
					color=5;
				}
			else
				{
					padding=f;
					color=1;
				}
			
			for(int i= (20-padding)/2 ; i<((20-padding)/2)+padding ; i++)
				{
					for(int j= (20-padding)/2 ; j<((20-padding)/2)+padding ; j++)
						{
							if(modulo!=0)
								matrixColor[i][j]=modulo;			
							matrixColor[(20-padding)/2][j]=color;
							matrixColor[i][(20-padding)/2]=color;
							matrixColor[((20-padding)/2)+padding-1][j]=color;
							matrixColor[i][((20-padding)/2)+padding-1]=color;
						}
				}
			
			for(int i= (20-padding)/2 ; i<((20-padding)/2)+padding ; i++)
				{
					for(int j= 180+((20-padding)/2) ; j<(180+((20-padding)/2))+padding ; j++)
						{
							if(modulo!=0)
								matrixColor[i][j]=modulo;
							matrixColor[(20-padding)/2][j]=color;
							matrixColor[i][180+((20-padding)/2)]=color;
							matrixColor[((20-padding)/2)+padding-1][j]=color;
							matrixColor[i][180+((20-padding)/2)+padding-1]=color;
						}
				}
			
			for(int i= 180+((20-padding)/2) ; i<(180+((20-padding)/2))+padding ; i++)
				{
					for(int j= (20-padding)/2 ; j<((20-padding)/2)+padding ; j++)
						{
							if(modulo!=0)
								matrixColor[i][j]=modulo;
							matrixColor[180+((20-padding)/2)][j]=color;
							matrixColor[i][(20-padding)/2]=color;
							matrixColor[180+((20-padding)/2)+padding-1][j]=color;
							matrixColor[i][((20-padding)/2)+padding-1]=color;
						}
				}
			
			for(int i = 20 ; i<180 ; i+=2*f)
				{
					detector=0;
					for(int j = 20 ; j<180 ; j+=2*f)
						{
							if(y<recoded.size())
								{
									/*definition du code de coloration pour chaque triplet de bits*/
									if(		recoded.get(y)==0 && recoded.get(y+1)==0 && recoded.get(y+2)==0)
										matrixColor[i][j]=0;
									else if(recoded.get(y)==0 && recoded.get(y+1)==0 && recoded.get(y+2)==1)
											matrixColor[i][j]=7;
									else if(recoded.get(y)==0 && recoded.get(y+1)==1 && recoded.get(y+2)==0)
											matrixColor[i][j]=2;
									else if(recoded.get(y)==1 && recoded.get(y+1)==0 && recoded.get(y+2)==0)
											matrixColor[i][j]=3;
									else if(recoded.get(y)==1 && recoded.get(y+1)==0 && recoded.get(y+2)==1)
											matrixColor[i][j]=4;
									else if(recoded.get(y)==1 && recoded.get(y+1)==1 && recoded.get(y+2)==0)
											matrixColor[i][j]=5;
									else if(recoded.get(y)==1 && recoded.get(y+1)==1 && recoded.get(y+2)==1)
											matrixColor[i][j]=1;
									else
											matrixColor[i][j]=6;
									y+=3;
									detector = detector+matrixColor[i][j];
						
								}
							else
								break;
							
							for(int k = i ; k<i+(f) ; k++)
								for(int l = j ; l<j+(f) ; l++)
									if(k<179 && l<179)
										matrixColor[k][l]=matrixColor[i][j];
							
							for(int m = 0 ; m<19 ; m++)/*ajout du code detecteur*/
								matrixColor[i][m]=detector;
						}
			}

			if(background==1)
				{
					for(int i = 20 ; i<180 ; i++)
						for(int j = 20 ; j<180 ; j++)
							if(matrixColor[i][j]==0 && logo[i-20][j-20]==8)
								matrixColor[i][j]=logo[i-20][j-20];
				}
					
			System.out.println("HERE ----- "+y+"-----"+f);
		}
		
		
		/**
		 * affiche la matrice 1 bit
		 */
		public String toString()
		{
			String result = "";
			for(int i = 0 ; i < matrix.length ; i++)
			{
				for(int j = 0 ; j < matrix.length ; j++)
					result+=matrix[i][j];
				result+="\n";
			}
			return result;
		}
		/**
		 * affiche la matrice 3 bits
		 * @return la matrice avec les sequence de 3 bits reprensentés par des entiers.
		 */
		public String toString2()
		{
			String result = "";
			for(int i = 0 ; i < matrixColor.length ; i++)
			{
				for(int j = 0 ; j < matrixColor.length ; j++)
					result+=matrixColor[i][j];
				result+="\n";
			}
			return result;
		}
		/**
		 * methode logoToBytes, Convertie le logo en une sequence de bytes.
		 * @return une matrice contenant le logo represente en sequence de bytes.
		 * @throws IOException en cas d'echec d'ouverture du fichier contenant le logo
		 */
		public int[][] logoToBytes () throws IOException {
			 int[][] logo = new int[160][160];
			 File imgPath = new File("logo.png");
			 BufferedImage bufferedImage = ImageIO.read(imgPath);
			 for(int i = 0 ; i < 160 ; i++)
			 {
				 for(int j = 0 ; j < 160 ; j++)
				 {
					 if(bufferedImage.getRGB(i, j)==-1)
						 logo[i][j]=8;
					 else
						 logo[i][j]=0;
				 }
			 }
			 return logo ;
			}
}
