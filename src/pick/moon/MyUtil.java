package pick.moon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.regex.Pattern;

public class MyUtil {

	/*
	 * 判断读入的行变量
	 *  1、注释说明：注释用双斜杠起始注释必过点写第一行
	 *  2、必经点(食物和水果储存室)定义说明：必经点用mDot:+点+逗号例如: mDot:7,12
	 *  3、必经路径(有奖励的绿色路径)定义说明：必经路径mEdge:+点+逗号+空格+其他路径 例如: mEdge: 2,4 14,13
	 *  4、不能通过的路径(有奖励的绿色路径)定义说明：必经路径mNEdge:+点+逗号+空格+其他路径 例如: mNEdge: 2,4 14,13
	 *  5、邻接矩阵(蜂房)定义说明：用Graph:换行 不能达到的权值为-1，自身到自身定义为 0，权值用空格分开一个点占用一行
	 *  6、邻接矩阵(蜂房)定义说明：用GraphEnd结束
	 */
	public static int judge(String line){
		if(line != null && !line.equals("")){
			if(line.contains("//")){
				return 1;
			}
			if(line.contains("mDot")){
				return 2;
			}
			if(line.contains("mEdge")){
				return 3;
			}
			if(line.contains("mNEdge")){
				return 4;
			}
			if(line.contains("GraphStart")){
				return 5;
			}
			if(line.contains("GraphEnd")){
				return 6;
			}
			if(line.contains("startAndEnd")){
				return 7;
			}
			if(line.contains("step")){
				return 8;
			}
		}
		return 1;
	}

