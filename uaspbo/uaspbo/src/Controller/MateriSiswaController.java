package Controller;

import Model.Materi;
import Service.DatabaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

import java.awt.Desktop;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;

public class MateriSiswaController {

    @FXML
    private TableView<Materi> materiTable;
    @FXML
    private TableColumn<Materi, Integer> colId;
    @FXML
    private TableColumn<Materi, String> colTitle;
    @FXML
    private TableColumn<Materi, String> colDescription;
    @FXML
    private TableColumn<Materi, Void> colFile;
    @FXML
    private Button btnBack;

    private ObservableList<Materi> materiList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colTitle.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        colDescription.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));

        addFileOpenButton(); 
        loadMateri();
    }

    private void loadMateri() {
        try (Connection conn = DatabaseService.getConnection()) {
            String sql = "SELECT * FROM materi";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            materiList.clear();
            while (rs.next()) {
                materiList.add(new Materi(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("file_path")  
                ));
            }
            materiTable.setItems(materiList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addFileOpenButton() {
        Callback<TableColumn<Materi, Void>, TableCell<Materi, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Materi, Void> call(final TableColumn<Materi, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Buka");

                    {
                        btn.setStyle("-fx-background-color: #4682b4; -fx-text-fill: white;");
                        btn.setOnAction((event) -> {
                            Materi materi = getTableView().getItems().get(getIndex());
                            String filePath = materi.getFilePath();
                            if (filePath != null && !filePath.isEmpty()) {
                                try {
                                    File file = new File(filePath);
                                    if (file.exists()) {
                                        Desktop.getDesktop().open(file);
                                    } else {
                                        showAlert("File tidak ditemukan.");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    showAlert("Gagal membuka file.");
                                }
                            } else {
                                showAlert("File belum tersedia.");
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        colFile.setCellFactory(cellFactory);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/dashboard_siswa.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setUserRole(Service.Session.currentRole); 

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard - " + Service.Session.currentRole);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
