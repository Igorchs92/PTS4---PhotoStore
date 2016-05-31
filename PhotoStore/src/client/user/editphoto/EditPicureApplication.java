package client.user.editphoto;


import client.user.UserClientRunnable;
import client.user.payment.Item;
import client.user.payment.StoreCart;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import shared.files.Picture;


/**
 * Load image, provide rectangle for rubberband selection. Press right mouse
 * button for "crop" context menu which then crops the image at the selection
 * rectangle and saves it as jpg.
 */
public class EditPicureApplication extends Application {
    
    static RubberBandSelection rubberBandSelection;
    static ImageView imageView;
    static ImageView imagePreview;
    static ImageView specialView;
    static double imageWidth;
    static double imageHeight;
    static double imageX;
    static double imageY;
    static Picture p;
    
    Stage primaryStage;
    
    @Override
    public void start(Stage primaryStage) {
        
        this.primaryStage = primaryStage;
        
        primaryStage.setTitle("Image Crop");
        
        BorderPane root = new BorderPane();

        // container for image layers
        ScrollPane scrollPane = new ScrollPane();

        // image layer: a group of images
        Group imageLayer = new Group();

        //Buttons
        Image Original = new Image(getClass().getResourceAsStream("PreviewOriginal.png"));
        Button btnOriginal = new Button("", new ImageView(Original));
        
        Image Grayscale = new Image(getClass().getResourceAsStream("PreviewGrayscale.png"));
        Button btnGrayscale = new Button("", new ImageView(Grayscale));
        
        Image Sepia = new Image(getClass().getResourceAsStream("PreviewSepia.png"));
        Button btnSepia = new Button("", new ImageView(Sepia));

        //Combobox
        final ComboBox specialBox = new ComboBox();
        specialBox.getItems().addAll(
                "Foto",
                "Mok",
                "T-Shirt");
        
        specialBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                switch(t1) {
                    case "Foto":
                        specialView.setImage(null);
                        break;
                    case "T-Shirt":
                        specialView.setImage(new Image(getClass().getResourceAsStream("T-Shirt.jpg")));
                        break;
                    case "Mok":
                        specialView.setImage(new Image(getClass().getResourceAsStream("Mok.jpg")));
                        break;
                }
            }
        });

        //Locations
        btnOriginal.setTranslateX(300);
        btnOriginal.setTranslateY(30);
        
        btnGrayscale.setTranslateX(300);
        btnGrayscale.setTranslateY(180);
        
        btnSepia.setTranslateX(300);
        btnSepia.setTranslateY(330);
        
        specialBox.setTranslateX(600);
        specialBox.setTranslateY(30);

        //Events
        btnOriginal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                imagePreview.setEffect(null);
            }
        });
        
        btnGrayscale.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ColorAdjust blackout = new ColorAdjust();
                blackout.setSaturation(-1);
                imagePreview.setEffect(null);
                imagePreview.setEffect(blackout);
                StoreCart.addToCart(new Item(p.getName(), (float)p.getPrice(), 1) );
            }
        });
        
        btnSepia.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                SepiaTone sepiaTone = new SepiaTone();
                sepiaTone.setLevel(1);
                imagePreview.setEffect(null);
                imagePreview.setEffect(sepiaTone);
            }
        });

        // load the image
        p = UserClientRunnable.clientRunnable.pictureToEdit;
        Image image = new Image(p.getFile().toURI().toString()); 

        // the container for the image as a javafx node
        imageView = new ImageView(image);
        imagePreview = new ImageView(image);
        specialView = new ImageView();
        imageWidth = imageView.getBoundsInLocal().getMaxX();
        imageHeight = imageView.getBoundsInLocal().getMaxY();
        imageX = imageView.getBoundsInLocal().getMinX();
        imageY = imageView.getBoundsInLocal().getMinY();

        //X and Y of the imageViews
        imageView.setTranslateX(20);
        imageView.setTranslateY(30);
        
        imagePreview.setTranslateX(640);
        imagePreview.setTranslateY(200);
        
        specialView.setTranslateX(500);
        specialView.setTranslateY(50);

        // add image to layer
        imageLayer.getChildren().add(imageView);        
        imageLayer.getChildren().add(specialView);
        imageLayer.getChildren().add(imagePreview);
        
        imageLayer.getChildren().add(btnOriginal);
        imageLayer.getChildren().add(btnGrayscale);
        imageLayer.getChildren().add(btnSepia);
        imageLayer.getChildren().add(specialBox);

        // use scrollpane for image view in case the image is large
        scrollPane.setContent(imageLayer);

        // put scrollpane in scene
        root.setCenter(scrollPane);

        // rubberband selection
        rubberBandSelection = new RubberBandSelection(imageLayer);
        
        primaryStage.setScene(new Scene(root, 1024, 600));
        primaryStage.show();
    }
    
    private static void crop(Bounds bounds) {

//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save Image");
//
//        File file = fileChooser.showSaveDialog(primaryStage);
//        if (file == null) {
//            return;
//        }
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();
        
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        parameters.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), width, height));
        
        WritableImage wi = new WritableImage(width, height);
        imageView.snapshot(parameters, wi);

        // save image 
        // !!! has bug because of transparency (use approach below) !!!
        // --------------------------------
