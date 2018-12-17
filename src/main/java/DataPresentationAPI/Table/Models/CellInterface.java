package DataPresentationAPI.Table.Models;

public interface CellInterface {
	
	public void set(Integer columnIndex, Object data);
	
	public Object get(Integer columnIndex);

}
