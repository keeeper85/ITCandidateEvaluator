package view.swing;

import controller.Controller;
import model.Model;
import model.Presets;
import model.storage.FileStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;
import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.*;

class ViewTest {

    private View view;
    private Model model;

    @BeforeEach
    public void setUp()
    {
        model = new Model(new FileStrategy());
        Controller controller = new Controller(model);
        view = new View(model, controller);
    }
    @Test
    void initView() {
        view.initView();

        boolean isTitleSet = false;
        boolean isLayoutSet = false;
        boolean isVisibleSet = view.isVisible();
        boolean isResizableSet = view.isResizable();
        boolean isInitialViewSet = false;

        if (view.getTitle().equals(ViewConstants.APP_NAME)) isTitleSet = true;
        if (view.getLayout() instanceof BorderLayout) isLayoutSet = true;
        if (view.getCurrentPanel() instanceof InitialView) isInitialViewSet = true;

        assertTrue(isTitleSet);
        assertTrue(isLayoutSet);
        assertTrue(isVisibleSet);
        assertFalse(isResizableSet);
        assertTrue(isInitialViewSet);
    }

    @Test
    void setCurrentPanel() {
        view.initView();
        JPanel presetsView = new PresetsView(view);
        view.setCurrentPanel(presetsView);

        boolean isNewPanelSet = false;
        if (view.getCurrentPanel() instanceof PresetsView) isNewPanelSet = true;

        assertTrue(isNewPanelSet);
    }

    @Test
    void resetPreviousPanels() {
        view.initView();
        view.setCurrentPanel(new PresetsView(view));
        view.setCurrentPanel(new RecruitmentsListView(view));
        view.setCurrentPanel(new CandidateListView(view));
        view.resetPreviousPanels();
        view.returnToPreviousPanel();

        boolean isReset = false;
        if (view.getCurrentPanel() instanceof InitialView) isReset = true;

        assertTrue(isReset);
    }

    @Test
    void returnToPreviousPanel() {
        view.initView();
        view.setCurrentPanel(new PresetsView(view));
        view.setCurrentPanel(new RecruitmentsListView(view));
        view.returnToPreviousPanel();

        boolean hasReturned = false;
        if (view.getCurrentPanel() instanceof PresetsView) hasReturned = true;

        assertTrue(hasReturned);
    }

    @Test
    void startOver() {
        assertThrows(EmptyStackException.class, () -> {
            view.initView();
            view.startOver();
            view.returnToPreviousPanel(); });

        boolean isInitialViewSet = false;
        if (view.getCurrentPanel() instanceof InitialView) isInitialViewSet = true;

        assertTrue(isInitialViewSet);
    }
}