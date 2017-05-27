package pick.moon;

import java.util.ArrayList;

public class ShortPathNode {
	//两个点给定点过的的路径
	private ArrayList<ShortPathForTwo> shortPathEdge;
	//路径上最短值
	private int pathValue;
	//父亲到孩子使用了多少个剩余点
	private int useDotNumber;
	//父亲
	private ShortPathNode parentShortPathNode;
	//孩子结点
	private ArrayList<ShortPathNode> childShortPathNode;
	//剩余多少个结点可以用
	private int leveStep;
	//转换树
	private LimitTreeNode limitTreeNode;
	public ArrayList<ShortPathForTwo> getShortPathEdge() {
		return shortPathEdge;
	}
	public void setShortPathEdge(ArrayList<ShortPathForTwo> shortPathEdge) {
		this.shortPathEdge = shortPathEdge;
	}
	public int getPathValue() {
		return pathValue;
	}
	public void setPathValue(int pathValue) {
		this.pathValue = pathValue;
	}
	public ShortPathNode getParentShortPathNode() {
		return parentShortPathNode;
	}
	public void setParentShortPathNode(ShortPathNode parentShortPathNode) {
		this.parentShortPathNode = parentShortPathNode;
	}
	public int getUseDotNumber() {
		return useDotNumber;
	}
	public void setUseDotNumber(int useDotNumber) {
		this.useDotNumber = useDotNumber;
	}
	public ArrayList<ShortPathNode> getChildShortPathNode() {
		return childShortPathNode;
	}
	public void setChildShortPathNode(ArrayList<ShortPathNode> childShortPathNode) {
		this.childShortPathNode = childShortPathNode;
	}
	public int getLeveStep() {
		return leveStep;
	}
	public void setLeveStep(int leveStep) {
		this.leveStep = leveStep;
	}
	public LimitTreeNode getLimitTreeNode() {
		return limitTreeNode;
	}
	public void setLimitTreeNode(LimitTreeNode limitTreeNode) {
		this.limitTreeNode = limitTreeNode;
	}
	
	
}