//        try {
//          ImageIO.write(SwingFXUtils.fromFXImage( wi, null), "jpg", file);
//      } catch (IOException e) {
//          e.printStackTrace();
//      }
        // save image (without alpha)
        // --------------------------------
        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
        BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);
        
        Graphics2D graphics = bufImageRGB.createGraphics();
        graphics.drawImage(bufImageARGB, 0, 0, null);
        
        WritableImage writeImage = null;
        if (bufImageRGB != null) {
            writeImage = new WritableImage(bufImageRGB.getWidth(), bufImageRGB.getHeight());
            PixelWriter pixelWriter = writeImage.getPixelWriter();
            for (int x = 0; x < bufImageRGB.getWidth(); x++) {
                for (int y = 0; y < bufImageRGB.getHeight(); y++) {
                    pixelWriter.setArgb(x, y, bufImageRGB.getRGB(x, y));
                }
            }
        }

//        switch (effect) {
//            case "Original":
//                imagePreview.setEffect(null);
//                break;
//            case "Grayscale":
//                ColorAdjust blackout = new ColorAdjust();
//                blackout.setSaturation(-1);
//                imagePreview.setEffect(null);
//                imagePreview.setEffect(blackout);
//                break;
//            case "Sepia":
//                SepiaTone sepiaTone = new SepiaTone();
//                sepiaTone.setLevel(1);
//                imagePreview.setEffect(null);
//                imagePreview.setEffect(sepiaTone);
//                break;
//        }
        imagePreview.setImage(writeImage);

//        try {
//
//            ImageIO.write(bufImageRGB, "jpg", file);
//
//            System.out.println("Image saved to " + file.getAbsolutePath());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        graphics.dispose();
        
    }

    /**
     * Drag rectangle with mouse cursor in order to get selection bounds
     */
    public static class RubberBandSelection {
        
        final DragContext dragContext = new DragContext();
        Rectangle rect = new Rectangle();
        
        Group group;
        
        public Bounds getBounds() {
            return rect.getBoundsInParent();
        }
        
        public RubberBandSelection(Group group) {
            
            this.group = group;
            
            rect = new Rectangle(0, 0, 0, 0);
            rect.setStroke(Color.BLUE);
            rect.setStrokeWidth(1);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));
            
            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
            
        }
        
        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent event) {
                
                if (event.isSecondaryButtonDown()) {
                    return;
                }

                // remove old rect
                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);
                
                group.getChildren().remove(rect);

                // prepare new drag operation
                dragContext.mouseAnchorX = event.getX();
                dragContext.mouseAnchorY = event.getY();
                
                rect.setX(dragContext.mouseAnchorX);
                rect.setY(dragContext.mouseAnchorY);
                rect.setWidth(0);
                rect.setHeight(0);
                
                group.getChildren().add(rect);
                
                Picture p = UserClientRunnable.clientRunnable.pictureToEdit;
                imagePreview.setImage(new Image(p.getFile().toURI().toString()));
                
            }
        };
        
        EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent event) {
                
                if (event.isSecondaryButtonDown()) {
                    return;
                }
                
                double offsetX = event.getX() - dragContext.mouseAnchorX;
                double offsetY = event.getY() - dragContext.mouseAnchorY;
                
                if (event.getX() < imageWidth && event.getX() > imageX) {
                    if (offsetX > 0) {
                        rect.setWidth(offsetX);
                    } else {
                        rect.setX(event.getX());
                        rect.setWidth(dragContext.mouseAnchorX - rect.getX());
                    }
                }
                
                if (event.getY() < imageHeight && event.getY() > imageY) {
                    if (offsetY > 0 && event.getY() <= imageHeight) {
                        rect.setHeight(offsetY);
                    } else if (event.getY() <= imageHeight) {
                        rect.setY(event.getY());
                        rect.setHeight(dragContext.mouseAnchorY - rect.getY());
                    }
                }

                // get bounds for image crop
                Bounds selectionBounds = rubberBandSelection.getBounds();

                // show bounds info
                System.out.println("Selected area: " + selectionBounds);

                // crop the image
                crop(selectionBounds);
            }
        };
        
        EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent event) {
                
                if (event.isSecondaryButtonDown()) {
                    return;
                }

                // remove rectangle
                // note: we want to keep the ruuberband selection for the cropping => code is just commented out
                /*
                 rect.setX(0);
                 rect.setY(0);
                 rect.setWidth(0);
                 rect.setHeight(0);

                 group.getChildren().remove( rect);
                 */
            }
        };
        
        private static final class DragContext {
            
            public double mouseAnchorX;
            public double mouseAnchorY;
            
        }
    }
}