	/*
	 * 区间组合选择器，从List中选择所有为2的组合选择，选择不重复，如果是必过路径上的点不能进行拆分
	 */
	public static Dot[][] selectTwoNotReplay(final Graph graph){
		Dot[][] twoNotReplay = null;
		if(graph != null && graph.getMustPassDotList() != null && graph.getMustPassDotList().size() > 0){
			ArrayList<Dot> mustPassDotList = graph.getMustPassDotList();
			//必经点的个数
			int mustPassDotNumber = mustPassDotList.size();
			//加入起始点的最短大小
			int twoNotLength =  ((mustPassDotNumber+2)*(mustPassDotNumber+1))/2;
			twoNotReplay = new Dot[twoNotLength][2];
			//加入起始点开始计算
			Dot startDot = graph.getStartDot();
			Dot endDot = graph.getEndDot();
			//第一个位置存入起点到终点的路段
			twoNotReplay[0][0] = startDot;
			twoNotReplay[0][1] = endDot;
			//开始对数组的起点到必经点的路段进行添加
			for(int i = 1,j = 0; i < mustPassDotNumber+1 ; i ++,j++){
				twoNotReplay[i][0] = startDot;
				twoNotReplay[i][1] = mustPassDotList.get(j);
			}
			//开始对数组的始点到必经点的路段进行添加
			for(int i = 1 + mustPassDotNumber,j = 0; i < mustPassDotNumber*2+1 ; i ++,j++){
				twoNotReplay[i][0] = mustPassDotList.get(j);
				twoNotReplay[i][1] = endDot;
			}
			if(mustPassDotList != null && mustPassDotList.size() > 0){
				//计算分区间的二维数组行数
				int k = mustPassDotNumber*2+1;
				for(int i = 0 ; i < mustPassDotNumber - 1 ; i ++){
					for(int j = i+1 ; j < mustPassDotNumber ; j ++){
						twoNotReplay[k][0] = mustPassDotList.get(i);
						twoNotReplay[k++][1] = mustPassDotList.get(j);
					}
				}
			}
		}
		return twoNotReplay;
	}
	/*重载BFSForLeastDot方法，默认是从图中读取最多走多少步到达
	 * 利用广度优先求解点到点之间至少经过多少个点以及点与点之间的路径长度,如果大于给定的点个数则返回0，如果没超过则返回最少经过点的个数
	 * leastNumber 最少经过多少个点，startDotIndex起点序号  endDotIndex终点序号 graph 需要处理的图
	 */
	public static int BFSForLeastDot(int startDotIndex,int endDotIndex,final Graph graph){
		//最多经过多少个点到达
		int leastNumber = graph.getStep();
		
		return BFSForLeastDot(leastNumber,startDotIndex,endDotIndex,graph);
	}
	/*
	 * 利用广度优先求解点到点之间至少经过多少个点以及点与点之间的路径长度,如果大于给定的点个数则返回-1，如果没超过则返回最少经过点的个数 不包括起始点
	 * leastNumber 最少经过多少个点，startDotIndex起点序号  endDotIndex终点序号 graph 需要处理的图
	 */
	public static int BFSForLeastDot(int leastNumber,int startDotIndex,int endDotIndex,final Graph graph){

		int leastDot = 0;
		//取得图的邻接矩阵
		Edge [][]edgeArry = graph.getAdjWeight();
		//取得所有点
		//初始化节点没被访问
		Dot[] dotList = graph.getDotList();
		for(int i = 0 ; i < dotList.length ; i ++){
			Dot newD = dotList[i];
			newD.setVisited(false);
			dotList[i] = newD;
		}
		//存放层次遍历存放的点
		Queue<Dot> queueDot = new LinkedBlockingDeque<Dot>();
		
		//存放每个层最后一个元素
		ArrayList<Dot> hIndexArry = new ArrayList<Dot>();
		//把开始点加到队列
		Dot startDot = dotList[startDotIndex];
		startDot.setVisited(true);
		dotList[startDotIndex] = startDot;
		queueDot.add(startDot);
		//第一层为开始节点
		hIndexArry.add(startDot);
		while(!queueDot.isEmpty()){
			
			//printQueue(queueDot);
			Dot dot = queueDot.poll();
			//如果是这一层上最后一个元素那么层数加一，并且从层上移除掉
			boolean isLastDotThisH = false;
			for(Dot t : hIndexArry){
				if(dot.getIndex() == t.getIndex()){
					leastDot++;
					hIndexArry.remove(t);
					isLastDotThisH = true;
					break;
				}
			}
			Dot queueLast = null;
			for(int i = 0 ; i < edgeArry[dot.getIndex()].length ; i ++){
				if(edgeArry[dot.getIndex()][i].getTime() != -1 
						&& edgeArry[dot.getIndex()][i].getTime() != 0
						&& !dotList[i].isVisited()){
					//大于查找步骤则停止查询
					if(i == endDotIndex){
						if(!isLastDotThisH){
							return leastDot;
						}
						return leastDot-1;
					}else if(leastDot >= leastNumber){
						return -1;
					}else{
						Dot t = dotList[i];
						t.setVisited(true);
						dotList[i] = t;
						if(isLastDotThisH){
							queueLast = t;
						}
						
						queueDot.add(t);
					}
					
				}
			}
			if(queueLast != null){
				hIndexArry.add(queueLast);
			}
		}
		return leastDot-2;
	}
	
