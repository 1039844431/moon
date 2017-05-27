package pick.moon;

import java.util.ArrayList;

public class LastShortPath {
	private ArrayList<ArrayList<ShortPathForTwo>> shortPathEdge;
	private int pathValue;
	
	public ArrayList<ArrayList<ShortPathForTwo>> getShortPathEdge() {
		return shortPathEdge;
	}
	public void setShortPathEdge(ArrayList<ArrayList<ShortPathForTwo>> shortPathEdge) {
		this.shortPathEdge = shortPathEdge;
	}
	public int getPathValue() {
		return pathValue;
	}
	public void setPathValue(int pathValue) {
		this.pathValue = pathValue;
	}
	
}
