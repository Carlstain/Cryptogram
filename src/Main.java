import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
/***
 * 
 * @author Mahadi Eric DIALLO
 *
 */


@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener{
	
    JTextField input,output;
    JButton button;
    JButton button2;
    JButton button3;
    String string;
  
/**
 * Constructeur par defaut de la Classe Main. Genere les elements de notre fenetre Jframe   
 */
    public Main(){
         
          setLayout(null);
          input = new JTextField();
          input.setBounds(15,20,650,200);
          add(input);
           
          button = new JButton("Generate(1 bit version)");
          button.setBounds(10,300,200,20);
          button.addActionListener(this);
          add(button);
          
          button2 = new JButton("Generate(3 bits version)");
          button2.setBounds(230,300,200,20);
          button2.addActionListener(this);
          add(button2);
          
          button3 = new JButton("Generate(3 bits version) no logo");
          button3.setBounds(450,300,220,20);
          button3.addActionListener(this);
          add(button3);
          
          output = new JTextField(5);
          output.setBounds(270,250,115,20);
          add(output);
         
    }
   /**
    * methode actionPerformed, permettant d'attendre l'evement un boutton a ete cliqué.
    * Selon le bouton cliqué le cryptogramme correspondant est généré.
    */
    public void actionPerformed(ActionEvent e) {
           if(e.getSource()== button )
           {
        	   	if(input.getText().length()<225 && input.getText().length()>=1)
        	   	{
        	   		string=input.getText();
	                Message mod = new Message(string);
	         		try {
						mod.createCrypto();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	         		output.setText("Cryptogram Created");
	         		input.setText("");
	         		System.out.println(mod);
        	   	}
        	   	else
        	   	{
        	   		output.setText(" Too long / Empty ");
        	   		input.setText("");
        	   	}
           }
           
           if(e.getSource()== button2 )
           {
        	   	if(input.getText().length()<225 && input.getText().length()>=1)
        	   	{
        	   		string=input.getText();
	                Message mod = new Message(string);
	         		try {
						mod.createCryptoColor(1);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	         		output.setText("Cryptogram Created");
	         		input.setText("");
	         		System.out.println(mod.toString2());
        	   	}
        	   	else
        	   	{
        	   		output.setText(" Too long / Empty ");
        	   		input.setText("");
        	   	}
           }
           if(e.getSource()== button3 )
           {
        	   	if(input.getText().length()<225 && input.getText().length()>=1)
        	   	{
        	   		string=input.getText();
	                Message mod = new Message(string);
	         		try {
						mod.createCryptoColor(0);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	         		output.setText("Cryptogram Created");
	         		input.setText("");
	         		System.out.println(mod.toString2());
        	   	}
        	   	else
        	   	{
        	   		output.setText(" Too long / Empty ");
        	   		input.setText("");
        	   	}
           }
    }
	/**
	 * methode main, execution du programme
	 * @param args
	 */
	public static void main(String[] args){
		
		Main frame = new Main();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        frame.setSize(700,400);;
        frame.setVisible(true);
        frame.setLocation(550, 500);
        frame. setTitle("Cryptographer");
		
	}

}
