package examples;

import java.util.ArrayList;
import java.util.List;

public class Examples {
	private List<Example> exs;
	
	public Examples() {
		exs = new ArrayList<>();
	}
	
	public void addExample(Example ex) {
		exs.add(ex);
	}
	
	public List<Example> getExs(){
		return exs;
	}
}
