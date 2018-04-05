package com;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Panel extends JPanel implements MouseListener,KeyListener
{
    private int x,y;
    private GrahamScan s= new GrahamScan();
    private List<Point> hullPoints= new ArrayList<Point>();
    private List<Point> points = new ArrayList<Point>();
    public Panel(){
        setSize(800,800);
        setPreferredSize(new Dimension(800,800));
        this.setFocusable(true);
        addMouseListener(this);
        addKeyListener(this);
    }
 
    public static void main(String[] args){
        JFrame f= new JFrame();
        Panel panel= new Panel();
        f.add(panel);
        f.setTitle("ConvexHull");
        f.setSize(800,800);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new FlowLayout());
        f.setResizable(false);
        f.setVisible(true);
        
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.x=e.getX();
        this.y=e.getY();
        Point p= new Point();
        p.setLocation(x, y);
        this.points.add(p);   
        this.getGraphics().drawOval(x, y, 5, 5);       
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        int c = arg0.getKeyCode();
        if(c==KeyEvent.VK_ENTER){
        this.repaint();       
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        int c= arg0.getKeyCode();
        if(c==KeyEvent.VK_ENTER){
      
            for(int i=0;i<points.size();i++)
            {
                this.getGraphics().drawOval(points.get(i).x, points.get(i).y, 5, 5);
            }
            hullPoints=s.computeConvexHull(points);
            System.out.println("Hull vertices:");
            for(int i=0;i<hullPoints.size()-1;i++)
            {
                System.out.println("P"+i+"("+hullPoints.get(i).x+","+hullPoints.get(i).y+")");
            }
           Smawk s= new Smawk();
           Matrix matrix = s.generateMatrix(hullPoints);//该矩阵经过MaxCompute()后结构会发生改变，因此需要镜像保存
           Matrix matrix2= s.generateMatrix(hullPoints);//镜像matrix，保存原始矩阵，然后利用坐标找最近点
           System.out.println("-----------------------------------------------------");
           long t1=System.currentTimeMillis();
           ArrayList<Position> positionsOfMaxima=s.MaxCompute(matrix);
           long t2=System.currentTimeMillis();
           System.out.println("Run time : "+(t2-t1)+"ms");
           System.out.println("the index of maximum entry of each row:");
           for(int i=0;i<positionsOfMaxima.size();i++){
               System.out.println("["+positionsOfMaxima.get(i).n+"]["+positionsOfMaxima.get(i).m+"]");
           }
           for(int i=0;i<positionsOfMaxima.size();i++){
               if(positionsOfMaxima.get(i).m<matrix.length)
                   System.out.println("The farthest neighbor of P"+i+" is :P"+positionsOfMaxima.get(i).m+"  the distance is "+matrix2.matrix[positionsOfMaxima.get(i).n][positionsOfMaxima.get(i).m]);
               if(positionsOfMaxima.get(i).m>=matrix.length)
                   System.out.println("The farthest neighbor of P"+i+" is :P"+(positionsOfMaxima.get(i).m-matrix.length)+"  the distance is "+matrix2.matrix[positionsOfMaxima.get(i).n][positionsOfMaxima.get(i).m]);
           }
            for(int i=1;i<hullPoints.size();i++){
                this.getGraphics().drawLine(hullPoints.get(i-1).x, hullPoints.get(i-1).y, hullPoints.get(i).x, hullPoints.get(i).y);
            }
        }
        
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
      
        
    }
   
}
