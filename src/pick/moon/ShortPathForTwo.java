package pick.moon;

import java.util.ArrayList;

public class ShortPathForTwo {
	private Dot startDot;
	private Dot endDot;
	private int shortValue;
	private int preEdgeValue;
	
	private ArrayList<Edge> edgeList;
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
	public ArrayList<Edge> getEdgeList() {
		return edgeList;
	}
	public void setEdgeList(ArrayList<Edge> edgeList) {
		this.edgeList = edgeList;
	}
	public int getShortValue() {
		return shortValue;
	}
	public void setShortValue(int shortValue) {
		this.shortValue = shortValue;
	}
	public int getPreEdgeValue() {
		return preEdgeValue;
	}
	public void setPreEdgeValue(int preEdgeValue) {
		this.preEdgeValue = preEdgeValue;
	}
	
	
	
	
}
