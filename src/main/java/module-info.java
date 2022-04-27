module artsishevskiy.rz_3des {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires commons.codec;
    requires org.bouncycastle.provider;

    opens artsishevskiy.rz_3des to javafx.fxml;
    exports artsishevskiy.rz_3des;
}