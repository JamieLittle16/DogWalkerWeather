package cambridge.weatherapp.dogwalkerweather.controller;

import cambridge.weatherapp.dogwalkerweather.model.Location;
import cambridge.weatherapp.dogwalkerweather.model.WeatherData;
import cambridge.weatherapp.dogwalkerweather.util.IconUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;

public class SettingsController {
    @FXML private Label currentLocationLabel;
    @FXML private ListView<Location> locationsList;
    @FXML private FontIcon currentWeatherIcon;
    @FXML private TextField searchField;

    // Choice Dialog
    @FXML private StackPane addLocationOverlay;
    @FXML private ListView<Location> allLocationsList;
    @FXML private javafx.scene.control.Button cancelButton;
    @FXML private TextField popupSearchField;

    @FXML
    private void initialize() {
        // Set current Locarion card text
        Location current = RootController.getInstance().getCurrentLocation();
        currentLocationLabel.setText(current.getDisplayName());

        // Fetch the weather data for the current location
        WeatherData currentData = RootController.getInstance().getWeatherProvider().getCurrentWeather(current);

        // Generate the correct icon using your new utility
        FontIcon dynamicIcon = IconUtil.getWeatherIconFromCode(currentData.getCurrentWeatherCode());

        // Apply the icon to the FXML element
        currentWeatherIcon.setIconLiteral(dynamicIcon.getIconLiteral());
        currentWeatherIcon.setIconColor(dynamicIcon.getIconColor());

        // Create a filter that hides the active location from the list
        FilteredList<Location> filteredLocations = new FilteredList<>(
                RootController.getInstance().getSavedLocations(),
                loc -> loc != current
        );

        // Filter for search findings
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredLocations.setPredicate(loc -> {
                // Rule 1: Always hide the current location from this list
                if (loc == current) return false;

                // Rule 2: If the search bar is empty, show all saved locations
                if (newValue == null || newValue.isEmpty()) return true;

                // Rule 3: Substring search (Case-insensitive)
                String searchString = newValue.toLowerCase();
                return loc.getDisplayName().toLowerCase().contains(searchString);
            });
        });

        // Bind the location UI list to the state list
        // Since it's an observable list it will auto update the UI upon changing
        locationsList.setItems(filteredLocations);

        // Create UI card for each saved element
        locationsList.setCellFactory(listView -> {
            ListCell<Location> cell = new ListCell<Location>() {
                @Override
                protected void updateItem(Location location, boolean empty) {
                    super.updateItem(location, empty);

                    // If row is empty make it invisible
                    if (empty || location == null) {
                        setText(null);
                        setGraphic(null);
                        setStyle("-fx-background-color: transparent; -fx-padding: 0;");
                    } else {
                        // Create text label
                        Label label = new Label(location.getDisplayName());
                        label.getStyleClass().add("title-4");

                        // Puts a mapPin next to each of them.
                        FontIcon mapPin = cambridge.weatherapp.dogwalkerweather.util.IconUtil.getLocationIcon();

                        Region spacer = new Region();
                        HBox.setHgrow(spacer, Priority.ALWAYS);

                        // Get location weather data:
                        WeatherData data = RootController.getInstance().getWeatherProvider().getCurrentWeather(location);
                        FontIcon weatherIcon = IconUtil.getWeatherIconFromCode(data.getCurrentWeatherCode());
                        weatherIcon.setIconSize(20);
                        weatherIcon.setIconColor(Color.valueOf("#bdc3c7")); // Soft silver

                        // Put it in HBox (acting as the "Card")
                        HBox card = new HBox(15, mapPin, label, spacer, weatherIcon);
                        card.setAlignment(Pos.CENTER_LEFT);
                        card.setPadding(new Insets(15, 20, 15, 20));

                        String defaultStyle =
                                "-fx-background-color: rgba(255, 255, 255, 0.85); " +
                                        "-fx-background-radius: 12; " +
                                        "-fx-cursor: hand; " + // Turns the mouse into a pointer
                                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 8, 0, 0, 4);";

                        String hoverStyle =
                                "-fx-background-color: rgba(255, 255, 255, 1.0); " +
                                        "-fx-background-radius: 12; " +
                                        "-fx-cursor: hand; " +
                                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 12, 0, 0, 6);";

                        // Set card style
                        card.setStyle(defaultStyle);

                        // Applies hover styles
                        card.setOnMouseEntered(event -> card.setStyle(hoverStyle));
                        card.setOnMouseExited(event -> card.setStyle(defaultStyle));

                        // Tell JavaFX to display card, not text
                        setGraphic(card);
                        setText(null);

                        // Make list row transparent and put a 10 px gap between elements
                        setStyle("-fx-background-color: transparent; -fx-padding: 0 0 12 0; -fx-control-inner-background-alt: transparent; -fx-selection-bar: transparent; -fx-selection-bar-non-focused: transparent;");
                    }
                }
            };

            cell.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> event.consume());
            cell.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_RELEASED, event -> {
                event.consume();
                if (!cell.isEmpty() && cell.getItem() != null) {
                    javafx.application.Platform.runLater(() -> {
                        RootController.getInstance().setCurrentLocation(cell.getItem());
                        RootController.getInstance().onHomeClicked();
                    });
                }
            });

            return cell;
        });

        // Search field in popup
        javafx.collections.ObservableList<Location> allLocationsObservable =
                javafx.collections.FXCollections.observableArrayList(Location.values());

        // Put them in a FilteredList
        FilteredList<Location> filteredAllLocations = new FilteredList<>(allLocationsObservable, loc -> true);

        // Listen to the new popup search bar
        popupSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAllLocations.setPredicate(loc -> {
                // If search is empty, show everything
                if (newValue == null || newValue.isEmpty()) return true;

                // Otherwise, fuzzy match the name
                String searchString = newValue.toLowerCase();
                return loc.getDisplayName().toLowerCase().contains(searchString);
            });
        });

        // Fill the location list with all enum Locations
        allLocationsList.setItems(filteredAllLocations);

        allLocationsList.setCellFactory(listView -> {
            ListCell<Location> cell = new ListCell<Location>() {
                @Override
                protected void updateItem(Location location, boolean empty) {
                    super.updateItem(location, empty);

                    if (empty || location == null) {
                        setText(null);
                        setGraphic(null);
                        setStyle("-fx-background-color: transparent; -fx-padding: 0;");
                    } else {
                        Label label = new Label(location.getDisplayName());
                        label.setStyle("-fx-font-size: 16px; -fx-text-fill: #333333;");

                        HBox card = new HBox(label);
                        card.setAlignment(Pos.CENTER);
                        card.setPadding(new Insets(12, 20, 12, 20));

                        // Interactive overlay styles
                        String defaultStyle = "-fx-background-color: transparent; -fx-background-radius: 8; -fx-cursor: hand;";
                        String hoverStyle = "-fx-background-color: #f0f4f8; -fx-background-radius: 8; -fx-cursor: hand;"; // Soft background highlight

                        card.setStyle(defaultStyle);
                        card.setOnMouseEntered(event -> card.setStyle(hoverStyle));
                        card.setOnMouseExited(event -> card.setStyle(defaultStyle));

                        setGraphic(card);
                        setText(null);

                        // Make the underlying row structure transparent to prevent the default blue flash selection highlight
                        setStyle("-fx-background-color: transparent; -fx-padding: 0 0 5 0; -fx-control-inner-background-alt: transparent; -fx-selection-bar: transparent; -fx-selection-bar-non-focused: transparent;");
                    }
                }
            };
            cell.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> event.consume());
            cell.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_RELEASED, event -> {
                event.consume();
                if (!cell.isEmpty() && cell.getItem() != null) {
                    javafx.application.Platform.runLater(() -> {
                        Location newVal = cell.getItem();
                        if (!RootController.getInstance().getSavedLocations().contains(newVal)) {
                            RootController.getInstance().getSavedLocations().add(newVal);
                        }
                        addLocationOverlay.setVisible(false);
                    });
                }
            });

            return cell;
        });
    }

    // Triggered by Back Arrow
    @FXML
    public void onBackClicked() {
        RootController.getInstance().onHomeClicked();
    }

    // Triggered by pressing + button
    @FXML
    public void onAddClicked() {
        // Show the dark overlay and white card!
        addLocationOverlay.setVisible(true);
    }

    @FXML
    public void onCancelAdd() {
        // Hide it if they change their mind
        addLocationOverlay.setVisible(false);
    }

    @FXML
    public void onCancelHover() {
        // Darker text and a soft gray background highlight on hover
        cancelButton.setStyle("-fx-background-color: #f0f2f5; -fx-border-color: #7f8c8d; -fx-border-width: 1.5; -fx-border-radius: 10; -fx-text-fill: #222222; -fx-font-weight: bold; -fx-min-width: 110; -fx-min-height: 40; -fx-cursor: hand;");
    }

    @FXML
    public void onCancelNormal() {
        // Returns seamlessly to the default outlined state
        cancelButton.setStyle("-fx-background-color: transparent; -fx-border-color: #bdc3c7; -fx-border-width: 1.5; -fx-border-radius: 10; -fx-text-fill: #555555; -fx-font-weight: bold; -fx-min-width: 110; -fx-min-height: 40; -fx-cursor: hand;");
    }
}
