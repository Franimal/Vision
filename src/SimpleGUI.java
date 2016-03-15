import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class SimpleGUI extends JPanel implements MouseListener {

	private JFrame frame;
	
	private JMenuBar menuBar;
	
	private JMenu fileMenu;
	private JMenuItem loadImage;
	
	private BufferedImage baseImage;
	private BufferedImage newImage;
	
	public SimpleGUI(){
		frame = new JFrame();
		
		setPreferredSize(new Dimension(500, 500));
		
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		
		loadImage = new JMenuItem("Load Image");
		loadImage.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileFilter(){

					 public String getDescription() {
					       return "Images";
					   }

					   public boolean accept(File f) {
					       if (f.isDirectory()) {
					           return false;
					       } else {
					           String filename = f.getName().toLowerCase();
					           return filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png")
					        		   || filename.endsWith(".bmp");
					       }
					   }
					
				});
				 int returnVal = fc.showOpenDialog(null);

			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fc.getSelectedFile();
			            try {
							baseImage = ImageIO.read(file);
							newImage = VisionProcessing.deepCopy(baseImage);
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			            //This is where a real application would open the file.
			          System.out.println("Opening: " + file.getName());
			        } else {
			         System.out.println("Open command cancelled by user.");
			        }
			        repaint();
			}
			
		});
		
		fileMenu.add(loadImage);
		menuBar.add(fileMenu);
		frame.setJMenuBar(menuBar);
		
		this.addMouseListener(this);
		
		frame.add(this);
		
		frame.setEnabled(true);
		frame.setVisible(true);
		frame.pack();
	}
	

	
	@Override
	public void paintComponent(Graphics g){
		if(baseImage == null){
			return;
		}
		
		int x = 0;
		int y = 0;
		
		int width = (int)(getWidth()/2f);
		int height = (baseImage.getWidth()/baseImage.getHeight()) * width;
		
		g.drawImage(baseImage, 0, 0, width, height, null);
		g.drawImage(newImage, width, 0, width, height, null);
	}



	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	public void mouseReleased(MouseEvent e) {
		int x = 0;
		int y = 0;
		
		int width = (int)(getWidth()/2f);
		int height = (baseImage.getWidth()/baseImage.getHeight()) * width;
		
		if(new Rectangle(x,y,width,height).contains(e.getPoint())){
			System.out.println("Focusing on " + (e.getX()/(width*1.0f)) + " , " + (e.getY()/(height*1.0f)));
			newImage = VisionProcessing.focusOnPoint(baseImage, e.getX()/(width*1.0f), e.getY()/(height*1.0f));
		}
		System.out.println("Done.");
		repaint();
		
	}
	
	
}
