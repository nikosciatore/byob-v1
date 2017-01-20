package application;

import java.net.URL;
import java.util.ResourceBundle;
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

/**
 * La classe UserInterfaceController gestisce l'interfaccia grafica.
 */
public class UserInterfaceController implements Initializable{

	@FXML private TabPane tabPane;
	
	@FXML private Tab configurationTab, controlTab, logTab, systemInfoTab;
	
	@FXML private Label leftStatusLabel, rightStatusLabel;
	
	@FXML private Button newContactButton, editContactButton, deleteContactButton, cancelButton, saveContactButton,
						 startBotButton, stopBotButton, startBotButton1, stopBotButton1;
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
	public TableView<BotIdEntryProperty> getBotIdTableView() {
		return botIdTableView;
	}

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
		programLogTableViewInit();
		systemInfoTableViewInit();
		botIdTableViewInit();
		
		appMode = AppMode.IDLE;
	}

	/**
	 * Inizializzazione della tabella contenente le voci del file di configurazione
	 */
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

	/**
	 * Inizializzazione della tabella contenente le informazioni relative agli host
	 */
	private void systemInfoTableViewInit() {

		systemInfoPropertyTableCol.setCellValueFactory(new PropertyValueFactory<SystemInfoEntryProperty, String>("property"));
		systemInfoValueTableCol.setCellValueFactory(new PropertyValueFactory<SystemInfoEntryProperty, String>("value"));

		systemInfoObservableList = FXCollections.observableArrayList();		
	}

	
	/**
	 * Inizializzazione della tabella contenente la lista dei bot
	 */
	private void botIdTableViewInit() {
		botIdTableCol.setCellValueFactory(new PropertyValueFactory<BotIdEntryProperty, String>("botId"));
				
		botIdTableView.setItems(SystemInfoBotServer.getBotIdEntryObservableList());		

		botIdTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BotIdEntryProperty>() {
			@Override
			public void changed(ObservableValue<? extends BotIdEntryProperty> observable, BotIdEntryProperty oldValue, BotIdEntryProperty newValue) {
				
				int selectedIndex = botIdTableView.getSelectionModel().getSelectedIndex();
				
				if(selectedIndex >= 0){
					fillSystemInfoTableView(selectedIndex);
				}
			}
			
		});
		
	}

	/**
	 * Inizializzazione della tabella contenente il log di programma
	 */
	private void programLogTableViewInit() {

		programLogTypeTableCol.setCellValueFactory(new PropertyValueFactory<ProgramLogEntryProperty, String>("type"));
		programLogTimestampTableCol.setCellValueFactory(new PropertyValueFactory<ProgramLogEntryProperty, String>("timestamp"));
		programLogMessageTableCol.setCellValueFactory(new PropertyValueFactory<ProgramLogEntryProperty, String>("message"));
				
		programLogTableView.setItems(ProgramLog.getProgramLogEntryObservableList());

	}

	/**
	 * Azione corrispondente alla pressione del bottone "New"
	 */
	@FXML protected void newContact(ActionEvent event){
		setUserInterface("newContact");
		appMode = AppMode.NEW;		
	}

	/**
	 * Azione corrispondente alla pressione del bottone "Edit"
	 */
	@FXML protected void editContact(ActionEvent event){
		setUserInterface("editContact");
		appMode = AppMode.EDIT;
	}

	/**
	 * Azione corrispondente alla pressione del bottone "Delete"
	 */
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
	
	/**
	 * Azione corrispondente alla pressione del bottone "Cancel"
	 */
	@FXML protected void cancel(ActionEvent event){
		setUserInterface("cancel");
	}

	/**
	 * Azione corrispondente alla pressione del bottone "Save"
	 */
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
	
	/**
	 * Azione corrispondente alla pressione del tasto Start
	 */
	@FXML protected void startBot(ActionEvent event){
		Main.botServer.start();
		setUserInterface("startBot");
	}

	/**
	 * Azione corrispondente alla pressione del tasto Stop
	 */
	@FXML protected void stopBot(ActionEvent event){
		Main.botServer.stop();	
		setUserInterface("stopBot");
	}

	/**
	 * Azione corrispondente all'uscita dall'applicazione
	 */
	@FXML protected void quitApplication(ActionEvent event){
		Main.stage.close();
	}
	
	/**
	 * Metodo che modifica l'interfaccia in base al parametro mode
	 */
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
			break;
		case "stopBot":
			startBotButton.setDisable(false);
			stopBotButton.setDisable(true);
			startBotButton1.setDisable(false);
			stopBotButton1.setDisable(true);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Medoto che verifica che tutte le caselle di testo siano vuote
	 */
	private boolean isTextFieldsEmpty() {
		if(ttlTextField.getText().equals("") || urlTextField.getText().equals("") || periodTextField.getText().equals("") || 
		   maxContactTextField.getText().equals("") || sleepModeTextField.getText().equals("")){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Metodo che riempie la tabelle contenente le informazioni relative agli host
	 * sulla base del bot selezionato
	 */
	private void fillSystemInfoTableView(int selectedIndex) {
		systemInfoObservableList.clear();
		systemInfoObservableList.addAll(Main.botServer.getSystemInfoProperty(selectedIndex));
		systemInfoTableView.setItems(systemInfoObservableList);

	}
	
	/**
	 * Metodo che rienpie le caselle di testo presenti nel tab "Config" 
	 * conseguentemente alla selezione di un elemento presente nella
	 * tabella contenente la lista dei contatti.
	 */
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

	/**
	 * Metodo che elimina il contenuto delle caselle di testo
	 */
	private void emptyTextFields() {
		urlTextField.setText("");
		periodTextField.setText("");
		maxContactTextField.setText("");
		sleepModeTextField.setText("");
		userAgentTextField.setText("");
		proxyTextField.setText("");
	}
	
	/**
	 * Metodo che in base al parametro mode rende le caselle di testo 
	 * editabili o meno
	 * @param mode
	 */
	private void setTextFieldsEditable(boolean mode) {
		ttlTextField.setEditable(mode);
		urlTextField.setEditable(mode);
		periodTextField.setEditable(mode);
		maxContactTextField.setEditable(mode);
		sleepModeTextField.setEditable(mode);
		userAgentTextField.setEditable(mode);
		proxyTextField.setEditable(mode);
		
	}

	/**
	 * Metodo che consente di modificare il tab visualizzato
	 * @param tabName tab da visualizzare
	 */
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

	/**
	 * Metodo che consente di impostare il testo della barra di stato
	 * @param text
	 */
	public void setLeftStatusLabelText(String text) {
		leftStatusLabel.setText(text);
	}

	/**
	 * Metodo che consente di impostare il testo della barra di stato
	 * @param text
	 */
	public void setRightStatusLabelText(String text, Color textColor) {
		rightStatusLabel.setText(text);
		rightStatusLabel.setTextFill(textColor);
	}
	
}
