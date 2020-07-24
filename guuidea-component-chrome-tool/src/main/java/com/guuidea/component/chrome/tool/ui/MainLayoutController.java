package com.guuidea.component.chrome.tool.ui;

import static com.guuidea.component.chrome.tool.common.Constant.EMPTY_STR;
import static com.guuidea.component.chrome.tool.common.StageLoaderUtils.loadController;
import static com.guuidea.component.chrome.tool.model.SystemConf.*;
import static com.guuidea.component.utils.TimeFormatterUtils.formatYYYYMMddHHmmss;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guuidea.component.chrome.tool.GuuideaApp;
import com.guuidea.component.chrome.tool.common.AlertUtils;
import com.guuidea.component.chrome.tool.common.CollectionUtils;
import com.guuidea.component.chrome.tool.common.StoreUtils;
import com.guuidea.component.chrome.tool.common.StringUtils;
import com.guuidea.component.chrome.tool.libs.proxy.cryto.CryptFactory;
import com.guuidea.component.chrome.tool.libs.proxy.network.proxy.IProxy;
import com.guuidea.component.chrome.tool.listener.TaskStopListener;
import com.guuidea.component.chrome.tool.model.GoogleAccount;
import com.guuidea.component.chrome.tool.model.ProxyAccount;
import com.guuidea.component.chrome.tool.service.ChromeLoginStatusService;
import com.guuidea.component.chrome.tool.service.GoogleStoreDB;
import com.guuidea.component.chrome.tool.service.ProxyStoreDB;
import com.guuidea.component.chrome.tool.service.StoreDB;
import com.guuidea.component.chrome.tool.task.CheckIPTask;
import com.guuidea.component.chrome.tool.task.ProxyTask;
import com.guuidea.component.chrome.tool.task.SeleniumTask;
import com.guuidea.component.chrome.tool.task.TaskChain;
import com.guuidea.component.utils.UUIDUtils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 主页面
 *
 * @Author: hzchendou
 * @Date: 2019-07-03 14:09
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class MainLayoutController implements StageLifeCycle {

    private static final Logger logger = LoggerFactory.getLogger(MainLayoutController.class);

    @FXML
    public TabPane mainTabPane;
    //Google账户列信息
    @FXML
    public TableView<GoogleAccount> googleAccountTable;
    @FXML
    public TableColumn<GoogleAccount, String> gUserNameCol;
    @FXML
    public TableColumn<GoogleAccount, String> gPreIpCol;
    @FXML
    public TableColumn<GoogleAccount, String> gLastLoiginAtCol;
    @FXML
    public TableColumn<GoogleAccount, String> gCheckIpCol;
    @FXML
    public TableColumn<GoogleAccount, String> gPrxoyNameCol;
    @FXML
    public TableColumn<GoogleAccount, String> gOptCol;
    @FXML
    public TableView<ProxyAccount> proxyAccountTable;
    @FXML
    public TableColumn<ProxyAccount, String> pServerName;

    //代理账号列信息
    @FXML
    public TableColumn<ProxyAccount, String> pServerAddrCol;
    @FXML
    public TableColumn<ProxyAccount, String> pServerPortCol;
    @FXML
    public TableColumn<ProxyAccount, String> pProxyMethodCol;
    @FXML
    public TableColumn<ProxyAccount, String> pProxyTypeCol;
    @FXML
    public TableColumn<ProxyAccount, String> pLocalPortCol;
    @FXML
    public TableColumn<ProxyAccount, String> pOptCol;
    private GuuideaApp app;
    private Stage mainStage;
    /**
     * 代理控制器
     */
    private ProxyLayoutController proxyController;

    /**
     * 谷歌账号设置控制器
     */
    private GoogleAccountController accountController;


    /**
     * 设置主界面
     *
     * @param gui
     */
    public void setMainGui(GuuideaApp gui) {
        this.app = gui;

    }

    /**
     * 初始化代理账户表格
     */
    public void initProxyAccountTable() {
        pServerName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        pServerAddrCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getRemoteIpAddress()));
        pServerPortCol.setCellValueFactory(
                param -> new SimpleStringProperty(String.valueOf(param.getValue().getRemotePort())));
        pProxyMethodCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getMethod()));
        pProxyTypeCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getProxyType().name()));
        pLocalPortCol.setCellValueFactory(
                param -> new SimpleStringProperty(String.valueOf(param.getValue().getLocalPort())));
        pOptCol.setCellFactory(col -> {
            TableCell<ProxyAccount, String> cell = new TableCell<ProxyAccount, String>() {
                public HBox paddedBtnBox = new HBox();
                Label delBtn = new Label("删除");
                Label editBtn = new Label("编辑");

                {
                    editBtn.setCursor(Cursor.HAND);
                    delBtn.setCursor(Cursor.HAND);
                    paddedBtnBox.getChildren().addAll(delBtn, editBtn);
                    paddedBtnBox.setSpacing(10);
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        paddedBtnBox.setPadding(new Insets(3));
                        delBtn.setOnMouseClicked((m) -> {
                            ProxyAccount proxyAccount = proxyAccountTable.getSelectionModel().getSelectedItem();
                            Boolean checkStatus = AlertUtils.showConfirmAlert("确认删除代理信息:" + proxyAccount.getName());
                            //确认删除操作
                            if (checkStatus) {
                                ProxyStoreDB.delProxy(proxyAccount);
                                reloadProxyAccountTable();
                            }
                        });
                        editBtn.setOnMouseClicked((m) -> {
                            ProxyAccount proxyAccount = proxyAccountTable.getSelectionModel().getSelectedItem();
                            proxyController.editProxyAccount(proxyAccount);
                        });
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        setGraphic(paddedBtnBox);
                    } else {
                        setGraphic(null);
                    }
                }
            };
            return cell;
        });
    }

    /**
     * 重新刷新代理数据
     */
    public void reloadProxyAccountTable() {
        List<ProxyAccount> proxyAccounts = ProxyStoreDB.loadAllProxyAccount();
        ObservableList<ProxyAccount> proxyAccountObservableList;
        if (CollectionUtils.isEmpty(proxyAccounts)) {
            proxyAccountObservableList = FXCollections.observableArrayList();
        } else {
            proxyAccountObservableList = FXCollections.observableArrayList(proxyAccounts);
        }
        proxyAccountTable.setItems(proxyAccountObservableList);
        proxyAccountTable.refresh();
    }

    /**
     * 重新加载谷歌账户信息
     */
    public void reloadGoogleAccountTable() {
        List<GoogleAccount> googleAccounts = GoogleStoreDB.loadAllGoogleAccount();
        ObservableList<GoogleAccount> googleAccountObservableList;
        if (CollectionUtils.isEmpty(googleAccounts)) {
            googleAccountObservableList = FXCollections.observableArrayList();
        } else {
            googleAccountObservableList = FXCollections.observableArrayList(googleAccounts);
        }
        googleAccountTable.setItems(googleAccountObservableList);
        googleAccountTable.refresh();
    }

    /**
     * 初始化Google账号表格
     */
    public void initGoogleAccountTable() {
        gUserNameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUserName()));
        gPreIpCol.setCellValueFactory(
                param -> new SimpleStringProperty(StringUtils.defaultIfBlank(param.getValue().getPreIp(), "-")));
        gLastLoiginAtCol.setCellValueFactory(param -> {
            Long lastLoginAt = param.getValue().getLastLoginAt();
            if (lastLoginAt == null) {
                return new SimpleStringProperty("-");
            }
            return new SimpleStringProperty(formatYYYYMMddHHmmss(lastLoginAt));
        });
        gCheckIpCol.setCellValueFactory(param -> {
            Boolean checkIp = param.getValue().getCheckIp();
            if (checkIp == null || !checkIp) {
                return new SimpleStringProperty("不校验");
            }
            return new SimpleStringProperty("校验");

        });
        gPrxoyNameCol.setCellValueFactory(param -> {
            String proxyId = param.getValue().getProxyId();
            if (StringUtils.isBlank(proxyId)) {
                return new SimpleStringProperty("无");
            }
            ProxyAccount account = ProxyStoreDB.queryById(proxyId);
            if (account == null) {
                param.getValue().setProxyId(EMPTY_STR);
                return new SimpleStringProperty("无");
            }
            return new SimpleStringProperty(account.getName() + "/" + account.getLocalPort());
        });
        //设置操作按钮

        gOptCol.setCellFactory(col -> {
            TableCell<GoogleAccount, String> cell = new TableCell<GoogleAccount, String>() {
                public HBox paddedBtnBox = new HBox();
                Label editBtn = new Label("编辑");
                Label delBtn = new Label("删除");
                Label autoLoginBtn = new Label("一键登录");

                {
                    editBtn.setCursor(Cursor.HAND);
                    delBtn.setCursor(Cursor.HAND);
                    autoLoginBtn.setCursor(Cursor.HAND);
                    paddedBtnBox.getChildren().addAll(delBtn, editBtn, autoLoginBtn);
                    paddedBtnBox.setSpacing(10);
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        paddedBtnBox.setPadding(new Insets(4));
                        delBtn.setOnMouseClicked((m) -> {
                            GoogleAccount googleAccount = googleAccountTable.getSelectionModel().getSelectedItem();
                            Boolean checkStatus =
                                    AlertUtils.showConfirmAlert("确认删除Google账户信息:" + googleAccount.getUserName());
                            //确认删除操作
                            if (checkStatus) {
                                GoogleStoreDB.delGoogleAccount(googleAccount);
                                reloadGoogleAccountTable();
                            }
                        });
                        editBtn.setOnMouseClicked((m) -> {
                            GoogleAccount googleAccount = googleAccountTable.getSelectionModel().getSelectedItem();
                            accountController.editAccount(googleAccount);
                        });
                        //一键登录操作
                        autoLoginBtn.setOnMouseClicked((m) -> {
                            GoogleAccount googleAccount = googleAccountTable.getSelectionModel().getSelectedItem();
                            ProxyAccount proxyAccount = ProxyStoreDB.queryById(googleAccount.getProxyId());
                            TaskChain bootChain = null;
                            AtomicBoolean ready = new AtomicBoolean(Boolean.TRUE);
                            try {
                                //组装Task
                                bootChain = TaskChain.chain(new TaskStopListener() {

                                    /**
                                     * 成功结束
                                     */
                                    @Override
                                    public void success() {
                                        //释放菜单
                                        app.removeMenuItem(googleAccount);
                                        //释放账户锁
                                        ChromeLoginStatusService.release(proxyAccount, googleAccount);
                                        logger.info("停止登录任务, {}", googleAccount.getUserName());
                                    }

                                    /**
                                     * 异常结束
                                     *
                                     * @param msg
                                     */
                                    @Override
                                    public void error(String msg) {
                                        //释放菜单
                                        ready.set(Boolean.FALSE);
                                        app.removeMenuItem(googleAccount);
                                        //释放账户锁
                                        ChromeLoginStatusService.release(proxyAccount, googleAccount);
                                        AlertUtils.showError(msg);
                                        logger.info("停止登录任务, {}, 任务异常, {}", googleAccount.getUserName(), msg);
                                    }
                                });
                                bootChain.setNext(new ProxyTask(proxyAccount))
                                        .setNext(new CheckIPTask(proxyAccount, googleAccount))
                                        .setNext(new SeleniumTask(googleAccount, proxyAccount)).end();
                                if (!ChromeLoginStatusService.lockAccount(proxyAccount, googleAccount)) {
                                    AlertUtils.showWarn("当前帐号已登录，请勿重复操作！！！");
                                    return;
                                }
                                bootChain.start();
                                //添加设置停止按钮
                                //修复启动异常时引起的菜单异常现象
                                if (ready.get()) {
                                    app.addStopMenuItem(proxyAccount, googleAccount, bootChain);
                                }
                            } catch (Exception ex) {
                                ChromeLoginStatusService.release(proxyAccount, googleAccount);
                            }
                        });
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        setGraphic(paddedBtnBox);
                    } else {
                        setGraphic(null);
                    }
                }
            };
            return cell;
        });
    }

    //    代理设置方法

    /**
     * 新建代理
     */
    public void handleNewProxy() {
        proxyController.show();
    }

    /**
     * 导入代理配置信息
     */
    public void handleFileChoice() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导入代理配置");
        File file = fileChooser.showOpenDialog(mainStage);
        //开始解析文件
        if (file != null && file.exists()) {
            JSONObject fileJson = StoreUtils.parseFileContent(file.getAbsolutePath());
            if (fileJson == null) {
                AlertUtils.showError("文件格式不正确, 必须是JSON格式");
                return;
            }
            try {
                int successNum = 0;
                JSONArray jsonArray = fileJson.getJSONArray("configs");
                if (jsonArray != null && jsonArray.size() > 0) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String serverAddr = jsonObject.getString("server");
                        Integer serverPort = jsonObject.getInteger("server_port");
                        Integer localPort = jsonObject.getInteger("local_port");
                        String password = jsonObject.getString("password");
                        String method = jsonObject.getString("method");
                        String remarks = jsonObject.getString("remarks");
                        if (StringUtils.isBlank(serverAddr) || StringUtils.isBlank(password) || StringUtils
                                .isBlank(method) || StringUtils.isBlank(remarks)) {
                            continue;
                        }

                        if (serverPort == null || serverPort < 1) {
                            continue;
                        }
                        if (localPort == null || localPort < 1) {
                            continue;
                        }

                        if (!CryptFactory.isCipherExisted(method)) {
                            continue;
                        }

                        ProxyAccount account = new ProxyAccount();
                        account.setId(UUIDUtils.genUUID());
                        account.setRemoteIpAddress(serverAddr);
                        account.setRemotePort(serverPort);
                        account.setLocalPort(localPort);
                        account.setLocalIpAddress("127.0.0.1");
                        account.setPassword(password);
                        account.setMethod(method);
                        account.setProxyType(IProxy.TYPE.SOCKS5);
                        account.setLogLevel("INFO");
                        account.setName(remarks);
                        ProxyStoreDB.addProxy(account);
                        successNum++;
                    }
                }
                AlertUtils.showMsg("导入数据:" + successNum);
                if (successNum > 0) {
                    reloadProxyAccountTable();
                }
            } catch (Exception ex) {
                AlertUtils.showError("解析配置文件错误,请检查后重试");
            }
        }
    }

    //    Google账号设置方法

    /**
     * 处理新建账号
     */
    public void handleNewAccount() {
        accountController.show();
    }

    /**
     * 初始化
     */
    @Override
    public void init(Stage stage) {
        try {
            this.mainStage = stage;
            //初始化主面板控制器资源
            this.proxyController =
                    loadController("/resources/ui/proxyLayout.fxml", "代理设置", ProxyLayoutController.class);
            this.proxyController.setParentController(this);
            this.accountController =
                    loadController("/resources/ui/accountLayout.fxml", "Google账号设置", GoogleAccountController.class);
            this.accountController.setParentController(this);
            //添加Tab页切换，可以用户刷新数据
            mainTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
                @Override
                public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                    if (StringUtils.equalsIgnoreCase(newValue.getText(), "代理")) {
                        reloadProxyAccountTable();
                        return;
                    }
                    reloadGoogleAccountTable();
                }
            });
            initGoogleAccountTable();
            //加载google账号信息
            reloadGoogleAccountTable();
            initProxyAccountTable();
            reloadProxyAccountTable();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //默认显示
    @Override
    public void show() {
        mainStage.show();
    }

    /**
     * 隐藏
     */
    @Override
    public void hide() {
        mainStage.hide();
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        //TODO 服务停止时，清理资源
        stopAllLogin();
        //存储系统配置信息
        StoreUtils.saveFile(DEFAULT_PATH + File.separator + DEFAULT_SYS_CONF_NAME, toJSON());
        //存储Google账号、代理信息数据
        StoreDB.store();

    }

    /**
     * 停止登录
     */
    public void stopAllLogin() {
        ChromeLoginStatusService.releasAll();
        java.awt.MenuItem menuItem = app.trayIcon.getPopupMenu().getItem(1);
        menuItem.setEnabled(Boolean.FALSE);
    }
}
