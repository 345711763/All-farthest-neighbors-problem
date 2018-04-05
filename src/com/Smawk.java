package com;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Smawk
{
 /*   private List<Point> hullPoints= new ArrayList<Point>();
    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    private int length,width;//numbers of rows and columns
    private List<Integer> indexOfAllFarthestNeigbors = new ArrayList<Integer>();
    private double[][] matrixA;
    */
    public Smawk ()
    {
        //this.hullPoints=hullPoints;
       // this.length=(hullPoints.size()-1);
       // this.width=(hullPoints.size()-1)*2-1;
       // System.out.println("矩阵包含:"+(hullPoints.size()-1)+"个凸包顶点");
      //  System.out.println("矩阵包含"+length+"行");
      //  System.out.println("矩阵包含"+width+"列");
    }

    //根据HullPoints生成一个矩阵
    public Matrix generateMatrix(List<Point> hullPoints){
        int length=(hullPoints.size()-1);
        int width=(hullPoints.size()-1)*2-1;
        double[][] matrix= new double[length][width];
        for(int i=0;i<length;i++)
            for(int j=0;j<width;j++)
            {
                if(i<j && j<=(i+length-1))
                    matrix[i][j]= Math.pow((hullPoints.get(i).x)-(hullPoints.get((j%length)).x),2)+Math.pow((hullPoints.get(i).y)-(hullPoints.get((j%length)).y),2);  
                if(j<=i)
                    matrix[i][j]=j-i;
                if(j>=i+length)
                    matrix[i][j]=-1;
            }
        Matrix matrixA = new Matrix(matrix,length,width);
        return matrixA;
        
    }
    public void printMatrix(Matrix matrix){
        for(int i=0;i<matrix.length;i++){
           
            for(int j=0;j<matrix.width;j++)
            {
                System.out.printf("%13.2f", matrix.matrix[i][j]);
            }
            System.out.println();
            }
        
    }
    public Matrix reduceMatrix(Matrix matrix){
        ArrayList<Integer> columns= new ArrayList<Integer>();
        for(int i=0;i<matrix.width;i++){
            columns.add(i);
        }
        int widthLeft= matrix.width;//初始化剩余列数
        int k=0;
        while(widthLeft>matrix.length){
            double[][] modifiedMatrix = new double[matrix.length][widthLeft-1];
            if(matrix.matrix[k][k]>=matrix.matrix[k][k+1] && k<matrix.length-1){
                
                k=k+1;
                continue;
                }
            if(matrix.matrix[k][k]>=matrix.matrix[k][k+1] && k==matrix.length-1)
            {

                matrix.deletedColumnsNumber.add(columns.get(k+1));
                System.out.println("Column  "+columns.get(k+1)+"  was deleted");
                columns.remove(k+1);
                for(int i=0;i<matrix.length;i++){
                    for(int j=0;j<=k;j++){
                        modifiedMatrix[i][j]=matrix.matrix[i][j];
                        
                    }
                    for(int j=k+2;j<widthLeft;j++){
                        modifiedMatrix[i][j-1]=matrix.matrix[i][j];
                    }
                }             
                matrix.matrix= new double[matrix.length][widthLeft-1];
                matrix.matrix=modifiedMatrix;
                widthLeft--;  
                continue;
            }
            if(matrix.matrix[k][k]<matrix.matrix[k][k+1]){
                matrix.deletedColumnsNumber.add(columns.get(k));
                System.out.println("Column  "+ columns.get(k)+"  was deleted");
                //删除第k列，更新生成删除第K列后的矩阵
                columns.remove(k);
                for(int i=0;i<matrix.length;i++){
                    for(int j=0;j<k;j++){
                        modifiedMatrix[i][j]=matrix.matrix[i][j];
                    }
                    for(int j=k+1;j<widthLeft;j++){
                        modifiedMatrix[i][j-1]=matrix.matrix[i][j];
                    }
                }
                if(k>0){
                    k=k-1;
                }
                matrix.matrix= new double[matrix.length][widthLeft-1];
                matrix.matrix=modifiedMatrix;
                widthLeft--;  
                continue;
            }
    
             
        }
        matrix.width=matrix.length;
        return matrix;
    }
public ArrayList<Position> MaxCompute(Matrix A){
       ArrayList<Position> positionsOfMaxima= new ArrayList<Position>();
       printMatrix(A);
       System.out.println("----------------------------------------------------------------------------------------------------- ");
       System.out.println("Reduce matrix , delete columns:");
       Matrix B;
       if(A.length<A.width)
       {    B= reduceMatrix(A);
       }else{
            B= A;
       }
       //Matrix B= reduceMatrix(A);
       System.out.println("-----------------------------------------------------------------------------------------------------");
       System.out.println("Matrix after reducing");
       System.out.println();
       printMatrix(B);
       System.out.println("-----------------------------------------------------------------------------------------------------");
       Position position = new Position();
       
    // 若如果B只有一行一列则直接输出最大值的位置
      /*  if(B.length==1) 
        {
            position.n=0;
            position.m=0;
            for(int i=0;i<B.deletedColumnsNumber.size();i++)
            {
                if(B.deletedColumnsNumber.get(i)<=0)
                    position.m++;
            }
         
            positionsOfMaxima.add(position);
            return positionsOfMaxima;
        }*/
       if(B.length==1) 
        {
            position.n=0;
            position.m=0;
            Collections.sort(B.deletedColumnsNumber);
            for(int i=0;i<B.deletedColumnsNumber.size();i++)
            {
                if(B.deletedColumnsNumber.get(i)==i){
                    position.m++;
                }
            }
         
            positionsOfMaxima.add(position);
            return positionsOfMaxima;
        }
     //如果B有多行多列，则删除奇数行
        System.out.println("delete odd rows and then get a new matrix:");
        System.out.println();
        if(B.length%2==0){
            Matrix C = new Matrix(new double[B.length/2][B.length],B.length/2,B.length);
            //将从B中删除奇数行后得到的新矩阵保存在C里
            for(int i=0;i<B.length/2;i++)
                for(int j=0;j<B.length;j++)
                {
                    C.matrix[i][j]= B.matrix[i*2+1][j];
                }
            //迭代调用MaxCompute()方法
            positionsOfMaxima=MaxCompute(C);
            for(int i=0;i<positionsOfMaxima.size();i++)
            {
                positionsOfMaxima.get(i).n=(positionsOfMaxima.get(i).n)*2+1;
            }
            //排序 sort positionsOfMaxima 让行数小的position出现在前面
            Collections.sort(positionsOfMaxima,new Comparator<Position>(){
                public int compare(Position p1, Position p2){
                    return Integer.valueOf(p1.n).compareTo(p2.n);
                }
            });
            //求B中奇数行的最大数的位置
            int count=0;
            for(int i=0;i<B.length;i=i+2){
                double maxima=-99;
                Position p= new Position();
                  if(i==0){                   
                      for(int j=0;j<=positionsOfMaxima.get(0).m;j++){
                          if(B.matrix[i][j]>maxima)
                          {
                              maxima=B.matrix[i][j];
                              p.n=i;
                              p.m=j;
                          }
                      }
                      count++;
                  }else{
                      for(int j=positionsOfMaxima.get(count-1).m;j<=positionsOfMaxima.get(count).m;j++){
                          if(B.matrix[i][j]>maxima)
                          {
                              maxima=B.matrix[i][j];
                              p.n=i;
                              p.m=j;
                          }
                      }
                      count++;
                  }
                  positionsOfMaxima.add(p);
            }
        }else{
           
            Matrix C = new Matrix(new double[B.length/2][B.length],B.length/2,B.length);
            for(int i=0;i<B.length/2;i++)
                for(int j=0;j<B.length;j++){
                    C.matrix[i][j]=B.matrix[i*2+1][j];
                }
            positionsOfMaxima=MaxCompute(C);
            for(int i=0;i<positionsOfMaxima.size();i++)
            {
                positionsOfMaxima.get(i).n=(positionsOfMaxima.get(i).n)*2+1;
            }
            //排序 sort positionsOfMaxima 让行数小的position出现在前面
            Collections.sort(positionsOfMaxima,new Comparator<Position>(){
                public int compare(Position p1, Position p2){
                    return Integer.valueOf(p1.n).compareTo(p2.n);
                }
            });
          //求B中奇数行的最大数的位置
            int size= positionsOfMaxima.size();
            int count=0;
            for(int i=0;i<B.length;i=i+2){
                double maxima=-99;
                Position p= new Position();
                  if(i==0){                   
                      for(int j=0;j<=positionsOfMaxima.get(0).m;j++){
                          if(B.matrix[i][j]>maxima)
                          {
                              maxima=B.matrix[i][j];
                              p.n=i;
                              p.m=j;
                          }
                      }
                      count++;
                  }else if(i==B.length-1){
                   for(int j=positionsOfMaxima.get(size-1).m;j<B.width;j++){
                       if(B.matrix[i][j]>maxima)
                       {
                           maxima=B.matrix[i][j];
                           p.n=i;
                           p.m=j;
                       }
                   }
                  }else
                  {
                      for(int j=positionsOfMaxima.get(count-1).m;j<=positionsOfMaxima.get(count).m;j++){
                          if(B.matrix[i][j]>maxima)
                          {
                              maxima=B.matrix[i][j];
                              p.n=i;
                              p.m=j;
                          }
                      }
                      count++;
                  }
                  positionsOfMaxima.add(p);
            }

        }
      //排序 sort positionsOfMaxima 让行数小的position出现在前面
        Collections.sort(positionsOfMaxima,new Comparator<Position>(){
            public int compare(Position p1, Position p2){
                return Integer.valueOf(p1.n).compareTo(p2.n);
            }
        });
     
        for(int i=0;i<positionsOfMaxima.size();i++)
        {
            for(int j=0;j<B.deletedColumnsNumber.size();j++){
                if(B.deletedColumnsNumber.get(j)<=positionsOfMaxima.get(i).m)
                    positionsOfMaxima.get(i).m++;
            }
        }
        return positionsOfMaxima;
        
    }
//only for test

   public static void main(String args[]){
        double[][] test = new double[9][17];
        test[0][0]=0;
        test[0][1]=14013;
        test[0][2]=57265;
        test[0][3]=80660;
        test[0][4]=100841;
        test[0][5]=60196;
        test[0][6]=35053;
        test[0][7]=21760;
        test[0][8]=7753;
        for(int i=9;i<17;i++)
        {
            test[0][i]=-1;
        }
        test[1][0]=-1;
        test[1][1]=0;
        test[1][2]=37672;
        test[1][3]=63029;
        test[1][4]=131108;
        test[1][5]=104953;
        test[1][6]=82132;
        test[1][7]=68317;
        test[1][8]=42250; 
        test[1][9]=14013;
        for(int i=10;i<17;i++){
            test[1][i]=-1;
        }
        test[2][0]=-2;
        test[2][1]=-1;
        test[2][2]=0;
        test[2][3]=3425;
        test[2][4]=58132;
        test[2][5]=74117;
        test[2][6]=83924;
        test[2][7]=97425;
        test[2][8]=83282;
        test[2][9]=57265;
        test[2][10]=37672;
        for(int i=11;i<17;i++){
            test[2][i]=-1;
        }
        test[3][0]=-3;
        test[3][1]=-2;
        test[3][2]=-1;
        test[3][3]=0;
        test[3][4]=44465;
        test[3][5]=71240;
        test[3][6]=90557;
        test[3][7]=112340;
        test[3][8]=103349;
        test[3][9]=80660;
        test[3][10]=63029;
        test[3][11]=3425;
        for(int i=12;i<17;i++){
            test[3][i]=-1;
        }
        test[4][0]=-4;
        test[4][1]=-3;
        test[4][2]=-2;
        test[4][3]=-1;
        test[4][4]=0;
        test[4][5]=14125;
        test[4][6]=39400;
        test[4][7]=70537;
        test[4][8]=86578;
        test[4][9]=100841;
        test[4][10]=131108;
        test[4][11]=58132;
        test[4][12]=44465;
        for(int i=13;i<17;i++){
            test[4][i]=-1;
        }
        test[5][0]=-1;
        test[5][1]=-4;
        test[5][2]=-3;
        test[5][3]=-2;
        test[5][4]=-1;
        test[5][5]=0;
        test[5][6]=5069;
        test[5][7]=14562;
        test[5][8]=35053;
        test[5][9]=82132;
        test[5][10]=83924;
        test[5][11]=90557;
        test[5][12]=39400;
        test[5][13]=7065;
        for(int i=14;i<17;i++){
            test[5][i]=-1;
        }
        test[6][0]=-6;
        test[6][1]=-5;
        test[6][2]=-4;
        test[6][3]=-3;
        test[6][4]=-2;
        test[6][5]=-1;
        test[6][6]=0;
        test[6][7]=5069;
        test[6][8]=14562;
        test[6][9]=35053;
        test[6][10]=82132;
        test[6][11]=83924;
        test[6][12]=90557;
        test[6][13]=39400;
        test[6][14]=7065;
        test[6][15]=-1;
        test[6][16]=-1;
        test[7][0]=-7;
        test[7][1]=-6;
        test[7][2]=-5;
        test[7][3]=-4;
        test[7][4]=-3;
        test[7][5]=-2;
        test[7][6]=-1;
        test[7][7]=0;
        test[7][8]=3977;
        test[7][9]=21760;
        test[7][10]=68317;
        test[7][11]=97425;
        test[7][12]=112340;
        test[7][13]=70537;
        test[7][14]=23972;
        test[7][15]=5069;
        test[7][16]=-1;
        test[8][0]=-8;
        test[8][1]=-7;
        test[8][2]=-6;
        test[8][3]=-5;
        test[8][4]=-4;
        test[8][5]=-3;
        test[8][6]=-2;
        test[8][7]=-1;
        test[8][8]=0;
        test[8][9]=7753;
        test[8][10]=42250;
        test[8][11]=83282;
        test[8][12]=103349;
        test[8][13]=86578;
        test[8][14]=38673;
        test[8][15]=14562;
        test[8][16]=3977;
        Smawk s = new Smawk();
        Matrix matrix= new Matrix(test,9,17);
        Matrix matrix2= new Matrix(test,9,17);
        
        long t1=System.currentTimeMillis();
        ArrayList<Position> positionsOfMaxima=s.MaxCompute(matrix);
        long t2=System.currentTimeMillis();
        System.out.println("Run time : "+(t2-t1)+"ms");
        for(int i=0;i<positionsOfMaxima.size();i++){
            System.out.println("["+positionsOfMaxima.get(i).n+"]["+positionsOfMaxima.get(i).m+"]");
        }
        for(int i=0;i<positionsOfMaxima.size();i++){
            if(positionsOfMaxima.get(i).m<matrix.length)
                System.out.println("The farthest neighbor of P"+i+" is :P"+positionsOfMaxima.get(i).m+"  the distance is "+matrix2.matrix[positionsOfMaxima.get(i).n][positionsOfMaxima.get(i).m]);
            if(positionsOfMaxima.get(i).m>=matrix.length)
                System.out.println("The farthest neighbor of P"+i+" is :P"+(positionsOfMaxima.get(i).m-matrix.length)+"  the distance is "+matrix2.matrix[positionsOfMaxima.get(i).n][positionsOfMaxima.get(i).m]);
        }                                                                                                                                                                                                                                                                    
        
    }
}
