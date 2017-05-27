package pick.moon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//构建图
public class Graph {
	
	
	//点个数
	private int dotNumber;
	//邻接矩阵存放图的权值
	private Edge adjWeight[][];
	//存放点的属性
	private Dot dotList[];
	//存放必过点存放池(不包括开始节点和结束节点)
	private ArrayList<Dot> mustPassDotList;
	//存放开始点
	private Dot startDot;
	//存放结束点
	private Dot endDot;
	//最多经过多少个点到达
	private int step;
	/*
	 * adjWeight边的数组
	 * dotList点的数组
	 */
	public Graph(Edge adjWeight[][],Dot dotList[]){
		this.adjWeight = adjWeight;
		this.dotNumber = adjWeight.length;
		this.dotList = dotList;
	}
	
	/*
	 * 通过文件路径读取创建开始
	 */
	public Graph(String filePath){
		/*
		 * 判断读入的行变量
		 *  1、注释说明：注释用双斜杠起始注释必过点写第一行
		 *  2、必经点(食物和水果储存室)定义说明：必经点用mDot:+点+逗号例如: mDot:7,12
		 *  3、必经路径(有奖励的绿色路径)定义说明：必经路径mEdge:+点+逗号+空格+其他路径 例如: mEdge: 2,4 14,13
		 *  4、不能通过的路径(有奖励的绿色路径)定义说明：必经路径mNEdge:+点+逗号+空格+其他路径 例如: mNEdge: 2,4 14,13
		 *  5、邻接矩阵(蜂房)定义说明：用Graph:换行 不能达到的权值为-1，自身到自身定义为 0，权值用空格分开一个点占用一行
		 */
		//必经点(食物和水果储存室)
		int mDot[] = null;
		//必经路径(有奖励的绿色路径)
		String mEdge[] = null;
		//不能通过的路径mNEdge
		String mNEdge[] = null;
		//存放邻接矩阵
		int adjWeightMatrix[][] = null;
		//点的定义
		Dot dotList[] = null;
		File file = new File(filePath);
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(file), "gb2312")
					);
			//邻接矩阵读入开始
			String tempString = null;
			//判断文件中定义变量类型
			int varType = 1;
			//判断邻接矩阵加载行数
			int adjWeightLine = 0;
			while((tempString = reader.readLine()) != null){
				int  graphEnd = MyUtil.judge(tempString);
				//是否开始读取邻接矩阵
				if(varType != 5 || graphEnd == 6){
					varType = MyUtil.judge(tempString);
					if(varType == 2){
						String []mDotStrArray = tempString.split(":");
						//必经点的处理开始
						if(mDotStrArray.length != 2){
							System.out.println("文件："+file.getName()+"中：必经点(食物和水果储存室)定义错误");
						}else{
							String mDotStr = mDotStrArray[1];
							System.out.println("必经点为:"+mDotStr);
							String [] mDotStrIntArray =  mDotStr.split(",");
							mDot = new int[mDotStrIntArray.length];
							for(int i = 0 ; i < mDotStrIntArray.length ; i ++){
								mDot[i] = Integer.valueOf(mDotStrIntArray[i]);
							}
						}
					}//必经点处理完成
					//必经路径处理开始
					else if(varType == 3){
						String []mEdgeStrArray = tempString.split(":");
						if(mEdgeStrArray.length != 2){
							System.out.println("文件："+file.getName()+"中：必经路径(绿色路径)定义错误");
							return;
						}else{
							String mEdgeStr = mEdgeStrArray[1].trim();
							System.out.println("必经路径为:"+mEdgeStr);
							mEdge =  mEdgeStr.split("\\s+");
						}
					}//必经路径处理结束
					//不能通过路径处理开始
					else if(varType == 4){
						String []mNEdgeStrArray = tempString.split(":");
						if(mNEdgeStrArray.length != 2){
							System.out.println("文件："+file.getName()+"中：不能通过的路径(红色路径)定义错误");
							return;
						}else{
							String mNEdgeStr = mNEdgeStrArray[1].trim();
							System.out.println("不能通过的路径为:"+mNEdgeStr);
							mNEdge =  mNEdgeStr.split("\\s+");
						}
					}//不能通过的路径结束
					else if(varType == 7){
						String []startEndDotStrArray = tempString.split(":");
						if(startEndDotStrArray.length != 2){
							System.out.println("文件："+file.getName()+"中：开始和结束节点定义错误");
							return;
						}else{
							String startEndDot = startEndDotStrArray[1].trim();
							System.out.println("开始节点和结束节点为:"+startEndDot);
							String []startEndDotArray =  startEndDot.split(",");
							if(startEndDotArray == null || startEndDotArray.length != 2){
								System.out.println("文件："+file.getName()+"中：开始和结束节点定义错误");
								return;
							}else{
								this.startDot =new Dot(Integer.valueOf(startEndDotArray[0]), "S", false);
								this.endDot =new Dot(Integer.valueOf(startEndDotArray[1]), "E", false);
							}
						}
					}
					else if(varType == 8){
						String []stepStrArray = tempString.split(":");
						if(stepStrArray.length != 2){
							System.out.println("文件："+file.getName()+"中：最多经过的点定义错误");
							return;
						}else{
							String stepStr = stepStrArray[1].trim();
							System.out.println("最多进过的点个数为:"+stepStr);
							this.step = Integer.valueOf(stepStr);
							
						}
					}
				}else{//开始处理邻接矩阵
					String []adjWeightStr = tempString.split("\\s+");
					if(adjWeightStr == null || adjWeightStr.length == 0){
						System.out.println("文件："+file.getName()+"中：图的邻接矩阵 行中 定义错误");
						return;
					}
					//如果图中点还没有定义个数开始赋值点的个数
					if(this.dotNumber == 0){
						this.dotNumber = adjWeightStr.length;
					}else if(this.dotNumber != adjWeightStr.length){
						System.out.println("文件："+file.getName()+"中：图的邻接矩阵 行中个数 定义错误");
						return;
					}
					if(adjWeightMatrix == null){
						adjWeightMatrix = new int[this.dotNumber][this.dotNumber];
					}
					if(adjWeightLine > adjWeightStr.length){
						System.out.println("文件："+file.getName()+"中：图的邻接矩阵中 列的数大于行数 定义错误");
						return;
					}
					//加载邻接矩阵的行数
					for(int i = 0 ; i < adjWeightStr.length ;i ++){
						adjWeightMatrix[adjWeightLine][i] = Integer.valueOf(adjWeightStr[i]);
					}
					adjWeightLine ++;
				}
			}//文件读写完成
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("文件不存在！请查看路径");
			e.printStackTrace();
			
		} catch (IOException e) {
			System.out.println("文件读写错误！请查看！");
			e.printStackTrace();
		}
		if(mDot == null){
			System.out.println("提示：图中没有定义食物和水果间");
		}
		if(mEdge == null){
			System.out.println("提示：图中没有定义有奖励的必经的路径");
		}
		if(mNEdge == null){
			System.out.println("提示：图中没有定义食蚁兽经过的路径");
		}
		if(this.dotNumber == 0){
			System.out.println("文件中点定义错误");
			return;
		}
		//开始初始化点
		System.out.println("开始初始化点");
		dotList = new Dot[this.dotNumber];
		for(int i = 0 ; i < this.dotNumber ; i ++){
			Dot dot = null;
			if(mDot != null){
				boolean isFood = false;
				for(int food : mDot){
					if(food == i){
						isFood = true;
					}
				}
				dot = new Dot(i,"N" + i, isFood);
				//将必过点放入存放池
				if(isFood){
					if(this.mustPassDotList == null){
						this.mustPassDotList = new ArrayList<Dot>();
					}
						this.mustPassDotList.add(dot);
					
				}
			}else{
				dot = new Dot(i,"N" + i, false);
			}
			dotList[i] = dot;  
		}//初始化点结束
		this.dotList = dotList;
		
		boolean isPassType = true;
		//查找是否为必经路径
		if(mEdge != null){
			for(String mEdgeStr : mEdge){
				String []mEdgeStrArray =  mEdgeStr.split(",");
				if(mEdgeStrArray == null || mEdgeStrArray.length != 2){
					System.out.println("构建路径时，必经点错误，请查看必经点是否正确！");
					return;
				}else{
					int x = Integer.valueOf(mEdgeStrArray[0].trim());
					int y = Integer.valueOf(mEdgeStrArray[1].trim());
					if(dotList != null){
						for(int d = 0 ; d < dotList.length ; d ++){
							if(x == d || y == d){
								Dot dot = dotList[d];
								dot.setPassEdgeDot(true);
								//设置兄弟节点
								if(x == d){
									dot.setBrotherDot(y);
								}else{
									dot.setBrotherDot(x);
								}
								dotList[d] = dot;
								//将必过点放入存放池
								if(this.mustPassDotList == null){
									this.mustPassDotList = new ArrayList<Dot>();
								}
									this.mustPassDotList.add(dot);
							}
							
						}
					}
				}
			}//必经路径判断完成
		}
		
		System.out.println("初始化点结束");
		//初始化边开始
		System.out.println("初始化边开始");
		adjWeight = new Edge[this.dotNumber][this.dotNumber];
		if(adjWeightMatrix == null){
			System.out.println("初始化边失败");
			return;
		}else{
			Edge edge = null; 
			for(int i = 0 ; i < adjWeightMatrix.length ; i ++){
				for(int j = 0 ; j < adjWeightMatrix[i].length ; j ++){
					//边是否能访问,默认是true，true为可以访问，false不能访问
					if(mNEdge != null){
					//判断是否为红色路径
						for(String mNEdgeStr : mNEdge){
							String []mNEdgeStrArray =  mNEdgeStr.split(",");
							if(mNEdgeStrArray == null || mNEdgeStrArray.length != 2){
								System.out.println("构建路径时，不能经点错误，请查看不能经点是否正确！");
								return;
							}else{
								int x = Integer.valueOf(mNEdgeStrArray[0].trim());
								int y = Integer.valueOf(mNEdgeStrArray[1].trim());
								if(x == i){
									if(y == j){
										isPassType = false;
									}
								}else if(x == j){
									if(y == i){
										isPassType = false;
									}
								}
							}
						}
					}//红色路径判断完成
					edge = new Edge(adjWeightMatrix[i][j], isPassType,new Dot(i),new Dot(j));
					adjWeight[i][j] = edge;
				}
			}
		System.out.println("蚁房构建成功！");
		}
	}
	/*
	 * 通过文件路径读取创建结束
	 */
	
	
	public int getDotNumber() {
		return dotNumber;
	}
	public void setDotNumber(int dotNumber) {
		this.dotNumber = dotNumber;
	}
	public Edge[][] getAdjWeight() {
		return adjWeight;
	}
	public void setAdjWeight(Edge[][] adjWeight) {
		this.adjWeight = adjWeight;
	}
	public Dot[] getDotList() {
		return dotList;
	}
	public void setDotList(Dot[] dotList) {
		this.dotList = dotList;
	}

	public ArrayList<Dot> getMustPassDotList() {
		return mustPassDotList;
	}

	public void setMustPassDotList(ArrayList<Dot> mustPassDotList) {
		this.mustPassDotList = mustPassDotList;
	}

	public Dot getStartDot() {
		return startDot;
	}

	public void setStartDot(Dot startDot) {
		this.startDot = startDot;
	}

	public Dot getEndDot() {
		return endDot;
	}

	public void setEndDot(Dot endDot) {
		this.endDot = endDot;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	
	
	
}
