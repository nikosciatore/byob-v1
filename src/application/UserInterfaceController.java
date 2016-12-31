package application;

import java.net.URL;
import java.util.ResourceBundle;

import control.client.Log;
import control.server.ProgramLog;
import control.server.SystemInfoBotServer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import model.AppMode;
import model.URLEntry;
import model.gui.BotIdEntryProperty;
import model.gui.LogEntryProperty;
import model.gui.ProgramLogEntryProperty;
import model.gui.SystemInfoEntryProperty;
import model.gui.URLEntryProperty;

public class UserInterfaceController implements Initializable{

	@FXML private TabPane tabPane; /*TODO switch tab*/
	
	@FXML private Tab configurationTab, controlTab, logTab, systemInfoTab;
	
	@FXML private Label leftStatusLabel, rightStatusLabel; /*TODO update status label*/
	
	@FXML private Button newContactButton, editContactButton, deleteContactButton, cancelButton, saveContactButton,
						 startBotButton, stopBotButton, startBotButton1, stopBotButton1, pauseResumeBotButton;
	@FXML private TableView<URLEntryProperty> contactsTableView;	
	@FXML private TableColumn<URLEntryProperty, String> contactIdTableCol, contactUrlTableCol, contactPeriodTableCol,
														contactSleepModeTableCol, contactUserAgentTableCol, 
														contactProxyTableCol;
	@FXML private TableColumn<URLEntryProperty, Integer> contactMaxContactTableCol;

	@FXML private TableView<LogEntryProperty> logsTableView;
	@FXML private TableColumn<LogEntryProperty, String> logIdTableCol, logTimestampTableCol, logUrlTableCol, logPeriodTableCol,
														logSleepModeTableCol, logUserAgentTableCol, 
														logProxyTableCol;
	@FXML private TableColumn<LogEntryProperty, Integer> logMaxContactTableCol;

	@FXML private TableView<SystemInfoEntryProperty> systemInfoTableView;
	@FXML private TableColumn<SystemInfoEntryProperty, String> systemInfoPropertyTableCol, systemInfoValueTableCol;
	
	@FXML private TableView<BotIdEntryProperty> botIdTableView;
	@FXML private TableColumn<BotIdEntryProperty, String> botIdTableCol;
	
	
	@FXML private TableView<ProgramLogEntryProperty> programLogTableView;
	@FXML private TableColumn<ProgramLogEntryProperty, String> programLogTypeTableCol, programLogTimestampTableCol, programLogMessageTableCol;

	
	
	@FXML private TextField ttlTextField, urlTextField, periodTextField, maxContactTextField, 
							sleepModeTextField, userAgentTextField, proxyTextField;

	ObservableList<URLEntryProperty> contactsObservableList;
	ObservableList<SystemInfoEntryProperty> systemInfoObservableList;
	
	AppMode appMode;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		
		appMode = AppMode.INIT;
		ttlTextField.setText(Main.botServer.getConfig().getConfigHeader().getTtl().toString());
		contactsTableViewInit();
		logTableViewInit();
		programLogTableViewInit();
		systemInfoTableViewInit();
		botIdTableViewInit();
		
