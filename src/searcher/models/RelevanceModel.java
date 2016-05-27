package searcher.models;


import javafx.beans.property.SimpleStringProperty;

public class RelevanceModel {
    private final SimpleStringProperty id_orgn = new SimpleStringProperty("");
    private final SimpleStringProperty value_orgn = new SimpleStringProperty("");
    private final SimpleStringProperty id_dest = new SimpleStringProperty("");
    private final SimpleStringProperty value_dest = new SimpleStringProperty("");

    public RelevanceModel(){
        this("", "", "", "");
    }

    public RelevanceModel(String id_orgn, String value_orgn, String id_dest, String value_dest){
        this.id_orgn.set(id_orgn);
        this.value_orgn.set(value_orgn);
        this.id_dest.set(id_dest);
        this.value_dest.set(value_dest);
    }

}