	/*
	 * 在点的限制条件下利用深度优先构建路径最少点树
	 * graph 图   twoNotReplay点到点之间的区间
	 */
	public static LimitTreeNode isExplainLimitDotNumber(final Graph graph){
		ArrayList<Dot> mustPassDotList = graph.getMustPassDotList();
		//必经点的个数
		int mustPassDotNumber = mustPassDotList.size();
		//路径上最多还能经过多少个点
		int leastStep = graph.getStep() - mustPassDotNumber - 2;
		int limitStep = BFSForLeastDot(graph.getStartDot().getIndex(),graph.getEndDot().getIndex(),graph);
		if(limitStep == 0){
			return null;
		}
		//利用深度优先构建路径最少点树
		Stack<LimitTreeNode> resultTreeStack = new Stack<LimitTreeNode>();
		//创建根节点
		LimitTreeNode rootLimitTreeNode = new LimitTreeNode();
		rootLimitTreeNode.setDateDot(graph.getStartDot());
		//根节点父亲到孩子的至少经过0个
		rootLimitTreeNode.setLeastDotNumber(0);
		//接下来的边还剩多少个结点可以走
		rootLimitTreeNode.setLeaveStep(leastStep);
		//初始将必经点加入到必选孩子集
		rootLimitTreeNode.setReadyChildrenDot(mustPassDotList);
		//设置父节点为空
		rootLimitTreeNode.setParentDot(null);
		//设置根节点的层次为1
		rootLimitTreeNode.setLevel(1);
		//存放一个点到另一个点最少点：最少点存放池
		Map leastDotNumberMap = new HashMap<String, Integer>();
		resultTreeStack.add(rootLimitTreeNode);
		while(!resultTreeStack.isEmpty()){
			LimitTreeNode limitTreeNode = resultTreeStack.pop();
			//取出准备子集
			ArrayList<Dot> OdlReadyChildrenDotList = limitTreeNode.getReadyChildrenDot();
			int readyChildrenDotSize = OdlReadyChildrenDotList.size();
			ArrayList<Dot> readyChildrenDotList = new ArrayList<Dot>(readyChildrenDotSize);
			for(Dot t : OdlReadyChildrenDotList){
				Dot newT = new Dot();
				newT.setBrotherDot(t.getBrotherDot());
				newT.setFood(t.isFood());
				newT.setIndex(t.getIndex());
				newT.setLabel(t.getLabel());
				newT.setVisited(t.isVisited());
				newT.setPassEdgeDot(t.isPassEdgeDot());
				readyChildrenDotList.add(newT);
			}
			//从必选孩子集中移除自己
			for(int i = 0 ; i < readyChildrenDotSize ; i ++){
				if(limitTreeNode.getDateDot().getIndex() == readyChildrenDotList.get(i).getIndex()){
					readyChildrenDotList.remove(i);
					break;
				}
			}
			//判断是否有可以走到底
			boolean isAll = false;
			//移除自己后还有的话就尝试根据条件查找 深度搜索
			if(readyChildrenDotList.size() != 0){
				ArrayList<LimitTreeNode> limitTreeNodeArray = new ArrayList<LimitTreeNode>();
				//待选子集里面有没有必过路径点
				boolean havePassEdge = false;
				LimitTreeNode passEdgeDot =  new LimitTreeNode();
				for(Dot dot : readyChildrenDotList){
					int leaveStep = limitTreeNode.getLeaveStep();
					//如果子集中含必过点，并且当前也是必过点
					if(dot.isPassEdgeDot() && limitTreeNode.getDateDot().isPassEdgeDot() && limitTreeNode.getDateDot().getBrotherDot() == dot.getIndex()){
						havePassEdge = true;
						passEdgeDot.setParentDot(limitTreeNode);
						passEdgeDot.setDateDot(dot);
						passEdgeDot.setLevel(limitTreeNode.getLevel() + 1);
						passEdgeDot.setLeastDotNumber(0);
						passEdgeDot.setLeaveStep(leaveStep);
						passEdgeDot.setReadyChildrenDot(readyChildrenDotList);
					}
					int rootToChildStep = 0;
					//开始从最少点存放池中取出
					Integer exist= (Integer) leastDotNumberMap.get(limitTreeNode.getDateDot().getIndex() + ","+ dot.getIndex());
					if(exist == null){
						exist= (Integer) leastDotNumberMap.get(dot.getIndex() + ","+ limitTreeNode.getDateDot().getIndex());
					}
					if(exist == null){
						//如果存放池中没有就进行查找
						rootToChildStep = BFSForLeastDot(graph.getStep(),limitTreeNode.getDateDot().getIndex(),dot.getIndex(),graph);
					}else{
						rootToChildStep = exist;
					}
					//如果还在限制步骤内开始加入到孩子节点
					if(rootToChildStep != -1 && rootToChildStep <= leaveStep){
						//将已查找到的最少节点步骤加入到最少节点池
						leastDotNumberMap.put(limitTreeNode.getDateDot().getIndex() + ","+ dot.getIndex(), rootToChildStep);
						//说明有个可以继续往下走
						isAll = true;
						LimitTreeNode childrenLimitTreeNode = new LimitTreeNode();
						childrenLimitTreeNode.setParentDot(limitTreeNode);
						childrenLimitTreeNode.setDateDot(dot);
						childrenLimitTreeNode.setLevel(limitTreeNode.getLevel() + 1);
						childrenLimitTreeNode.setLeastDotNumber(rootToChildStep);
						childrenLimitTreeNode.setLeaveStep(leaveStep-rootToChildStep);
						childrenLimitTreeNode.setReadyChildrenDot(readyChildrenDotList);
						limitTreeNodeArray.add(childrenLimitTreeNode);
//						resultTreeStack.add(childrenLimitTreeNode);
					}
				}
				
				//1、如果当前点是必过路径上的点，父亲还不是必过路径上的点，那下一条路径必定是必过路径上的点
				if(havePassEdge && limitTreeNode.getParentDot() != null && !limitTreeNode.getParentDot().getDateDot().isPassEdgeDot() ||
						havePassEdge && limitTreeNode.getParentDot() != null && limitTreeNode.getParentDot().getDateDot().isPassEdgeDot() && limitTreeNode.getParentDot().getParentDot() != null
						&& limitTreeNode.getParentDot().getParentDot().getDateDot().isPassEdgeDot()){
					ArrayList<LimitTreeNode> passEdgeDotArray = new ArrayList<LimitTreeNode>();
					passEdgeDotArray.add(passEdgeDot);
					resultTreeStack.add(passEdgeDot);
					limitTreeNode.setChildrenDot(passEdgeDotArray);
				}else{
					for(LimitTreeNode child : limitTreeNodeArray){
						resultTreeStack.add(child);
					}
					limitTreeNode.setChildrenDot(limitTreeNodeArray);
				}
				if(!isAll){
					//如果没有继续进行下去的子树
					reMoveChildParent(limitTreeNode,mustPassDotNumber);
				}
			}else{
				//测试开始
//				if(limitTreeNode.getDateDot().getIndex() == 7 && limitTreeNode.getChildrenDot() == null){
//					System.out.println();
//				}
				//测试结束
				//为树的底节点加一个标识
				int toEndDot = BFSForLeastDot(graph.getStep(),limitTreeNode.getDateDot().getIndex(),graph.getEndDot().getIndex(),graph);
				
				//如果最后一个节点到介绍节点大于剩余的步骤时移除掉
				if(limitTreeNode.getLeaveStep() < toEndDot){
					reMoveChildParent(limitTreeNode,mustPassDotNumber);
					
				}else{
					//如果到的话就将最后一个节点加入到最后面
					ArrayList<LimitTreeNode> endDotTreeNodeArray = new ArrayList<LimitTreeNode>();
					int lastLeaveStep = limitTreeNode.getLeaveStep() - toEndDot;
					LimitTreeNode endDotTreeNode = new LimitTreeNode();
					endDotTreeNode.setParentDot(limitTreeNode);
					endDotTreeNode.setDateDot(graph.getEndDot());
					endDotTreeNode.setLeastDotNumber(toEndDot);
					endDotTreeNode.setLeaveStep(lastLeaveStep);
					endDotTreeNode.setReadyChildrenDot(readyChildrenDotList);
					endDotTreeNodeArray.add(endDotTreeNode);
					Map<String,Integer> parentNameToEndDotMap = new HashMap<String, Integer>();
					String parentNameToEndDot = limitTreeNode.getDateDot().getIndex() + "," + endDotTreeNode.getDateDot().getIndex();
					parentNameToEndDotMap.put(parentNameToEndDot, lastLeaveStep);
					limitTreeNode.setChildrenDot(endDotTreeNodeArray);
					//开始将父亲的盗孩子的最多能走多少个点开始赋值
					LimitTreeNode parentNode = limitTreeNode.getParentDot();
					LimitTreeNode childeNode = limitTreeNode;
					while(parentNode != null){
						Map<String,Integer> parentToChildMostDotMap = parentNode.getMostDotNumber();
						if(parentToChildMostDotMap == null){
							parentToChildMostDotMap = new HashMap<String, Integer>();
						}
						String parentNameToChildDot = parentNode.getDateDot().getIndex() + "," + childeNode.getDateDot().getIndex();
						parentToChildMostDotMap.put(parentNameToChildDot, childeNode.getLeastDotNumber() + lastLeaveStep);
						parentNode.setMostDotNumber(parentToChildMostDotMap);
						childeNode = parentNode;
						parentNode = parentNode.getParentDot();
					}
				}
			}
			
			
		}
		
		return rootLimitTreeNode;
	}
	
