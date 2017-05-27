package pick.moon;
/*
 * 边实体
 */
public class Edge {
	//蚂蚁走过这条路径需要花费的时间
	private int time;
	//边的访问次数
	private int visitNumber;
	//边是否能访问,默认是true，true为可以访问，false不能访问
	private boolean visited;
	//连接的开始点
	private Dot startDot;
	//连接的结束点
	private Dot endDot;
	//深度优先计算第几步经历的点
	private int level;
	//是否已经访问过
	private boolean haveCome;
	//前面一条边
	private Edge preEdge;
	/*
	 * time表示路径需要花费时间，ispass表示是否必须通过
	 */
	
	public Edge(int time) {
		this.time = time;
		this.visitNumber = 0;
		this.visited = true;
		this.haveCome = false;
	}

	public Edge(int time,boolean visited,Dot startDot,Dot endDot) {
		this.time = time;
		this.visited = visited;
		this.visitNumber = 0;
		this.startDot = startDot;
		this.endDot = endDot;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}

	public int getVisitNumber() {
		return visitNumber;
	}

	public void setVisitNumber(int visitNumber) {
		this.visitNumber = visitNumber;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isHaveCome() {
		return haveCome;
	}

	public void setHaveCome(boolean haveCome) {
		this.haveCome = haveCome;
	}

	public Edge getPreEdge() {
		return preEdge;
	}

	public void setPreEdge(Edge preEdge) {
		this.preEdge = preEdge;
	}
	
}
