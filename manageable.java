import javax.swing.table.DefaultTableModel;

public interface manageable {
    void loadData (DefaultTableModel model);
    void saveData();
}
