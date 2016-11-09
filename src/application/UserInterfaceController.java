package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.AppMode;
import model.URLEntry;
import model.URLEntryProperty;

public class UserInterfaceController implements Initializable{

	@FXML private Button newContactButton, editContactButton, deleteContactButton, cancelButton, saveContactButton;
	@FXML private TableView<URLEntryProperty> contactsTableView;
	
	@FXML private TableColumn<URLEntryProperty, String> idTableCol, urlTableCol, periodTableCol,
														sleepModeTableCol, userAgentTableCol, proxyTableCol;

	@FXML private TableColumn<URLEntryProperty, Integer> maxContactTableCol;

	
	@FXML private TextField urlTextField, periodTextField, maxContactTextField, 
							sleepModeTextField, userAgentTextField, proxyTextField;

	ObservableList<URLEntryProperty> contactsObservableList;
	
	AppMode appMode;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		
		appMode = AppMode.INIT;
		tableViewInit();
		appMode = AppMode.IDLE;

	}

	private void tableViewInit() {

		idTableCol.setCellValueFactory(new PropertyValueFactory<URLEntryProperty, String>("ID"));
		urlTableCol.setCellValueFactory(new PropertyValueFactory<URLEntryProperty, String>("URL"));
		periodTableCol.setCellValueFactory(new PropertyValueFactory<URLEntryProperty, String>("periodicRangeSec"));
		maxContactTableCol.setCellValueFactory(new PropertyValueFactory<URLEntryProperty, Integer>("maxContactNumber"));
		sleepModeTableCol.setCellValueFactory(new PropertyValueFactory<URLEntryProperty, String>("sleepMode"));
		userAgentTableCol.setCellValueFactory(new PropertyValueFactory<URLEntryProperty, String>("userAgent"));
		proxyTableCol.setCellValueFactory(new PropertyValueFactory<URLEntryProperty, String>("proxy"));
		
		contactsObservableList = FXCollections.observableArrayList();
		contactsObservableList.addAll(Main.bot.getContactsListProperty());
		
		contactsTableView.setItems(contactsObservableList);
		
		contactsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<URLEntryProperty>() {
			@Override
			public void changed(ObservableValue<? extends URLEntryProperty> observable, URLEntryProperty oldValue, URLEntryProperty newValue) {
				fillTextFields(newValue);
			}
		});
		

		contactsTableView.requestFocus();
		contactsTableView.getSelectionModel().select(0);
	}

	@FXML protected void newContact(ActionEvent event){
		setUserInterface("newContact");
		appMode = AppMode.NEW;		
	}

	@FXML protected void editContact(ActionEvent event){
		setUserInterface("editContact");
		appMode = AppMode.EDIT;
	}

	@FXML protected void deleteContact(ActionEvent event){
		try {
			String idString = contactsTableView.getSelectionModel().getSelectedItem().getID();
			Integer id = Integer.parseInt(idString);
			Main.bot.removeContact(id);
			contactsTableView.getItems().remove(contactsTableView.getSelectionModel().getSelectedItem());
			setUserInterface("deleteContact");
		} catch (NullPointerException e) {
			/*nessun elemento nella lista dei contatti*/
		}
		
		
	}
	
	@FXML protected void cancel(ActionEvent event){
		setUserInterface("cancel");
	}

	@FXML protected void saveContact(ActionEvent event){
		if(!isTextFieldsEmpty()){
			URLEntry newUrlEntry = new URLEntry();
			newUrlEntry.setURL(urlTextField.getText());
			newUrlEntry.setPeriod(periodTextField.getText());
			newUrlEntry.setMaxContactNumber(maxContactTextField.getText());
			newUrlEntry.setSleepMode(sleepModeTextField.getText());
			newUrlEntry.setUserAgent(userAgentTextField.getText());
			newUrlEntry.setProxy(proxyTextField.getText());
			
			if(appMode == AppMode.NEW){
				Main.bot.addContact(newUrlEntry);
				contactsTableView.getItems().add(new URLEntryProperty(newUrlEntry));
			}else if(appMode == AppMode.EDIT){
				String idString = contactsTableView.getSelectionModel().getSelectedItem().getID();
				Integer id = Integer.parseInt(idString);
				newUrlEntry.setID(id);
				Main.bot.editContact(newUrlEntry);
				contactsTableView.getItems().set(id-1, new URLEntryProperty(newUrlEntry));
			}
			setUserInterface("saveContact");
			
			appMode = AppMode.IDLE;
		}
	}
	
	@FXML protected void quitApplication(ActionEvent event){
		Main.stage.close();
	}

	
	private void setUserInterface(String mode){
		switch (mode) {
		case "newContact":
			newContactButton.setDisable(true);
			editContactButton.setDisable(true);
			deleteContactButton.setDisable(true);	
			saveContactButton.setDisable(false);
			cancelButton.setDisable(false);

			fillTextFields("http://www.example.com","10-20","100","1000-10-10","BYOBv1","http://www.example.com");
			setTextFieldsEditable(true);
			urlTextField.requestFocus();
			
			break;
		case "editContact":
			newContactButton.setDisable(true);
			editContactButton.setDisable(true);
			deleteContactButton.setDisable(true);	
			saveContactButton.setDisable(false);
			cancelButton.setDisable(false);
			setTextFieldsEditable(true);
			urlTextField.requestFocus();
			break;
		case "deleteContact":
			contactsTableView.requestFocus();			
			break;
		case "cancel":
			saveContactButton.setDisable(true);
			cancelButton.setDisable(true);
			newContactButton.setDisable(false);
			editContactButton.setDisable(false);
			deleteContactButton.setDisable(false);
			setTextFieldsEditable(false);
			contactsTableView.requestFocus();
			break;
		case "saveContact":
			saveContactButton.setDisable(true);
			cancelButton.setDisable(true);
			newContactButton.setDisable(false);
			editContactButton.setDisable(false);
			deleteContactButton.setDisable(false);
			setTextFieldsEditable(false);
			contactsTableView.requestFocus();

			if(appMode==AppMode.NEW){
				int lastItemIndex = contactsObservableList.size()-1;
				contactsTableView.getSelectionModel().select(lastItemIndex);
				contactsTableView.scrollTo(lastItemIndex);
			}

			break;

		default:
			break;
		}
	}
	
	private boolean isTextFieldsEmpty() {
		if(urlTextField.getText().equals("") || periodTextField.getText().equals("") || 
		   maxContactTextField.getText().equals("") || sleepModeTextField.getText().equals("")
		   || userAgentTextField.getText().equals("") || proxyTextField.getText().equals("")){
			return true;
		}else{
			return false;
		}
	}

	private void fillTextFields(URLEntryProperty urlEntryProperty) {
		if(!(urlEntryProperty==null)){
			urlTextField.setText(urlEntryProperty.getURL().toString());
			periodTextField.setText(urlEntryProperty.getPeriodicRangeSec().toString());
			maxContactTextField.setText(urlEntryProperty.getMaxContactNumber().toString());
			sleepModeTextField.setText(urlEntryProperty.getSleepMode().toString());
			userAgentTextField.setText(urlEntryProperty.getUserAgent().toString());
			proxyTextField.setText(urlEntryProperty.getProxy().toString());
		}else{
			emptyTextFields();
		}
	}
	
	private void fillTextFields(String urlString, String periodString, String maxContactNumberString, 
							    String sleepModeString, String userAgentString,String proxyString){
			urlTextField.setText(urlString);
			periodTextField.setText(periodString);
			maxContactTextField.setText(maxContactNumberString);
			sleepModeTextField.setText(sleepModeString);
			userAgentTextField.setText(userAgentString);
			proxyTextField.setText(proxyString);
	}


	private void emptyTextFields() {
		urlTextField.setText("");
		periodTextField.setText("");
		maxContactTextField.setText("");
		sleepModeTextField.setText("");
		userAgentTextField.setText("");
		proxyTextField.setText("");
	}
	
	private void setTextFieldsEditable(boolean mode) {
		urlTextField.setEditable(mode);
		periodTextField.setEditable(mode);
		maxContactTextField.setEditable(mode);
		sleepModeTextField.setEditable(mode);
		userAgentTextField.setEditable(mode);
		proxyTextField.setEditable(mode);
		
	}

	
}
