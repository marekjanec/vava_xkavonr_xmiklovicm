package controllers;

import database.Company;
import database.Person;
import database.ProgramData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import managers.ManagerCompany;
import managers.ManagerPerson;

import java.util.Locale;
import java.util.ResourceBundle;


public class CompanyMainWindowController implements Controller {
    Stage primaryStage;
    Parent root;

    @FXML
    Label nameLabel;
    @FXML
    Label streetLabel;
    @FXML
    Label cityLabel;
    @FXML
    Label countryLabel;
    @FXML
    Label postalCodeLabel;
    @FXML
    Label mailLabel;
    @FXML
    Label phoneNumberLabel;
    @FXML
    Hyperlink signOutHyperlink;
    @FXML
    Button modifyButton;
    @FXML
    Label roomLabel;
    @FXML
    TableView usersTable;
    @FXML
    TextField searchTextField;
    @FXML
    Button changePasswordButton;

    @Override
    public void startController(Stage stage) throws Exception {
        primaryStage = stage;
        root = FXMLLoader.load(UserRegistrationController.class.getResource("../GUI/CompanyMainWindow.fxml"));

        Scene sceneUserRegistration = new Scene(root);

        primaryStage.setScene(sceneUserRegistration);
        primaryStage.show();
    }

    @FXML
    public void initialize() {

        String bundle = ProgramData.getInstance().getLanguage();
        ResourceBundle rbSk = ResourceBundle.getBundle(bundle, Locale.forLanguageTag("mainCom"));

        roomLabel.setText(rbSk.getString("companyMain.roomLabel") + " " +
                ProgramData.getInstance().getCompany().getId());
        searchTextField.setPromptText(rbSk.getString("companyMain.searchField"));
        signOutHyperlink.setText(rbSk.getString("companyMain.signOut"));
        modifyButton.setText(rbSk.getString("companyMain.modifyInfo"));
        changePasswordButton.setText(rbSk.getString("companyMain.changePassword"));
        primaryStage = ProgramData.getInstance().getPrimaryStage();
        primaryStage.setTitle(rbSk.getString("companyMain.window"));
        Company company = ProgramData.getInstance().getCompany();
        nameLabel.setText(company.getName());
        streetLabel.setText(company.getStreet());
        cityLabel.setText(company.getCity());
        countryLabel.setText(company.getCountry());
        postalCodeLabel.setText(company.getPostalCode());
        mailLabel.setText(company.getMail());
        phoneNumberLabel.setText(company.getPhoneNumber());
        fillTable();
    }

    public void slovakFlagClicked(MouseEvent mouseEvent) {
        ProgramData.getInstance().setLanguage("sk");
        initialize();
    }

    public void britishFlagClicked(MouseEvent mouseEvent) {
        ProgramData.getInstance().setLanguage("en");
        initialize();
    }

    public void signOutHyperlinkClicked(ActionEvent actionEvent) throws Exception {
        primaryStage = ProgramData.getInstance().getPrimaryStage();

        Controller clc = new CompanyLoginController();
        clc.startController(primaryStage);
    }

    public void magnifierClicked(MouseEvent mouseEvent) {
        fillTable();
    }

    public void modifyButtonClicked(ActionEvent actionEvent) throws Exception {
        primaryStage = new Stage();

        Controller mcc = new ModifyCompanyController();
        mcc.startController(primaryStage);
    }

    public void changePasswordButtonClicked(ActionEvent actionEvent) throws Exception {
        primaryStage = ProgramData.getInstance().getPrimaryStage();

        Controller cpc = new ChangePasswordController();
        cpc.startController(primaryStage);
    }

    public void fillTable() {
        int roomNumber = ProgramData.getInstance().getCompany().getId();
        String bundle = ProgramData.getInstance().getLanguage();
        ResourceBundle rbSk = ResourceBundle.getBundle(bundle, Locale.forLanguageTag("mainCom"));

        TableColumn firstName = new TableColumn(rbSk.getString("companyMain.fnColumn"));
        TableColumn lastName = new TableColumn(rbSk.getString("companyMain.lnColumn"));
        TableColumn username = new TableColumn(rbSk.getString("companyMain.unColumn"));
        usersTable.getColumns().setAll(firstName, lastName, username);

        firstName.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        username.setCellValueFactory(new PropertyValueFactory<Person, String>("username"));
        try {
            final ObservableList<Person> users;
            usersTable.setItems(null);
            users = FXCollections.observableArrayList(ManagerPerson.getUsers(searchTextField.getText(), roomNumber));
            usersTable.setItems(users);
        } catch (Exception e) {
        }

    }

    public void deleteCMClicked(ActionEvent actionEvent) {
        Person person = (Person) usersTable.getSelectionModel().getSelectedItem();
        if (person != null) {
            ManagerPerson.deleteUser(person.getUsername());
        }
        initialize();
    }

    public void showCMClicked(ActionEvent actionEvent) throws Exception {
        Person person = (Person) usersTable.getSelectionModel().getSelectedItem();
        if (person != null) {
            primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(UserRegistrationController.class.getResource("../GUI/UserDetail.fxml"));
            Parent root = loader.load();
            Scene sceneUserRegistration = new Scene(root);

            primaryStage.setScene(sceneUserRegistration);

            UserDetailController udc = loader.getController();
            udc.setPerson(person);
            udc.startController(primaryStage);
            primaryStage.show();
        }
    }
}