	//从父亲移除孩子中移除某个点
	public static void reMoveChildParent(LimitTreeNode limitTreeNode,int mustPassDotNumber){
//		if(limitTreeNode.getDateDot().getIndex() == 2){
//			System.out.println();
//		}
		
		if(limitTreeNode.getLevel() < mustPassDotNumber+2){
			limitTreeNode.setReadyChildrenDot(new ArrayList<Dot>());
			LimitTreeNode reMoveChildParent = limitTreeNode.getParentDot();
			while(reMoveChildParent != null){
				
				ArrayList<LimitTreeNode> childrenArray = reMoveChildParent.getChildrenDot();
				for(LimitTreeNode limitDot : childrenArray){
					if(limitTreeNode.getDateDot().getIndex() == limitDot.getDateDot().getIndex()){
						childrenArray.remove(limitDot);
						break;
					}
				}
				if(childrenArray.size() == 0){
					limitTreeNode = reMoveChildParent;
					reMoveChildParent = reMoveChildParent.getParentDot();
					
				}else{
					reMoveChildParent.setChildrenDot(childrenArray);
					break;
				}
				
			}
		}
	}
	
	//利用深度优先查找给定固定的点查找最短的路径
	public static ArrayList<ShortPathForTwo> getShortPathForNode(Dot startDot,Dot endDot,int dotStep,Graph graph){
		ArrayList<ShortPathForTwo> shortNodeArrayList = new ArrayList<ShortPathForTwo>();
		ShortPathForTwo tempShortNode = null;
		ArrayList<ShortPathForTwo> tempShortNodeArray = new ArrayList<ShortPathForTwo>();
		if(startDot != null && endDot != null && graph != null){
			//取出图的邻居矩阵
			Edge[][] edge = graph.getAdjWeight();
			Edge[][] myEdgeArry = new Edge[edge.length][edge.length];
			for(int i = 0 ; i < edge.length;i++){
				for(int j = 0 ; j < edge[i].length ; j ++){
					Edge edgeArry = new Edge(edge[i][j].getTime());
					myEdgeArry[i][j] = edgeArry;
				}
			}
			//存放取出的边
			Stack<Edge> resultTreeStack = new Stack<Edge>();
			//取出图中的所有点
			Dot []dotList = graph.getDotList();
			int startX = startDot.getIndex();
			//把和开始节点相连的边加入到栈中
			for(int i= 0 ; i < myEdgeArry.length ; i ++){
				Edge startEdge = myEdgeArry[startX][i];
				if(startEdge.getTime() > 0){
					//在这条路径上存放临时点
					myEdgeArry[startX][i].setHaveCome(true);
					Edge tempDge = new Edge(startEdge.getTime());
					tempDge.setPreEdge(null);
					tempDge.setLevel(1);
					tempDge.setStartDot(new Dot(dotList[startX].getLabel(),dotList[startX].getIndex(),dotList[startX].isFood(),dotList[startX].isPassEdgeDot()));
					tempDge.setEndDot(new Dot(dotList[i].getLabel(),dotList[i].getIndex(),dotList[i].isFood(),dotList[i].isPassEdgeDot()));
					resultTreeStack.add(tempDge);
				}
			}
			ShortPathForTwo shortNode = null;
			
			while(!resultTreeStack.isEmpty()){
				Edge popEdge = resultTreeStack.pop();
				//存路径
				if(popEdge.getLevel() == 1){
					shortNode = new ShortPathForTwo();
					shortNode.setEdgeList(new ArrayList<Edge>());
				}
				//得到下一个点的下标
				Dot nextDot = popEdge.getEndDot();
				
				int nextDotIndex = nextDot.getIndex();
				
				if(nextDot.getIndex() != endDot.getIndex() && popEdge.getLevel() <= dotStep
						){
					//如果下一步还没有超过规定步骤而且又不是终点
					for(int i = 0 ; i < myEdgeArry.length ; i ++){
						Edge startEdge = myEdgeArry[nextDot.getIndex()][i];
						if(startEdge.getTime() > 0 && !startEdge.isHaveCome() || startEdge.getTime() > 0 && startEdge.isHaveCome() 
								&& startEdge.getLevel() < popEdge.getLevel()){
							startEdge.setHaveCome(true);
							//取出上一个路径加上这条能走的路径的值
							shortNode.setPreEdgeValue(popEdge.getTime());
							//在这条路径上存放临时点
							shortNode.getEdgeList().add(popEdge);
							Edge tempEdge = new Edge(startEdge.getTime());
							tempEdge.setLevel(popEdge.getLevel() + 1);
							tempEdge.setStartDot(new Dot(dotList[nextDotIndex].getLabel(),dotList[nextDotIndex].getIndex(),dotList[nextDotIndex].isFood(),dotList[nextDotIndex].isPassEdgeDot()));
							tempEdge.setEndDot(new Dot(dotList[i].getLabel(),dotList[i].getIndex(),dotList[i].isFood(),dotList[i].isPassEdgeDot()));
							//设置路径的前一条边
							tempEdge.setPreEdge(popEdge);
							resultTreeStack.add(tempEdge);
						}
					}
				}else {
					if(nextDot.getIndex() == endDot.getIndex() && popEdge.getLevel() == dotStep+1){
					//取出上一个路径加上这条能走的路径的值
					shortNode.getEdgeList().add(popEdge);
					//存储路径以及路径上的值
					ShortPathForTwo nShortNode = new ShortPathForTwo();
					
					ArrayList<Edge> nEdgeList = new ArrayList<Edge>();
					nEdgeList.add(popEdge);
					int value = popEdge.getTime();
					//取上一条边
					Edge preEdge = popEdge.getPreEdge();
					
					while(preEdge != null){
						nEdgeList.add(preEdge);
						value += preEdge.getTime();
						//if(preEdge.getStartDot() != null && preEdge.getEndDot() != null)
						//	System.out.println("开始" + preEdge.getStartDot().getIndex() + "结束" + preEdge.getEndDot().getIndex());
						preEdge = preEdge.getPreEdge();
						
					}
					nShortNode.setShortValue(value);
					nShortNode.setEdgeList(nEdgeList);
					shortNodeArrayList.add(nShortNode);
					}
					popEdge.setLevel(0);
					if(shortNode != null && shortNode.getEdgeList() != null && shortNode.getEdgeList().size() > 0){
						shortNode.getEdgeList().remove(shortNode.getEdgeList().size() -1);
					}
				}
			}
		}
		
		//查找最小路径
		if(shortNodeArrayList.size() > 0){
			tempShortNode = shortNodeArrayList.get(0);
			for(int i = 0 ; i < shortNodeArrayList.size() ; i ++){
				if(tempShortNode.getShortValue() >= shortNodeArrayList.get(i).getShortValue()){
					tempShortNodeArray.add(tempShortNode);
					tempShortNode = shortNodeArrayList.get(i);
				}
			}
		}
		
		return tempShortNodeArray;
	}
	