		appMode = AppMode.IDLE;

	}


	private void contactsTableViewInit() {

		contactIdTableCol.setCellValueFactory(new PropertyValueFactory<URLEntryProperty, String>("ID"));
		contactUrlTableCol.setCellValueFactory(new PropertyValueFactory<URLEntryProperty, String>("URL"));
		contactPeriodTableCol.setCellValueFactory(new PropertyValueFactory<URLEntryProperty, String>("period"));
		contactMaxContactTableCol.setCellValueFactory(new PropertyValueFactory<URLEntryProperty, Integer>("maxContactNumber"));
		contactSleepModeTableCol.setCellValueFactory(new PropertyValueFactory<URLEntryProperty, String>("sleepMode"));
		contactUserAgentTableCol.setCellValueFactory(new PropertyValueFactory<URLEntryProperty, String>("userAgent"));
		contactProxyTableCol.setCellValueFactory(new PropertyValueFactory<URLEntryProperty, String>("proxy"));
		
		contactsObservableList = FXCollections.observableArrayList();
		contactsObservableList.addAll(Main.botServer.getContactsListProperty());
		
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
	
	
	
	
	private void logTableViewInit() {

		logIdTableCol.setCellValueFactory(new PropertyValueFactory<LogEntryProperty, String>("ID"));
		logTimestampTableCol.setCellValueFactory(new PropertyValueFactory<LogEntryProperty, String>("timestamp"));
		logUrlTableCol.setCellValueFactory(new PropertyValueFactory<LogEntryProperty, String>("URL"));
		logPeriodTableCol.setCellValueFactory(new PropertyValueFactory<LogEntryProperty, String>("period"));
		logMaxContactTableCol.setCellValueFactory(new PropertyValueFactory<LogEntryProperty, Integer>("maxContactNumber"));
		logSleepModeTableCol.setCellValueFactory(new PropertyValueFactory<LogEntryProperty, String>("sleepMode"));
		logUserAgentTableCol.setCellValueFactory(new PropertyValueFactory<LogEntryProperty, String>("userAgent"));
		logProxyTableCol.setCellValueFactory(new PropertyValueFactory<LogEntryProperty, String>("proxy"));
				
		logsTableView.setItems(Log.getLogEntryObservableList());

	}

	private void systemInfoTableViewInit() {

		systemInfoPropertyTableCol.setCellValueFactory(new PropertyValueFactory<SystemInfoEntryProperty, String>("property"));
		systemInfoValueTableCol.setCellValueFactory(new PropertyValueFactory<SystemInfoEntryProperty, String>("value"));

		systemInfoObservableList = FXCollections.observableArrayList();		
	}

	private void botIdTableViewInit() {
		botIdTableCol.setCellValueFactory(new PropertyValueFactory<BotIdEntryProperty, String>("botId"));
				
		botIdTableView.setItems(SystemInfoBotServer.getBotIdEntryObservableList());		

		botIdTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BotIdEntryProperty>() {
			@Override
			public void changed(ObservableValue<? extends BotIdEntryProperty> observable, BotIdEntryProperty oldValue, BotIdEntryProperty newValue) {
				fillSystemInfoTableView(botIdTableView.getSelectionModel().getSelectedIndex());
			}
		});
}

	private void programLogTableViewInit() {

		programLogTypeTableCol.setCellValueFactory(new PropertyValueFactory<ProgramLogEntryProperty, String>("type"));
		programLogTimestampTableCol.setCellValueFactory(new PropertyValueFactory<ProgramLogEntryProperty, String>("timestamp"));
		programLogMessageTableCol.setCellValueFactory(new PropertyValueFactory<ProgramLogEntryProperty, String>("message"));
				
		programLogTableView.setItems(ProgramLog.getProgramLogEntryObservableList());

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
			Main.botServer.removeContact(id);
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
			Main.botServer.getConfig().getConfigHeader().setTtl(ttlTextField.getText());			
			newUrlEntry.setURL(urlTextField.getText());
			newUrlEntry.setPeriod(periodTextField.getText());
			newUrlEntry.setMaxContactNumber(maxContactTextField.getText());
			newUrlEntry.setSleepMode(sleepModeTextField.getText());
			newUrlEntry.setUserAgent(userAgentTextField.getText());
			newUrlEntry.setProxy(proxyTextField.getText());
			
			if(appMode == AppMode.NEW){
				newUrlEntry = Main.botServer.addContact(newUrlEntry);
				contactsTableView.getItems().add(new URLEntryProperty(newUrlEntry));
			}else if(appMode == AppMode.EDIT){
				String idString = contactsTableView.getSelectionModel().getSelectedItem().getID();
				Integer id = Integer.parseInt(idString);
				newUrlEntry.setID(id);
				Main.botServer.editContact(newUrlEntry);

				int i;
				for (i = 0; i < contactsTableView.getItems().size(); i++) {
					if(contactsTableView.getItems().get(i).getID().equals(newUrlEntry.getID().toString())){
						break;
					}
				}
				contactsTableView.getItems().set(i, new URLEntryProperty(newUrlEntry));
			}
			setUserInterface("saveContact");
			
			appMode = AppMode.IDLE;
		}
	}
	
	@FXML protected void startBot(ActionEvent event){
		Main.botServer.start();
		setUserInterface("startBot");
	}

	@FXML protected void stopBot(ActionEvent event){
		Main.botServer.stop();	
		setUserInterface("stopBot");
	}

	@FXML protected void pauseResumeBot(ActionEvent event){
		if(pauseResumeBotButton.getText().equals("PAUSE")){
			Main.botServer.pause();
			setUserInterface("pauseBot");
		}else if(pauseResumeBotButton.getText().equals("RESUME")){
			Main.botServer.resume();
			setUserInterface("resumeBot");
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

			fillTextFields("http://www.example.com","1-2","5","1-1-1","BYOBv1","http://www.example.com");
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
		case "startBot":
			startBotButton.setDisable(true);
			stopBotButton.setDisable(false);
			startBotButton1.setDisable(true);
			stopBotButton1.setDisable(false);
			pauseResumeBotButton.setDisable(false);
			break;
		case "stopBot":
			startBotButton.setDisable(false);
			stopBotButton.setDisable(true);
			startBotButton1.setDisable(false);
			stopBotButton1.setDisable(true);
			pauseResumeBotButton.setDisable(true);
			break;
		case "pauseBot":
			pauseResumeBotButton.setText("RESUME");
			break;
		case "resumeBot":
			pauseResumeBotButton.setText("PAUSE");
			break;
		default:
			break;
		}
	}
	
	private boolean isTextFieldsEmpty() {
		if(ttlTextField.getText().equals("") || urlTextField.getText().equals("") || periodTextField.getText().equals("") || 
		   maxContactTextField.getText().equals("") || sleepModeTextField.getText().equals("")){
			return true;
		}else{
			return false;
		}
	}

	private void fillSystemInfoTableView(int selectedIndex) {
		systemInfoObservableList.clear();
		systemInfoObservableList.addAll(Main.botServer.getSystemInfoProperty(selectedIndex));
		systemInfoTableView.setItems(systemInfoObservableList);

	}
	
	private void fillTextFields(URLEntryProperty urlEntryProperty) {
		if(!(urlEntryProperty==null)){
			urlTextField.setText(urlEntryProperty.getURL().toString());
			periodTextField.setText(urlEntryProperty.getPeriod().toString());
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
		ttlTextField.setEditable(mode);
		urlTextField.setEditable(mode);
		periodTextField.setEditable(mode);
		maxContactTextField.setEditable(mode);
		sleepModeTextField.setEditable(mode);
		userAgentTextField.setEditable(mode);
		proxyTextField.setEditable(mode);
		
	}

	public void setTab(String tabName) {
		switch (tabName) {
		case "configuration":
			tabPane.getSelectionModel().select(configurationTab);			
			break;
		case "control":
			tabPane.getSelectionModel().select(controlTab);			
			break;
		case "log":
			tabPane.getSelectionModel().select(logTab);			
			break;
		case "systemInfo":
			tabPane.getSelectionModel().select(systemInfoTab);			
			break;

		default:
			break;
		}
	}

	public void setLeftStatusLabelText(String text) {
		leftStatusLabel.setText(text);
	}

	public void setRightStatusLabelText(String text, Color textColor) {
		rightStatusLabel.setText(text);
		rightStatusLabel.setTextFill(textColor);
	}
	
}
