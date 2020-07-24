package com.guuidea.component.chrome.tool.ui;

import static com.guuidea.component.chrome.tool.common.AlertUtils.showAlert;
import static com.guuidea.component.chrome.tool.common.Constant.EMPTY_STR;
import static com.guuidea.component.utils.UUIDUtils.genUUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.guuidea.component.chrome.tool.GuuideaApp;
import com.guuidea.component.chrome.tool.common.Constant;
import com.guuidea.component.chrome.tool.common.StringUtils;
import com.guuidea.component.chrome.tool.libs.proxy.cryto.CryptFactory;
import com.guuidea.component.chrome.tool.libs.proxy.network.proxy.IProxy;
import com.guuidea.component.chrome.tool.libs.proxy.network.proxy.ProxyFactory;
import com.guuidea.component.chrome.tool.model.ProxyAccount;
import com.guuidea.component.chrome.tool.service.ProxyStoreDB;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * 代理服务界面结构
 *
 * @Author: hzchendou
 * @Date: 2019-07-03 20:30
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class ProxyLayoutController implements StageLifeCycle {

    private static Logger logger = LoggerFactory.getLogger(GuuideaApp.class);

    @FXML
    private TextField txtServerName;
    @FXML
    private TextField txtServerIP;
    @FXML
    private TextField txtServerPort;
    @FXML
    private ComboBox cboCipher;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtLocalPort;
    @FXML
    private ComboBox cboProxyType;
    @FXML
    private Button btnSubmit;
    @FXML
    private Button btnCancel;

    private Stage proxyStage;

    /**
     * 当前面板对应的account数据
     */
    private ProxyAccount account;

    private MainLayoutController parentController;

    /**
     * 初始化
     */
    @Override
    public void init(Stage stage) {
        this.proxyStage = stage;
    }

    /**
     * 初始化
     */
    @FXML
    public void initialize() {
        //设置加密类型
        ObservableList<String> ciphers = FXCollections.observableArrayList();
        ciphers.addAll(CryptFactory.getSupportedCiphers());
        cboCipher.setItems(ciphers);

        //设置代理方式
        ObservableList<IProxy.TYPE> proxyTypes = FXCollections.observableArrayList();
        proxyTypes.addAll(ProxyFactory.getSupportedProxyTypes());
        cboProxyType.setItems(proxyTypes);
        //将内容置空
        setProxyInfo(null);
    }

    /**
     * 将面板信息转换为代理账户信息
     *
     * @return
     */
    private ProxyAccount toProxyAccount() {

        if (StringUtils.isBlank(txtServerName.getText())) {
            showAlert(Constant.APP_NAME, "代理名称不能为空", Alert.AlertType.ERROR);
            return null;
        }
        String name = txtServerName.getText();
        if (StringUtils.isBlank(txtServerIP.getText())) {
            showAlert(Constant.APP_NAME, "服务器地址不能为空", Alert.AlertType.ERROR);
            return null;
        }
        String ip = txtServerIP.getText();
        if (!txtServerPort.getText().matches("[0-9]+")) {
            showAlert(Constant.APP_NAME, "服务器端口号信息错误", Alert.AlertType.ERROR);
            return null;
        }
        int port = Integer.parseInt(txtServerPort.getText());

        String method = (String) cboCipher.getValue();
        if (StringUtils.isBlank(method)) {
            showAlert(Constant.APP_NAME, "请选择加密方式", Alert.AlertType.ERROR);
            return null;
        }
        if (txtPassword.getText().length() == 0) {
            showAlert(Constant.APP_NAME, "请输入代理服务器连接密码", Alert.AlertType.ERROR);
            return null;
        }
        String password = txtPassword.getText();
        IProxy.TYPE type = (IProxy.TYPE) cboProxyType.getValue();

        if (type == null) {
            showAlert(Constant.APP_NAME, "请选择代理类型", Alert.AlertType.ERROR);
            return null;
        }

        if (!txtLocalPort.getText().matches("[0-9]+")) {
            showAlert(Constant.APP_NAME, "本地代理端口异常", Alert.AlertType.ERROR);
            return null;
        }
        int localPort = Integer.parseInt(txtLocalPort.getText());
        ProxyAccount account;
        if (this.account != null) {
            account = this.account;
        } else {
            account = new ProxyAccount();
            account.setId(genUUID());
        }
        // create config
        account.setName(name);
        account.setRemoteIpAddress(ip);
        account.setRemotePort(port);
        account.setLocalIpAddress("127.0.0.1");
        account.setLocalPort(localPort);
        account.setMethod(method);
        account.setPassword(password);
        account.setProxyType(type);
        return account;
    }

    /**
     * 初始化代理信息
     *
     * @param account
     */
    private void setProxyInfo(ProxyAccount account) {
        this.account = account;
        if (account == null) {
            txtServerName.setText(EMPTY_STR);
            txtServerIP.setText(EMPTY_STR);
            txtServerPort.setText(EMPTY_STR);
            txtPassword.setText(EMPTY_STR);
            txtLocalPort.setText("1080");
            return;
        }
        //设置变量信息
        txtServerName.setText(account.getName());
        txtServerIP.setText(account.getRemoteIpAddress());
        txtServerPort.setText(String.valueOf(account.getRemotePort()));
        txtPassword.setText(account.getPassword());
        txtLocalPort.setText(String.valueOf(account.getLocalPort()));
        cboCipher.setValue(account.getMethod());
        cboProxyType.setValue(account.getProxyType());
    }

    /**
     * 编辑代理账号信息
     *
     * @param account
     */
    public void editProxyAccount(ProxyAccount account) {
        setProxyInfo(account);
        show();
    }

    //按钮事件

    /**
     * 处理取消事件,需要清空面板内容
     */
    public void handleCancel() {
        hide();
    }

    /**
     * 处理提交事件
     * 1. 校验面板内容是否符合条件
     * 2. 将符合条件的内容保存到缓存中
     */
    public void handleSubmit() {
        ProxyAccount account = toProxyAccount();
        if (account == null) {
            hide();
            return;
        }
        //更新数据
        if (this.account != null) {
            ProxyStoreDB.updateProxy(account);
            parentController.reloadProxyAccountTable();
            hide();
            return;
        }
        //添加新数据
        ProxyStoreDB.addProxy(account);
        parentController.reloadProxyAccountTable();
        //销毁窗口
        hide();
    }


    /**
     * 显示
     */
    @Override
    public void show() {
        proxyStage.show();
    }

    /**
     * 隐藏
     */
    @Override
    public void hide() {
        setProxyInfo(null);
        proxyStage.hide();
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {

    }

    public void setParentController(MainLayoutController parentController) {
        this.parentController = parentController;
    }
}