	//求一棵树，给定边的多经过多少个点，最少经过多少个点的最短路径
	//root为给定边的多经过多少个点，最少经过多少个点的最短路径
	public static LastShortPath lastPathForGrap(LimitTreeNode root,Graph graph){
		LastShortPath  tempShortPath = null;
		LastShortPath minShortPath = null;
		//给定点最短路径数
		ShortPathNode shortPathRoot = new ShortPathNode();
		//存储开始到结束经过必过的可行的路径
		if(root != null){
			shortPathRoot.setParentShortPathNode(null);
			shortPathRoot.setLimitTreeNode(root);
			shortPathRoot.setLeveStep(-1);
			
			Stack<ShortPathNode> shortPathRootStack = new Stack<ShortPathNode>();
			
			//设置最短路径根节点父亲为空
			shortPathRoot.setParentShortPathNode(null);
			shortPathRoot.setUseDotNumber(0);
			shortPathRootStack.add(shortPathRoot);
			
			while(!shortPathRootStack.isEmpty()){
				ShortPathNode popShortPathNode = shortPathRootStack.pop();
				//取得限制条件
				LimitTreeNode popLimitTreeNode = popShortPathNode.getLimitTreeNode();
				//取得孩子限制条件点
				ArrayList<LimitTreeNode> limitTreeChildList = popLimitTreeNode.getChildrenDot();
				//ShortPathNod的孩子结点
				ArrayList<ShortPathNode> childShortNodeList = new ArrayList<ShortPathNode>();
				for(int i = 0 ; limitTreeChildList != null && i <  limitTreeChildList.size() ; i ++){
					LimitTreeNode childLimitTreeNode = limitTreeChildList.get(i);
					//取得限制通过结点个数
					int least = childLimitTreeNode.getLeastDotNumber();
					int mostStep = 0;
					if(popLimitTreeNode.getLimitLeave() == -1){
						 mostStep = popLimitTreeNode.getMostDotNumber().get(popLimitTreeNode.getDateDot().getIndex() + "," + childLimitTreeNode.getDateDot().getIndex());
					}else{
						 mostStep = least + popLimitTreeNode.getLimitLeave();
					}
					for(int step = least ; step <= mostStep ; step ++){
						ShortPathNode childShortNode = new ShortPathNode();
						//设置父亲点
						childShortNode.setParentShortPathNode(popShortPathNode);
						//设置孩子限制点
						childLimitTreeNode.setLimitLeave(mostStep-step);
						childShortNode.setLimitTreeNode(childLimitTreeNode);
						//看孩子是不是必过结点
						//1、如果是必过路径上的点
						if(childLimitTreeNode.getDateDot().isPassEdgeDot() && childLimitTreeNode.getParentDot() != null 
								&& childLimitTreeNode.getParentDot().getDateDot().isPassEdgeDot() && childLimitTreeNode.getParentDot().getDateDot().getBrotherDot() == childLimitTreeNode.getDateDot().getIndex()){
							ArrayList<ShortPathForTwo> shortPathForTwo = getShortPathForNode(popLimitTreeNode.getDateDot(),childLimitTreeNode.getDateDot(),0,graph);
							childShortNode.setShortPathEdge(shortPathForTwo);
							childShortNode.setUseDotNumber(0);
							childShortNode.setLeveStep(mostStep-least);
						}else{
							//2、如果不是是必过路径上的点
							ArrayList<ShortPathForTwo> shortPathForTwo = getShortPathForNode(popLimitTreeNode.getDateDot(),childLimitTreeNode.getDateDot(),step,graph);
							childShortNode.setShortPathEdge(shortPathForTwo);
							childShortNode.setUseDotNumber(step);
							childShortNode.setLeveStep(mostStep-least);
						}
						//到结束节点
						if(childLimitTreeNode.getDateDot().getIndex() == graph.getEndDot().getIndex()){
							if(tempShortPath == null){
								tempShortPath = new LastShortPath();
								tempShortPath.setPathValue(-1);
							}
							int pathValue = 0;
							if(childShortNode.getShortPathEdge() != null && childShortNode.getShortPathEdge().size() > 0){
								//路径距离
								pathValue = childShortNode.getShortPathEdge().get(0).getShortValue();
							}
							ShortPathNode parentShortPathNode = childShortNode.getParentShortPathNode();
							ArrayList<ArrayList<ShortPathForTwo>> shortPathForTwo = new ArrayList<ArrayList<ShortPathForTwo>>();
							while(parentShortPathNode != null && parentShortPathNode.getShortPathEdge() != null && parentShortPathNode.getShortPathEdge().size() > 0){
								pathValue += parentShortPathNode.getShortPathEdge().get(0).getShortValue();
								shortPathForTwo.add(parentShortPathNode.getShortPathEdge());
								parentShortPathNode = parentShortPathNode.getParentShortPathNode();
							}
							tempShortPath.setShortPathEdge(shortPathForTwo);
							tempShortPath.setPathValue(pathValue);
							if(minShortPath == null || tempShortPath.getPathValue() < minShortPath.getPathValue()){
								minShortPath = tempShortPath;
							}
						}
						//将孩子加到孩子集
						childShortNodeList.add(childShortNode);
						//入队
						shortPathRootStack.add(childShortNode);
					}
				}
				//设置孩子节点
				popShortPathNode.setChildShortPathNode(childShortNodeList);
			}
		}
		
		return minShortPath;
	}
}


