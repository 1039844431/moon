package pick.moon;

/*
 * 存放点的
 */
public class Dot {
	
	//点的名称
	private String label;
	//点的序号
	private int index;
	//是否是必过线的点
	private boolean isPassEdgeDot;
	//如果是必过路径上的点设置兄弟点的index
	private int brotherDot;
	//是否是食物点
	private boolean isFood;
	//广度优先是查看是否已经访问过点
	private boolean visited;
	public Dot(int index,String label,boolean isFood){
		this.label = label;
		this.isFood = isFood;
		this.isPassEdgeDot = false;
		this.index = index;
		this.visited = false;
	}
	
	public Dot(String label,boolean isFood,boolean isPassEdgeDot){
		this.label = label;
		this.isFood = isFood;
		this.isPassEdgeDot = isPassEdgeDot;
		this.visited = false;
	}
	public Dot(String label,int index,boolean isFood,boolean isPassEdgeDot){
		this.label = label;
		this.isFood = isFood;
		this.isPassEdgeDot = isPassEdgeDot;
		this.index = index;
	}
	public Dot(int index){
		this.index = index;
	}
	public Dot(){}

	public boolean isPassEdgeDot() {
		return isPassEdgeDot;
	}



	public void setPassEdgeDot(boolean isPassEdgeDot) {
		this.isPassEdgeDot = isPassEdgeDot;
	}



	public String getLabel() {
		return label;
	}



	public void setLabel(String label) {
		this.label = label;
	}






	public boolean isFood() {
		return isFood;
	}



	public void setFood(boolean isFood) {
		this.isFood = isFood;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getBrotherDot() {
		return brotherDot;
	}

	public void setBrotherDot(int brotherDot) {
		this.brotherDot = brotherDot;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	
	
}
