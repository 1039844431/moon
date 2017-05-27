package pick.moon;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class ShortMain {
	
	
	
	
	
	
	public static void main(String [] args){
		String rootPath=ShortMain.class.getResource("/").getFile().toString() + "pick/moon";
		String  filePath = rootPath + "/test.txt";
		Graph graph = new Graph(filePath);
		System.out.println("----------蚂蚁开始就绪，开始思考-----------");
		System.out.println("----------蚂蚁开始分工尝试---------------");
		LimitTreeNode root = MyUtil.isExplainLimitDotNumber(graph);
		if(root == null || root.getChildrenDot().size() == 0){
			System.out.println("该图在满足最多通过 "+graph.getStep()+" 的限制条件下无解！");
			System.out.println("请输入确定是否寻找满足最少通过多少个点有解情况：1、确定。2、结束");
			Scanner sc = new Scanner(System.in);
			int isContinue = sc.nextInt();
			if(isContinue == 1){
				for(int i = graph.getStep() +1 ; ;i++){
					graph.setStep(i);
					root = MyUtil.isExplainLimitDotNumber(graph);
					if(root != null && root.getChildrenDot().size() > 0){
						System.out.println("该图至少需要通过：" + i + " 才能符合条件，到达结束节点");
						break;
					}
				}
			}else{
				return;
			}
			
		}
		System.out.println("寻找出的路径为：");
		LastShortPath shortPathNode = MyUtil.lastPathForGrap(root,graph);
		ArrayList<ArrayList<ShortPathForTwo>> path = shortPathNode.getShortPathEdge();
		for(int i = path.size() - 1 ; i >=0 ; i -- ){
			ArrayList<ShortPathForTwo> onePath = path.get(i);
			StringBuffer strPath = new StringBuffer();
			for(int j = 0 ; j < onePath.size() ; j ++){
				ShortPathForTwo oneMustToOtherMust = onePath.get(j);
				
				ArrayList<Edge>  edge = oneMustToOtherMust.getEdgeList();
				StringBuffer oneMustToOtherMustBf = new StringBuffer();
				for(int c = edge.size() - 1 ; c >= 0 ; c--){
					oneMustToOtherMustBf.append(edge.get(c).getStartDot().getIndex() + "-->");
					if(i==0 && c== 0){
						oneMustToOtherMustBf.append(edge.get(c).getEndDot().getIndex() + "-->");
					}
				}
				strPath.append(oneMustToOtherMustBf);
				System.out.print(strPath);
			}
			
		}
		
		System.out.println(graph.getEndDot().getIndex());
		System.out.println("最短路径长度为：" + shortPathNode.getPathValue());
		//对区间进行划分
//		Dot [][]twoNotReplay = MyUtil.selectTwoNotReplay(graph);
		
//		LimitTreeNode root = MyUtil.isExplainLimitDotNumber(graph);
//		ArrayList<ShortPathForTwo> shortNodeArray = MyUtil.getShortPathForNode(new Dot(2),new Dot(4),0,graph);
//		if(shortNodeArray != null && shortNodeArray.size() > 0){
//			int path = shortNodeArray.size();
//			System.out.println("共有：" + path + "条！");
//			for(int i = 0 ; i < path ; i ++){
//				int c = i+1;
//				System.out.println("第 " + c + "条：");
//				ShortPathForTwo s = shortNodeArray.get(i);
//				ArrayList<Edge> eArray = s.getEdgeList();
//				for(int j = eArray.size() - 1 ; j >= 0 ; j --){
//					System.out.print(eArray.get(j).getStartDot().getIndex() + "-->");
//				}
//				System.out.println(eArray.get(0).getEndDot().getIndex());
//			}
//			
//		}
//		System.out.println();
//		System.out.println(MyUtil.BFSForLeastDot(graph.getStep(),0,7,graph));
//		for(int i = 0 ; i < twoNotReplay.length ; i ++){
//			
//				System.out.println(twoNotReplay[i][0].getIndex() +"--"+twoNotReplay[i][1].getIndex());
//			
//		}
//		
		//System.out.println(graph.getDotList().length - graph.getMustPassDotList().size());
	}

	
}
	