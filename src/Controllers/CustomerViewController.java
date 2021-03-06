package Controllers;

import Models.Customer;
import Utilities.DBUtility;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class CustomerViewController implements Initializable {


    @FXML
    private TableView<Customer> tableView;

    @FXML
    private TableColumn<Customer, String> firstNameColumn;

    @FXML
    private TableColumn<Customer, String> lastNameColumn;

    @FXML
    private TableColumn<Customer, Integer> ageColumn;

    @FXML
    private TableColumn<Customer, String> genderColumn;

    @FXML
    private TableColumn<Customer, String> provinceColumn;

    @FXML
    private TableColumn<Customer, String> bloodTypeColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private Label rowSelectedLabel;

    @FXML
    private Label malePercentLabel;

    @FXML
    private Label femalePercentLabel;

    @FXML
    private Label averageAgeLabel;

    @FXML
    private CheckBox abCheckBox;

    @FXML
    private CheckBox mbCheckBox;

    @FXML
    private CheckBox bcCheckBox;

    @FXML
    private CheckBox nbCheckBox;

    @FXML
    private CheckBox nlCheckBox;

    @FXML
    private CheckBox nsCheckBox;

    @FXML
    private CheckBox ntCheckBox;

    @FXML
    private CheckBox onCheckBox;

    @FXML
    private CheckBox qcCheckBox;

    @FXML
    private CheckBox skCheckBox;

    private ArrayList<Customer> allCustomers;
    private ArrayList<CheckBox> checkBoxes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allCustomers = DBUtility.getCustomers();
        //This set's up the checkbox objects to filter by province
        //and puts them all in an ArrayList<CheckBox> called
        //checkBoxes.  This way you can loop over the CheckBox objects
        configureCheckBoxes();

        //configure the table columns
        configureTableColumns();

        //add a listener to the search TextField.  When ever there is a change in the
        //TextField, it will call the method setNameSearchTextField() - add your filtering
        //code in there
        searchTextField.textProperty().addListener((obs, oldValue, searchString)->
                search(searchString));
        updateLabels();
    }


    private void updateLabels() {
        rowSelectedLabel.setText("Rows Returned: " + tableView.getItems().size());
        malePercentLabel.setText(String.format("Male: %.1f%%", getGenderPercent("male")));
        femalePercentLabel.setText(String.format("Female: %.1f%%", getGenderPercent("female")));
        averageAgeLabel.setText(getAverageAge());
    }

    private String getAverageAge()
    {
        OptionalDouble avgAge = tableView.getItems().stream()  //stream of Customer objects
                            .mapToDouble(Customer::getAge)  //converts the stream to hold Double objects
                            .average();

        if (avgAge.isPresent())
            return String.format("Avg Age: %.1f", avgAge.getAsDouble());
        else
            return "Average Age: Not Applicable";
    }

    private double getGenderPercent(String gender)
    {
        long count = tableView.getItems()
                    .stream()
                    .filter(customer -> customer.getGender().equalsIgnoreCase(gender))
                    .count();
        return (double) count / tableView.getItems().size() * 100;
    }


    /**
     * This method adds the checkboxes to an ArrayList
     * and set's their initial state to selected
     */
    private void configureCheckBoxes()
    {
        //Add the CheckBox object to an ArrayList
        checkBoxes = new ArrayList<>();
        checkBoxes.addAll(Arrays.asList(abCheckBox,bcCheckBox,mbCheckBox,
                nbCheckBox,nlCheckBox,nsCheckBox,ntCheckBox,onCheckBox,qcCheckBox,skCheckBox));
    }


    /**
     * This method configures the table columns with value factories
     */
    private void configureTableColumns()
    {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        provinceColumn.setCellValueFactory(new PropertyValueFactory<>("province"));
        bloodTypeColumn.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
        tableView.getItems().addAll(allCustomers);
    }

    /**
     * This method is called whenever someone enters or removes a character from the search
     * textField
     * @param searchString
     */
    @FXML
    private void search(String searchString){
        List<Customer> filtered = allCustomers.stream()
                                            .filter(customer -> customer.getFirstName()
                                                            .contains(searchString))
                                            .collect(Collectors.toList());
        tableView.getItems().clear();
        tableView.getItems().addAll(filtered);
        updateLabels();
    }

    /**
     * This method is called whenever one of the checkboxes is selected or
     * deselected.  The TableView's ObservableList should be updated to match
     * the provinces selected.  Be sure to update all the labels for
     * male %, female % and average Age
     */
    @FXML private void checkBoxChanged()
    {

    }
}

