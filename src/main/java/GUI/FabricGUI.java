package GUI;

import factory.suppliers.AccessorySupplier;
import factory.suppliers.BodySupplier;
import factory.configReader.ConfigReader;
import factory.fabric.Fabric;
import factory.suppliers.MotorSupplier;
import factory.dealer.Dealer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.List;

public class FabricGUI extends JDialog {
    private JPanel MainPanel;
    private JPanel right;
    private JPanel left;
    private JButton startButton;
    private JPanel settingsPanel;
    private JSlider motorSlider;
    private JSlider bodySlider;
    private JSlider accessorySlider;
    private JTextArea motorsMakingDelaySlider;
    private JTextArea accessoryMakingDelaySlider;
    private JTextArea bodyMakingDelaySlider;
    private JTextArea MENUTextArea;
    private JButton settingsButton;
    private JButton exitButton;
    private JPanel menu;
    private JPanel infoPanel;
    private JButton infoButton;
    private JTextArea buyingDelaySelling;
    private JSlider buyingSlider;
    private JTextArea madeCarsNum;
    private JTextArea madeCarsText;
    private JTextArea carsDynamic;
    private JTextArea statHeader;
    private JTextArea waitingTasksText;
    private JTextArea waitingTasksNum;
    private JTextArea dynamicTasksWaiting;
    private JTextArea storedMotorsText;
    private JTextArea storedMotorsNum;
    private JTextArea motorsStoredDynamic;
    private JTextArea storedAccsNum;
    private JTextArea storedBodiesNum;
    private JTextArea bodyStoredDynamic;
    private JTextArea accsStoredDynamic;
    private JTextArea storedAccsText;
    private JTextArea storedBodiesText;
    private JTextArea valueHeader;
    private JTextArea dynamicHeader;
    private JLabel picLabel;
    private static Fabric fabric;
    private int displayDelay = 500;
    private File logFile = new File("src\\log\\log.txt");

    Timer displayTimer = new Timer(displayDelay, new ActionListener() {
        static int madeNum = 0;
        static int tasksLastTime = 0;
        static int storedMotors = 0;
        static int storedBodies = 0;
        static int storedAccs = 0;

        @Override
        public void actionPerformed(ActionEvent event) {
            List<Integer> info = fabric.getInfo();
            Integer cars = info.get(1);
            madeCarsNum.setText(cars.toString());
            carsDynamic.setText((cars - madeNum >= 0 ? "+" : "") + (cars - madeNum));
            madeNum = cars;

            int actualTasksNum = info.get(0);
            waitingTasksNum.setText("" + actualTasksNum);
            dynamicTasksWaiting.setText((actualTasksNum - tasksLastTime >= 0 ? "+" : "") + (actualTasksNum - tasksLastTime));
            tasksLastTime = actualTasksNum;

            Integer actualStoredNum = info.get(2);
            storedMotorsNum.setText(actualStoredNum.toString());
            motorsStoredDynamic.setText((actualStoredNum - storedMotors >= 0 ? "+" : "") + (actualStoredNum - storedMotors));
            storedMotors = actualStoredNum;

            actualStoredNum = info.get(3);
            storedBodiesNum.setText(actualStoredNum.toString());
            bodyStoredDynamic.setText((actualStoredNum - storedBodies >= 0 ? "+" : "") + (actualStoredNum - storedBodies));
            storedBodies = actualStoredNum;

            actualStoredNum = info.get(4);
            storedAccsNum.setText(actualStoredNum.toString());
            accsStoredDynamic.setText((actualStoredNum - storedAccs >= 0 ? "+" : "") + (actualStoredNum - storedAccs));
            storedAccs = actualStoredNum;
        }
    });

    private void changeScreen(JPanel where, JComponent newScreen) {
        where.removeAll();
        where.add(newScreen);
        where.repaint();
        where.revalidate();
    }

    public FabricGUI() {
        createUIComponents();
        setContentPane(MainPanel);
        setModal(true);

        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeScreen(right, infoPanel);
                infoPanel.requestFocus();
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeScreen(right, settingsPanel);
                settingsPanel.requestFocus();
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeScreen(right, infoPanel);
                infoPanel.requestFocus();
                if (!fabric.isStarted()) {
                    try {
                        Files.newBufferedWriter(logFile.toPath(), StandardOpenOption.TRUNCATE_EXISTING);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    fabric = new Fabric();
                    fabric.start();
                    displayTimer.start();
                    startButton.setText("Stop");
                } else {
                    fabric.stop();
                    displayTimer.stop();
                    startButton.setText("Restart");
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fabric.stop();
                dispose();

            }
        });

        motorSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                MotorSupplier.setSupplyingDelay(motorSlider.getValue());
            }
        });

        bodySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                BodySupplier.setSupplyingDelay(bodySlider.getValue());
            }
        });

        accessorySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                AccessorySupplier.setSupplyingDelay(accessorySlider.getValue());
            }
        });

        buyingSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Dealer.setBuyingDelay(buyingSlider.getValue());
            }
        });
    }

    static void initiateFabric() {
        fabric = new Fabric();
    }

    public static void main(String[] args) {
        FabricGUI dialog = new FabricGUI();

        initiateFabric();

        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        MainPanel = new JPanel();
        MainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        MainPanel.setBackground(new Color(-1));
        MainPanel.setForeground(new Color(-1));
        MainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        right = new JPanel();
        right.setLayout(new CardLayout(0, 0));
        right.setBackground(new Color(-1));
        right.setForeground(new Color(-1));
        MainPanel.add(right, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(900, 700), new Dimension(900, 700), null, 0, false));
        settingsPanel = new JPanel();
        settingsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(9, 1, new Insets(0, 0, 0, 0), -1, -1));
        settingsPanel.setBackground(new Color(-1));
        settingsPanel.setForeground(new Color(-1));
        right.add(settingsPanel, "Card2");
        bodySlider = new JSlider();
        bodySlider.setBackground(new Color(-1));
        bodySlider.setForeground(new Color(-3723232));
        bodySlider.setInverted(false);
        bodySlider.setMajorTickSpacing(5);
        bodySlider.setMaximum(95);
        bodySlider.setMinimum(5);
        bodySlider.setMinorTickSpacing(1);
        bodySlider.setPaintLabels(true);
        bodySlider.setPaintTicks(true);
        bodySlider.setPaintTrack(true);
        bodySlider.setSnapToTicks(false);
        bodySlider.setToolTipText("");
        bodySlider.setValue(50);
        bodySlider.setValueIsAdjusting(true);
        settingsPanel.add(bodySlider, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(600, -1), null, 0, false));
        accessorySlider = new JSlider();
        accessorySlider.setBackground(new Color(-1));
        accessorySlider.setForeground(new Color(-3723232));
        accessorySlider.setInverted(false);
        accessorySlider.setMajorTickSpacing(5);
        accessorySlider.setMaximum(95);
        accessorySlider.setMinimum(5);
        accessorySlider.setMinorTickSpacing(1);
        accessorySlider.setPaintLabels(true);
        accessorySlider.setPaintTicks(true);
        accessorySlider.setPaintTrack(true);
        accessorySlider.setSnapToTicks(false);
        accessorySlider.setToolTipText("");
        accessorySlider.setValue(50);
        accessorySlider.setValueIsAdjusting(true);
        settingsPanel.add(accessorySlider, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(600, -1), null, 0, false));
        accessoryMakingDelaySlider = new JTextArea();
        accessoryMakingDelaySlider.setBackground(new Color(-1));
        accessoryMakingDelaySlider.setEditable(false);
        Font accessoryMakingDelaySliderFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 24, accessoryMakingDelaySlider.getFont());
        if (accessoryMakingDelaySliderFont != null) accessoryMakingDelaySlider.setFont(accessoryMakingDelaySliderFont);
        accessoryMakingDelaySlider.setForeground(new Color(-3723232));
        accessoryMakingDelaySlider.setText("Accessory suppling delay");
        settingsPanel.add(accessoryMakingDelaySlider, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, 30), null, 0, false));
        bodyMakingDelaySlider = new JTextArea();
        bodyMakingDelaySlider.setBackground(new Color(-1));
        bodyMakingDelaySlider.setEditable(false);
        Font bodyMakingDelaySliderFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 24, bodyMakingDelaySlider.getFont());
        if (bodyMakingDelaySliderFont != null) bodyMakingDelaySlider.setFont(bodyMakingDelaySliderFont);
        bodyMakingDelaySlider.setForeground(new Color(-3723232));
        bodyMakingDelaySlider.setText("Body building delay");
        settingsPanel.add(bodyMakingDelaySlider, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, 30), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        settingsPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buyingDelaySelling = new JTextArea();
        buyingDelaySelling.setBackground(new Color(-1));
        buyingDelaySelling.setEditable(false);
        Font buyingDelaySellingFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 24, buyingDelaySelling.getFont());
        if (buyingDelaySellingFont != null) buyingDelaySelling.setFont(buyingDelaySellingFont);
        buyingDelaySelling.setForeground(new Color(-3723232));
        buyingDelaySelling.setText("Dealers selling delay");
        settingsPanel.add(buyingDelaySelling, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, 30), null, 0, false));
        buyingSlider = new JSlider();
        buyingSlider.setBackground(new Color(-1));
        buyingSlider.setForeground(new Color(-3723232));
        buyingSlider.setInverted(false);
        buyingSlider.setMajorTickSpacing(5);
        buyingSlider.setMaximum(95);
        buyingSlider.setMinimum(5);
        buyingSlider.setMinorTickSpacing(1);
        buyingSlider.setPaintLabels(true);
        buyingSlider.setPaintTicks(true);
        buyingSlider.setPaintTrack(true);
        buyingSlider.setSnapToTicks(false);
        buyingSlider.setToolTipText("");
        buyingSlider.setValue(50);
        buyingSlider.setValueIsAdjusting(true);
        settingsPanel.add(buyingSlider, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(600, -1), null, 0, false));
        motorSlider = new JSlider();
        motorSlider.setBackground(new Color(-1));
        motorSlider.setForeground(new Color(-3723232));
        motorSlider.setInverted(false);
        motorSlider.setMajorTickSpacing(5);
        motorSlider.setMaximum(95);
        motorSlider.setMinimum(5);
        motorSlider.setMinorTickSpacing(1);
        motorSlider.setPaintLabels(true);
        motorSlider.setPaintTicks(true);
        motorSlider.setPaintTrack(true);
        motorSlider.setSnapToTicks(false);
        motorSlider.setToolTipText("");
        motorSlider.setValue(50);
        motorSlider.setValueIsAdjusting(true);
        settingsPanel.add(motorSlider, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(600, -1), null, 0, false));
        motorsMakingDelaySlider = new JTextArea();
        motorsMakingDelaySlider.setBackground(new Color(-1));
        motorsMakingDelaySlider.setEditable(false);
        Font motorsMakingDelaySliderFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 24, motorsMakingDelaySlider.getFont());
        if (motorsMakingDelaySliderFont != null) motorsMakingDelaySlider.setFont(motorsMakingDelaySliderFont);
        motorsMakingDelaySlider.setForeground(new Color(-3723232));
        motorsMakingDelaySlider.setText("Motor suppling delay");
        settingsPanel.add(motorsMakingDelaySlider, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, 30), null, 0, false));
        infoPanel = new JPanel();
        infoPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(13, 5, new Insets(0, 0, 0, 0), -1, -1));
        infoPanel.setBackground(new Color(-1));
        right.add(infoPanel, "Card3");
        madeCarsText = new JTextArea();
        madeCarsText.setBackground(new Color(-1));
        madeCarsText.setEditable(false);
        Font madeCarsTextFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, madeCarsText.getFont());
        if (madeCarsTextFont != null) madeCarsText.setFont(madeCarsTextFont);
        madeCarsText.setForeground(new Color(-3723232));
        madeCarsText.setText("Cars made");
        infoPanel.add(madeCarsText, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        madeCarsNum = new JTextArea();
        madeCarsNum.setBackground(new Color(-1));
        madeCarsNum.setEditable(false);
        Font madeCarsNumFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, madeCarsNum.getFont());
        if (madeCarsNumFont != null) madeCarsNum.setFont(madeCarsNumFont);
        madeCarsNum.setForeground(new Color(-3723232));
        madeCarsNum.setText("0");
        infoPanel.add(madeCarsNum, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        carsDynamic = new JTextArea();
        carsDynamic.setBackground(new Color(-1));
        carsDynamic.setEditable(false);
        Font carsDynamicFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, carsDynamic.getFont());
        if (carsDynamicFont != null) carsDynamic.setFont(carsDynamicFont);
        carsDynamic.setForeground(new Color(-3723232));
        carsDynamic.setText("+0");
        infoPanel.add(carsDynamic, new com.intellij.uiDesigner.core.GridConstraints(3, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        statHeader = new JTextArea();
        statHeader.setBackground(new Color(-1));
        statHeader.setEditable(false);
        Font statHeaderFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, statHeader.getFont());
        if (statHeaderFont != null) statHeader.setFont(statHeaderFont);
        statHeader.setForeground(new Color(-3723232));
        statHeader.setText("Statistic");
        infoPanel.add(statHeader, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        valueHeader = new JTextArea();
        valueHeader.setBackground(new Color(-1));
        valueHeader.setEditable(false);
        Font valueHeaderFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, valueHeader.getFont());
        if (valueHeaderFont != null) valueHeader.setFont(valueHeaderFont);
        valueHeader.setForeground(new Color(-3723232));
        valueHeader.setText("Value");
        infoPanel.add(valueHeader, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        dynamicHeader = new JTextArea();
        dynamicHeader.setBackground(new Color(-1));
        dynamicHeader.setEditable(false);
        Font dynamicHeaderFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, dynamicHeader.getFont());
        if (dynamicHeaderFont != null) dynamicHeader.setFont(dynamicHeaderFont);
        dynamicHeader.setForeground(new Color(-3723232));
        dynamicHeader.setText("Dynamic");
        infoPanel.add(dynamicHeader, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        infoPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        infoPanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        waitingTasksText = new JTextArea();
        waitingTasksText.setBackground(new Color(-1));
        waitingTasksText.setEditable(false);
        Font waitingTasksTextFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, waitingTasksText.getFont());
        if (waitingTasksTextFont != null) waitingTasksText.setFont(waitingTasksTextFont);
        waitingTasksText.setForeground(new Color(-3723232));
        waitingTasksText.setText("Tasks wait");
        infoPanel.add(waitingTasksText, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        waitingTasksNum = new JTextArea();
        waitingTasksNum.setBackground(new Color(-1));
        waitingTasksNum.setEditable(false);
        Font waitingTasksNumFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, waitingTasksNum.getFont());
        if (waitingTasksNumFont != null) waitingTasksNum.setFont(waitingTasksNumFont);
        waitingTasksNum.setForeground(new Color(-3723232));
        waitingTasksNum.setText("0");
        infoPanel.add(waitingTasksNum, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        dynamicTasksWaiting = new JTextArea();
        dynamicTasksWaiting.setBackground(new Color(-1));
        dynamicTasksWaiting.setEditable(false);
        Font dynamicTasksWaitingFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, dynamicTasksWaiting.getFont());
        if (dynamicTasksWaitingFont != null) dynamicTasksWaiting.setFont(dynamicTasksWaitingFont);
        dynamicTasksWaiting.setForeground(new Color(-3723232));
        dynamicTasksWaiting.setText("+0");
        infoPanel.add(dynamicTasksWaiting, new com.intellij.uiDesigner.core.GridConstraints(5, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        storedMotorsText = new JTextArea();
        storedMotorsText.setBackground(new Color(-1));
        storedMotorsText.setEditable(false);
        Font storedMotorsTextFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, storedMotorsText.getFont());
        if (storedMotorsTextFont != null) storedMotorsText.setFont(storedMotorsTextFont);
        storedMotorsText.setForeground(new Color(-3723232));
        storedMotorsText.setText("Motor stored");
        infoPanel.add(storedMotorsText, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        storedMotorsNum = new JTextArea();
        storedMotorsNum.setBackground(new Color(-1));
        storedMotorsNum.setEditable(false);
        Font storedMotorsNumFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, storedMotorsNum.getFont());
        if (storedMotorsNumFont != null) storedMotorsNum.setFont(storedMotorsNumFont);
        storedMotorsNum.setForeground(new Color(-3723232));
        storedMotorsNum.setText("0");
        infoPanel.add(storedMotorsNum, new com.intellij.uiDesigner.core.GridConstraints(7, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        motorsStoredDynamic = new JTextArea();
        motorsStoredDynamic.setBackground(new Color(-1));
        motorsStoredDynamic.setEditable(false);
        Font motorsStoredDynamicFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, motorsStoredDynamic.getFont());
        if (motorsStoredDynamicFont != null) motorsStoredDynamic.setFont(motorsStoredDynamicFont);
        motorsStoredDynamic.setForeground(new Color(-3723232));
        motorsStoredDynamic.setText("+0");
        infoPanel.add(motorsStoredDynamic, new com.intellij.uiDesigner.core.GridConstraints(7, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        storedBodiesText = new JTextArea();
        storedBodiesText.setBackground(new Color(-1));
        storedBodiesText.setEditable(false);
        Font storedBodiesTextFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, storedBodiesText.getFont());
        if (storedBodiesTextFont != null) storedBodiesText.setFont(storedBodiesTextFont);
        storedBodiesText.setForeground(new Color(-3723232));
        storedBodiesText.setLineWrap(false);
        storedBodiesText.setText("Bodies stored");
        infoPanel.add(storedBodiesText, new com.intellij.uiDesigner.core.GridConstraints(9, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        storedBodiesNum = new JTextArea();
        storedBodiesNum.setBackground(new Color(-1));
        storedBodiesNum.setEditable(false);
        Font storedBodiesNumFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, storedBodiesNum.getFont());
        if (storedBodiesNumFont != null) storedBodiesNum.setFont(storedBodiesNumFont);
        storedBodiesNum.setForeground(new Color(-3723232));
        storedBodiesNum.setText("0");
        infoPanel.add(storedBodiesNum, new com.intellij.uiDesigner.core.GridConstraints(9, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        bodyStoredDynamic = new JTextArea();
        bodyStoredDynamic.setBackground(new Color(-1));
        bodyStoredDynamic.setEditable(false);
        Font bodyStoredDynamicFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, bodyStoredDynamic.getFont());
        if (bodyStoredDynamicFont != null) bodyStoredDynamic.setFont(bodyStoredDynamicFont);
        bodyStoredDynamic.setForeground(new Color(-3723232));
        bodyStoredDynamic.setText("+0");
        infoPanel.add(bodyStoredDynamic, new com.intellij.uiDesigner.core.GridConstraints(9, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        storedAccsText = new JTextArea();
        storedAccsText.setBackground(new Color(-1));
        storedAccsText.setEditable(false);
        Font storedAccsTextFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, storedAccsText.getFont());
        if (storedAccsTextFont != null) storedAccsText.setFont(storedAccsTextFont);
        storedAccsText.setForeground(new Color(-3723232));
        storedAccsText.setText("Accessory stored");
        infoPanel.add(storedAccsText, new com.intellij.uiDesigner.core.GridConstraints(11, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        storedAccsNum = new JTextArea();
        storedAccsNum.setBackground(new Color(-1));
        storedAccsNum.setEditable(false);
        Font storedAccsNumFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, storedAccsNum.getFont());
        if (storedAccsNumFont != null) storedAccsNum.setFont(storedAccsNumFont);
        storedAccsNum.setForeground(new Color(-3723232));
        storedAccsNum.setText("0");
        infoPanel.add(storedAccsNum, new com.intellij.uiDesigner.core.GridConstraints(11, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        accsStoredDynamic = new JTextArea();
        accsStoredDynamic.setBackground(new Color(-1));
        accsStoredDynamic.setEditable(false);
        Font accsStoredDynamicFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, accsStoredDynamic.getFont());
        if (accsStoredDynamicFont != null) accsStoredDynamic.setFont(accsStoredDynamicFont);
        accsStoredDynamic.setForeground(new Color(-3723232));
        accsStoredDynamic.setText("+0");
        infoPanel.add(accsStoredDynamic, new com.intellij.uiDesigner.core.GridConstraints(11, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 30), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        infoPanel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(12, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        infoPanel.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer6 = new com.intellij.uiDesigner.core.Spacer();
        infoPanel.add(spacer6, new com.intellij.uiDesigner.core.GridConstraints(10, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer7 = new com.intellij.uiDesigner.core.Spacer();
        infoPanel.add(spacer7, new com.intellij.uiDesigner.core.GridConstraints(8, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer8 = new com.intellij.uiDesigner.core.Spacer();
        infoPanel.add(spacer8, new com.intellij.uiDesigner.core.GridConstraints(6, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer9 = new com.intellij.uiDesigner.core.Spacer();
        infoPanel.add(spacer9, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer10 = new com.intellij.uiDesigner.core.Spacer();
        infoPanel.add(spacer10, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        picLabel = new JLabel();
        picLabel.setHorizontalAlignment(0);
        picLabel.setHorizontalTextPosition(0);
        picLabel.setText("Label");
        right.add(picLabel, "Card4");
        left = new JPanel();
        left.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        left.setBackground(new Color(-16737390));
        Font leftFont = this.$$$getFont$$$("Consolas", Font.BOLD, 21, left.getFont());
        if (leftFont != null) left.setFont(leftFont);
        left.setForeground(new Color(-1));
        MainPanel.add(left, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(303, 700), null, 0, false));
        left.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final com.intellij.uiDesigner.core.Spacer spacer11 = new com.intellij.uiDesigner.core.Spacer();
        left.add(spacer11, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        menu = new JPanel();
        menu.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 1, new Insets(0, 0, 0, 0), -1, -1));
        menu.setBackground(new Color(-16492714));
        left.add(menu, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, 300), null, 0, false));
        settingsButton = new JButton();
        settingsButton.setBackground(new Color(-1));
        Font settingsButtonFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, settingsButton.getFont());
        if (settingsButtonFont != null) settingsButton.setFont(settingsButtonFont);
        settingsButton.setForeground(new Color(-3723232));
        settingsButton.setText("Settings");
        menu.add(settingsButton, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(130, 30), null, 0, false));
        exitButton = new JButton();
        exitButton.setBackground(new Color(-1));
        Font exitButtonFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, exitButton.getFont());
        if (exitButtonFont != null) exitButton.setFont(exitButtonFont);
        exitButton.setForeground(new Color(-3723232));
        exitButton.setText("Exit");
        menu.add(exitButton, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(130, 30), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer12 = new com.intellij.uiDesigner.core.Spacer();
        menu.add(spacer12, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 60), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer13 = new com.intellij.uiDesigner.core.Spacer();
        menu.add(spacer13, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer14 = new com.intellij.uiDesigner.core.Spacer();
        menu.add(spacer14, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        startButton = new JButton();
        startButton.setBackground(new Color(-1));
        Font startButtonFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, startButton.getFont());
        if (startButtonFont != null) startButton.setFont(startButtonFont);
        startButton.setForeground(new Color(-3723232));
        startButton.setText("Start");
        menu.add(startButton, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(130, 30), null, 0, false));
        infoButton = new JButton();
        infoButton.setBackground(new Color(-1));
        Font infoButtonFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 20, infoButton.getFont());
        if (infoButtonFont != null) infoButton.setFont(infoButtonFont);
        infoButton.setForeground(new Color(-3723232));
        infoButton.setText("Info");
        menu.add(infoButton, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(130, 30), null, 0, false));
        MENUTextArea = new JTextArea();
        MENUTextArea.setBackground(new Color(-16492714));
        MENUTextArea.setEditable(false);
        Font MENUTextAreaFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 33, MENUTextArea.getFont());
        if (MENUTextAreaFont != null) MENUTextArea.setFont(MENUTextAreaFont);
        MENUTextArea.setForeground(new Color(-2833252));
        MENUTextArea.setText("MENU");
        menu.add(MENUTextArea, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(102, 30), null, 1, false));
        final com.intellij.uiDesigner.core.Spacer spacer15 = new com.intellij.uiDesigner.core.Spacer();
        left.add(spacer15, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer16 = new com.intellij.uiDesigner.core.Spacer();
        left.add(spacer16, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer17 = new com.intellij.uiDesigner.core.Spacer();
        left.add(spacer17, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return MainPanel;
    }

    private void createUIComponents() {
        menu.setBorder(BorderFactory.createLineBorder(new Color(-1)));
        settingsButton.setBorder(BorderFactory.createLineBorder(new Color(-1)));
        startButton.setBorder(BorderFactory.createLineBorder(new Color(-1)));
        exitButton.setBorder(BorderFactory.createLineBorder(new Color(-1)));
        infoButton.setBorder(BorderFactory.createLineBorder(new Color(-1)));
        ConfigReader configReader = new ConfigReader();
        configReader.readConfig();

        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File("picture.jpg"));
        } catch (IOException e) {
        }
        Image img = myPicture.getScaledInstance(900, 700, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(img);
        picLabel.setIcon(icon);
        picLabel.setText("FABRIC");
        picLabel.setForeground(new Color(39826));
        Font picLabelFont = this.$$$getFont$$$("Yu Gothic UI Semibold", Font.BOLD, 160, picLabel.getFont());
        picLabel.setFont(picLabelFont);
        picLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        changeScreen(right, picLabel);
    }
}
