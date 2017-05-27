package pick.moon;

import java.util.ArrayList;
import java.util.Map;

public class LimitTreeNode {

	//节点内容
	private Dot dateDot;
	//孩子节点集
	private ArrayList<LimitTreeNode> childrenDot;
	//记录父亲到孩子点上的权值（最少经过多少个点）
	private int leastDotNumber;
	//记录父亲到孩子最多能走多少个点
	private Map<String,Integer> mostDotNumber;
	//接下来的边还剩多少个结点可以走
	private int leaveStep;
	//备选孩子集,从上一个父亲的备选孩子集去掉自己
	private ArrayList<Dot> readyChildrenDot;
	//父节点
	private LimitTreeNode parentDot;
	//节点所在的层次
	private int level;
	//构建最短树时限制条件剩余的点数
	private int limitLeave;
	
	public LimitTreeNode(){
		this.limitLeave = -1;
	}
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public LimitTreeNode getParentDot() {
		return parentDot;
	}

	public void setParentDot(LimitTreeNode parentDot) {
		this.parentDot = parentDot;
	}

	public ArrayList<Dot> getReadyChildrenDot() {
		return readyChildrenDot;
	}

	public void setReadyChildrenDot(ArrayList<Dot> readyChildrenDot) {
		this.readyChildrenDot = readyChildrenDot;
	}

	public int getLeaveStep() {
		return leaveStep;
	}

	public void setLeaveStep(int leaveStep) {
		this.leaveStep = leaveStep;
	}

	

	public int getLeastDotNumber() {
		return leastDotNumber;
	}

	public void setLeastDotNumber(int leastDotNumber) {
		this.leastDotNumber = leastDotNumber;
	}

	

	public Map<String, Integer> getMostDotNumber() {
		return mostDotNumber;
	}

	public void setMostDotNumber(Map<String, Integer> mostDotNumber) {
		this.mostDotNumber = mostDotNumber;
	}

	public Dot getDateDot() {
		return dateDot;
	}

	public void setDateDot(Dot dateDot) {
		this.dateDot = dateDot;
	}

	public ArrayList<LimitTreeNode> getChildrenDot() {
		return childrenDot;
	}

	public void setChildrenDot(ArrayList<LimitTreeNode> childrenDot) {
		this.childrenDot = childrenDot;
	}

	public int getLimitLeave() {
		return limitLeave;
	}

	public void setLimitLeave(int limitLeave) {
		this.limitLeave = limitLeave;
	}
	 
	
}
